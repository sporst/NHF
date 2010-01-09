package net.sourceforge.jnhf.fceux.tracefile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jnhf.disassembler.CAddress;
import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.helpers.FileHelpers;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.GmlConverter;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.InternalTranslationException;
import net.sourceforge.jnhf.reil.ReilGraph;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.ReilTranslator;
import net.sourceforge.jnhf.tainttracker.AddressFilter;
import net.sourceforge.jnhf.tainttracker.TaintElement;
import net.sourceforge.jnhf.tainttracker.TaintGraph;
import net.sourceforge.jnhf.tainttracker.TaintGraphBuilder;
import net.sourceforge.jnhf.tainttracker.TaintGraphFilter;

public class TraceLogParser
{
	private static final Pattern linePattern = Pattern.compile("([^@=]*)(@ \\$(....) )?(= #\\$(..))?.*");

	public static void main(final String[] args)
	{
		try
		{
			TaintGraphBuilder builder = new TaintGraphBuilder();
			
			final IFilledList<TraceLogLine> lines = parse(new File("F:\\fce\\Faxanadu (U).log"));

			for (final TraceLogLine traceLogLine : lines)
			{
				final Instruction instruction = InstructionDisassembler.disassemble(Integer.valueOf(traceLogLine.getAddress(), 16), traceLogLine.getData());

				System.out.println(instruction);

				final ReilGraph reil = ReilTranslator.translate(instruction);

				System.out.println(reil.getNodes().get(0).getInstructions());

				final IAddress address = getMemoryAddress(traceLogLine);

				for (final ReilInstruction reilInstruction : reil.getNodes().get(0).getInstructions())
				{
					builder.add(new TaintElement(reilInstruction, address));
				}
			}
			
			TaintGraph graph = builder.getGraph();

//			FileHelpers.writeTextFile("C:\\foo.gml", GmlConverter.toGml(graph));
			FileHelpers.writeTextFile("C:\\filtered.gml", GmlConverter.toGml(TaintGraphFilter.filter(graph, new AddressFilter(0x431))));
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final IllegalTraceLineException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final InternalTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static IAddress getMemoryAddress(TraceLogLine traceLogLine)
	{
		if (traceLogLine.getInstruction().startsWith("PHA"))
		{
			return new CAddress(0x10000 + Long.valueOf(traceLogLine.getValueS(), 16));
		}
		
		return traceLogLine.getMemoryAddress() == null ? null : new CAddress(Long.valueOf(traceLogLine.getMemoryAddress(), 16));
	}

	public static IFilledList<TraceLogLine> parse(final File file) throws IOException, IllegalTraceLineException
	{
		final BufferedReader reader = new BufferedReader(new FileReader(file));

		final IFilledList<TraceLogLine> lines = new FilledList<TraceLogLine>();

		String currentLine = null;

		int counter = 0;

		while ((currentLine = reader.readLine()) != null)
		{
//			if (counter++ == 11) break;

			final TraceLogLine line = parseLine(currentLine);

			if (line == null)
			{
				continue;
			}

			lines.add(line);
		}

		return lines;
	}

	public static TraceLogLine parseLine(final String currentLine) throws IllegalTraceLineException
	{
		if (currentLine.contains("Trace Log File"))
		{
			return null;
		}

		if (currentLine.length() < 68)
		{
			throw new IllegalTraceLineException(currentLine);
		}

		// For performance reasons we are not handling the whole line
		// with a regular expression.

		final String address = currentLine.substring(1, 5);
		final String data = currentLine.substring(6, 16).replaceAll(" ", "");

		final String inner = currentLine.substring(16, 43);

		final Matcher matcher = linePattern.matcher(inner);

		if (!matcher.matches())
		{
			throw new IllegalTraceLineException(currentLine);
		}

		final String instruction = matcher.group(1).trim();
		String memoryAddress = matcher.group(3);
		final String memoryValue = matcher.group(5);

		if (memoryAddress == null && memoryValue != null)
		{
			final int index = currentLine.indexOf(" =");

			memoryAddress = currentLine.substring(index - 4, index);
		}

		final String valueA = currentLine.substring(45, 47);
		final String valueX = currentLine.substring(50, 52);
		final String valueY = currentLine.substring(55, 57);
		final String valueS = currentLine.substring(60, 62);
		final String valueFlags = currentLine.substring(65, 73);

		return new TraceLogLine(address, data, instruction, memoryAddress, memoryValue, valueA, valueX, valueY, valueS, valueFlags);
	}
}

package net.sourceforge.jnhf.examples;

import java.io.File;
import java.io.IOException;

import net.sourceforge.jnhf.fceux.tracefile.IllegalTraceLineException;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogLine;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogParser;
import net.sourceforge.jnhf.helpers.FileHelpers;
import net.sourceforge.jnhf.helpers.GmlConverter;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.helpers.IGmlEnhancer;
import net.sourceforge.jnhf.reil.InternalTranslationException;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.tainttracker.AddressFilter;
import net.sourceforge.jnhf.tainttracker.LogfileConverter;
import net.sourceforge.jnhf.tainttracker.TaintGraph;
import net.sourceforge.jnhf.tainttracker.TaintGraphFilter;
import net.sourceforge.jnhf.tainttracker.TaintGraphNode;

/**
 * This example program demonstrates how to parse an FCEUX trace log
 * and generate a filtered taint tracking graph from the parsed trace
 * log data. The generated graph is written to a file.
 */
public final class TaintGraphWriter
{
	public static void main(final String[] args)
	{
		// Step I  : Parse a log file
		// Step II : Create a taint graph from the parsed file
		// Step III: Write the file to disk.

		final String logFile = "F:\\fce\\simplified.log";
		final int address = 0x431;

		final String unfilteredOutputFile = "C:\\foo.gml";
		final String filtedOutputFile = "C:\\filtered.gml";

		try
		{
			System.out.printf("Parsing log file %s\n", logFile);

			final IFilledList<TraceLogLine> lines = TraceLogParser.parse(new File(logFile));

			System.out.println("Creating taint graph");

			final TaintGraph graph = LogfileConverter.buildGraph(lines);

			System.out.println("Writing graph files to disk");

			FileHelpers.writeTextFile(unfilteredOutputFile, GmlConverter.toGml(graph, new MyEnhancer(address)));
			FileHelpers.writeTextFile(filtedOutputFile, GmlConverter.toGml(TaintGraphFilter.filter(graph, new AddressFilter(address)), new MyEnhancer(address)));
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		catch (final IllegalTraceLineException e)
		{
			e.printStackTrace();
		}
		catch (final InternalTranslationException e)
		{
			e.printStackTrace();
		}
	}

	private static class MyEnhancer implements IGmlEnhancer<TaintGraphNode>
	{
		private final int m_address;

		public MyEnhancer(final int address)
		{
			m_address = address;
		}

		@Override
		public String enhance(final TaintGraphNode node)
		{
			final String mnemonic = node.getInstruction().getMnemonic();

			if (node.getStore().getAddress() != null && node.getStore().getAddress().toLong() == m_address)
			{
				return "fill \"#00FFFF\"";
			}
			else if (ReilHelpers.OPCODE_LDM.equals(mnemonic) || ReilHelpers.OPCODE_STM.equals(mnemonic))
			{
				return "fill \"#FF0000\"";
			}
			else if (ReilHelpers.OPCODE_JCC.equals(mnemonic))
			{
				return "fill \"#00FF00\"";
			}
			else
			{
				return null;
			}
		}
	}
}

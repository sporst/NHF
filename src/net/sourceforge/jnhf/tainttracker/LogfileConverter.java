package net.sourceforge.jnhf.tainttracker;

import net.sourceforge.jnhf.disassembler.Address;
import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogLine;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.InternalTranslationException;
import net.sourceforge.jnhf.reil.ReilGraph;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.ReilTranslator;

public class LogfileConverter
{
	/**
	 * Returns the memory address accessed by a given instruction.
	 *
	 * @param traceLogLine A line from the trace log.
	 *
	 * @return The accessed memory address or null if the instruction in the
	 * log line does not actually access the memory.
	 */
	private static IAddress getMemoryAddress(final TraceLogLine traceLogLine)
	{
		if (traceLogLine.getInstruction().startsWith("PHA"))
		{
			return new Address(0x10000 + Long.valueOf(traceLogLine.getValueS(), 16));
		}

		if (traceLogLine.getInstruction().startsWith("PHP"))
		{
			return new Address(0x10000 + Long.valueOf(traceLogLine.getValueS(), 16));
		}

		if (traceLogLine.getInstruction().startsWith("PLA"))
		{
			return new Address(0x10000 + Long.valueOf(traceLogLine.getValueS(), 16));
		}

		if (traceLogLine.getInstruction().startsWith("PLP"))
		{
			return new Address(0x10000 + Long.valueOf(traceLogLine.getValueS(), 16));
		}

		if (traceLogLine.getInstruction().startsWith("RTS"))
		{
			return new Address(0x10000 + Long.valueOf(traceLogLine.getValueS(), 16));
		}

		if (traceLogLine.getInstruction().startsWith("RTI"))
		{
			return new Address(0x10000 + Long.valueOf(traceLogLine.getValueS(), 16));
		}

		if (traceLogLine.getInstruction().startsWith("JSR"))
		{
			return new Address(0x10000 + Long.valueOf(traceLogLine.getValueS(), 16));
		}

		return traceLogLine.getMemoryAddress() == null ?
			null :
			new Address(Long.valueOf(traceLogLine.getMemoryAddress(), 16));
	}

	/**
	 * Creates a taint graph from a list of parsed trace log lines.
	 *
	 * @param lines The lines to put into the graph.
	 *
	 * @return The built taint graph.
	 *
	 * @throws InternalTranslationException Thrown if one of the instructions
	 * from the trace log could not be converted to REIL.
	 */
	public static TaintGraph buildGraph(final IFilledList<TraceLogLine> lines) throws InternalTranslationException
	{
		final TaintGraphBuilder builder = new TaintGraphBuilder();

		int counter = 0;

		for (final TraceLogLine traceLogLine : lines)
		{
			if (counter++ == 700)
			{
//				break;
			}

			final Instruction instruction = InstructionDisassembler.disassemble(Integer.valueOf(traceLogLine.getAddress(), 16), traceLogLine.getData());

			final ReilGraph reil = ReilTranslator.translate(instruction);

			final IAddress address = getMemoryAddress(traceLogLine);

			for (final ReilInstruction reilInstruction : reil.getNodes().get(0).getInstructions())
			{
				builder.add(new TaintElement(reilInstruction, address));
			}
		}

		return builder.getGraph();
	}

}

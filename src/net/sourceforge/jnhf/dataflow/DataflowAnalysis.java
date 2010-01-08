package net.sourceforge.jnhf.dataflow;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogLine;
import net.sourceforge.jnhf.helpers.IFilledList;

public class DataflowAnalysis
{
	public static void analyze(final IFilledList<TraceLogLine> trace)
	{
		for (final TraceLogLine traceLogLine : trace)
		{
			final Instruction instruction = InstructionDisassembler.disassemble(0, traceLogLine.getData());

			System.out.println(instruction);
		}
	}
}

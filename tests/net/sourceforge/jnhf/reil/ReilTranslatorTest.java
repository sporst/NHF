package net.sourceforge.jnhf.reil;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.fceux.tracefile.IllegalTraceLineException;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogLine;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogParser;
import net.sourceforge.jnhf.reil.InternalTranslationException;
import net.sourceforge.jnhf.reil.ReilGraph;
import net.sourceforge.jnhf.reil.ReilTranslator;

import org.junit.Test;

public class ReilTranslatorTest
{
	@Test
	public void foo() throws IllegalTraceLineException, InternalTranslationException
	{
		final TraceLogLine line = TraceLogParser.parseLine("$FEB0:E8        INX                        A:01 X:00 Y:04 P:NvUbdizc");

		final Instruction instruction = InstructionDisassembler.disassemble(0, line.getData());

		final ReilGraph result = ReilTranslator.translate(instruction);

		System.out.println(result.getNodes().get(0).getInstructions());
	}
}

package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;
import net.sourceforge.jnhf.reil.translators.DeyTranslator;

import org.junit.Test;

public class DeyTranslatorTest
{
	@Test
	public void test()
	{
		final List<ReilInstruction> result = DeyTranslator.translate(new StandardEnvironment(), InstructionDisassembler.disassemble(0, "88"));

		System.out.println(result);
	}
}

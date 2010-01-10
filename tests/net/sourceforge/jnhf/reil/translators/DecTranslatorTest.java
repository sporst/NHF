package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;
import net.sourceforge.jnhf.reil.translators.DecTranslator;

import org.junit.Test;

public class DecTranslatorTest
{
	@Test
	public void test()
	{
		final List<ReilInstruction> result = DecTranslator.translate(new StandardEnvironment(), InstructionDisassembler.disassemble(0, "CE2C04"));

		System.out.println(result);
	}
}

package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;
import net.sourceforge.jnhf.reil.translators.OraTranslator;

import org.junit.Test;

public class OraTranslatorTest
{
	@Test
	public void test()
	{
		final List<ReilInstruction> result = OraTranslator.translate(new StandardEnvironment(), InstructionDisassembler.disassemble(0, "1D2C07"));

		System.out.println(result);
	}
}

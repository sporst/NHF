package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;
import net.sourceforge.jnhf.reil.translators.CmpTranslator;

import org.junit.Test;

public class CmpTranslatorTest
{
	@Test
	public void test()
	{
		final List<ReilInstruction> result = CmpTranslator.translate(new StandardEnvironment(), InstructionDisassembler.disassemble(0, "C904"));

		System.out.println(result);
	}
}

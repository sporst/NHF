package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;
import net.sourceforge.jnhf.reil.translators.JmpTranslator;

import org.junit.Test;

public class JmpTranslatorTest
{
	@Test
	public void test()
	{
		final List<ReilInstruction> result = JmpTranslator.translate(new StandardEnvironment(), InstructionDisassembler.disassemble(0, "4CA4FE"));

		System.out.println(result);
	}

	@Test
	public void testIndirect()
	{
		final List<ReilInstruction> result = JmpTranslator.translate(new StandardEnvironment(), InstructionDisassembler.disassemble(0, "6CC100"));

		System.out.println(result);
	}
}

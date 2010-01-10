package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;
import net.sourceforge.jnhf.reil.translators.InxTranslator;

import org.junit.Test;

public class InxTranslatorTest
{
	@Test
	public void test()
	{
		final List<ReilInstruction> result = InxTranslator.translate(new StandardEnvironment(), InstructionDisassembler.disassemble(0, "E8"));
	}
}

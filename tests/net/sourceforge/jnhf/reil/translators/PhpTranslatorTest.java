package net.sourceforge.jnhf.reil.translators;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.reil.InternalTranslationException;
import net.sourceforge.jnhf.reil.ReilGraph;
import net.sourceforge.jnhf.reil.ReilTranslator;

import org.junit.Test;

public class PhpTranslatorTest
{
	@Test
	public void test() throws InternalTranslationException
	{
		final ReilGraph result = ReilTranslator.translate(InstructionDisassembler.disassemble(0, "08"));

		System.out.println(result.getNodes().get(0).getInstructions());
	}
}

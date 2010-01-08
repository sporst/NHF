package reil.translators;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.reil.InternalTranslationException;
import net.sourceforge.jnhf.reil.ReilGraph;
import net.sourceforge.jnhf.reil.ReilTranslator;

import org.junit.Test;

public class PhaTranslatorTest
{
	@Test
	public void test() throws InternalTranslationException
	{
		final ReilGraph result = ReilTranslator.translate(InstructionDisassembler.disassemble(0, "48"));

		System.out.println(result.getNodes().get(0).getInstructions());
	}
}

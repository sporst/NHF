package reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;
import net.sourceforge.jnhf.reil.translators.IncTranslator;

import org.junit.Test;

public class IncTranslatorTest
{
	@Test
	public void test()
	{
		final List<ReilInstruction> result = IncTranslator.translate(new StandardEnvironment(), InstructionDisassembler.disassemble(0, "E692"));

		System.out.println(result);
	}
}

package reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;
import net.sourceforge.jnhf.reil.translators.JsrTranslator;

import org.junit.Test;

public class JsrTranslatorTest
{
	@Test
	public void test()
	{
		final List<ReilInstruction> result = JsrTranslator.translate(new StandardEnvironment(), InstructionDisassembler.disassemble(0xC13A, "205EFF"));

		System.out.println(result);
	}
}

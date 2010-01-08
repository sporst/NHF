package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.ListHelpers;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class BeqTranslator
{

	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final int offset = instruction.getAddress() * 0x100;
		final String jumpTarget = instruction.getOperand().getRoot().getValue();

		return ListHelpers.list(ReilHelpers.createJcc(offset, OperandSize.BYTE, "Z", OperandSize.WORD, jumpTarget));
	}

}

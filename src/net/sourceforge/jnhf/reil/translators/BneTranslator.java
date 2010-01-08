package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class BneTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		final String jumpTarget = instruction.getOperand().getRoot().getValue();

		final String negatedZ = environment.getNextVariableString();

		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		instructions.add(ReilHelpers.createBisz(offset++, OperandSize.BYTE, "Z", OperandSize.BYTE, negatedZ));
		instructions.add(ReilHelpers.createJcc(offset++, OperandSize.BYTE, negatedZ, OperandSize.WORD, jumpTarget));

		return instructions;
	}
}

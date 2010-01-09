package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class TxsTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final long baseOffset = instruction.getAddress() * 0x100;

		final String maskedSp = environment.getNextVariableString();

		instructions.add(ReilHelpers.createAnd(baseOffset, OperandSize.WORD, "SP", OperandSize.WORD, String.valueOf(0xFF00), OperandSize.WORD, maskedSp, instruction));
		instructions.add(ReilHelpers.createOr(baseOffset + 1, OperandSize.WORD, maskedSp, OperandSize.BYTE, "X", OperandSize.WORD, "SP", instruction));

		return instructions;
	}
}

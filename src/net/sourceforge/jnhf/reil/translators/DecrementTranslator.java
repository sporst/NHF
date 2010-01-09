package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class DecrementTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction, final String operand)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final long baseOffset = instruction.getAddress() * 0x100;
		long offset = baseOffset;

		final String subtractResult = environment.getNextVariableString();

		instructions.add(ReilHelpers.createSub(offset++, OperandSize.BYTE, operand, OperandSize.BYTE, "1", OperandSize.WORD, subtractResult, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.WORD, subtractResult, OperandSize.BYTE, "255", OperandSize.BYTE, operand, instruction));

		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), operand, instruction));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), operand, instruction));

		return instructions;
	}
}

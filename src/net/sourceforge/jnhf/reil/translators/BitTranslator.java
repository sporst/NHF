package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class BitTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		final TranslationResult operandResult = OperandTranslator.translate(environment, offset, instruction.getOperand(), true, instruction);
		instructions.addAll(operandResult.getInstructions());
		offset = baseOffset + instructions.size();

		final String andResult = environment.getNextVariableString();
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, operandResult.getResultRegister(), OperandSize.BYTE, "A", OperandSize.BYTE, andResult, instruction));

		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), andResult, instruction));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), andResult, instruction));

		return instructions;
	}
}

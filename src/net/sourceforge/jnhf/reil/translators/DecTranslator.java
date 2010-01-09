package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class DecTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		final TranslationResult operandResult = OperandTranslator.translate(environment, offset, instruction.getOperand(), true, instruction);
		instructions.addAll(operandResult.getInstructions());

		offset = baseOffset + instructions.size();

		final String subResult = environment.getNextVariableString();
		final String truncatedResult = environment.getNextVariableString();

		instructions.add(ReilHelpers.createSub(offset++, OperandSize.BYTE, operandResult.getResultRegister(), OperandSize.BYTE, "1", OperandSize.WORD, subResult, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.WORD, subResult, OperandSize.BYTE, "255", OperandSize.BYTE, truncatedResult, instruction));

		instructions.add(ReilHelpers.createStm(offset++, OperandSize.BYTE, truncatedResult, OperandSize.WORD, operandResult.getMemoryAddress(), instruction));

		instructions.addAll(FlagTranslator.translateZ(environment, offset + instructions.size(), truncatedResult, instruction));
		instructions.addAll(FlagTranslator.translateN(environment, offset + instructions.size(), truncatedResult, instruction));

		return instructions;
	}
}

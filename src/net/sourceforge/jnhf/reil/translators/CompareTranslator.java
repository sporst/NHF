package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class CompareTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction, final String operand)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		final TranslationResult operandResult = OperandTranslator.translate(environment, offset, instruction.getOperand(), true, instruction);
		instructions.addAll(operandResult.getInstructions());

		offset = baseOffset + instructions.size();

		final String subtractionResult = environment.getNextVariableString();
		final String maskedResult = environment.getNextVariableString();

		instructions.add(ReilHelpers.createSub(offset++, OperandSize.BYTE, operand, OperandSize.BYTE, operandResult.getResultRegister(), OperandSize.WORD, subtractionResult, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.WORD, subtractionResult, OperandSize.BYTE, "255", OperandSize.BYTE, maskedResult, instruction));

		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), maskedResult, instruction));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), maskedResult, instruction));
		instructions.addAll(FlagTranslator.translateC(environment, baseOffset + instructions.size(), subtractionResult, instruction));
		instructions.add(ReilHelpers.createBisz(baseOffset + instructions.size(), OperandSize.BYTE, "C", OperandSize.BYTE, "C", instruction));

		return instructions;
	}
}

package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class AdcTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		final TranslationResult operandResult = OperandTranslator.translate(environment, offset, instruction.getOperand(), true, instruction);
		instructions.addAll(operandResult.getInstructions());

		offset = baseOffset + instructions.size();

		final String subResult1 = environment.getNextVariableString();
		final String subResult2 = environment.getNextVariableString();

		instructions.add(ReilHelpers.createAdd(offset++, OperandSize.BYTE, "A", OperandSize.BYTE, operandResult.getResultRegister(), OperandSize.WORD, subResult1, instruction));
		instructions.add(ReilHelpers.createAdd(offset++, OperandSize.WORD, subResult1, OperandSize.BYTE, "C", OperandSize.WORD, subResult2, instruction));

		instructions.addAll(FlagTranslator.translateV2(environment, baseOffset + instructions.size(), "A", operandResult.getResultRegister(), subResult2, instruction));

		instructions.add(ReilHelpers.createAnd(baseOffset + instructions.size(), OperandSize.WORD, subResult2, OperandSize.BYTE, "255", OperandSize.BYTE, "A", instruction));

		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), "A", instruction));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), "A", instruction));
		instructions.addAll(FlagTranslator.translateC(environment, baseOffset + instructions.size(), subResult2, instruction));

		return instructions;
	}
}
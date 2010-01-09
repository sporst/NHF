package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class SbcTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		final TranslationResult operandResult = OperandTranslator.translate(environment, offset, instruction.getOperand(), true, instruction);
		instructions.addAll(operandResult.getInstructions());

		offset = baseOffset + instructions.size();

		final String negatedC = environment.getNextVariableString();
		final String subResult1 = environment.getNextVariableString();
		final String subResult2 = environment.getNextVariableString();

		// Negate C
		instructions.add(ReilHelpers.createBisz(offset++, OperandSize.BYTE, "C", OperandSize.BYTE, negatedC, instruction));

		instructions.add(ReilHelpers.createSub(offset++, OperandSize.BYTE, "A", OperandSize.BYTE, operandResult.getResultRegister(), OperandSize.WORD, subResult1, instruction));
		instructions.add(ReilHelpers.createSub(offset++, OperandSize.WORD, subResult1, OperandSize.BYTE, negatedC, OperandSize.WORD, subResult2, instruction));

		instructions.addAll(FlagTranslator.translateV(environment, baseOffset + instructions.size(), "A", operandResult.getResultRegister(), subResult2, instruction));

		offset = baseOffset + instructions.size();

		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.WORD, subResult2, OperandSize.BYTE, "255", OperandSize.BYTE, "A", instruction));

		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), "A", instruction));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), "A", instruction));
		instructions.addAll(FlagTranslator.translateC(environment, baseOffset + instructions.size(), subResult2, instruction));
		instructions.add(ReilHelpers.createBisz(baseOffset + instructions.size(), OperandSize.BYTE, "C", OperandSize.BYTE, "C", instruction));

		return instructions;
	}
}

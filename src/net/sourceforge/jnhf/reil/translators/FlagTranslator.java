package net.sourceforge.jnhf.reil.translators;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class FlagTranslator
{
	public static IFilledList<ReilInstruction> translateC(final StandardEnvironment environment, long offset, final String result, final Instruction instruction)
	{
		// CF is set if A >= compareValue
		//    => CF is set if (A - compareValue) >= 0

		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final String shiftedResult = environment.getNextVariableString();

		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.WORD, result, OperandSize.BYTE, "-8", OperandSize.BYTE, shiftedResult, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, shiftedResult, OperandSize.BYTE, "1", OperandSize.BYTE, "C", instruction));

		return instructions;
	}

	public static IFilledList<ReilInstruction> translateN(final StandardEnvironment environment, long offset, final String result, final Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final String isolatedMsb = environment.getNextVariableString();
		final String resultPositive = environment.getNextVariableString();

		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, result, OperandSize.BYTE, "128", OperandSize.BYTE, isolatedMsb, instruction));
		instructions.add(ReilHelpers.createBisz(offset++, OperandSize.BYTE, isolatedMsb, OperandSize.BYTE, resultPositive, instruction));
		instructions.add(ReilHelpers.createBisz(offset++, OperandSize.BYTE, resultPositive, OperandSize.BYTE, "N", instruction));

		return instructions;
	}

	public static IFilledList<ReilInstruction> translateV(final StandardEnvironment environment, long offset, final String firstOperand, final String secondOperand, final String result, final Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final String temp1 = environment.getNextVariableString();
		final String temp2 = environment.getNextVariableString();
		final String temp3 = environment.getNextVariableString();
		final String temp4 = environment.getNextVariableString();

		instructions.add(ReilHelpers.createXor(offset++, OperandSize.BYTE, firstOperand, OperandSize.BYTE, result, OperandSize.BYTE, temp1, instruction));
		instructions.add(ReilHelpers.createXor(offset++, OperandSize.BYTE, firstOperand, OperandSize.BYTE, secondOperand, OperandSize.BYTE, temp2, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, temp1, OperandSize.BYTE, temp2, OperandSize.BYTE, temp3, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, temp3, OperandSize.BYTE, "128", OperandSize.BYTE, temp4, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, temp4, OperandSize.BYTE, "-7", OperandSize.BYTE, "V", instruction));

		return instructions;
	}

	public static IFilledList<ReilInstruction> translateV2(final StandardEnvironment environment, long offset, final String firstOperand, final String secondOperand, final String result, final Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final String temp1 = environment.getNextVariableString();
		final String temp2 = environment.getNextVariableString();
		final String temp3 = environment.getNextVariableString();
		final String temp4 = environment.getNextVariableString();
		final String temp5 = environment.getNextVariableString();

		instructions.add(ReilHelpers.createXor(offset++, OperandSize.BYTE, firstOperand, OperandSize.BYTE, result, OperandSize.BYTE, temp1, instruction));
		instructions.add(ReilHelpers.createXor(offset++, OperandSize.BYTE, firstOperand, OperandSize.BYTE, secondOperand, OperandSize.BYTE, temp2, instruction));
		instructions.add(ReilHelpers.createXor(offset++, OperandSize.BYTE, temp2, OperandSize.BYTE, "128", OperandSize.BYTE, temp3, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, temp1, OperandSize.BYTE, temp3, OperandSize.BYTE, temp4, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, temp4, OperandSize.BYTE, "128", OperandSize.BYTE, temp5, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, temp5, OperandSize.BYTE, "-7", OperandSize.BYTE, "V", instruction));

		return instructions;
	}

	public static IFilledList<ReilInstruction> translateZ(final StandardEnvironment environment, long offset, final String result, Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		instructions.add(ReilHelpers.createBisz(offset++, OperandSize.BYTE, result, OperandSize.BYTE, "Z", instruction));

		return instructions;
	}
}

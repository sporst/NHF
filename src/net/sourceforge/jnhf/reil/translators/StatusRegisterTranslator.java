package net.sourceforge.jnhf.reil.translators;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class StatusRegisterTranslator
{
	public static TranslationResult merge(final StandardEnvironment environment, long offset, Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final String shiftedN = environment.getNextVariableString();
		final String shiftedV = environment.getNextVariableString();
		final String shiftedB = environment.getNextVariableString();
		final String shiftedD = environment.getNextVariableString();
		final String shiftedI = environment.getNextVariableString();
		final String shiftedZ = environment.getNextVariableString();
		final String tempResult1 = environment.getNextVariableString();
		final String tempResult2 = environment.getNextVariableString();
		final String tempResult3 = environment.getNextVariableString();
		final String tempResult4 = environment.getNextVariableString();
		final String tempResult5 = environment.getNextVariableString();
		final String tempResult6 = environment.getNextVariableString();
		final String result = environment.getNextVariableString();

		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, "N", OperandSize.BYTE, "7", OperandSize.BYTE, shiftedN, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, "V", OperandSize.BYTE, "6", OperandSize.BYTE, shiftedV, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, "B", OperandSize.BYTE, "4", OperandSize.BYTE, shiftedB, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, "D", OperandSize.BYTE, "3", OperandSize.BYTE, shiftedD, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, "I", OperandSize.BYTE, "2", OperandSize.BYTE, shiftedI, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, "Z", OperandSize.BYTE, "1", OperandSize.BYTE, shiftedZ, instruction));

		instructions.add(ReilHelpers.createOr(offset++, OperandSize.BYTE, shiftedN, OperandSize.BYTE, shiftedV, OperandSize.BYTE, tempResult1, instruction));
		instructions.add(ReilHelpers.createOr(offset++, OperandSize.BYTE, String.valueOf(0x20), OperandSize.BYTE, shiftedB, OperandSize.BYTE, tempResult2, instruction));
		instructions.add(ReilHelpers.createOr(offset++, OperandSize.BYTE, shiftedD, OperandSize.BYTE, shiftedI, OperandSize.BYTE, tempResult3, instruction));
		instructions.add(ReilHelpers.createOr(offset++, OperandSize.BYTE, shiftedZ, OperandSize.BYTE, "C", OperandSize.BYTE, tempResult4, instruction));

		instructions.add(ReilHelpers.createOr(offset++, OperandSize.BYTE, tempResult1, OperandSize.BYTE, tempResult2, OperandSize.BYTE, tempResult5, instruction));
		instructions.add(ReilHelpers.createOr(offset++, OperandSize.BYTE, tempResult3, OperandSize.BYTE, tempResult4, OperandSize.BYTE, tempResult6, instruction));

		instructions.add(ReilHelpers.createOr(offset++, OperandSize.BYTE, tempResult5, OperandSize.BYTE, tempResult6, OperandSize.BYTE, result, instruction));

		return new TranslationResult(offset, result, OperandSize.BYTE, null, instructions);
	}

	public static IFilledList<ReilInstruction> split(final StandardEnvironment environment, long offset, final String loadedValue, final Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final String maskedN = environment.getNextVariableString();
		final String maskedV = environment.getNextVariableString();
		final String maskedB = environment.getNextVariableString();
		final String maskedD = environment.getNextVariableString();
		final String maskedI = environment.getNextVariableString();
		final String maskedZ = environment.getNextVariableString();

		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, loadedValue, OperandSize.BYTE, String.valueOf(0x80), OperandSize.BYTE, maskedN, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, maskedN, OperandSize.BYTE, "-7", OperandSize.BYTE, "N", instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, loadedValue, OperandSize.BYTE, String.valueOf(0x40), OperandSize.BYTE, maskedV, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, maskedV, OperandSize.BYTE, "-6", OperandSize.BYTE, "V", instruction));

		// Bit 5 is always 1

		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, loadedValue, OperandSize.BYTE, String.valueOf(0x10), OperandSize.BYTE, maskedB, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, maskedB, OperandSize.BYTE, "-4", OperandSize.BYTE, "B", instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, loadedValue, OperandSize.BYTE, String.valueOf(0x08), OperandSize.BYTE, maskedD, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, maskedD, OperandSize.BYTE, "-3", OperandSize.BYTE, "D", instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, loadedValue, OperandSize.BYTE, String.valueOf(0x04), OperandSize.BYTE, maskedI, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, maskedI, OperandSize.BYTE, "-2", OperandSize.BYTE, "I", instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, loadedValue, OperandSize.BYTE, String.valueOf(0x02), OperandSize.BYTE, maskedZ, instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, maskedZ, OperandSize.BYTE, "-1", OperandSize.BYTE, "Z", instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.BYTE, loadedValue, OperandSize.BYTE, String.valueOf(0x01), OperandSize.BYTE, "C", instruction));

		return instructions;
	}
}

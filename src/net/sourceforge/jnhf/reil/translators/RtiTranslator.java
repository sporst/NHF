package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class RtiTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		final String loadedValue = environment.getNextVariableString();
		final String added1 = environment.getNextVariableString();
		final String loadedAddress = environment.getNextVariableString();
		final String addedAddress = environment.getNextVariableString();
		final String correctedAddress = environment.getNextVariableString();
		final String correctedStackPointer = environment.getNextVariableString();

		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		// Take the status register off the stack
		instructions.add(ReilHelpers.createLdm(offset++, OperandSize.WORD, "SP", OperandSize.BYTE, loadedValue, instruction));
		instructions.addAll(StatusRegisterTranslator.split(environment, offset, loadedValue, instruction));

		offset = baseOffset + instructions.size();

		// Correct the stack
		instructions.add(ReilHelpers.createAdd(offset++, OperandSize.WORD, "SP", OperandSize.WORD, "1", OperandSize.DWORD, added1, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.DWORD, added1, OperandSize.WORD, "65535", OperandSize.WORD, "SP", instruction));

		// Take return offset off the stack
		instructions.add(ReilHelpers.createLdm(offset++, OperandSize.WORD, "SP", OperandSize.WORD, loadedAddress, instruction));

		// Add 1 to the return address
		instructions.add(ReilHelpers.createAdd(offset++, OperandSize.WORD, loadedAddress, OperandSize.BYTE, "1", OperandSize.DWORD, addedAddress, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.DWORD, addedAddress, OperandSize.WORD, "65535", OperandSize.WORD, correctedAddress, instruction));

		// Correct the stack
		instructions.add(ReilHelpers.createAdd(offset++, OperandSize.WORD, "SP", OperandSize.WORD, "2", OperandSize.DWORD, correctedStackPointer, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.DWORD, correctedStackPointer, OperandSize.WORD, "65535", OperandSize.WORD, "SP", instruction));

		// Jump to the target
		instructions.add(ReilHelpers.createJcc(offset++, OperandSize.BYTE, "1", OperandSize.WORD, correctedAddress, instruction));

		return instructions;
	}

}

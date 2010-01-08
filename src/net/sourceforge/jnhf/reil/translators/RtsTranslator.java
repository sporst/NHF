package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class RtsTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		final String loadedAddress = environment.getNextVariableString();
		final String addedAddress = environment.getNextVariableString();
		final String correctedAddress = environment.getNextVariableString();
		final String subtractedStackPointer = environment.getNextVariableString();

		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		// Correct the stack
		instructions.add(ReilHelpers.createAdd(offset++, OperandSize.WORD, "SP", OperandSize.WORD, "1", OperandSize.DWORD, subtractedStackPointer));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.DWORD, subtractedStackPointer, OperandSize.WORD, "65535", OperandSize.WORD, "SP"));

		// Take return offset off the stack
		instructions.add(ReilHelpers.createLdm(offset++, OperandSize.WORD, "SP", OperandSize.WORD, loadedAddress));

		// Add 1 to the return address
		instructions.add(ReilHelpers.createAdd(offset++, OperandSize.WORD, loadedAddress, OperandSize.BYTE, "1", OperandSize.WORD, addedAddress));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.WORD, addedAddress, OperandSize.BYTE, "255", OperandSize.WORD, correctedAddress));

		// Correct the stack - Part II
		instructions.add(ReilHelpers.createAdd(offset++, OperandSize.WORD, "SP", OperandSize.WORD, "1", OperandSize.DWORD, subtractedStackPointer));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.DWORD, subtractedStackPointer, OperandSize.WORD, "65535", OperandSize.WORD, "SP"));

		// Jump to the target
		instructions.add(ReilHelpers.createJcc(offset++, OperandSize.BYTE, "1", OperandSize.WORD, correctedAddress));

		return instructions;
	}

}

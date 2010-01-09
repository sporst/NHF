package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class StackTranslator
{
	public static List<ReilInstruction> decrementStackPointer(final long offset, final StandardEnvironment environment, final Instruction instruction)
	{
		final String decrementedStackPointer = environment.getNextVariableString();

		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		instructions.add(ReilHelpers.createSub(offset, OperandSize.WORD, "SP", OperandSize.WORD, "1", OperandSize.DWORD, decrementedStackPointer, instruction));
		instructions.add(ReilHelpers.createAnd(offset + 1, OperandSize.DWORD, decrementedStackPointer, OperandSize.WORD, "65535", OperandSize.WORD, "SP", instruction));

		return instructions;
	}

	public static List<ReilInstruction> incrementStackPointer(final long offset, final StandardEnvironment environment, final Instruction instruction)
	{
		final String decrementedStackPointer = environment.getNextVariableString();

		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		instructions.add(ReilHelpers.createAdd(offset, OperandSize.WORD, "SP", OperandSize.WORD, "1", OperandSize.DWORD, decrementedStackPointer, instruction));
		instructions.add(ReilHelpers.createAnd(offset + 1, OperandSize.DWORD, decrementedStackPointer, OperandSize.WORD, "65535", OperandSize.WORD, "SP", instruction));

		return instructions;
	}
}

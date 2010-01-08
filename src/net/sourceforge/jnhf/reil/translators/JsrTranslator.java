package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class JsrTranslator
{

	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		final String jumpTarget = instruction.getOperand().getRoot().getValue();

		final String subtractedStackPointer = environment.getNextVariableString();

		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		instructions.add(ReilHelpers.createSub(offset++, OperandSize.WORD, "SP", OperandSize.BYTE, "1", OperandSize.DWORD, subtractedStackPointer));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.DWORD, subtractedStackPointer, OperandSize.WORD, "65535", OperandSize.WORD, "SP"));

		// Push offset + 2 onto the stack
		instructions.add(ReilHelpers.createStm(offset++, OperandSize.WORD, String.valueOf(instruction.getAddress() + 2), OperandSize.WORD, "SP"));

		instructions.add(ReilHelpers.createSub(offset++, OperandSize.WORD, "SP", OperandSize.BYTE, "1", OperandSize.DWORD, subtractedStackPointer));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.DWORD, subtractedStackPointer, OperandSize.WORD, "65535", OperandSize.WORD, "SP"));

		instructions.add(ReilHelpers.createJcc(offset++, OperandSize.BYTE, "1", OperandSize.WORD, jumpTarget));

		return instructions;
	}

}

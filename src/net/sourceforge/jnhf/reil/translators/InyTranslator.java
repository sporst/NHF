package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class InyTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final long baseOffset = instruction.getAddress() * 0x100;
		long offset = baseOffset;

		final String addResult = environment.getNextVariableString();

		instructions.add(ReilHelpers.createAdd(offset++, OperandSize.BYTE, "Y", OperandSize.BYTE, "1", OperandSize.WORD, addResult, instruction));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.WORD, addResult, OperandSize.BYTE, "255", OperandSize.BYTE, "Y", instruction));

		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), "Y", instruction));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), "Y", instruction));

		return instructions;
	}
}

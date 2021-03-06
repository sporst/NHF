package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class TsxTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final long baseOffset = instruction.getAddress() * 0x100;

		instructions.add(ReilHelpers.createAnd(baseOffset, OperandSize.WORD, "SP", OperandSize.BYTE, "255", OperandSize.BYTE, "X", instruction));
		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), "X", instruction));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), "X", instruction));

		return instructions;
	}
}

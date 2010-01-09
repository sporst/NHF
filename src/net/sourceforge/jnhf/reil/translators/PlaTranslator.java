package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class PlaTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final long baseOffset = instruction.getAddress() * 0x100;

		instructions.addAll(StackTranslator.incrementStackPointer(baseOffset, environment, instruction));
		instructions.add(ReilHelpers.createLdm(baseOffset + instructions.size(), OperandSize.BYTE, "SP", OperandSize.BYTE, "A", instruction));
		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), "A", instruction));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), "A", instruction));

		return instructions;
	}
}

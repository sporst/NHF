package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class PlpTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final long baseOffset = instruction.getAddress() * 0x100;

		final String loadedValue = environment.getNextVariableString();

		instructions.addAll(StackTranslator.incrementStackPointer(baseOffset + instructions.size(), environment));

		instructions.add(ReilHelpers.createLdm(baseOffset + instructions.size(), OperandSize.BYTE, "SP", OperandSize.BYTE, loadedValue));
		instructions.addAll(StatusRegisterTranslator.split(environment, baseOffset + instructions.size(), loadedValue));

		return instructions;
	}
}

package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class PhpTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final long baseOffset = instruction.getAddress() * 0x100;
		long offset = baseOffset;

		final TranslationResult result = StatusRegisterTranslator.merge(environment, offset, instruction);

		instructions.addAll(result.getInstructions());

		offset = baseOffset + instructions.size();

		instructions.add(ReilHelpers.createStm(baseOffset + instructions.size(), OperandSize.BYTE, result.getResultRegister(), OperandSize.BYTE, "SP", instruction));
		instructions.addAll(StackTranslator.decrementStackPointer(baseOffset + instructions.size(), environment, instruction));

		return instructions;
	}
}

package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class LoadTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction, final String resultRegister)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final int offset = instruction.getAddress() * 0x100;

		final TranslationResult operandResult = OperandTranslator.translate(environment, offset, instruction.getOperand(), true, instruction);

		instructions.addAll(operandResult.getInstructions());
		instructions.add(ReilHelpers.createStr(offset + instructions.size(), OperandSize.BYTE, operandResult.getResultRegister(), OperandSize.BYTE, resultRegister, instruction));

		instructions.addAll(FlagTranslator.translateZ(environment, offset + instructions.size(), resultRegister, instruction));
		instructions.addAll(FlagTranslator.translateN(environment, offset + instructions.size(), resultRegister, instruction));

		return instructions;
	}
}

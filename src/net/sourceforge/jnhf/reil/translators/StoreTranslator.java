package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class StoreTranslator
{
	public static List<ReilInstruction> translate( final StandardEnvironment environment, final Instruction instruction, final String storeRegister)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		int offset = instruction.getAddress() * 0x100;

		final TranslationResult operandResult = OperandTranslator.translate(environment, offset, instruction.getOperand(), false, instruction);

		instructions.addAll(operandResult.getInstructions());

		offset = instruction.getAddress() * 0x100 + instructions.size();

		instructions.add(ReilHelpers.createStm(offset, OperandSize.BYTE, storeRegister, operandResult.getResultSize(), operandResult.getResultRegister(), instruction));

		return instructions;
	}
}

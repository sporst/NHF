package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class AslTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		String source;
		String target;

		if (instruction.getOperand() == null)
		{
			source = "A";
			target = "A";
		}
		else
		{
			final TranslationResult operandResult = OperandTranslator.translate(environment, offset, instruction.getOperand(), true);
			instructions.addAll(operandResult.getInstructions());

			offset = baseOffset + instructions.size();

			source = operandResult.getResultRegister();
			target = operandResult.getMemoryAddress();
		}

		final String shiftResult = environment.getNextVariableString();
		final String truncatedResult = instruction.getOperand() == null ? "A" : environment.getNextVariableString();

		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, source, OperandSize.BYTE, "1", OperandSize.WORD, shiftResult));
		instructions.add(ReilHelpers.createAnd(offset++, OperandSize.WORD, shiftResult, OperandSize.BYTE, "255", OperandSize.BYTE, truncatedResult));

		if (instruction.getOperand() != null)
		{
			instructions.add(ReilHelpers.createStm(offset++, OperandSize.BYTE, truncatedResult, OperandSize.WORD, target));
		}

		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), truncatedResult));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), truncatedResult));
		instructions.addAll(FlagTranslator.translateC(environment, baseOffset + instructions.size(), shiftResult));

		return instructions;
	}
}

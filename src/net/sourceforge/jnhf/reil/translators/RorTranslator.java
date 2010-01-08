package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class RorTranslator
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
		final String shiftedC = environment.getNextVariableString();
		final String combinedResult = instruction.getOperand() == null ? "A" : environment.getNextVariableString();

		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, source, OperandSize.BYTE, "-1", OperandSize.BYTE, shiftResult));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, "C", OperandSize.BYTE, "7", OperandSize.BYTE, shiftedC));
		instructions.add(ReilHelpers.createAnd(baseOffset + instructions.size(), OperandSize.BYTE, source, OperandSize.BYTE, "1", OperandSize.BYTE, "C"));
		instructions.add(ReilHelpers.createOr(offset++, OperandSize.BYTE, shiftResult, OperandSize.BYTE, shiftedC, OperandSize.BYTE, combinedResult));

		if (instruction.getOperand() != null)
		{
			instructions.add(ReilHelpers.createStm(offset++, OperandSize.BYTE, combinedResult, OperandSize.WORD, target));
		}

		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), combinedResult));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), combinedResult));

		return instructions;
	}
}

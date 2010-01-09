package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class LsrTranslator
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		final int baseOffset = instruction.getAddress() * 0x100;
		int offset = baseOffset;

		String source;
		String target;
		final String result;

		if (instruction.getOperand() == null)
		{
			source = "A";
			target = "A";
		}
		else
		{
			final TranslationResult operandResult = OperandTranslator.translate(environment, offset, instruction.getOperand(), true, instruction);
			instructions.addAll(operandResult.getInstructions());

			offset = baseOffset + instructions.size();

			source = operandResult.getResultRegister();
			target = operandResult.getMemoryAddress();
		}

		final String shiftResult = environment.getNextVariableString();

		instructions.add(ReilHelpers.createAnd(offset, OperandSize.BYTE, source, OperandSize.BYTE, "1", OperandSize.BYTE, "C", instruction));
		instructions.add(ReilHelpers.createBsh(offset++, OperandSize.BYTE, source, OperandSize.BYTE, "-1", OperandSize.BYTE, shiftResult, instruction));

		if (instruction.getOperand() == null)
		{
			instructions.add(ReilHelpers.createStr(offset++, OperandSize.BYTE, shiftResult, OperandSize.BYTE, "A", instruction));

			result = "A";
		}
		else
		{
			instructions.add(ReilHelpers.createStm(offset++, OperandSize.BYTE, shiftResult, OperandSize.WORD, target, instruction));

			result = shiftResult;
		}

		instructions.addAll(FlagTranslator.translateZ(environment, baseOffset + instructions.size(), result, instruction));
		instructions.addAll(FlagTranslator.translateN(environment, baseOffset + instructions.size(), result, instruction));

		return instructions;
	}
}

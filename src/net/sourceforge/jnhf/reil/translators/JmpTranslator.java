package net.sourceforge.jnhf.reil.translators;

import java.util.List;

import net.sourceforge.jnhf.disassembler.ExpressionType;
import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.helpers.ListHelpers;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class JmpTranslator
{

	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction)
	{
		final int baseOffset = instruction.getAddress() * 0x100;

		if (instruction.getOperand().getRoot().getType() == ExpressionType.Integer)
		{
			final String jumpTarget = TranslationHelpers.toReilImmediate(instruction.getOperand().getRoot());

			return ListHelpers.list(ReilHelpers.createJcc(baseOffset, OperandSize.BYTE, "1", OperandSize.WORD, jumpTarget, instruction));
		}
		else
		{
			final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

			final TranslationResult operandResult = OperandTranslator.translate(environment, baseOffset, instruction.getOperand(), true, instruction);

			instructions.addAll(operandResult.getInstructions());

			int offset = baseOffset + instructions.size();

			final String loadedAddress = environment.getNextVariableString();

			instructions.add(ReilHelpers.createJcc(offset++, OperandSize.BYTE, "1", OperandSize.WORD, loadedAddress, instruction));

			return instructions;
		}
	}

}

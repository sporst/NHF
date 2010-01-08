package net.sourceforge.jnhf.reil.translators;

import net.sourceforge.jnhf.disassembler.ExpressionType;
import net.sourceforge.jnhf.disassembler.Operand;
import net.sourceforge.jnhf.disassembler.OperandExpression;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.StandardEnvironment;

public class OperandTranslator
{
	private static TranslationResult translate(final StandardEnvironment environment, final long offset, final OperandExpression expression, final boolean load)
	{
		final IFilledList<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		if (expression.getType() == ExpressionType.MemoryDereference)
		{
			final TranslationResult childResult1 = translate(environment, offset, expression.getChildren().get(0), false);
			instructions.addAll(childResult1.getInstructions());

			if (load)
			{
				final String resultVariable = environment.getNextVariableString();
				instructions.add(ReilHelpers.createLdm(childResult1.getNextOffset(), childResult1.getResultSize(), childResult1.getResultRegister(), OperandSize.BYTE, resultVariable));
				return new TranslationResult(childResult1.getNextOffset() + 2, resultVariable, OperandSize.WORD, childResult1.getResultRegister(), instructions);
			}
			else
			{
				return childResult1;
			}
		}
		if (expression.getType() == ExpressionType.IndirectMemoryDereference)
		{
			final TranslationResult childResult1 = translate(environment, offset, expression.getChildren().get(0), false);

			instructions.addAll(childResult1.getInstructions());

			final String resultVariable = environment.getNextVariableString();

			instructions.add(ReilHelpers.createLdm(childResult1.getNextOffset(), childResult1.getResultSize(), childResult1.getResultRegister(), OperandSize.WORD, resultVariable));

			return new TranslationResult(childResult1.getNextOffset() + 1, resultVariable, OperandSize.WORD, childResult1.getResultRegister(), instructions);
		}
		else if (expression.getType() == ExpressionType.Operator)
		{
			final TranslationResult childResult1 = translate(environment, offset, expression.getChildren().get(0), false);
			final TranslationResult childResult2 = translate(environment, childResult1.getNextOffset(), expression.getChildren().get(1), false);

			instructions.addAll(childResult1.getInstructions());
			instructions.addAll(childResult2.getInstructions());

			final String addedVariable = environment.getNextVariableString();
			final String resultVariable = environment.getNextVariableString();

			instructions.add(ReilHelpers.createAdd(childResult2.getNextOffset(), childResult1.getResultSize(), childResult1.getResultRegister(), childResult2.getResultSize(), childResult2.getResultRegister(), OperandSize.DWORD, addedVariable));
			instructions.add(ReilHelpers.createAnd(childResult2.getNextOffset() + 1, OperandSize.DWORD, addedVariable, OperandSize.WORD, "65535", OperandSize.WORD, resultVariable));

			return new TranslationResult(childResult2.getNextOffset() + 2, resultVariable, OperandSize.BYTE, null, instructions);
		}
		else if (expression.getType() == ExpressionType.Integer)
		{
			return new TranslationResult(offset, TranslationHelpers.toReilImmediate(expression), OperandSize.WORD, null, instructions);
		}
		else if (expression.getType() == ExpressionType.Register)
		{
			return new TranslationResult(offset, expression.getValue(), OperandSize.BYTE, null, instructions);
		}
		else
		{
			System.out.println(expression.getType());

			throw new IllegalStateException();
		}
	}

	public static TranslationResult translate(final StandardEnvironment environment, final long offset, final Operand operand, final boolean load)
	{
		return translate(environment, offset, operand.getRoot(), load);
	}
}

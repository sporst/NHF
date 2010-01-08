package net.sourceforge.jnhf.reil.translators;

import net.sourceforge.jnhf.disassembler.OperandExpression;

public class TranslationHelpers
{
	public static String toReilImmediate(final OperandExpression expression)
	{
		return String.valueOf(Integer.parseInt(expression.getValue(), 16));
	}
}

package net.sourceforge.jnhf.disassembler;

import net.sourceforge.jnhf.helpers.Commafier;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;

public final class OperandExpression
{
	private final String m_value;

	private OperandExpression m_parent;

	private final IFilledList<OperandExpression> m_children = new FilledList<OperandExpression>();

	public OperandExpression(final OperandExpression parent, final String value)
	{
		m_value = value;

		if (parent != null)
		{
			link(parent, this);
		}
	}

	private static void link(final OperandExpression parent, final OperandExpression child)
	{
		parent.m_children.add(child);
		child.m_parent = parent;
	}

	public FilledList<OperandExpression> getChildren()
	{
		return new FilledList<OperandExpression>(m_children);
	}

	public ExpressionType getType()
	{
		if (m_value.equals("["))
		{
			return ExpressionType.MemoryDereference;
		}
		else if (m_value.equals("("))
		{
			return ExpressionType.IndirectMemoryDereference;
		}
		else if (m_value.length() == 4)
		{
			return ExpressionType.Integer;
		}
		else if (m_value.equals("+"))
		{
			return ExpressionType.Operator;
		}
		else
		{
			return ExpressionType.Register;
		}
	}

	public String getValue()
	{
		return m_value;
	}

	@Override
	public String toString()
	{
		return m_children.isEmpty() ?
			getValue() :
			Commafier.commafy(m_children);
	}
}

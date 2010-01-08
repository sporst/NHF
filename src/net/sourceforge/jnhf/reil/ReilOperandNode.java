package net.sourceforge.jnhf.reil;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.disassembler.ExpressionType;

public class ReilOperandNode
{
	private final String m_value;

	private final List<ReilOperandNode> children = new ArrayList<ReilOperandNode>();

	private final Object m_type;

	public ReilOperandNode(final String value, final ExpressionType type)
	{
		if (value == null)
		{
			throw new IllegalArgumentException("Error: Value argument can not be null");
		}

		m_value = value;
		m_type = type;
	}

	public static void link(final ReilOperandNode parent, final ReilOperandNode child)
	{
		parent.children.add(child);
	}

	public List<ReilOperandNode> getChildren()
	{
		return new ArrayList<ReilOperandNode>(children);
	}

	public String getValue()
	{
		return m_value;
	}

}

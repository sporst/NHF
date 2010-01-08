package net.sourceforge.jnhf.reil;

import net.sourceforge.jnhf.disassembler.EdgeType;

public class ReilEdge
{
	private final ReilBlock m_source;
	private final ReilBlock m_target;

	private final EdgeType m_type;

	public ReilEdge(final ReilBlock source, final ReilBlock target, final EdgeType type)
	{
		if (source == null)
		{
			throw new IllegalArgumentException("Error: Source argument can't be null");
		}

		if (target == null)
		{
			throw new IllegalArgumentException("Error: Target argument can't be null");
		}

		m_source = source;
		m_target = target;
		m_type = type;
	}

	public ReilBlock getSource()
	{
		return m_source;
	}

	public ReilBlock getTarget()
	{
		return m_target;
	}

	public EdgeType getType()
	{
		return m_type;
	}

	@Override
	public String toString()
	{
		return m_source.getAddress().toHexString() + " -> " + m_target.getAddress().toHexString();
	}
}

package net.sourceforge.jnhf.disassembler;

/**
 * Represents a single address.
 */
public class Address implements IAddress
{
	private final long m_value;

	public Address(final long value)
	{
		m_value = value;
	}

	@Override
	public int compareTo(final IAddress o)
	{
		return (int) (m_value - o.toLong());
	}

	@Override
	public boolean equals(final Object rhs)
	{
		return rhs instanceof IAddress && ((IAddress) rhs).toLong() == m_value;
	}

	@Override
	public int hashCode()
	{
		return Long.valueOf(m_value).hashCode();
	}

	@Override
	public String toHexString()
	{
		return String.format("%04X", m_value);
	}

	@Override
	public long toLong()
	{
		return m_value;
	}

	@Override
	public String toString()
	{
		return toHexString();
	}
}

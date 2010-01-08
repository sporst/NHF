package net.sourceforge.jnhf.tainttracker;

import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.reil.ReilOperand;

public class AbstractStore
{
	private final ReilOperand m_operand;
	private final IAddress m_address;

	public AbstractStore(final IAddress address)
	{
		if (address == null)
		{
			throw new IllegalArgumentException("Address argument can not be null");
		}

		m_operand = null;
		m_address = address;
	}

	public AbstractStore(final ReilOperand operand)
	{
		if (operand == null)
		{
			throw new IllegalArgumentException("Operand argument can not be null");
		}

		m_operand = operand;
		m_address = null;
	}

	@Override
	public boolean equals(final Object rhs)
	{
		if (!(rhs instanceof AbstractStore))
		{
			return false;
		}

		final AbstractStore rhsOperand = (AbstractStore) rhs;

		return m_operand != null && rhsOperand.m_operand != null && m_operand.getValue().equals(rhsOperand.m_operand.getValue()) ||
			m_address != null && rhsOperand.m_address != null && m_address.equals(rhsOperand.m_address)
		;
	}

	@Override
	public int hashCode()
	{
		return m_operand == null ? m_address.hashCode() : m_operand.getValue().hashCode();
	}

	@Override
	public String toString()
	{
		return m_operand == null ? "$" + m_address.toHexString() : m_operand.toString();
	}
}
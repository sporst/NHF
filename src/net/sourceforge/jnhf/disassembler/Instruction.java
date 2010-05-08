package net.sourceforge.jnhf.disassembler;

public final class Instruction
{
	private final String m_mnemonic;
	private final Operand m_operand;
	private final int m_address;

	public Instruction(final int address, final String mnemonic, final Operand operand)
	{
		m_address = address;
		m_mnemonic = mnemonic;
		m_operand = operand;
	}

	public int getAddress()
	{
		return m_address;
	}

	public String getMnemonic()
	{
		return m_mnemonic;
	}

	public Operand getOperand()
	{
		return m_operand;
	}

	@Override
	public String toString()
	{
		return String.format("%04X: %s %s", m_address, m_mnemonic, m_operand == null ? "" : m_operand);
	}
}

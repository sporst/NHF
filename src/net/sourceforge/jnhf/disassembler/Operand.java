package net.sourceforge.jnhf.disassembler;

public final class Operand
{
	private final OperandExpression m_root;

	public Operand(final OperandExpression root)
	{
		m_root = root;
	}

	public OperandExpression getRoot()
	{
		return m_root;
	}

	@Override
	public String toString()
	{
		return m_root.toString();
	}
}

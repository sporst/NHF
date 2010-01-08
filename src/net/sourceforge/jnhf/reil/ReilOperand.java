package net.sourceforge.jnhf.reil;


/**
 * This class can be used to store information about the
 * operands of REIL instructions.
 *
 */
public class ReilOperand
{
	private final ReilOperandNode m_root;

	/**
	 * Creates a new ReilOperand object.
	 *
	 * @param root The root operand node of the operand.
	 */
	public ReilOperand(final ReilOperandNode root)
	{
		if (root == null)
		{
			throw new IllegalArgumentException("Error: Root value can't be null");
		}

		m_root = root;
	}

	@Override
	public boolean equals(final Object rhs)
	{
		if (!(rhs instanceof ReilOperand))
		{
			return false;
		}

		final ReilOperand rhsOperand = (ReilOperand) rhs;

		return getType().equals(rhsOperand.getType())
			&& getValue().equals(rhsOperand.getValue())
			&& getSize().equals(rhsOperand.getSize());
	}

	public ReilOperandNode getRootNode()
	{
		return m_root;
	}

	/**
	 * Returns the size of the operand.
	 *
	 * @return The size of the operand.
	 */
	public OperandSize getSize()
	{
		return OperandSize.sizeStringToValue(m_root.getValue());
	}

	/**
	 * Returns the type of the operand.
	 *
	 * @return The type of the operand.
	 */
	public OperandType getType()
	{
		return OperandType.getOperandType(getValue());
	}

	/**
	 * Returns the value of the operand.
	 *
	 * @return The value of the operand.
	 */
	public String getValue()
	{
		return m_root.getChildren().get(0).getValue();
	}

	@Override
	public int hashCode()
	{
		return getType().hashCode() * getValue().hashCode() * getSize().hashCode();
	}

	/**
	 * Returns the string representation of the operand.
	 *
	 * @return The string representation of the operand.
	 */
	@Override
	public String toString()
	{
		return getValue();
	}
}

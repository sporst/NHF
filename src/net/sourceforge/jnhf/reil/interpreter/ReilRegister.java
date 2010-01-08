package net.sourceforge.jnhf.reil.interpreter;

import java.math.BigInteger;

import net.sourceforge.jnhf.reil.OperandSize;

/**
 * Used by the interpreter to keep track of the values of
 * the registers during interpretation of REIL code.
 */
public class ReilRegister
{
	/**
	 * Name of the register
	 */
	private final String register;

	/**
	 * Size of the register
	 */
	private final OperandSize size;

	/**
	 * Value of the register
	 */
	private final BigInteger value;

	/**
	 * Creates a new REIL register object.
	 *
	 * @param register Name of the register
	 * @param size Size of the register
	 * @param value Value of the register
	 */
	public ReilRegister(final String register, final OperandSize size, final BigInteger value)
	{
		if (register == null)
		{
			throw new IllegalArgumentException("Error: Argument register can't be null");
		}

		if (size == null)
		{
			throw new IllegalArgumentException("Error: Argument size can't be null");
		}

		this.register = register;
		this.size = size;
		this.value = value;
	}

	/**
	 * Returns the name of the register.
	 *
	 * @return The name of the register
	 */
	public String getRegister()
	{
		return register;
	}

	/**
	 * Returns the size of the register.
	 *
	 * @return The size of the register
	 */
	public OperandSize getSize()
	{
		return size;
	}

	/**
	 * Returns the value of the register.
	 *
	 * @return The value of the register
	 */
	public BigInteger getValue()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return String.format("%s: %s", register, value);
	}
}

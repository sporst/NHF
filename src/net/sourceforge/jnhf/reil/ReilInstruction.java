package net.sourceforge.jnhf.reil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.jnhf.disassembler.IAddress;

/**
 * Class that's used to store REIL instructions.
 */
public final class ReilInstruction implements Comparable<ReilInstruction>
{
	/**
	 * Offset of the REIL instruction.
	 */
	private final IAddress offset;

	/**
	 * Mnemonic of the REIL instruction.
	 */
	private final String mnemonic;

	/**
	 * First operand of the REIL instruction.
	 */
	private final ReilOperand firstOperand;

	/**
	 * Second operand of the REIL instruction.
	 */
	private final ReilOperand secondOperand;

	/**
	 * Third operand of the REIL instruction.
	 */
	private final ReilOperand thirdOperand;

	/**
	 * Metadata of the REIL instruction.
	 */
	private final Map<String, String> metaData = new HashMap<String, String>();

	/**
	 * Creates a new ReilInstruction object.
	 *
	 * @param offset The offset of the REIL instruction.
	 * @param mnemonic The mnemonic of the REIL instruction.
	 * @param firstOperand The first operand of the REIL instruction.
	 * @param secondOperand The second operand of the REIL instruction.
	 * @param thirdOperand The third operand of the REIL instruction.
	 */
	public ReilInstruction(final IAddress offset, final String mnemonic, final ReilOperand firstOperand, final ReilOperand secondOperand, final ReilOperand thirdOperand)
	{

		if (mnemonic == null)
		{
			throw new IllegalArgumentException("Error: Argument mnemonic can't be null");
		}

		if (firstOperand == null)
		{
			throw new IllegalArgumentException("Error: Argument firstOperand can't be null");
		}

		if (secondOperand == null)
		{
			throw new IllegalArgumentException("Error: Argument secondOperand can't be null");
		}

		if (thirdOperand == null)
		{
			throw new IllegalArgumentException("Error: Argument thirdOperand can't be null");
		}

		this.offset = offset;
		this.mnemonic = mnemonic;
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
		this.thirdOperand = thirdOperand;
	}

	/**
	 * Creates the string representation of meta-data.
	 *
	 * @return The string representation of meta-data.
	 */
	private String getMetaDataString()
	{
		final StringBuffer stringBuffer = new StringBuffer();

		String[] keys = new String[0];
		keys = metaData.keySet().toArray(keys);

		for (int i = 0; i < metaData.size(); i++)
		{

			stringBuffer.append(keys[i]);
			stringBuffer.append(" : ");
			stringBuffer.append(metaData.get(keys[i]));

			if (i != metaData.size() - 1)
			{
				stringBuffer.append(", ");
			}
		}

		return stringBuffer.toString();
	}

	/**
	 * Adds metadata to the REIL instruction.
	 *
	 * @param key The key of the metadata.
	 * @param value The value of the metadata.
	 */
	public void addMetaData(final String key, final String value)
	{

		// TODO: Check for duplicates.

		if (key == null)
		{
			throw new IllegalArgumentException("Error: Argument key can't be null");
		}

		if (value == null)
		{
			throw new IllegalArgumentException("Error: Argument value can't be null");
		}

		metaData.put(key, value);
	}

	@Override
	public int compareTo(final ReilInstruction o)
	{
		return offset.compareTo(o.offset);
	}

	@Override
	public boolean equals(final Object rhs)
	{
		if (!(rhs instanceof ReilInstruction))
		{
			return false;
		}

		final ReilInstruction rhsInstruction = (ReilInstruction) rhs;

		return offset.equals(rhsInstruction.getAddress())
			&& mnemonic.equals(rhsInstruction.getMnemonic())
			&& firstOperand.equals(rhsInstruction.getFirstOperand())
			&& secondOperand.equals(rhsInstruction.getSecondOperand())
			&& thirdOperand.equals(rhsInstruction.getThirdOperand())
			&& metaData.equals(rhsInstruction.getMetaData());
	}

	/**
	 * Returns the offset of the REIL instruction.
	 *
	 * @return The offset of the REIL instruction.
	 */
	public IAddress getAddress()
	{
		return offset;
	}

	/**
	 * Returns the first operand of the REIL instruction.
	 *
	 * @return The first operand of the REIL instruction.
	 */
	public ReilOperand getFirstOperand()
	{
		return firstOperand;
	}

	/**
	 * Returns all pieces of metadata.
	 *
	 * @return A map of metadata information.
	 */
	public Map<String, String> getMetaData()
	{
		return new HashMap<String, String>(metaData);
	}

	/**
	 * Returns a piece of metadata.
	 *
	 * @param key The key of the metadata.
	 * @return The piece of metadata that belongs to the key.
	 */
	public String getMetaData(final String key)
	{

		// TODO: Handle invalid keys

		if (key == null)
		{
			throw new IllegalArgumentException("Error: Argument key can't be null");
		}

		return metaData.get(key);
	}

	/**
	 * Returns the mnemonic of the REIL instruction.
	 *
	 * @return The mnemonic of the REIL instruction.
	 */
	public String getMnemonic()
	{
		return mnemonic;
	}

	public List<ReilOperand> getOperands()
	{
		final List<ReilOperand> operands = new ArrayList<ReilOperand>();

		operands.add(firstOperand);
		operands.add(secondOperand);
		operands.add(thirdOperand);

		return operands;
	}

	/**
	 * Returns the second operand of the REIL instruction.
	 *
	 * @return The second operand of the REIL instruction.
	 */
	public ReilOperand getSecondOperand()
	{
		return secondOperand;
	}

	/**
	 * Returns the third operand of the REIL instruction.
	 *
	 * @return The third operand of the REIL instruction.
	 */
	public ReilOperand getThirdOperand()
	{
		return thirdOperand;
	}

	/**
	 * Returns the tuple string representation of the REIL instruction.
	 *
	 * @return The tuple string representation of the REIL instruction.
	 */
	public String getTupleString()
	{
		return String.format("%010X: (%s, (%s, %s, %s), (%s, %s, %s), [%s])", offset, mnemonic, firstOperand.getValue(), secondOperand.getValue(), thirdOperand.getValue(), firstOperand.getSize(), secondOperand.getSize(), thirdOperand.getSize(), getMetaDataString());
	}

	@Override
	public int hashCode()
	{
		return offset.hashCode() * mnemonic.hashCode() * firstOperand.hashCode() * secondOperand.hashCode() * thirdOperand.hashCode() * metaData.hashCode();
	}

	/**
	 * Returns the string representation of the REIL instruction.
	 *
	 * @return The string represetnation of the operand.
	 */
	@Override
	public String toString()
	{
		return offset.toHexString() + ": " + mnemonic + " " + firstOperand + ", " + secondOperand + ", " + thirdOperand;
	}

}

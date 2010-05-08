package net.sourceforge.jnhf.codefinder;

/**
 * Represents a function call as returned by the FunctionCallFinder.
 */
public final class FunctionCall
{
	/**
	 * Address of the function call.
	 */
	private final int address;

	/**
	 * Target address of the function call.
	 */
	private final int target;

	/**
	 * Creates a new function call object.
	 *
	 * @param address Address of the function call.
	 * @param target Target address of the function call.
	 */
	public FunctionCall(final int address, final int target)
	{
		this.address = address;
		this.target = target;
	}

	public int getAddress()
	{
		return address;
	}

	public int getTarget()
	{
		return target;
	}
}

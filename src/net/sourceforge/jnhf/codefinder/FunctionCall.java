package net.sourceforge.jnhf.codefinder;

public class FunctionCall
{
	private final int address;
	private final int target;

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

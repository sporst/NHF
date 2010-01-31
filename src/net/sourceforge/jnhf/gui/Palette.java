package net.sourceforge.jnhf.gui;

public class Palette
{
	public static final int SIZE_IN_BYTES = 16;

	private final byte[] data;

	public Palette(final byte[] data)
	{
		this.data = data;
	}

	public byte[] getData()
	{
		return data.clone();
	}
}

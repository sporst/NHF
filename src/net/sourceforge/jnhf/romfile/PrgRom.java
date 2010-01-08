package net.sourceforge.jnhf.romfile;

public class PrgRom
{
	private final byte[] data;

	public PrgRom(final int index, final int offset, final byte[] data)
	{
		this.data = data.clone();
	}

	public byte[] getData()
	{
		return data.clone();
	}
}

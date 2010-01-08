package net.sourceforge.jnhf.romfile;

public class ChrRom
{
	private final byte[] data;

	public ChrRom(final int index, final int offset, final byte[] data)
	{
		this.data = data.clone();
	}

	public byte[] getData()
	{
		return data.clone();
	}
}

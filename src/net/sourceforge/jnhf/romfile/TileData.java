package net.sourceforge.jnhf.romfile;

public class TileData
{
	private final byte[] data;

	public TileData(final byte[] data)
	{
		this.data = data.clone();
	}

	public byte[] getData()
	{
		return data;
	}
}

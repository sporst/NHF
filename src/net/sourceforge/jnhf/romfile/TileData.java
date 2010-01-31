package net.sourceforge.jnhf.romfile;

public class TileData
{
	private final byte[] data;
	private final int index;

	public TileData(final int index, final byte[] data)
	{
		this.index = index;
		this.data = data.clone();
	}

	public byte[] getData()
	{
		return data;
	}

	public int getIndex()
	{
		return index;
	}
}

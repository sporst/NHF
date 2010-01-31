package net.sourceforge.jnhf.romfile;

import net.sourceforge.jnhf.helpers.BitReader;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;

public class TileDataReader
{
	public static IFilledList<TileData> readTiles(final byte[] data, final int offset, final int numberTiles)
	{
		final IFilledList<TileData> tiles = new FilledList<TileData>();

		for (int i=0;i<numberTiles;i++)
		{
			final byte[] tileData = new byte[64];

            final BitReader reven = new BitReader(data, offset + i * 16);
            final BitReader rodd = new BitReader(data, offset + 8 + i * 16);

            for (int j = 0; j < 8; j++)
            {
                for (int k = 0; k < 8; k++)
                {
                    final int bit1 = reven.readBits(1);
                    final int bit2 = rodd.readBits(1);
                    final int value = (bit2 << 1) | bit1;
                    tileData[j * 8 + k] = (byte) value;
                }
            }

            tiles.add(new TileData(i, tileData));
		}

		return tiles;
	}
}

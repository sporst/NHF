package net.sourceforge.jnhf.codefinder;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.gui.Palette;

/**
 * Contains methods for automatically finding palettes in ROM files.
 */
public final class PaletteFinder
{
	/**
	 * Looks through a byte array and searches for palettes.
	 *
	 * @param data The data to look through.
	 *
	 * @return A list of addresses where paletters were found in the given data.
	 */
	public static List<Integer> find(final byte[] data)
	{
		if (data == null)
		{
			throw new IllegalArgumentException("Error: Data argument can not be null");
		}

		final List<Integer> addresses = new ArrayList<Integer>();

		for (int i=0;i<data.length - Palette.SIZE_IN_BYTES;i++)
		{
			if (data[i] == 0xF && data[i + 8] == 0xF)
			{
				boolean palette = true;

				for (int j=0;j<Palette.SIZE_IN_BYTES;j++)
				{
					if (data[i + j] > 0x3C)
					{
						palette = false;
						break;
					}
				}

				if (palette)
				{
					addresses.add(i);
				}
			}
		}

		return addresses;
	}
}

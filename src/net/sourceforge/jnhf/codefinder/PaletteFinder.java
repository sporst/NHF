package net.sourceforge.jnhf.codefinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.FileHelpers;

public final class PaletteFinder
{
	public static List<Integer> find(final byte[] data)
	{
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

	public static void main(final String[] args) throws IOException
	{
		final List<Integer> addresses = find(FileHelpers.readFile(new File("F:\\fce\\Faxanadu (U).nes")));

		for (final Integer integer : addresses)
		{
			System.out.printf("%04X\n", integer);
		}
	}
}

package net.sourceforge.jnhf.romfile;

import java.io.File;
import java.io.IOException;

import net.sourceforge.jnhf.helpers.FileHelpers;
import net.sourceforge.jnhf.helpers.IFilledList;

public class INesSplitter
{
	public static void split(final INesRom rom, final File outputDirectory) throws IOException
	{
		if (rom == null)
		{
			throw new IllegalArgumentException("ROM argument can not be null");
		}

		if (outputDirectory == null)
		{
			throw new IllegalArgumentException("Output directory argument can not be null");
		}

		if (!outputDirectory.isDirectory())
		{
			throw new IllegalArgumentException("Output directory argument is not a directory");
		}

		final String inputFilename = rom.getFile().getName();

		final IFilledList<PrgRom> prgRoms = rom.getPRGRoms();

		for (int i = 0; i < prgRoms.size(); i++)
		{
			final String filename = String.format("%s%s%s - PRG%02X.bin", outputDirectory.getAbsolutePath(), File.separator, inputFilename, i);

			final byte[] data = prgRoms.get(i).getData();

			FileHelpers.writeFile(new File(filename), data);
		}

		final IFilledList<ChrRom> chrRoms = rom.getCHRRoms();

		for (int i = 0; i < chrRoms.size(); i++)
		{
			final String filename = String.format("%s%s%s - CHR%02X.bin", outputDirectory.getAbsolutePath(), File.separator, inputFilename, i);

			final byte[] data = chrRoms.get(i).getData();

			FileHelpers.writeFile(new File(filename), data);
		}
	}
}

package net.sourceforge.jnhf.romfile;

import java.io.File;
import java.io.IOException;

import net.sourceforge.jnhf.helpers.ArrayHelpers;
import net.sourceforge.jnhf.helpers.FileHelpers;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;

public class INesRomParser
{
	private static INesRom parseMMC3(final File file, final INesHeader header) throws IOException
	{
		final IFilledList<ChrRom> chrRoms = new FilledList<ChrRom>();
		final IFilledList<PrgRom> prgRoms = new FilledList<PrgRom>();

		final byte[] romData = FileHelpers.readFile(file);

		final int PRG_SIZE = 16 * 1024;
		final int CHR_SIZE = 8 * 1024;

		final int expectedSize = INesHeader.NES_HEADER_SIZE + header.getCHRCount() * CHR_SIZE + header.getPRGCount() * PRG_SIZE;

		if (romData.length != expectedSize)
		{
			throw new IllegalStateException(String.format("NES ROM file does not have the expected length (Expected %d : Found %d)", expectedSize, romData.length));
		}

		for (int i=0;i<header.getPRGCount();i++)
		{
			final int offset = INesHeader.NES_HEADER_SIZE + i * PRG_SIZE;

			final byte[] data = ArrayHelpers.copy(romData, offset, PRG_SIZE);

			prgRoms.add(new PrgRom(i, offset, data));
		}

		for (int i=0;i<header.getCHRCount();i++)
		{
			final int offset = INesHeader.NES_HEADER_SIZE + header.getPRGCount() * PRG_SIZE + i * CHR_SIZE;

			final byte[] data = ArrayHelpers.copy(romData, offset, CHR_SIZE);

			chrRoms.add(new ChrRom(i, offset, data));
		}

		return new INesRom(file, header, chrRoms, prgRoms);
	}

	public static INesRom parse(final File file) throws IOException, InvalidRomException
	{
		final INesHeader header = new INesHeader(file);

		switch(header.getMapperNumber())
		{
		case MapperNames.MMC3: return parseMMC3(file, header);
		default: throw new IllegalArgumentException(String.format("Unknown NES mapper %d", header.getMapperNumber()));
		}
	}
}

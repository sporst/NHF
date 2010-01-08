package net.sourceforge.jnhf.romfile;

import java.io.File;
import java.io.IOException;

import net.sourceforge.jnhf.helpers.FileHelpers;

public class INesHeader
{
	public static final int NES_HEADER_SIZE = 16;

	private final byte[] rawHeader;

	public INesHeader(final byte[] rawHeader) throws InvalidRomException
	{
		if (rawHeader == null)
		{
			throw new IllegalArgumentException("argument argument can not be null");
		}

		if (rawHeader.length != NES_HEADER_SIZE)
		{
			throw new InvalidRomException("File is too small to be a NES ROM");
		}

		// ROM files start with NES\x1A
		if (rawHeader[0] != 'N' || rawHeader[1] != 'E' || rawHeader[2] != 'S' || rawHeader[3] != 0x1A)
		{
			throw new InvalidRomException("File has an invalid iNES header");
		}

		this.rawHeader = rawHeader.clone();
	}

	public INesHeader(final File romFile) throws IOException, InvalidRomException
	{
		this(FileHelpers.readFile(romFile, NES_HEADER_SIZE));
	}

	public int getCHRCount()
	{
		return rawHeader[5];
	}

	public int getMapperNumber()
	{
		return ((getROMControlByte1() >> 4) & 0x0F) | (getROMControlByte2() & 0xF0);
	}

	public int getPRGCount()
	{
		return rawHeader[4];
	}

	public byte[] getRawHeader()
	{
		return rawHeader.clone();
	}

	public byte getROMControlByte1()
	{
		return rawHeader[6];
	}

	public byte getROMControlByte2()
	{
		return rawHeader[7];
	}

	public boolean isNESRom()
	{
		return (getROMControlByte2() & 0x2) == 0x0;
	}

	public boolean isPlayChoiceRom()
	{
		return (getROMControlByte2() & 0x2) == 0x2;
	}

	public boolean isSRAMEnabled()
	{
		return (getROMControlByte1() & 0x2) != 0;
	}

	public boolean isTrainerPresent()
	{
		return (getROMControlByte1() & 0x4) != 0;
	}

	public boolean isVSUnisytemRom()
	{
		return (getROMControlByte2() & 0x2) == 0x1;
	}

	public boolean usesFourScreenVRAMLayout()
	{
		return (getROMControlByte1() & 0x8) != 0;
	}

	public boolean usesHorizontalMirroring()
	{
		return (getROMControlByte1() & 0x1) == 0;
	}

	public boolean usesVerticalMirroring()
	{
		return !usesHorizontalMirroring();
	}
}

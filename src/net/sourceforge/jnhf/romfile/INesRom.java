package net.sourceforge.jnhf.romfile;

import java.io.File;

import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;

public class INesRom
{
	private final File file;
	private final INesHeader header;
	private final IFilledList<ChrRom> chrRoms;
	private final IFilledList<PrgRom> prgRoms;

	public INesRom(final File file, final INesHeader header, final IFilledList<ChrRom> chrRoms, final IFilledList<PrgRom> prgRoms)
	{
		if (file == null)
		{
			throw new IllegalArgumentException("File argument can not be null");
		}

		this.file = file;
		this.header = header;
		this.chrRoms = new FilledList<ChrRom>(chrRoms);
		this.prgRoms = new FilledList<PrgRom>(prgRoms);
	}

	public IFilledList<ChrRom> getCHRRoms()
	{
		return new FilledList<ChrRom>(chrRoms);
	}

	public File getFile()
	{
		return file;
	}

	public INesHeader getHeader()
	{
		return header;
	}

	public IFilledList<PrgRom> getPRGRoms()
	{
		return new FilledList<PrgRom>(prgRoms);
	}
}

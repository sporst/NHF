package net.sourceforge.jnhf.romfile;

public class MapperNames
{
	public static final int MMC3 = 4;

	public static String getMapperName(final int mapperNumber)
	{
		switch(mapperNumber)
		{
		case MMC3: return "MMC3";
		default: return null;
		}
	}
}

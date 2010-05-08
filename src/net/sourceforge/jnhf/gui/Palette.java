package net.sourceforge.jnhf.gui;

import java.util.ArrayList;
import java.util.List;

public class Palette
{
	public static final int SIZE_IN_BYTES = 16;

	private final byte[] data;

	private final List<IPaletteListener> listeners = new ArrayList<IPaletteListener>();

	public Palette(final byte[] data)
	{
		this.data = data;
	}

	public void addListener(final IPaletteListener listener)
	{
		listeners.add(listener);
	}

	public byte[] getData()
	{
		return data.clone();
	}

	public void removeListener(final IPaletteListener listener)
	{
		listeners.remove(listener);
	}

	public void setColor(final int clickedColumn, final byte colorIndex)
	{
		data[clickedColumn] = colorIndex;

		for (final IPaletteListener listener : listeners)
		{
			try
			{
				listener.paletteChanged(this, clickedColumn, colorIndex);
			}
			catch(final Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}
}

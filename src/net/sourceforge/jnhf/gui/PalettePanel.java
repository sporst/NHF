package net.sourceforge.jnhf.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PalettePanel extends JPanel
{
	private Palette palette;

	private final IPaletteListener internalPaletteListener = new InternalPaletteListener();

	private static int TILE_WIDTH = 32;

	public PalettePanel(final Palette palette)
	{
		this.palette = palette;

		palette.addListener(internalPaletteListener);

		setPreferredSize(new Dimension(TILE_WIDTH * palette.getData().length, TILE_WIDTH));
	}

	public Palette getPalette()
	{
		return palette;
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		super.paintComponent(g);

		final byte[] data = palette.getData();

		for (int i=0;i<data.length;i++)
		{
			g.setColor(Palettes.FCEU_PAL_PALETTE[data[i]]);
			g.fillRect(TILE_WIDTH * i, 0, TILE_WIDTH, TILE_WIDTH);
		}
	}

	public void setPalette(final Palette palette)
	{
		this.palette.removeListener(internalPaletteListener);

		this.palette = palette;

		this.palette.addListener(internalPaletteListener);

		repaint();
	}

	private class InternalPaletteListener implements IPaletteListener
	{
		@Override
		public void paletteChanged(final Palette palette, final int index, final byte colorIndex)
		{
			repaint();
		}
	}
}

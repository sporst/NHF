package net.sourceforge.jnhf.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class ColorSelectionPanel extends JPanel
{
	private final Color[] colors;

	private static int TILE_WIDTH = 32;

	private final List<IColorSelectionListener> listeners = new ArrayList<IColorSelectionListener>();

	public ColorSelectionPanel(final Color[] colors)
	{
		super(new BorderLayout());

		this.colors = colors.clone();

		setPreferredSize(new Dimension(8 * TILE_WIDTH, 8 * TILE_WIDTH));

		addMouseListener(new MouseAdapter()
		{
		    @Override
			public void mouseClicked(final MouseEvent e)
		    {
		    	final int index = e.getY() / TILE_WIDTH * 8 + e.getX() / TILE_WIDTH;

		    	for (final IColorSelectionListener listener : new ArrayList<IColorSelectionListener>(listeners))
				{
		    		try
		    		{
		    			listener.clickedColor(index);
		    		}
		    		catch(final Exception exception)
		    		{
		    			exception.printStackTrace();
		    		}
				}
		    }
		});
	}

	public void addListener(final IColorSelectionListener listener)
	{
		listeners.add(listener);
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		super.paintComponents(g);

		for (int i=0;i<8;i++)
		{
			for (int j=0;j<8;j++)
			{
				g.setColor(colors[j * 8 + i]);
				g.fillRect(i * TILE_WIDTH, j * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
			}
		}
	}

	public void removeListener(final IColorSelectionListener listener)
	{
		listeners.remove(listener);
	}
}

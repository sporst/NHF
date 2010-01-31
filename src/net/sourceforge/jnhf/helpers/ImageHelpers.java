package net.sourceforge.jnhf.helpers;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageHelpers
{
	public static BufferedImage resize(final BufferedImage image, final int width, final int height)
	{
		final BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		final Graphics2D graphics2D = scaledImage.createGraphics();

		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, width, height, null);

		graphics2D.dispose();

		return scaledImage;
	}
}

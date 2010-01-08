package net.sourceforge.jnhf.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelpers
{
	public static byte[] readFile(final File file) throws IOException
	{
		final FileInputStream streamer = new FileInputStream(file);

		final byte[] byteArray = new byte[streamer.available()];

		try
		{
			streamer.read(byteArray);
		}
		finally
		{
			streamer.close();
		}

		return byteArray;
	}

	public static byte[] readFile(final File file, final int maxLength) throws IOException
	{
		if (file == null)
		{
			throw new IllegalArgumentException("File argument can not be null");
		}

		if (maxLength <= 0)
		{
			throw new IllegalArgumentException("Maximum length argument must be positive");
		}

		final FileInputStream streamer = new FileInputStream(file);

		final int toRead = Math.min(maxLength, streamer.available());

		final byte[] byteArray = new byte[toRead];

		try
		{
			streamer.read(byteArray);
		}
		finally
		{
			streamer.close();
		}

		return byteArray;
	}

	/**
	 * Writes a byte array to a binary file.
	 *
	 * @param file The file to write to.
	 * @param data The data to write.
	 *
	 * @throws IOException
	 */
	public static void writeFile(final File file, final byte[] data) throws IOException
	{
		final FileOutputStream fos = new FileOutputStream(file);

		try
		{
			fos.write(data);
		}
		finally
		{
			fos.close();
		}
	}

	/**
	 * Writes a string to a text file.
	 *
	 * @param filename The name of the file.
	 * @param text The string to write.
	 *
	 * @throws IOException
	 */
	public static void writeTextFile(final String filename, final String text) throws IOException
	{
		final File file = new File(filename);

		final BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		writer.write(text);

		writer.close();
	}
}

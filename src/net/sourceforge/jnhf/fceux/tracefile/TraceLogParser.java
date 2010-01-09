package net.sourceforge.jnhf.fceux.tracefile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;

public class TraceLogParser
{
	private static final Pattern linePattern = Pattern.compile("([^@=]*)(@ \\$(....) )?(= #\\$(..))?.*");

	public static IFilledList<TraceLogLine> parse(final File file) throws IOException, IllegalTraceLineException
	{
		final BufferedReader reader = new BufferedReader(new FileReader(file));

		final IFilledList<TraceLogLine> lines = new FilledList<TraceLogLine>();

		String currentLine = null;

		while ((currentLine = reader.readLine()) != null)
		{
			final TraceLogLine line = parseLine(currentLine);

			if (line == null)
			{
				continue;
			}

			lines.add(line);
		}

		return lines;
	}

	public static TraceLogLine parseLine(final String currentLine) throws IllegalTraceLineException
	{
		if (currentLine.contains("Trace Log File"))
		{
			return null;
		}

		if (currentLine.length() < 68)
		{
			throw new IllegalTraceLineException(currentLine);
		}

		// For performance reasons we are not handling the whole line
		// with a regular expression.

		final String address = currentLine.substring(1, 5);
		final String data = currentLine.substring(6, 16).replaceAll(" ", "");

		final String inner = currentLine.substring(16, 43);

		final Matcher matcher = linePattern.matcher(inner);

		if (!matcher.matches())
		{
			throw new IllegalTraceLineException(currentLine);
		}

		final String instruction = matcher.group(1).trim();
		String memoryAddress = matcher.group(3);
		final String memoryValue = matcher.group(5);

		if (memoryAddress == null && memoryValue != null)
		{
			final int index = currentLine.indexOf(" =");

			memoryAddress = currentLine.substring(index - 4, index);
		}

		final String valueA = currentLine.substring(45, 47);
		final String valueX = currentLine.substring(50, 52);
		final String valueY = currentLine.substring(55, 57);
		final String valueS = currentLine.substring(60, 62);
		final String valueFlags = currentLine.substring(65, 73);

		return new TraceLogLine(address, data, instruction, memoryAddress, memoryValue, valueA, valueX, valueY, valueS, valueFlags);
	}
}

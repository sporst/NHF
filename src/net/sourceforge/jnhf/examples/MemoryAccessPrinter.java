package net.sourceforge.jnhf.examples;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import net.sourceforge.jnhf.fceux.tracefile.IllegalTraceLineException;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogLine;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogParser;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;

public class MemoryAccessPrinter
{
	public static void main(final String[] args)
	{
		final String logFile = "F:\\fce\\Faxanadu (U).log";

		try
		{
			System.out.printf("Parsing log file %s\n", logFile);

			final IFilledList<TraceLogLine> lines = TraceLogParser.parse(new File(logFile));

			final List<String> addresses = new FilledList<String>();

			for (final TraceLogLine traceLogLine : lines)
			{
				final String address = traceLogLine.getMemoryAddress();

				if (address != null && !addresses.contains(address))
				{
					addresses.add(address);
				}
			}

			Collections.sort(addresses);

			for (final String address : addresses)
			{
				System.out.println(address);
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		catch (final IllegalTraceLineException e)
		{
			e.printStackTrace();
		}
	}
}

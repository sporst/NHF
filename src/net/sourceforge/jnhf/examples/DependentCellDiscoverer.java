package net.sourceforge.jnhf.examples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.fceux.tracefile.IllegalTraceLineException;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogLine;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogParser;
import net.sourceforge.jnhf.helpers.GraphAlgorithms;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.InternalTranslationException;
import net.sourceforge.jnhf.tainttracker.AddressFilter;
import net.sourceforge.jnhf.tainttracker.LogfileConverter;
import net.sourceforge.jnhf.tainttracker.TaintGraph;
import net.sourceforge.jnhf.tainttracker.TaintGraphFilter;
import net.sourceforge.jnhf.tainttracker.TaintGraphNode;

public class DependentCellDiscoverer
{
	private static Set<IAddress> findInfluencingAddresses(final TaintGraph filteredGraph, final int address)
	{
		final Set<IAddress> addresses = new HashSet<IAddress>();

		for (final TaintGraphNode node : filteredGraph)
		{
			final IAddress storeAddress = node.getStore().getAddress();

			if (storeAddress != null && storeAddress.toLong() == address)
			{
				final Set<TaintGraphNode> predecessors = GraphAlgorithms.getPredecessors(node);

				for (final TaintGraphNode predecessor : predecessors)
				{
					final IAddress predecessorAddress = predecessor.getStore().getAddress();

					if (predecessorAddress != null)
					{
						addresses.add(predecessorAddress);
					}
				}
			}
		}

		return addresses;
	}

	public static void main(final String[] args)
	{
		final String logFile = "F:\\fce\\Faxanadu (U).log";
		final int address = 0x431;

		try
		{
			System.out.printf("Parsing log file %s\n", logFile);

			final IFilledList<TraceLogLine> lines = TraceLogParser.parse(new File(logFile));

			System.out.println("Creating taint graph");

			final TaintGraph graph = LogfileConverter.buildGraph(lines);

			final TaintGraph filteredGraph = TaintGraphFilter.filter(graph, new AddressFilter(address));

			final List<IAddress> addresses = new ArrayList<IAddress>(findInfluencingAddresses(filteredGraph, address));

			Collections.sort(addresses);

			for (final IAddress influencingAddress : addresses)
			{
				System.out.println(influencingAddress);
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
		catch (final InternalTranslationException e)
		{
			e.printStackTrace();
		}
	}
}

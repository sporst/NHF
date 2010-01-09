package net.sourceforge.jnhf.examples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.fceux.tracefile.IllegalTraceLineException;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogLine;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogParser;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.InternalTranslationException;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.tainttracker.AddressFilter;
import net.sourceforge.jnhf.tainttracker.LogfileConverter;
import net.sourceforge.jnhf.tainttracker.TaintGraph;
import net.sourceforge.jnhf.tainttracker.TaintGraphFilter;
import net.sourceforge.jnhf.tainttracker.TaintGraphNode;

public class ConditionalJumpDiscoverer
{
	private static List<TaintGraphNode> findConditionalJumps(final TaintGraph graph)
	{
		final List<TaintGraphNode> conditionalJumps = new ArrayList<TaintGraphNode>();

		for (final TaintGraphNode node : graph)
		{
			final ReilInstruction instruction = node.getInstruction();

			if (ReilHelpers.OPCODE_JCC.equals(instruction.getMnemonic()))
			{
				conditionalJumps.add(node);
			}
		}

		return conditionalJumps;
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

			final List<TaintGraphNode> jumps = findConditionalJumps(filteredGraph);

			for (final TaintGraphNode taintGraphNode : jumps)
			{
				System.out.println("Conditional Jump: " + taintGraphNode.getInstruction());
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

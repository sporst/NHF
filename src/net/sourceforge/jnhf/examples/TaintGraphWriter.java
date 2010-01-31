package net.sourceforge.jnhf.examples;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.disassembler.InstructionHelpers;
import net.sourceforge.jnhf.fceux.tracefile.IllegalTraceLineException;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogLine;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogParser;
import net.sourceforge.jnhf.helpers.FileHelpers;
import net.sourceforge.jnhf.helpers.GmlConverter;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.helpers.IGmlEdge;
import net.sourceforge.jnhf.helpers.IGmlEnhancer;
import net.sourceforge.jnhf.reil.InternalTranslationException;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.tainttracker.AddressFilter;
import net.sourceforge.jnhf.tainttracker.LogfileConverter;
import net.sourceforge.jnhf.tainttracker.NativeTaintGraph;
import net.sourceforge.jnhf.tainttracker.NativeTaintGraphNode;
import net.sourceforge.jnhf.tainttracker.TaintGraph;
import net.sourceforge.jnhf.tainttracker.TaintGraphFilter;
import net.sourceforge.jnhf.tainttracker.TaintGraphNode;

/**
 * This example program demonstrates how to parse an FCEUX trace log
 * and generate a filtered taint tracking graph from the parsed trace
 * log data. The generated graph is written to a file.
 */
public final class TaintGraphWriter
{
	public static void main(final String[] args)
	{
		// Step I  : Parse a log file
		// Step II : Create a taint graph from the parsed file
		// Step III: Write the file to disk.

		final String logFile = TaintGraphWriter.class.getResource("simplified.log").getFile(); // "simplified.log";
//		final String logFile = "F:\\fce\\Faxanadu (U).log";
		final int address = 0x431;

		final String unfilteredOutputFile = "C:\\foo.gml";
		final String nativeOutputFile = "C:\\native.gml";
		final String filtedOutputFile = "C:\\filtered.gml";

		try
		{
			System.out.printf("Parsing log file %s\n", logFile);

			final IFilledList<TraceLogLine> lines = TraceLogParser.parse(new File(logFile));

			System.out.println("Creating taint graph");

			final TaintGraph graph = LogfileConverter.buildGraph(lines);

			System.out.println("Writing graph files to disk");

			FileHelpers.writeTextFile(unfilteredOutputFile, GmlConverter.toGml(graph, new MyEnhancer(address)));

			final TaintGraph filteredGraph = TaintGraphFilter.filter(graph, new AddressFilter(address));

			FileHelpers.writeTextFile(nativeOutputFile, GmlConverter.toGml(NativeTaintGraph.convert(graph), new MyNativeEnhancer(address)));
			FileHelpers.writeTextFile(filtedOutputFile, GmlConverter.toGml(filteredGraph, new MyEnhancer(address)));
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

	private static class MyEnhancer implements IGmlEnhancer<TaintGraphNode>
	{
		private final int m_address;

		public MyEnhancer(final int address)
		{
			m_address = address;
		}

		@Override
		public String enhance(final TaintGraphNode node)
		{
			final String mnemonic = node.getInstruction().getMnemonic();

			if (node.getStore().getAddress() != null && node.getStore().getAddress().toLong() == m_address)
			{
				return "fill \"#00FFFF\"";
			}
			else if (ReilHelpers.OPCODE_LDM.equals(mnemonic))
			{
				return "fill \"#FF0000\"";
			}
			else if (ReilHelpers.OPCODE_STM.equals(mnemonic))
			{
				return "fill \"#FFFF00\"";
			}
			else if (ReilHelpers.OPCODE_JCC.equals(mnemonic))
			{
				return "fill \"#00FF00\"";
			}
			else
			{
				return null;
			}
		}

		@Override
		public IGmlEdge enhance(final TaintGraphNode parent, final TaintGraphNode child)
		{
			final IAddress firstAddress = ReilHelpers.toNativeAddress(parent.getInstruction().getAddress());
			final IAddress secondAddress = ReilHelpers.toNativeAddress(child.getInstruction().getAddress());

			if (!firstAddress.equals(secondAddress))
			{
				return new IGmlEdge()
				{

					@Override
					public Color getColor()
					{
						return Color.RED;
					}
				};
			}
			else
			{
				return null;
			}
		}
	}

	private static class MyNativeEnhancer implements IGmlEnhancer<NativeTaintGraphNode>
	{
		private final int m_address;

		public MyNativeEnhancer(final int address)
		{
			m_address = address;
		}

		@Override
		public String enhance(final NativeTaintGraphNode node)
		{
			final String mnemonic = node.getInstruction().getMnemonic();

			if (mnemonic.startsWith("LD"))
			{
				return "fill \"#FF0000\"";
			}
			else if (mnemonic.startsWith("ST"))
			{
				return "fill \"#FFFF00\"";
			}
			else if (InstructionHelpers.isBranch(node.getInstruction()))
			{
				return "fill \"#00FF00\"";
			}
			else
			{
				return null;
			}
		}

		@Override
		public IGmlEdge enhance(final NativeTaintGraphNode parent, final NativeTaintGraphNode child)
		{
			return null;
		}
	}
}

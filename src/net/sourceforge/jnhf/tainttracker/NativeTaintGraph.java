package net.sourceforge.jnhf.tainttracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.IDirectedGraph;


public class NativeTaintGraph implements IDirectedGraph<NativeTaintGraphNode>
{
	private final List<NativeTaintGraphNode> nodes = new ArrayList<NativeTaintGraphNode>();

	public static NativeTaintGraph convert(final TaintGraph graph)
	{
		final NativeTaintGraph nativeGraph = new NativeTaintGraph();

		final Map<Instruction, NativeTaintGraphNode> nodeMap = new HashMap<Instruction, NativeTaintGraphNode>();

		for (final TaintGraphNode node : graph)
		{
			final Instruction instruction = node.getInstruction().getInstruction();

			if (nodeMap.containsKey(instruction))
			{
				continue;
			}

			final NativeTaintGraphNode newNode = new NativeTaintGraphNode(instruction);

			nativeGraph.add(newNode);
			nodeMap.put(instruction, newNode);
		}

		for (final TaintGraphNode node : graph)
		{
			final Instruction instruction = node.getInstruction().getInstruction();

			final NativeTaintGraphNode parentNode = nodeMap.get(instruction);

			for (final TaintGraphNode child : node.getChildren())
			{
				final Instruction childInstruction = child.getInstruction().getInstruction();
				final NativeTaintGraphNode childNode = nodeMap.get(childInstruction);

				if (!parentNode.getChildren().contains(childNode))
				{
					if (parentNode == childNode)
					{
						continue;
					}

					NativeTaintGraphNode.link(parentNode, childNode);
				}
			}
		}

		return nativeGraph;
	}

	private void add(final NativeTaintGraphNode node)
	{
		nodes.add(node);
	}

	@Override
	public List<NativeTaintGraphNode> getNodes()
	{
		return new ArrayList<NativeTaintGraphNode>(nodes);
	}

	@Override
	public Iterator<NativeTaintGraphNode> iterator()
	{
		return new ArrayList<NativeTaintGraphNode>(nodes).iterator();
	}

	@Override
	public int nodeCount()
	{
		return nodes.size();
	}
}

package net.sourceforge.jnhf.tainttracker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jnhf.helpers.GraphAlgorithms;

public class TaintGraphFilter
{
	public static TaintGraph filter(final TaintGraph graph, final ITaintGraphNodeFilter condition)
	{
		final TaintGraph filteredGraph = new TaintGraph();

		final Map<TaintGraphNode, TaintGraphNode> m_nodeMap = new HashMap<TaintGraphNode, TaintGraphNode>();

		for (final TaintGraphNode node : graph)
		{
			if (condition.matches(node))
			{
				final Set<TaintGraphNode> relatedNodes = getRelatedNodes(filteredGraph, node);

				for (final TaintGraphNode relatedNode : relatedNodes)
				{
					if (m_nodeMap.containsKey(relatedNode))
					{
						continue;
					}

					final TaintGraphNode newNode = new TaintGraphNode(relatedNode.getInstruction(), relatedNode.getStore());

					filteredGraph.add(newNode);
					m_nodeMap.put(relatedNode, newNode);
				}
			}
		}

		for (final TaintGraphNode node : graph)
		{
			if (m_nodeMap.containsKey(node))
			{
				for (final TaintGraphNode child : node.getChildren())
				{
					if (m_nodeMap.containsKey(child))
					{
						TaintGraphNode.link(m_nodeMap.get(node), m_nodeMap.get(child));
					}
				}
			}
		}

		return filteredGraph;
	}

	private static Set<TaintGraphNode> getRelatedNodes(final TaintGraph filteredGraph, final TaintGraphNode node)
	{
		// Once we have found a node we want to add to the filtered graph,
		// we have to add all child nodes to the filtered graph and we
		// have to add all parent nodes of all child nodes to the filtered
		// graph.

		final Set<TaintGraphNode> successors = GraphAlgorithms.getSuccessors(node);

		return GraphAlgorithms.getPredecessors(successors);
	}
}

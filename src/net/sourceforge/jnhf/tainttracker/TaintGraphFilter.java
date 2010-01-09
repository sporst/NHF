package net.sourceforge.jnhf.tainttracker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.helpers.GraphAlgorithms;

public class TaintGraphFilter
{
	public static TaintGraph filter(TaintGraph graph, ITaintGraphNodeFilter condition)
	{
		TaintGraph filteredGraph = new TaintGraph();
		
		Map<TaintGraphNode, TaintGraphNode> m_nodeMap = new HashMap<TaintGraphNode, TaintGraphNode>();
		
		for (TaintGraphNode node : graph)
		{
			if (condition.matches(node))
			{
				Set<TaintGraphNode> relatedNodes = getRelatedNodes(filteredGraph, node);
				
				for (TaintGraphNode relatedNode : relatedNodes)
				{
					if (m_nodeMap.containsKey(relatedNode))
					{
						continue;
					}
					
					TaintGraphNode newNode = new TaintGraphNode(relatedNode.getInstruction(), relatedNode.getStore());
					
					filteredGraph.add(newNode);
					m_nodeMap.put(relatedNode, newNode);
				}
			}
		}
		
		for (TaintGraphNode node : graph)
		{
			if (m_nodeMap.containsKey(node))
			{
				for (TaintGraphNode child : node.getChildren())
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

	private static Set<TaintGraphNode> getRelatedNodes(TaintGraph filteredGraph, TaintGraphNode node)
	{
		// Once we have found a node we want to add to the filtered graph,
		// we have to add all child nodes to the filtered graph and we
		// have to add all parent nodes of all child nodes to the filtered
		// graph.
		
		Set<TaintGraphNode> successors = GraphAlgorithms.getSuccessors(node);
		
		return GraphAlgorithms.getPredecessors(successors);
	}
}

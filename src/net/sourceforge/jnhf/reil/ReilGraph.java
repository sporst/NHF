package net.sourceforge.jnhf.reil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ReilGraph implements Iterable<ReilBlock>
{
	/**
	 * Nodes of the graph.
	 */
	private final List<ReilBlock> m_nodes;

	/**
	 * Edges of the graph.
	 */
	private final List<ReilEdge> m_edges;

	/**
	 * Creates a new directed edge object.
	 *
	 * @param nodes Nodes of the graph.
	 * @param edges Edges of the graph.
	 */
	public ReilGraph(final Collection<ReilBlock> nodes, final Collection<ReilEdge> edges)
	{
		if (nodes == null)
		{
			throw new IllegalArgumentException("Error: Nodes argument can not be null");
		}

		for (final ReilBlock node : nodes)
		{
			if (node == null)
			{
				throw new IllegalArgumentException("Error: Node list contains null-nodes");
			}
		}

		if (edges == null)
		{
			throw new IllegalArgumentException("Error: Edges argument can not be null");
		}

		m_nodes = new ArrayList<ReilBlock>(nodes);
		m_edges = new ArrayList<ReilEdge>(edges);
	}

	public int edgeCount()
	{
		return m_edges.size();
	}

	public List<ReilEdge> getEdges()
	{
		return new ArrayList<ReilEdge>(m_edges);
	}

	public List<ReilBlock> getNodes()
	{
		return new ArrayList<ReilBlock>(m_nodes);
	}

	public Iterator<ReilBlock> iterator()
	{
		return m_nodes.iterator();
	}

	public int nodeCount()
	{
		return m_nodes.size();
	}

	@Override
	public String toString()
	{
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("{\n");

		for (final ReilBlock block : this)
		{
			stringBuilder.append(block + "\n");

			for (final ReilEdge edge : block.getOutgoingEdges())
			{
				stringBuilder.append(block + " -> " + edge.getTarget());
				stringBuilder.append("\n");
			}
		}

		stringBuilder.append("}\n");

		return stringBuilder.toString();
	}
}

package net.sourceforge.jnhf.tainttracker;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IDirectedGraph;

public class TaintGraph implements IDirectedGraph<TaintGraphNode>
{
	private final List<TaintGraphNode> m_nodes = new FilledList<TaintGraphNode>();

	public void add(final TaintGraphNode node)
	{
		m_nodes.add(node);
	}

	@Override
	public List<TaintGraphNode> getNodes()
	{
		return new FilledList<TaintGraphNode>(m_nodes);
	}

	@Override
	public Iterator<TaintGraphNode> iterator()
	{
		return new FilledList<TaintGraphNode>(m_nodes).iterator();
	}

	@Override
	public int nodeCount()
	{
		return m_nodes.size();
	}
}

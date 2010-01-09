package net.sourceforge.jnhf.tainttracker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.jnhf.disassembler.CAddress;
import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IDirectedGraph;
import net.sourceforge.jnhf.reil.OperandType;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.ReilOperand;

public class TaintGraph implements IDirectedGraph<TaintGraphNode>
{
	private final List<TaintGraphNode> m_nodes = new FilledList<TaintGraphNode>();

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

	public void add(TaintGraphNode node)
	{
		m_nodes.add(node);
	}
}

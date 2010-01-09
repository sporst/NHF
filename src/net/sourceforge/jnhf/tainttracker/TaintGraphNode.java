package net.sourceforge.jnhf.tainttracker;

import java.util.List;

import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IGraphNode;
import net.sourceforge.jnhf.reil.ReilInstruction;

public class TaintGraphNode implements IGraphNode<TaintGraphNode>
{
	private final AbstractStore m_store;

	private final List<TaintGraphNode> m_children = new FilledList<TaintGraphNode>();

	private final List<TaintGraphNode> m_parents = new FilledList<TaintGraphNode>();
	
	private final ReilInstruction m_instruction;

	public TaintGraphNode(final ReilInstruction instruction, final AbstractStore store)
	{
		m_instruction = instruction;
		m_store = store;
	}

	public static void link(final TaintGraphNode parent, final TaintGraphNode child)
	{
		parent.m_children.add(child);
		child.m_parents.add(parent);
	}

	@Override
	public List<TaintGraphNode> getChildren()
	{
		return new FilledList<TaintGraphNode>(m_children);
	}
	
	public ReilInstruction getInstruction()
	{
		return m_instruction;
	}
	
	public AbstractStore getStore()
	{
		return m_store;
	}

	@Override
	public String toString()
	{
		return m_instruction.toString() + " / " + m_store.toString();
	}

	@Override
	public List<TaintGraphNode> getParents()
	{
		return new FilledList<TaintGraphNode>(m_parents);
	}
}

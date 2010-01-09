package net.sourceforge.jnhf.tainttracker;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.IGraphNode;

public class NativeTaintGraphNode implements IGraphNode<NativeTaintGraphNode>
{
	private final Instruction instruction;

	private final List<NativeTaintGraphNode> m_children = new ArrayList<NativeTaintGraphNode>();

	private final List<NativeTaintGraphNode> m_parents = new ArrayList<NativeTaintGraphNode>();

	public static void link(final NativeTaintGraphNode parent, final NativeTaintGraphNode child)
	{
		parent.m_children.add(child);
		child.m_parents.add(parent);
	}

	public NativeTaintGraphNode(final Instruction instruction)
	{
		this.instruction = instruction;
	}

	@Override
	public List<NativeTaintGraphNode> getChildren()
	{
		return new ArrayList<NativeTaintGraphNode>(m_children);
	}

	@Override
	public List<NativeTaintGraphNode> getParents()
	{
		return new ArrayList<NativeTaintGraphNode>(m_parents);
	}

	@Override
	public String toString()
	{
		return instruction.toString();
	}
}

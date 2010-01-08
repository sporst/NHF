package net.sourceforge.jnhf.reil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.jnhf.disassembler.IAddress;

public class ReilBlock implements Iterable<ReilInstruction>
{
	private final List<ReilInstruction> instructions = new ArrayList<ReilInstruction>();

	private final List<ReilEdge> outedges = new ArrayList<ReilEdge>();

	private final List<ReilEdge> inedges = new ArrayList<ReilEdge>();

	public ReilBlock(final Collection<ReilInstruction> instructions)
	{
		this.instructions.addAll(instructions);
	}

	public static void link(final ReilBlock source, final ReilBlock target, final ReilEdge edge)
	{
		source.outedges.add(edge);
		target.inedges.add(edge);
	}

	public void addInstruction(final ReilInstruction instruction)
	{
		this.instructions.add(instruction);
	}

	public IAddress getAddress()
	{
		return instructions.get(0).getAddress();
	}

	public List<ReilEdge> getIncomingEdges()
	{
		return new ArrayList<ReilEdge>(inedges);
	}

	public List<ReilInstruction> getInstructions()
	{
		return new ArrayList<ReilInstruction>(instructions);
	}

	public List<ReilEdge> getOutgoingEdges()
	{
		return new ArrayList<ReilEdge>(outedges);
	}

	@Override
	public Iterator<ReilInstruction> iterator()
	{
		return instructions.iterator();
	}

	@Override
	public String toString()
	{
		return "REIL Block " + getAddress().toHexString();
	}
}

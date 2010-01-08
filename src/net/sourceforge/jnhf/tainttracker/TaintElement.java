package net.sourceforge.jnhf.tainttracker;

import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.reil.ReilInstruction;

public class TaintElement implements ITaintElement
{
	private final ReilInstruction m_instruction;
	private final IAddress m_address;

	public TaintElement(final ReilInstruction instruction, final IAddress address)
	{
		m_instruction = instruction;
		m_address = address;
	}

	@Override
	public ReilInstruction getInstruction()
	{
		return m_instruction;
	}

	@Override
	public IAddress getMemoryAccessAddress()
	{
		return m_address;
	}
}

package net.sourceforge.jnhf.tainttracker;

import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.reil.ReilInstruction;

public interface ITaintElement
{
	ReilInstruction getInstruction();

	IAddress getMemoryAccessAddress();
}

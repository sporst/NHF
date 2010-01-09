package net.sourceforge.jnhf.tainttracker;

import net.sourceforge.jnhf.disassembler.IAddress;

public class AddressFilter implements ITaintGraphNodeFilter
{
	private long m_address;

	public AddressFilter(long address)
	{
		m_address = address;
	}
	
	@Override
	public boolean matches(TaintGraphNode node)
	{
		IAddress address = node.getStore().getAddress();
		
		return address != null && address.toLong() == m_address;
	}
}

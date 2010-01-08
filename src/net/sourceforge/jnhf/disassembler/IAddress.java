package net.sourceforge.jnhf.disassembler;

public interface IAddress extends Comparable<IAddress>
{
	String toHexString();

	long toLong();
}

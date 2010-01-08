package net.sourceforge.jnhf.disassembler;

public enum ExpressionType
{
	SYMBOL,
	Integer,
	Operator,
	Register,
	SIZE_PREFIX,
	MemoryDereference,
	IndirectMemoryDereference
}
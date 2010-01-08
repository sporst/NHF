package net.sourceforge.jnhf.reil.interpreter;

import net.sourceforge.jnhf.reil.OperandSize;

public class Policy6502 implements ICpuPolicy
{
	@Override
	public String[] getFlags()
	{
		return new String[] { "Z", "C", "I", "V", "D", "B", "U", "N"} ;
	}

	@Override
	public String getProgramCounter()
	{
		return "PC";
	}

	@Override
	public String[] getRegisters()
	{
		return new String[] { "A", "X", "Y", "PC", "SP" };
	}

	@Override
	public OperandSize getRegisterSize(final String programCounter)
	{
		return OperandSize.BYTE;
	}

	@Override
	public void start(final ReilInterpreter interpreter)
	{
	}
}

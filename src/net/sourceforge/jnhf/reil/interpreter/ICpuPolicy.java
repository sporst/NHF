package net.sourceforge.jnhf.reil.interpreter;

import net.sourceforge.jnhf.reil.OperandSize;

/**
 * Interface used by all CPU policies that give information
 * about the native source CPU to the REIL interpreter.
 */
public interface ICpuPolicy
{
	String[] getFlags();

	String getProgramCounter();

	String[] getRegisters();

	OperandSize getRegisterSize(String programCounter);

	void start(ReilInterpreter interpreter);
}

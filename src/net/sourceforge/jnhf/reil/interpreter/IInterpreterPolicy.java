package net.sourceforge.jnhf.reil.interpreter;

/**
 * Interface used by all interpreter policies that are used
 * by the REIL interpreter.
 */
public interface IInterpreterPolicy
{
	void end();

	void nextInstruction(ReilInterpreter interpreter);

	void start();
}

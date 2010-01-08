package net.sourceforge.jnhf.reil.interpreter;

/**
 * Used by the REIL interpreter to keep track of defined and undefined
 * registers.
 */
public enum ReilRegisterStatus
{
	/**
	 * Registers with that status have a defined value
	 */
	DEFINED,

	/**
	 * Registers with that status have an undefined value
	 */
	UNDEFINED
}

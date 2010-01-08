package net.sourceforge.jnhf.reil.interpreter;

/**
 * Exception used to signal exceptional conditions that occur
 * during interpretation of REIL code.
 */
public class InterpreterException extends Exception
{
	private static final long serialVersionUID = 2631894222110770167L;

	private final String msg;

	public InterpreterException(final String msg)
	{
		this.msg = msg;
	}

	@Override
	public String getMessage()
	{
		return msg;
	}
}

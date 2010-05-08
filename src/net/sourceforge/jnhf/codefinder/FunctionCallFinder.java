package net.sourceforge.jnhf.codefinder;

import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;

/**
 * Contains methods for identifying function calls. This is useful for
 * automatically identifying code in ROM files.
 */
public final class FunctionCallFinder
{
	/**
	 * Identifies potential function calls in a binary array.
	 *
	 * @param data The data to look through.
	 *
	 * @return A list of identified function calls.
	 */
	public static IFilledList<FunctionCall> findFunctionCalls(final byte[] data)
	{
		if (data == null)
		{
			throw new IllegalArgumentException("Error: Data argument can not be null");
		}

		final IFilledList<FunctionCall> functionCalls = new FilledList<FunctionCall>();

		for (int i=0;i<data.length - 3;i++)
		{
			final int opcode = data[i] & 0xFF;
			final int lowByte = data[i + 1] & 0xFF;
			final int highByte = data[i + 2] & 0xFF;

			if (opcode == 0x20 && (highByte >= 0x80 && highByte <= 0xC0))
			{
				functionCalls.add(new FunctionCall(i, highByte << 8 | lowByte));
			}
		}

		return functionCalls;
	}
}

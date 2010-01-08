package net.sourceforge.jnhf.codefinder;

import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;


public class FunctionCallFinder
{
	public static IFilledList<FunctionCall> findFunctionCalls(final byte[] data)
	{
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

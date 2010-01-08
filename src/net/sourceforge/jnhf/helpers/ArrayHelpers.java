package net.sourceforge.jnhf.helpers;

import java.util.ArrayList;
import java.util.List;

public class ArrayHelpers
{
	public static <T> boolean any(final T[] objects, final T object)
	{
		for (final T t : objects)
		{
			if (t == object)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean contains(final int row, final int[] rows)
	{
		for (final int i : rows)
		{
			if (i == row)
			{
				return true;
			}
		}

		return false;
	}

	public static byte[] copy(final byte[] data, final int start, final int length)
	{
		final byte[] retData = new byte[length];

		System.arraycopy(data, start, retData, 0, length);

		return retData;
	}

	public static <T> List<T> toList(final T[] array)
	{
		final List<T> list = new ArrayList<T>();

		for (final T t : array)
		{
			list.add(t);
		}

		return list;
	}
}


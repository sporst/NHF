package net.sourceforge.jnhf.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListHelpers
{
	private ListHelpers()
	{
		// You are not supposed to instantiate this class.
	}

	/**
	 * Returns a list that contains all but the last element of an input list.
	 *
	 * @param <T> The type of the objects in the list.
	 * @param list The input list.
	 *
	 * @return All but the last element in the list.
	 */
	public static <T> List<T> butLast(final List<T> list)
	{
		if (list.size() == 0)
		{
			return new ArrayList<T>();
		}
		else
		{
			return list.subList(0, list.size() - 1);
		}
	}

	/**
	 * Combines the elements of various lists into one list.
	 *
	 * @param <T> Type of the elements in the list.
	 * @param lists The lists to combine.
	 *
	 * @return The list created from the elements of all input lists.
	 */
	public static <T> List<T> combine(final List<T> ... lists)
	{
		final List<T> list = new ArrayList<T>();

		for (final List<T> inputList : lists)
		{
			list.addAll(inputList);
		}

		return list;
	}

	/**
	 * Returns the last element of a list.
	 *
	 * @param <T> Type of the objects in the input list.
	 * @param list The input list.
	 *
	 * @return The last element of the input list.
	 */
	public static <T> T last(final List<T> list)
	{
		return list.get(list.size() - 1);
	}

	/**
	 * Creates a list by taking an input list and adding elements to the list.
	 *
	 * @param <T> Type of the list.
	 * @param list The input list.
	 * @param elems The elements to add to the list.
	 *
	 * @return The modified input list.
	 */
	public static <T> List<T> list(final List<T> list, final T ... elems)
	{
		for (final T elem : elems)
		{
			list.add(elem);
		}

		return list;
	}

	/**
	 * Creates a list from a number of elements.
	 *
	 * @param <T> The type of the elements.
	 * @param elems The elements to add to the list.
	 *
	 * @return The list that contains the input elements.
	 */
	public static <T> List<T> list(final T ... elems)
	{
		final List<T> list = new ArrayList<T>();

		for (final T elem : elems)
		{
			list.add(elem);
		}

		return list;
	}

	public static <T> List<T> remove(final List<T> list, final Collection<T> remove)
	{
		list.removeAll(remove);

		return list;
	}

	public static <T> List<T> remove(final List<T> list, final T object)
	{
		list.remove(object);

		return list;
	}

	/**
	 * Creates a list that contains all elements of argument elements repeated
	 * amount times.
	 *
	 * @param <T> Type of the elements.
	 * @param amount Tells how often the input elements are added to the list.
	 * @param elements The elements to add to the list.
	 *
	 * @return The created list that contains the input elements.
	 */
	public static <T> List<T> repeat(final int amount, final T ... elements)
	{
		if (amount <= 0)
		{
			throw new IllegalArgumentException("Error: Amount argument must be positive");
		}

		final List<T> list = new ArrayList<T>();

		for (int i=0;i<amount;i++)
		{
			list.addAll(ListHelpers.list(elements));
		}

		return list;
	}
}

package net.sourceforge.jnhf.helpers;

import java.util.Collection;
import java.util.HashSet;

/**
 * Set class that is guaranteed not to contain any null-elements.
 *
 * @param <T> Type of the objects stored in the set.
 */
public class FilledSet<T> extends HashSet<T> implements IFilledSet<T>
{
	private static final long serialVersionUID = 2364411798868695912L;

	/**
	 * Creates a new empty filled set.
	 */
	public FilledSet()
	{
	}

	/**
	 * Creates a new filled set from a collection.
	 *
	 * @param collection The collection whose elements are copied into the set.
	 */
	public FilledSet(final Collection<? extends T> collection)
	{
		super(collection);

		for (final T t : collection)
		{
			if (t == null)
			{
				throw new NullPointerException("Error: Filled Set can not contain null-elements");
			}
		}
	}

	/**
	 * Creates a new filled set from a collection.
	 *
	 * @param <T> Type of the objects stored in the collection.
	 * @param collection The collection whose elements are copied into the set.
	 *
	 * @return The created set.
	 */
	public static <T> FilledSet<T> create(final Collection<? extends T> collection)
	{
		return new FilledSet<T>(collection);
	}

	/**
	 * Creates a new filled set from a number of elements.
	 *
	 * @param <T> Type of the objects stored in the set.
	 * @param elems The elements to be stored in the set.
	 *
	 * @return The created set.
	 */
	public static <T> IFilledSet<T> create(final T ... elems)
	{
		final IFilledSet<T> list = new FilledSet<T>();

		for (final T elem : elems)
		{
			list.add(elem);
		}

		return list;
	}

	@Override
	public boolean add(final T e)
	{
		if (e == null)
		{
			throw new NullPointerException("Error: Filled Set can not contain null-elements");
		}

		return super.add(e);
	}

	@Override
	public boolean addAll(final Collection<? extends T> c)
	{
		for (final T t : c)
		{
			if (t == null)
			{
				throw new NullPointerException("Error: Filled Set can not contain null-elements");
			}
		}

		return super.addAll(c);
	}
}

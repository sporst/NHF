package net.sourceforge.jnhf.helpers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ArrayList which is guaranteed not to have null-elements.
 *
 * @param <T> Type of the objects in the list.
 */
public class FilledList<T> extends ArrayList<T> implements IFilledList<T>
{
	private static final long serialVersionUID = 435355418871530567L;

	/**
	 * Creates an empty filled list.
	 */
	public FilledList()
	{
	}

	/**
	 * Creates a filled list from a collection.
	 *
	 * @param collection The collection whose elements are put into the FilledList.
	 */
	public FilledList(final Collection<? extends T> collection)
	{
		super(collection);

		for (final T t : collection)
		{
			if (t == null)
			{
				throw new NullPointerException("Error: Can not add null-elements to filled lists");
			}
		}
	}

	/**
	 * Creates a new filled list from an array of values.
	 *
	 * @param values The values to add to the filled list.
	 */
	public FilledList(final T ... values)
	{
		super(ArrayHelpers.toList(values));

		for (final T t : values)
		{
			if (t == null)
			{
				throw new NullPointerException("Error: Can not add null-elements to filled lists");
			}
		}
	}

	/**
	 * Creates a new filled list from an array of values.
	 *
	 * @param <T> The type of the values.
	 * @param values The values to add to the filled list.
	 *
	 * @return The created filled list.
	 */
	public static <T> IFilledList<T> list(final T ... values)
	{
		return new FilledList<T>(values);
	}

	@Override
	public void add(final int index, final T o)
	{
		if (o == null)
		{
			throw new NullPointerException("Error: Can not add null-elements to filled lists");
		}

		super.add(index, o);
	}

	@Override
	public boolean add(final T o)
	{
		if (o == null)
		{
			throw new NullPointerException("Error: Can not add null-elements to filled lists");
		}

		return super.add(o);
	}

	@Override
	public boolean addAll(final Collection<? extends T> c)
	{
		for (final T t : c)
		{
			if (t == null)
			{
				throw new NullPointerException("Error: Can not add null-elements to filled lists");
			}
		}

		return super.addAll(c);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends T> c)
	{
		for (final T t : c)
		{
			if (t == null)
			{
				throw new NullPointerException("Error: Can not add null-elements to filled lists");
			}
		}

		return super.addAll(index, c);
	}
}

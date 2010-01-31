package net.sourceforge.jnhf.helpers;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Helper class that can be used by all classes that need to keep track of a
 * list of listeners.
 *
 * This class already takes care of parameter checking and proper iteration over
 * the listener list.
 *
 * To use this class, simply add a field of type ListenerProvider<T> to your
 * class and forward the addListener and removeListener functions to the
 * ListenerProvider functions. To notify the listeners use a for-each loop on
 * the ListenerProvider object.
 *
 * @param <T> The type of the listeners stored in the ListenerProvider.
 */
public class ListenerProvider<T> implements Iterable<T>
{
	private final Collection<WeakReference<T>> m_listeners = new LinkedHashSet<WeakReference<T>>();

	private String toString(final LinkedHashSet<WeakReference<T>> listeners)
	{
		String l = "";

		for (final WeakReference<T> weakReference : listeners)
		{
			if (weakReference.get() != null)
			{
				l += weakReference.get() + ", ";
			}
		}

		return l;
	}

	/**
	 * Adds a listener to the listener provider.
	 *
	 * @param listener The listener to add.
	 *
	 * @throws IllegalArgumentException Thrown if the listener object is null.
	 * @throws IllegalStateException Thrown if the listener is already managed by the listener provider.
	 */
	public void addListener(final T listener)
	{
		if (listener == null)
		{
			throw new IllegalArgumentException("Internal Error: Listener can't be null.");
		}

		if (!m_listeners.add(new ComparableReference(listener)))
		{
			throw new IllegalStateException(String.format("Internal Error: Listener '%s' can not be added more than once.", listener));
		}
	}

	@Override
	public Iterator<T> iterator()
	{
		final ArrayList<T> listeners = new ArrayList<T>();

		for (final WeakReference<T> weakT : m_listeners)
		{
			final T element = weakT.get();

			if (element != null)
			{
				listeners.add(element);
			}
			else
			{
				System.out.println("Error: Lost a listener reference.");
			}
		}

		return listeners.iterator();
	}

	/**
	 * Removes a listener from the listener provider.
	 *
	 * @param listener The listener to remove.
	 *
	 * @throws IllegalArgumentException Thrown if the listener object is null.
	 * @throws IllegalStateException Thrown if the listener object was not managed by the listener provider.
	 */
	public void removeListener(final T listener)
	{
		if (listener == null)
		{
			throw new IllegalArgumentException("Internal Error: Listener can't be null.");
		}

		if (!m_listeners.remove(new ComparableReference(listener)))
		{
			throw new IllegalStateException("Error: Listener was not listening.");
		}
	}

	/**
	 * We need this special weak reference class because we store the listeners
	 * in a set and we need set-comparability.
	 */
	public class ComparableReference extends WeakReference<T>
	{
		public ComparableReference(final T referent)
		{
			super(referent);
		}

		@Override
		public boolean equals(final Object rhs)
		{
			final ComparableReference rhso = (ComparableReference) rhs;

			final T lhsElement = get();
			final T rhsElement = rhso.get();

			if (lhsElement == null && rhsElement == null)
			{
				return true;
			}
			else if (lhsElement == null || rhsElement == null)
			{
				return false;
			}
			else
			{
				return lhsElement.equals(rhsElement);
			}
		}

		@Override
		public int hashCode()
		{
			final T element = get();

			return element == null ? 0 : element.hashCode();
		}
	}
}
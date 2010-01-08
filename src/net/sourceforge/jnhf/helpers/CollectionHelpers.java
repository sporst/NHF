package net.sourceforge.jnhf.helpers;

import java.util.Collection;

public class CollectionHelpers
{
	public static <ItemType> int count(final Collection<? extends ItemType> collection, final ItemType item)
	{
		int counter = 0;

		for (final ItemType itemType : collection)
		{
			if (itemType == item)
			{
				counter++;
			}
		}

		return counter;
	}
}

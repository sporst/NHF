package net.sourceforge.jnhf.helpers;

public interface ICollectionFilter<ItemType>
{
	boolean qualifies(ItemType item);
}


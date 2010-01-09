package net.sourceforge.jnhf.helpers;

import java.util.List;

public interface IGraphNode<T>
{
	List<T> getChildren();
	
	List<T> getParents();
}

package net.sourceforge.jnhf.helpers;

public interface IGmlEnhancer<T>
{
	String enhance(T node);

	IGmlEdge enhance(T parent, T child);
}

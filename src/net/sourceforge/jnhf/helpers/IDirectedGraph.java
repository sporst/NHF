package net.sourceforge.jnhf.helpers;

import java.util.List;

/**
 * Interface for directed graphs.
 *
 * @param <NodeType> Type of the nodes in the graph.
 */
public interface IDirectedGraph<NodeType> extends Iterable<NodeType>
{
	/**
	 * Returns the nodes of the graph.
	 *
	 * @return The nodes of the graph.
	 */
	List<NodeType> getNodes();

	/**
	 * Returns the number of nodes in the graph.
	 *
	 * @return The number of nodes in the graph.
	 */
	int nodeCount();
}

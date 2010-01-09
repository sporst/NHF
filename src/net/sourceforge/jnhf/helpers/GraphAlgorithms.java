package net.sourceforge.jnhf.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphAlgorithms
{
	/**
	 * Helper function that supports the public getPredecessors function.
	 *
	 * @param <NodeType> The type of the nodes in the graph.
	 *
	 * @param node The node which is the starting point for finding the predecessors.
	 * @param predecessors List of predecessors of the start node.
	 * @param visited List of nodes in the graph that were already visited.
	 */
	private static <NodeType extends IGraphNode<NodeType>> void getPredecessors(final IGraphNode<NodeType> node, final Set<NodeType> predecessors, final Set<NodeType> visited)
	{
		for (final NodeType parent : node.getParents())
		{
			// Make sure that each node is only visited once.
			if (visited.contains(parent))
			{
				continue;
			}

			visited.add(parent);

			predecessors.add(parent);

			// Recursively find the predecessors of all parent nodes.
			getPredecessors(parent, predecessors, visited);
		}
	}

	private static <NodeType extends IGraphNode<NodeType>> void getPredecessorsInternal(final NodeType node, final int depth, final List<NodeType> nodes, final Set<NodeType> visited)
	{
		if (depth <= 0)
		{
			return;
		}

		for (final NodeType parent : node.getParents())
		{
			if (visited.contains(parent))
			{
				continue;
			}

			visited.add(parent);

			nodes.add(parent);

			getPredecessorsInternal(parent, depth - 1, nodes, visited);
		}
	}

	/**
	 * Helper function that supports the public getSuccessors function.
	 *
	 * @param <NodeType> The type of the nodes in the graph.
	 *
	 * @param node The node which is the starting point for finding the successors.
	 * @param successors List of successors of the start node.
	 * @param visited List of nodes in the graph that were already visited.
	 */
	private static <NodeType extends IGraphNode<NodeType>> void getSuccessors(final IGraphNode<NodeType> node, final Set<NodeType> successors, final Set<NodeType> visited)
	{
		for (final NodeType child : node.getChildren())
		{
			// Make sure that each node is only visited once.
			if (visited.contains(child))
			{
				continue;
			}

			visited.add(child);

			successors.add(child);

			// Recursively find the successors of all child nodes.
			getSuccessors(child, successors, visited);
		}
	}

	private static <NodeType extends IGraphNode<NodeType>> void getSuccessorsInternal(final NodeType node, final int depth, final List<NodeType> nodes, final HashSet<NodeType> visited)
	{
		if (depth <= 0)
		{
			return;
		}

		for (final NodeType child : node.getChildren())
		{
			if (visited.contains(child))
			{
				continue;
			}

			visited.add(child);

			nodes.add(child);

			getSuccessorsInternal(child, depth - 1, nodes, visited);
		}
	}

	/**
	 * Finds all predecessors of a collection of nodes. Those are all the nodes that have a direct or indirect
	 * path to the node.
	 *
	 * @param <NodeType> The node type of all nodes in the collection.
	 *
	 * @param nodes The collection of input nodes.
	 *
	 * @return All predecessors of the input nodes.
	 */
	public static <NodeType extends IGraphNode<NodeType>> Set<NodeType> getPredecessors(final Collection<NodeType> nodes)
	{
		if (nodes == null)
		{
			throw new IllegalArgumentException("Error: Nodes argument can't be null");
		}

		final HashSet<NodeType> predecessors = new HashSet<NodeType>();

		for (final NodeType zyGraphNode : nodes)
		{
			predecessors.addAll(getPredecessors(zyGraphNode));
		}

		return predecessors;
	}

	/**
	 * Finds all predecessors of a node. Those are all the nodes that have a direct or indirect
	 * path to the node.
	 *
	 * @param <NodeType> The type parameter that specifies the type of the nodes in the graph.
	 *
	 * @param node The start node.
	 *
	 * @return A list containing all predecessor nodes of the node.
	 */
	public static <NodeType extends IGraphNode<NodeType>> Set<NodeType> getPredecessors(final IGraphNode<NodeType> node)
	{
		if (node == null)
		{
			throw new IllegalArgumentException("Error: Start node can't be null");
		}

		final HashSet<NodeType> predecessors = new HashSet<NodeType>();
		final HashSet<NodeType> visited = new HashSet<NodeType>();

		getPredecessors(node, predecessors, visited);

		return predecessors;
	}

	public static <NodeType extends IGraphNode<NodeType>> List<NodeType> getPredecessors(final Iterable<NodeType> selectedNodes, final int depth)
	{
		final List<NodeType> nodes = new ArrayList<NodeType>();

		for (final NodeType node : selectedNodes)
		{
			nodes.addAll(getPredecessors(node, depth));
		}

		return nodes;
	}

	public static <NodeType extends IGraphNode<NodeType>> List<NodeType> getPredecessors(final NodeType node, final int depth)
	{
		final List<NodeType> nodes = new ArrayList<NodeType>();

		getPredecessorsInternal(node, depth, nodes, new HashSet<NodeType>());

		return nodes;
	}

	/**
	 * Finds all successors of a collection of nodes. Those are all the nodes that have a direct or indirect
	 * path from the node.
	 *
	 * @param <NodeType> The node type of all nodes in the collection.
	 *
	 * @param nodes The collection of input nodes.
	 *
	 * @return All successors of the input nodes.
	 */
	public static <NodeType extends IGraphNode<NodeType>> Collection<NodeType> getSuccessors(final Collection<NodeType> nodes)
	{
		if (nodes == null)
		{
			throw new IllegalArgumentException("Error: Nodes argument can't be null");
		}

		final HashSet<NodeType> successors = new HashSet<NodeType>();

		for (final NodeType zyGraphNode : nodes)
		{
			successors.addAll(getSuccessors(zyGraphNode));
		}

		return successors;
	}

	/**
	 * Finds all successors of a node. Those are all the nodes that have a direct or indirect
	 * path from the node.
	 *
	 * @param <NodeType> The type parameter that specifies the type of the nodes in the graph.
	 * @param node The start node.
	 *
	 * @return A list containing all successor nodes of the node.
	 */
	public static <NodeType extends IGraphNode<NodeType>> Set<NodeType> getSuccessors(final IGraphNode<NodeType> node)
	{
		if (node == null)
		{
			throw new IllegalArgumentException("Error: Start node can't be null");
		}

		final Set<NodeType> successors = new HashSet<NodeType>();
		final Set<NodeType> visited = new HashSet<NodeType>();

		getSuccessors(node, successors, visited);

		return successors;
	}

	public static <NodeType extends IGraphNode<NodeType>> List<NodeType> getSuccessors(final Iterable<NodeType> selectedNodes, final int depth)
	{
		final List<NodeType> nodes = new ArrayList<NodeType>();

		for (final NodeType node : selectedNodes)
		{
			nodes.addAll(getSuccessors(node, depth));
		}

		return nodes;
	}

	public static <NodeType extends IGraphNode<NodeType>> List<NodeType> getSuccessors(final NodeType node, final int depth)
	{
		final List<NodeType> nodes = new ArrayList<NodeType>();

		getSuccessorsInternal(node, depth, nodes, new HashSet<NodeType>());

		return nodes;
	}
}

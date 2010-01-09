package net.sourceforge.jnhf.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * This class can be used to generate GML files from input objects.
 */
public final class GmlConverter
{
	/**
	 * Creates GML code that represents a given directed graph.
	 *
	 * @param graph The input graph.
	 *
	 * @return The code generated for the input graph.
	 */
	public static <T extends IGraphNode<T>> String toGml(final IDirectedGraph<T> graph)
	{
		if (graph == null)
		{
			throw new IllegalStateException("Error: Graph argument can not be null");
		}

		final StringBuilder sb = new StringBuilder();

		sb.append("graph\n");
		sb.append("[\n");

		int currentId = 0;

		final Map<Object, Integer> nodeMap = new HashMap<Object, Integer>();

		for (final IGraphNode<?> node : graph.getNodes())
		{
			sb.append("\tnode\n");
			sb.append("\t[\n");
			sb.append("\tid " + currentId + "\n");
			sb.append("\tlabel \"" + node + "\"\n");
			if (node.toString().contains("ldm") || node.toString().contains("stm"))
			{
				sb.append("graphics [ fill \"#FF0000\" ]\n");
			}
			if (node.toString().contains("jcc"))
			{
				sb.append("graphics [ fill \"#00FF00\" ]\n");
			}
			sb.append("\t]\n");

			nodeMap.put(node, currentId);

			++currentId;
		}

		for (final IGraphNode<T> node : graph.getNodes())
		{
			for (final IGraphNode<T> child : node.getChildren())
			{
				sb.append("\tedge\n");
				sb.append("\t[\n");
				sb.append("\tsource " + nodeMap.get(node) + "\n");
				sb.append("\ttarget " + nodeMap.get(child) + "\n");
				sb.append("\tgraphics\n");
				sb.append("\t[\n");
				sb.append("\t\tfill \"#000000\"\n");
				sb.append("\t\ttargetArrow \"standard\"\n");
				sb.append("\t]\n");
				sb.append("\t]\n");
			}
		}

		sb.append("]\n");

		return sb.toString();
	}
}

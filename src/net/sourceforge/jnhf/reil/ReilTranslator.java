package net.sourceforge.jnhf.reil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import net.sourceforge.jnhf.disassembler.BasicBlock;
import net.sourceforge.jnhf.disassembler.EdgeType;
import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.helpers.Pair;
import net.sourceforge.jnhf.helpers.Triple;



/**
 * Translates disassembled programs to REIL code.
 */
public class ReilTranslator
{
	/**
	 * Creates a REIL graph from a pair that contains REIL blocks and REIL edges.
	 *
	 * @param pair The pair that contains the blocks and edges of the graph.
	 *
	 * @return The created graph object.
	 */
	private static ReilGraph createGraph(final Pair<List<ReilBlock>, List<ReilEdge>> pair)
	{
		return new ReilGraph(pair.first(), pair.second());
	}

	/**
	 * Creates REIL basic blocks and edges from a list of REIL instructions.
	 *
	 * @param instructions A list of REIL instructions.
	 * @param nativeJumpTargets Additional jump targets for the algorithm to consider.
	 *
	 * @return A pair containing the blocks and edges created from the REIL instructions.
	 */
	private static Pair<List<ReilBlock>, List<ReilEdge>> createGraphElements(final LinkedHashMap<BasicBlock, List<ReilInstruction>> instructionMap, final Collection<IAddress> nativeJumpTargets)
	{
		final BasicBlockGenerator generator = new BasicBlockGenerator(instructionMap, nativeJumpTargets);

		final List<ReilBlock> blocks = generator.getBlocks();
		final HashMap<IAddress, ReilBlock> addresses = generator.getAddressMapping();
		final ArrayList<Triple<ReilBlock, IAddress, EdgeType>> edgepairs = generator.getEdges();

		final List<ReilEdge> edges = new ArrayList<ReilEdge>();

		for (final Triple<ReilBlock, IAddress, EdgeType> p : edgepairs)
		{
			final ReilBlock source = p.first();
			final IAddress target = p.second();
			final EdgeType edgeType = p.third();

			if (target != null)
			{
				if (addresses.get(target) != null)
				{
					// Known target address

					final ReilEdge edge = new ReilEdge(source, addresses.get(target), edgeType);

					edges.add(edge);

					ReilBlock.link(source, addresses.get(target), edge);
				}
				else
				{
					// When we get here a jump target could not be resolved. Either
					// this is an error in the REIL translation, the basic block
					// algorithm, or the original input graph has a jump to an
					// instruction that is not in the graph. This is possible,
					// therefore we can not be sure that getting here is the result
					// of an error.
				}
			}
			else
			{
				// Unknown target address

				final int index = blocks.indexOf(source);

				if (blocks.size() > index + 1)
				{
					final ReilEdge edge = new ReilEdge(source, blocks.get(index + 1), edgeType);

					edges.add(edge);

					ReilBlock.link(source, blocks.get(index + 1), edge);
				}
			}
		}

		return new Pair<List<ReilBlock>, List<ReilEdge>>(blocks, edges);
	}

	public static ReilGraph translate(final Instruction instruction) throws InternalTranslationException
	{
		final List<ReilInstruction> instructions = new ArrayList<ReilInstruction>();

		final StandardEnvironment environment = new StandardEnvironment();

		environment.nextInstruction();

		instructions.addAll(Translator6502.translate(environment, instruction));

		final LinkedHashMap<BasicBlock, List<ReilInstruction>> instructionMap = new LinkedHashMap<BasicBlock, List<ReilInstruction>>();

		final BasicBlock container = new BasicBlock(instruction);

		instructionMap.put(container, instructions);

		return createGraph(createGraphElements(instructionMap, new ArrayList<IAddress>()));
	}
}

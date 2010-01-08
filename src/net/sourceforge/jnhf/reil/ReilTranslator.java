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
//	private final Map<String, ITranslator<InstructionType>> m_translators = new HashMap<String, ITranslator<InstructionType>>();
//
//	public ReilTranslator()
//	{
//		m_translators.put("x86-32", new TranslatorX86<InstructionType>());
//		m_translators.put("ARM-32", new TranslatorARM<InstructionType>());
//		m_translators.put("PowerPC-32", new TranslatorPPC<InstructionType>());
//		m_translators.put("REIL", new TranslatorREIL<InstructionType>());
//	}
//
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

//	/**
//	 * Returns the addresses of all basic blocks of a function. Note that the addresses
//	 * are already converted to REIL addresses.
//	 *
//	 * @param function The input function.
//	 *
//	 * @return A list of all basic block addresses of the function.
//	 */
//	private static Collection<IAddress> getBlockAddresses(final IBlockContainer<?> function)
//	{
//		return CollectionHelpers.map(function.getBasicBlocks(), new ICollectionMapper<ICodeContainer<?>, IAddress>()
//		{
//			@Override
//			public IAddress map(final ICodeContainer<?> block)
//			{
//				return ReilHelpers.toReilAddress(block.getAddress());
//			}
//		});
//	}
//
//	/**
//	 * Returns the first instruction from a code container.
//	 *
//	 * @param container The code container.
//	 *
//	 * @return The first instruction from the code container.
//	 */
//	private static IInstruction getFirstInstruction(final ICodeContainer<?> container)
//	{
//		return container.getInstructions().get(0);
//	}
//
//	/**
//	 * Returns the first instruction from a list of REIL instructions.
//	 *
//	 * @param list The list of REIL instructions.
//	 *
//	 * @return The first instruction from the list.
//	 */
//	private static ReilInstruction getFirstInstruction(final List<ReilInstruction> list)
//	{
//		return list.get(0);
//	}
//
//	/**
//	 * Returns the last instruction from a code container.
//	 *
//	 * @param container The code container.
//	 *
//	 * @return The last instruction from the code container.
//	 */
//	private static IInstruction getLastInstruction(final ICodeContainer<?> container)
//	{
//		return container.getInstructions().get(container.getInstructions().size() - 1);
//	}
//
//	/**
//	 * Returns the last instruction from a list of REIL instructions.
//	 *
//	 * @param list The list of REIL instructions.
//	 *
//	 * @return The last instruction from the list.
//	 */
//	private static ReilInstruction getLastInstruction(final List<ReilInstruction> list)
//	{
//		return list.get(list.size() - 1);
//	}
//
//	/**
//	 * Returns the REIL block from a list of REIL blocks that starts
//	 * with a given instruction.
//	 *
//	 * @param instruction The REIL instruction to search for.
//	 * @param blocks The blocks to search through.
//	 *
//	 * @return The block that starts with the given REIL instruction.
//	 */
//	private static ReilBlock getNode(final ReilInstruction instruction, final List<ReilBlock> blocks)
//	{
//		for (final ReilBlock block : blocks)
//		{
//			if (getFirstInstruction(block) == instruction)
//			{
//				return block;
//			}
//		}
//
//		throw new IllegalStateException(String.format("Error: Unknown block (Instruction '%s' not found)", instruction));
//	}
//
//	/**
//	 * Determines whether a REIL node has an outgoing edge to a given
//	 * REIL instruction.
//	 *
//	 * @param node The node whose outgoing edges are checked.
//	 * @param instruction The instruction to search for.
//	 *
//	 * @return True, if the node has an outgoing edge to the instructions. False, otherwise.
//	 */
//	private static boolean hasEdge(final ReilBlock node, final ReilInstruction instruction)
//	{
//		return CollectionHelpers.any(node.getOutgoingEdges(), new ICollectionFilter<ReilEdge>()
//		{
//			@Override
//			public boolean qualifies(final ReilEdge outgoingEdge)
//			{
//				return getFirstInstruction(outgoingEdge.getTarget()) == instruction;
//			}
//		});
//	}
//
//	/**
//	 * Inserts a single missing edge that could not be deduced automatically
//	 * from the REIL instructions and their operands.
//	 *
//	 * @param nodes List of translated REIL nodes.
//	 * @param edges List of translated REIL edges. This list is extended by the function.
//	 * @param nativeEdge The native to check for and add if necessary.
//	 * @param sourceReilInstruction Source REIL instruction of the edge.
//	 * @param targetReilInstruction Target REIL instruction of the edge.
//	 */
//	private static void insertNativeEdge(final List<ReilBlock> nodes, final List<ReilEdge> edges, final ICodeEdge<?> nativeEdge, final ReilInstruction sourceReilInstruction, final ReilInstruction targetReilInstruction)
//	{
//		for (final ReilBlock node : nodes)
//		{
//			if (sourceReilInstruction == getLastInstruction(node) && !hasEdge(node, targetReilInstruction))
//			{
//				final ReilBlock targetNode = getNode(targetReilInstruction, nodes);
//				final ReilEdge newEdge = new ReilEdge(node, targetNode, nativeEdge.getType());
//				ReilBlock.link(node, targetNode, newEdge);
//
//				edges.add(newEdge);
//			}
//		}
//	}
//
//	/**
//	 * Inserts missing edges that could not be deduced automatically
//	 * from the REIL instructions and their operands.
//	 *
//	 * @param nativeEdges List of native edges of the input graph.
//	 * @param nodes List of translated REIL nodes.
//	 * @param edges List of translated REIL edges. This list is extended by the function.
//	 * @param firstMap Maps between native instructions and their first REIL instructions.
//	 * @param lastMap Maps between native instructions and their last REIL instructions.
//	 */
//	private static void insertNativeEdges(final List<? extends ICodeEdge<?>> nativeEdges, final List<ReilBlock> nodes, final List<ReilEdge> edges, final Map<IInstruction, ReilInstruction> firstMap, final Map<IInstruction, ReilInstruction> lastMap)
//	{
//		for (final ICodeEdge<?> nativeEdge : nativeEdges)
//		{
//			final Object source = nativeEdge.getSource();
//			final Object target = nativeEdge.getTarget();
//
//			if (source instanceof ICodeContainer && target instanceof ICodeContainer)
//			{
//				final ICodeContainer<?> sourceCodeNode = (ICodeContainer<?>) source;
//				final ICodeContainer<?> targetCodeNode = (ICodeContainer<?>) target;
//
//				final IInstruction sourceInstruction = getLastInstruction(sourceCodeNode);
//				final IInstruction targetInstruction = getFirstInstruction(targetCodeNode);
//
//				final ReilInstruction sourceReilInstruction = lastMap.get(sourceInstruction);
//				final ReilInstruction targetReilInstruction = firstMap.get(targetInstruction);
//
//				insertNativeEdge(nodes, edges, nativeEdge, sourceReilInstruction, targetReilInstruction);
//			}
//		}
//	}
//
//	private static boolean isInlineSource(final ICodeContainer<?> container)
//	{
//		return container.getOutgoingEdges().size() == 1 && container.getOutgoingEdges().get(0).getType() == EdgeType.ENTER_INLINED_FUNCTION;
//	}
//
//	/**
//	 * Translates a disassembled function to REIL code.
//	 *
//	 * @param environment The translation environment for the translation process
//	 * @param function The disassembled function
//	 *
//	 * @return The function translated to REIL code
//	 *
//	 * @throws InternalTranslationException Thrown if an internal error occurs
//	 */
//	public ReilFunction translate(final ITranslationEnvironment environment, final IBlockContainer<InstructionType> function) throws InternalTranslationException
//	{
//		return translate(environment, function, new ArrayList<ITranslationExtension<InstructionType>>());
//	}
//
//	/**
//	 * Translates a disassembled function to REIL code.
//	 *
//	 * @param environment The translation environment for the translation process
//	 * @param function The disassembled function
//	 *
//	 * @return The function translated to REIL code
//	 *
//	 * @throws InternalTranslationException Thrown if an internal error occurs
//	 */
//	public ReilFunction translate(final ITranslationEnvironment environment, final IBlockContainer<InstructionType> function, final List<ITranslationExtension<InstructionType>> extensions) throws InternalTranslationException
//	{
//		final LinkedHashMap<ICodeContainer<InstructionType>, List<ReilInstruction>> instructionMap = new LinkedHashMap<ICodeContainer<InstructionType>, List<ReilInstruction>>();
//
//		final Map<IInstruction, ReilInstruction> firstMap = new HashMap<IInstruction, ReilInstruction>();
//		final Map<IInstruction, ReilInstruction> lastMap = new HashMap<IInstruction, ReilInstruction>();
//
//		for (final ICodeContainer<InstructionType> block : function.getBasicBlocks())
//		{
//			final List<InstructionType> blockInstructions = block.getInstructions();
//
//			final IInstruction lastBlockInstruction = blockInstructions.get(blockInstructions.size() - 1);
//
//			final boolean endsWithInlining = isInlineSource(block);
//
//			final ArrayList<ReilInstruction> instructions = new ArrayList<ReilInstruction>();
//
//			instructionMap.put(block, instructions);
//
//			for (final InstructionType instruction : blockInstructions)
//			{
//				environment.nextInstruction();
//
//				final ITranslator<InstructionType> translator = m_translators.get(instruction.getArchitecture());
//
//				final List<ReilInstruction> result = translator.translate(environment, instruction, extensions);
//				instructions.addAll(result);
//
//				if (endsWithInlining && instruction == lastBlockInstruction)
//				{
//					// We skip the last JCC instruction of blocks that were split by inlining. In 99%
//					// of all cases this should be the inlined call; unless the user removed the
//					// call from the block.
//
//					final ReilInstruction lastInstruction = instructions.get(instructions.size() - 1);
//
//					if (lastInstruction.getMnemonic().equals(ReilHelpers.OPCODE_JCC) && lastInstruction.getMetaData().containsKey("isCall"))
//					{
//						instructions.remove(instructions.size() - 1);
//						result.remove(result.size() - 1);
//					}
//				}
//
//				firstMap.put(instruction, getFirstInstruction(result));
//				lastMap.put(instruction, getLastInstruction(result));
//			}
//		}
//
//		// In this step we determine all jump targets of the input graph.
//		// We need them later because not all original jump targets can be
//		// found in the translated REIL graph. The reason for this is that
//		// source instructions of edges in the input graph do not necessarily
//		// have a reference to the address of the edge target. This happens
//		// for example when removing the first instruction from a code node.
//		// The edge still goes to the code node, but the jump instruction now
//		// refers to the removed instruction.
//		final Collection<IAddress> nativeJumpTargets = getBlockAddresses(function);
//
//		final Pair<List<ReilBlock>, List<ReilEdge>> pair = createGraphElements(instructionMap, nativeJumpTargets);
//
//		final List<ReilBlock> nodes = pair.first();
//		final List<ReilEdge> edges = pair.second();
//
//		// In a post-processing step all edges which could not be determined
//		// from the REIL instructions alone are inserted into the graph.
//		insertNativeEdges(function.getBasicBlockEdges(), nodes, edges, firstMap, lastMap);
//
//		return new ReilFunction("REIL - " + function.getName(), new ReilGraph(nodes, edges));
//	}

//	public ReilGraph translate(final BasicBlock block) throws InternalTranslationException
//	{
//		return translate(environment, block, new ArrayList<ITranslationExtension<InstructionType>>());
//	}

//	public ReilGraph translate(final ITranslationEnvironment environment, final ICodeContainer<InstructionType> block, final List<ITranslationExtension<InstructionType>> extensions) throws InternalTranslationException
//	{
//		final List<ReilInstruction> instructions = new ArrayList<ReilInstruction>();
//
//		for (final InstructionType instruction : block.getInstructions())
//		{
//			environment.nextInstruction();
//
//			final ITranslator<InstructionType> translator = m_translators.get(instruction.getArchitecture());
//
//			instructions.addAll(translator.translate(environment, instruction, extensions));
//		}
//
//		final LinkedHashMap<ICodeContainer<InstructionType>, List<ReilInstruction>> instructionMap = new LinkedHashMap<ICodeContainer<InstructionType>, List<ReilInstruction>>();
//
//		instructionMap.put(block, instructions);
//
//		return createGraph(createGraphElements(instructionMap, new ArrayList<IAddress>()));
//	}

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

//	public ReilGraph translate(final ITranslationEnvironment environment, final InstructionType instruction, final List<ITranslationExtension<InstructionType>> extensions) throws InternalTranslationException
//	{
//		final List<ReilInstruction> instructions = new ArrayList<ReilInstruction>();
//
//		environment.nextInstruction();
//
//		final ITranslator<InstructionType> translator = m_translators.get(instruction.getArchitecture());
//
//		instructions.addAll(translator.translate(environment, instruction, extensions));
//
//		final LinkedHashMap<ICodeContainer<InstructionType>, List<ReilInstruction>> instructionMap = new LinkedHashMap<ICodeContainer<InstructionType>, List<ReilInstruction>>();
//
//		final InstructionContainer<InstructionType> container = new InstructionContainer<InstructionType>(instruction);
//
//		instructionMap.put(container, instructions);
//
//		return createGraph(createGraphElements(instructionMap, new ArrayList<IAddress>()));
//	}
//
//	/**
//	 * Translates a disassembled program to REIL code.
//	 *
//	 * @param environment The translation environment for the translation process
//	 * @param functions The functions of the program to be translated.
//	 *
//	 * @return The program translated to REIL code
//	 *
//	 * @throws InternalTranslationException Thrown if an internal error occurs
//	 * @throws IllegalArgumentException Thrown if any of the arguments are invalid
//	 */
//	public ReilProgram<InstructionType> translate(final ITranslationEnvironment environment, final List<? extends IBlockContainer<InstructionType>> functions) throws InternalTranslationException
//	{
//		if (environment == null)
//		{
//			throw new IllegalArgumentException("Error: Argument environment can't be null");
//		}
//
//		final ReilProgram<InstructionType> program = new ReilProgram<InstructionType>();
//
//		for (final IBlockContainer<InstructionType> function : functions)
//		{
//			program.addFunction(function, translate(environment, function));
//		}
//
//		return program;
//	}
//
//	private static class InstructionContainer<InstructionType extends IInstruction> implements ICodeContainer<InstructionType>
//	{
//		private final InstructionType m_instruction;
//
//		private InstructionContainer(final InstructionType instruction)
//		{
//			m_instruction = instruction;
//		}
//
//		@Override
//		public IAddress getAddress()
//		{
//			return m_instruction.getAddress();
//		}
//
//		@SuppressWarnings("unchecked")
//		@Override
//		public List<InstructionType> getInstructions()
//		{
//			return ListHelpers.list(m_instruction);
//		}
//
//		@Override
//		public List<? extends ICodeEdge<?>> getOutgoingEdges()
//		{
//			return new ArrayList<ICodeEdge<?>>();
//		}
//	}
}

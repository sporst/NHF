
package net.sourceforge.jnhf.reil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sourceforge.jnhf.disassembler.BasicBlock;
import net.sourceforge.jnhf.disassembler.Address;
import net.sourceforge.jnhf.disassembler.EdgeType;
import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.helpers.Convert;
import net.sourceforge.jnhf.helpers.Triple;

public final class BasicBlockGenerator
{
	private List<ReilInstruction> currentBlock = new ArrayList<ReilInstruction>();

	private final List<ReilBlock> blocks = new ArrayList<ReilBlock>();

	private final HashMap<IAddress, ReilBlock> addresses = new HashMap<IAddress, ReilBlock>();

	private final ArrayList<Triple<ReilBlock, IAddress, EdgeType>> edgepairs = new ArrayList<Triple<ReilBlock, IAddress, EdgeType>>();

	public BasicBlockGenerator(final Map<BasicBlock, List<ReilInstruction>> instructionMap, final Collection<IAddress> nativeJumpTargets)
	{
		for (final Map.Entry<BasicBlock, List<ReilInstruction>> p : instructionMap.entrySet())
		{
			final List<ReilInstruction> instructions = p.getValue();

			final HashSet<IAddress> jumpTargets = fillJumpTargets(instructions);
			jumpTargets.addAll(nativeJumpTargets);

			final ReilInstruction lastInstruction = instructions.get(instructions.size() - 1);

			for (final ReilInstruction reilInstruction : instructions)
			{
				addInstruction(reilInstruction, jumpTargets, lastInstruction);
			}

			if (currentBlock.size() != 0)
			{
				final ReilBlock reilBlock = new ReilBlock(currentBlock);

				addresses.put(reilBlock.getAddress(), reilBlock);

				blocks.add(reilBlock);

				currentBlock = new ArrayList<ReilInstruction>();
			}
		}
	}

	/**
	 * Takes a list of REIL instructions and tries to deduces as many
	 * jump targets as possible from them.
	 *
	 * @param reilInstructions A list of REIL instructions.
	 *
	 * @return A list of jump target addresses that were deduced from the instructions.
	 */
	private static HashSet<IAddress> fillJumpTargets(final Collection<ReilInstruction> reilInstructions)
	{
		final HashSet<IAddress> jumpTargets = new HashSet<IAddress>();

		for (final ReilInstruction reilInstruction : reilInstructions)
		{
			if (reilInstruction.getMnemonic().equals(ReilHelpers.OPCODE_JCC))
			{
				final String jumpTarget = reilInstruction.getThirdOperand().getValue();

				if (Convert.isDecString(jumpTarget))
				{
					jumpTargets.add(toReilAddress(jumpTarget));
				}
				else if (reilInstruction.getThirdOperand().getType() == OperandType.SUB_ADDRESS)
				{
					jumpTargets.add(toReilAddress(jumpTarget.split("\\.")));
				}
			}
		}

		return jumpTargets;
	}

	private static IAddress toReilAddress(final String addressString)
	{
		return ReilHelpers.toReilAddress(new Address(Long.valueOf(addressString)));
	}

	private static IAddress toReilAddress(final String[] parts)
	{
		return new Address(toReilAddress(parts[0]).toLong() + Long.valueOf(parts[1]));
	}

	private void addInstruction(final ReilInstruction reilInstruction, final HashSet<IAddress> jumpTargets, final ReilInstruction lastInstruction)
	{
		if (jumpTargets.contains(reilInstruction.getAddress()) && currentBlock.size() != 0)
		{
			final ReilBlock reilBlock = new ReilBlock(currentBlock);

			final IAddress blockAddress = reilBlock.getAddress();

			addresses.put(blockAddress, reilBlock);

			blocks.add(reilBlock);

//			if ((reilBlock.getAddress().toLong() & 0xFFFFFFFFFFFFFF00L) == (reilInstruction.getAddress().toLong() & 0xFFFFFFFFFFFFFF00L))
			{
				edgepairs.add(new Triple<ReilBlock, IAddress, EdgeType>(reilBlock, reilInstruction.getAddress(), EdgeType.JUMP_UNCONDITIONAL));
			}

			currentBlock = new ArrayList<ReilInstruction>();
		}

		currentBlock.add(reilInstruction);

		if (reilInstruction.getMnemonic().equals(ReilHelpers.OPCODE_JCC) && reilInstruction != lastInstruction)
		{
			// Every JCC instruction finishes a block
			final ReilBlock reilBlock = new ReilBlock(currentBlock);

			addresses.put(reilBlock.getAddress(), reilBlock);

			blocks.add(reilBlock);
			currentBlock = new ArrayList<ReilInstruction>();

			final String jumpTarget = reilInstruction.getThirdOperand().getValue();

			if (ReilHelpers.isConditionalJump(reilInstruction))
			{
				// If we have a conditional jump we have to add two edges.

				edgepairs.add(new Triple<ReilBlock, IAddress, EdgeType>(reilBlock, null, EdgeType.JUMP_CONDITIONAL_FALSE));

				if (Convert.isDecString(jumpTarget))
				{
					edgepairs.add(new Triple<ReilBlock, IAddress, EdgeType>(reilBlock, toReilAddress(jumpTarget), EdgeType.JUMP_CONDITIONAL_TRUE));
				}
				else if (reilInstruction.getThirdOperand().getType() == OperandType.SUB_ADDRESS)
				{
					final String[] parts = jumpTarget.split("\\.");

					edgepairs.add(new Triple<ReilBlock, IAddress, EdgeType>(reilBlock, toReilAddress(parts), EdgeType.JUMP_CONDITIONAL_TRUE));
				}
			}
			else if (reilInstruction.getMetaData("isCall") != null)
			{
				edgepairs.add(new Triple<ReilBlock, IAddress, EdgeType>(reilBlock, null, EdgeType.JUMP_UNCONDITIONAL));
			}
			else if (Convert.isDecString(jumpTarget))
			{
				edgepairs.add(new Triple<ReilBlock, IAddress, EdgeType>(reilBlock, toReilAddress(jumpTarget), EdgeType.JUMP_CONDITIONAL_TRUE));
			}
			else if (reilInstruction.getThirdOperand().getType() == OperandType.SUB_ADDRESS)
			{
				final String[] parts = jumpTarget.split("\\.");

				edgepairs.add(new Triple<ReilBlock, IAddress, EdgeType>(reilBlock, toReilAddress(parts), EdgeType.JUMP_CONDITIONAL_TRUE));
			}
		}
	}

	public HashMap<IAddress, ReilBlock> getAddressMapping()
	{
		return new HashMap<IAddress, ReilBlock>(addresses);
	}

	public List<ReilBlock> getBlocks()
	{
		return new ArrayList<ReilBlock>(blocks);
	}

	public ArrayList<Triple<ReilBlock, IAddress, EdgeType>> getEdges()
	{
		return new ArrayList<Triple<ReilBlock,IAddress,EdgeType>>(edgepairs);
	}
}

package net.sourceforge.jnhf.tainttracker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.jnhf.disassembler.CAddress;
import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IDirectedGraph;
import net.sourceforge.jnhf.reil.OperandType;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.ReilOperand;

public class TaintGraph implements IDirectedGraph<TaintGraphNode>
{
	private final Map<AbstractStore, TaintGraphNode> m_lastNodes = new HashMap<AbstractStore, TaintGraphNode>();

	private final List<TaintGraphNode> m_rootNodes = new FilledList<TaintGraphNode>();

	private final List<TaintGraphNode> m_nodes = new FilledList<TaintGraphNode>();

	private void handleAdd(final ReilInstruction instruction)
	{
		final ReilOperand operand3 = instruction.getThirdOperand();

		final AbstractStore store3 = new AbstractStore(operand3);
		final TaintGraphNode outputNode = new TaintGraphNode(instruction, store3);

		processOperand(instruction.getFirstOperand(), instruction, outputNode);
		processOperand(instruction.getSecondOperand(), instruction, outputNode);

		m_lastNodes.put(store3, outputNode);
		m_nodes.add(outputNode);
	}

	private void handleLdm(final ReilInstruction instruction, IAddress address)
	{
		if (address == null && instruction.getFirstOperand().getType() == OperandType.INTEGER_LITERAL)
		{
			address = new CAddress(Long.valueOf(instruction.getFirstOperand().getValue(), 16));
		}

		if (address == null)
		{
			return;
		}

		// TODO: LDM is broken

		final ReilOperand operand3 = instruction.getThirdOperand();

		final AbstractStore store3 = new AbstractStore(operand3);
		final TaintGraphNode outputNode = new TaintGraphNode(instruction, store3);

//		processOperand(instruction.getFirstOperand(), instruction, outputNode);

		final AbstractStore store1 = new AbstractStore(address);
		final TaintGraphNode newNode = new TaintGraphNode(instruction, store1);

		if (!m_lastNodes.containsKey(store1))
		{
			m_rootNodes.add(newNode);
			m_lastNodes.put(store1, newNode);
		}
		else
		{
			final TaintGraphNode oldNode = m_lastNodes.get(store1);

			TaintGraphNode.link(oldNode, newNode);
		}

		TaintGraphNode.link(newNode, outputNode);

		m_nodes.add(newNode);

		m_lastNodes.put(store3, outputNode);
		m_nodes.add(outputNode);
	}

	private void handleStm(final ReilInstruction instruction, IAddress address)
	{
		if (address == null && instruction.getThirdOperand().getType() == OperandType.INTEGER_LITERAL)
		{
			address = new CAddress(Long.valueOf(instruction.getThirdOperand().getValue(), 16));
		}

		if (address == null)
		{
			return;
		}

		final AbstractStore store3 = new AbstractStore(address);
		final TaintGraphNode outputNode = new TaintGraphNode(instruction, store3);

		processOperand(instruction.getFirstOperand(), instruction, outputNode);

		m_lastNodes.put(store3, outputNode);
		m_nodes.add(outputNode);
	}

	private void processOperand(final ReilOperand operand1, final ReilInstruction instruction, final TaintGraphNode outputNode)
	{
		if (operand1.getType() == OperandType.REGISTER)
		{
			final AbstractStore store1 = new AbstractStore(operand1);
			final TaintGraphNode newNode = new TaintGraphNode(instruction, store1);

			if (!m_lastNodes.containsKey(store1))
			{
				m_rootNodes.add(newNode);
				m_lastNodes.put(store1, newNode);
			}
			else
			{
				final TaintGraphNode oldNode = m_lastNodes.get(store1);

				TaintGraphNode.link(oldNode, newNode);
			}

			TaintGraphNode.link(newNode, outputNode);

			m_nodes.add(newNode);
		}
	}

	public void add(final ITaintElement taintElement)
	{
		final ReilInstruction instruction = taintElement.getInstruction();

		if (
				ReilHelpers.OPCODE_ADD.equals(instruction.getMnemonic()) ||
				ReilHelpers.OPCODE_AND.equals(instruction.getMnemonic()) ||
				ReilHelpers.OPCODE_BISZ.equals(instruction.getMnemonic()) ||
				ReilHelpers.OPCODE_STR.equals(instruction.getMnemonic()) ||
				ReilHelpers.OPCODE_BSH.equals(instruction.getMnemonic()) ||
				ReilHelpers.OPCODE_OR.equals(instruction.getMnemonic()) ||
				ReilHelpers.OPCODE_XOR.equals(instruction.getMnemonic()) ||
				ReilHelpers.OPCODE_SUB.equals(instruction.getMnemonic())
			)
		{
			handleAdd(instruction);
		}
		else if (ReilHelpers.OPCODE_JCC.equals(instruction.getMnemonic()))
		{
		}
		else if (ReilHelpers.OPCODE_LDM.equals(instruction.getMnemonic()))
		{
			handleLdm(instruction, taintElement.getMemoryAccessAddress());
		}
		else if (ReilHelpers.OPCODE_STM.equals(instruction.getMnemonic()))
		{
			handleStm(instruction, taintElement.getMemoryAccessAddress());
		}
		else
		{
			System.out.println(instruction);

			throw new IllegalStateException();
		}
	}

	@Override
	public List<TaintGraphNode> getNodes()
	{
		return new FilledList<TaintGraphNode>(m_nodes);
	}

	@Override
	public Iterator<TaintGraphNode> iterator()
	{
		return new FilledList<TaintGraphNode>(m_nodes).iterator();
	}

	@Override
	public int nodeCount()
	{
		return m_nodes.size();
	}
}
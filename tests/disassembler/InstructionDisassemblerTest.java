package disassembler;

import static org.junit.Assert.assertEquals;
import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.disassembler.InstructionDisassembler;
import net.sourceforge.jnhf.disassembler.Operand;
import net.sourceforge.jnhf.disassembler.OperandExpression;

import org.junit.Test;

public class InstructionDisassemblerTest
{
	@Test
	public void testAbsolute()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0, "8D00E0");

		assertEquals("STA", instruction.getMnemonic());

		final Operand operand = instruction.getOperand();
		final OperandExpression root = operand.getRoot();

		assertEquals("E000", root.getValue());
	}

	@Test
	public void testAbsoluteX()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0, "BD4007");

		assertEquals("LDA", instruction.getMnemonic());

		final Operand operand = instruction.getOperand();
		final OperandExpression root = operand.getRoot();

		assertEquals("[", root.getValue());
		assertEquals("+", root.getChildren().get(0).getValue());
		assertEquals("0740", root.getChildren().get(0).getChildren().get(0).getValue());
		assertEquals("X", root.getChildren().get(0).getChildren().get(1).getValue());
	}

	@Test
	public void testBranches()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0x8885, "D0F7");

		assertEquals("BNE", instruction.getMnemonic());

		final Operand operand = instruction.getOperand();
		final OperandExpression root = operand.getRoot();

		assertEquals("887E", root.getValue());
	}

	@Test
	public void testImmediate()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0, "09FF");

		assertEquals("ORA", instruction.getMnemonic());

		final Operand operand = instruction.getOperand();
		final OperandExpression root = operand.getRoot();

		assertEquals("00FF", root.getValue());
	}

	@Test
	public void testIndirectY()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0, "B1C5");

		assertEquals("LDA", instruction.getMnemonic());

		final Operand operand = instruction.getOperand();
		final OperandExpression root = operand.getRoot();

		assertEquals("[", root.getValue());
		assertEquals("+", root.getChildren().get(0).getValue());
		assertEquals("(", root.getChildren().get(0).getChildren().get(0).getValue());
		assertEquals("Y", root.getChildren().get(0).getChildren().get(1).getValue());
		assertEquals("00C5", root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getValue());
	}

	@Test
	public void testJumps()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0x8885, "20AA83");

		assertEquals("JSR", instruction.getMnemonic());

		final Operand operand = instruction.getOperand();
		final OperandExpression root = operand.getRoot();

		assertEquals("83AA", root.getValue());
	}

	@Test
	public void testJumpsIndirect()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0x8885, "6CC100");

		assertEquals("JMP", instruction.getMnemonic());

		final Operand operand = instruction.getOperand();
		final OperandExpression root = operand.getRoot();

		assertEquals("(", root.getValue());
		assertEquals("00C1", root.getChildren().get(0).getValue());
	}

	@Test
	public void testSimple()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0, "E8");

		assertEquals("INX", instruction.getMnemonic());
		assertEquals(null, instruction.getOperand());
	}

	@Test
	public void testZeroPage()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0, "05FF");

		assertEquals("ORA", instruction.getMnemonic());

		final Operand operand = instruction.getOperand();
		final OperandExpression root = operand.getRoot();

		assertEquals("[", root.getValue());
		assertEquals("00FF", root.getChildren().get(0).getValue());
	}

	@Test
	public void testZeroPageX()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0, "B580");

		assertEquals("LDA", instruction.getMnemonic());

		final Operand operand = instruction.getOperand();
		final OperandExpression root = operand.getRoot();

		assertEquals("[", root.getValue());
		assertEquals("+", root.getChildren().get(0).getValue());
		assertEquals("0080", root.getChildren().get(0).getChildren().get(0).getValue());
		assertEquals("X", root.getChildren().get(0).getChildren().get(1).getValue());
	}

	@Test
	public void testZeroPageY()
	{
		final Instruction instruction = InstructionDisassembler.disassemble(0, "9682");

		assertEquals("STX", instruction.getMnemonic());

		final Operand operand = instruction.getOperand();
		final OperandExpression root = operand.getRoot();

		assertEquals("[", root.getValue());
		assertEquals("+", root.getChildren().get(0).getValue());
		assertEquals("0082", root.getChildren().get(0).getChildren().get(0).getValue());
		assertEquals("Y", root.getChildren().get(0).getChildren().get(1).getValue());
	}
}

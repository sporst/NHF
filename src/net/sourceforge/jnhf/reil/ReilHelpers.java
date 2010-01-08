package net.sourceforge.jnhf.reil;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.disassembler.CAddress;
import net.sourceforge.jnhf.disassembler.ExpressionType;
import net.sourceforge.jnhf.disassembler.IAddress;
import net.sourceforge.jnhf.helpers.CollectionHelpers;
import net.sourceforge.jnhf.helpers.Convert;
import net.sourceforge.jnhf.helpers.ListHelpers;

/**
 * This class provides some helper functions for
 * working with REIL instructions.
 *
 * @author sp
 *
 */
public class ReilHelpers
{
	/**
	 * The mnemonic of the REIL instruction ADD.
	 */
	public static final String OPCODE_ADD = "add";

	/**
	 * The mnemonic of the REIL instruction AND.
	 */
	public static final String OPCODE_AND = "and";

	/**
	 * The mnemonic of the REIL instruction BISZ.
	 */
	public static final String OPCODE_BISZ = "bisz";

	/**
	 * The mnemonic of the REIL instruction BSH.
	 */
	public static final String OPCODE_BSH = "bsh";

	/**
	 * The mnemonic of the REIL instruction CONSUME.
	 */
	public static final String OPCODE_CONSUME = "consume";

	/**
	 * The mnemonic of the REIL instruction DEFINE.
	 */
	public static final String OPCODE_DEFINE = "define";

	/**
	 * The mnemonic of the REIL instruction DIV.
	 */
	public static final String OPCODE_DIV = "div";

	/**
	 * The mnemonic of the REIL instruction JCC.
	 */
	public static final String OPCODE_JCC = "jcc";

	/**
	 * The mnemonic of the REIL instruction LDM.
	 */
	public static final String OPCODE_LDM = "ldm";

	/**
	 * The mnemonic of the REIL instruction MOD.
	 */
	public static final String OPCODE_MOD = "mod";

	/**
	 * The mnemonic of the REIL instruction MUL.
	 */
	public static final String OPCODE_MUL = "mul";

	/**
	 * The mnemonic of the REIL instruction NOP.
	 */
	public static final String OPCODE_NOP = "nop";

	/**
	 * The mnemonic of the REIL instruction OR.
	 */
	public static final String OPCODE_OR = "or";

	/**
	 * The mnemonic of the REIL instruction STM.
	 */
	public static final String OPCODE_STM = "stm";

	/**
	 * The mnemonic of the REIL instruction STR.
	 */
	public static final String OPCODE_STR = "str";

	/**
	 * The mnemonic of the REIL instruction SUB.
	 */
	public static final String OPCODE_SUB = "sub";

	/**
	 * The mnemonic of the REIL instruction UNDEF.
	 */
	public static final String OPCODE_UNDEF = "undef";

	/**
	 * The mnemonic of the REIL instruction UNKN.
	 */
	public static final String OPCODE_UNKNOWN = "unkn";

	/**
	 * The mnemonic of the REIL instruction XOR.
	 */
	public static final String OPCODE_XOR = "xor";

	/**
	 * Creates a REIL instruction with two operands.
	 *
	 * @param opcode The opcode of the instruction
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 * @param meta Meta data to be associated with the instruction.
	 *
	 * @return The created instruction
	 */
	private static ReilInstruction createBinaryInstruction(final String opcode, final IAddress offset, final OperandSize firstSize, final String firstValue, final OperandSize thirdSize, final String thirdValue, final String ... meta)
	{
		if (meta.length % 2 != 0)
		{
			throw new IllegalArgumentException("Error: Invalid number of arguments in metadata array");
		}

		final ReilOperand firstOperand = createOperand(firstSize, firstValue);
		final ReilOperand secondOperand = createOperand(OperandSize.EMPTY, "");
		final ReilOperand thirdOperand = createOperand(thirdSize, thirdValue);

		final ReilInstruction instruction = new ReilInstruction(offset, opcode, firstOperand, secondOperand, thirdOperand);

		for (int i = 0; i < meta.length; i+=2)
		{
			instruction.addMetaData(meta[i], meta[i + 1]);
		}

		return instruction;
	}

	/**
	 * Creates a REIL instruction with three operands.
	 *
	 * @param opcode The opcode of the instruction
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param secondSize The size of the second operand
	 * @param secondValue The value of the second operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	private static ReilInstruction createTrinaryInstruction(final String opcode, final IAddress offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		final ReilOperand firstOperand = createOperand(firstSize, firstValue);
		final ReilOperand secondOperand = createOperand(secondSize, secondValue);
		final ReilOperand thirdOperand = createOperand(thirdSize, thirdValue);

		return new ReilInstruction(offset, opcode, firstOperand, secondOperand, thirdOperand);
	}

	/**
	 * Creates an ADD instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param secondSize The size of the second operand
	 * @param secondValue The value of the second operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createAdd(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		if (firstSize == null)
		{
			throw new IllegalArgumentException("Error: First size argument can not be null");
		}

		if (secondSize == null)
		{
			throw new IllegalArgumentException("Error: Second size argument can not be null");
		}

		return createTrinaryInstruction(ReilHelpers.OPCODE_ADD, new CAddress(offset), firstSize, firstValue, secondSize, secondValue, thirdSize, thirdValue);
	}

	/**
	 * Creates an AND instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param secondSize The size of the second operand
	 * @param secondValue The value of the second operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createAnd(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createTrinaryInstruction(ReilHelpers.OPCODE_AND, new CAddress(offset), firstSize, firstValue, secondSize, secondValue, thirdSize, thirdValue);
	}

	/**
	 * Creates a BISZ instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createBisz(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createBinaryInstruction(ReilHelpers.OPCODE_BISZ, new CAddress(offset), firstSize, firstValue, thirdSize, thirdValue);
	}

	/**
	 * Creates a BSH instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param secondSize The size of the second operand
	 * @param secondValue The value of the second operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createBsh(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createTrinaryInstruction(ReilHelpers.OPCODE_BSH, new CAddress(offset), firstSize, firstValue, secondSize, secondValue, thirdSize, thirdValue);
	}

	public static ReilInstruction createDefine(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createTrinaryInstruction(ReilHelpers.OPCODE_DEFINE, new CAddress(offset), firstSize, firstValue, secondSize, secondValue, thirdSize, thirdValue);
	}

	/**
	 * Creates a DIV instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param secondSize The size of the second operand
	 * @param secondValue The value of the second operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createDiv(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createTrinaryInstruction(ReilHelpers.OPCODE_DIV, new CAddress(offset), firstSize, firstValue, secondSize, secondValue, thirdSize, thirdValue);
	}

	/**
	 * Creates a JCC instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 * @param meta Meta information to be associated with the instruction.
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createJcc(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize thirdSize, final String thirdValue, final String ... meta)
	{
		return createBinaryInstruction(ReilHelpers.OPCODE_JCC, new CAddress(offset), firstSize, firstValue, thirdSize, thirdValue, meta);
	}

	/**
	 * Creates a LDM instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createLdm(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createBinaryInstruction(ReilHelpers.OPCODE_LDM, new CAddress(offset), firstSize, firstValue, thirdSize, thirdValue);
	}

	/**
	 * Creates a MOD instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param secondSize The size of the second operand
	 * @param secondValue The value of the second operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createMod(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createTrinaryInstruction(ReilHelpers.OPCODE_MOD, new CAddress(offset), firstSize, firstValue, secondSize, secondValue, thirdSize, thirdValue);
	}

	/**
	 * Creates a MUL instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param secondSize The size of the second operand
	 * @param secondValue The value of the second operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createMul(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createTrinaryInstruction(ReilHelpers.OPCODE_MUL, new CAddress(offset), firstSize, firstValue, secondSize, secondValue, thirdSize, thirdValue);
	}

	/**
	 * Creates a NOP instruction.
	 *
	 * @param offset The offset of the instruction
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createNop(final long offset)
	{

		final ReilOperand firstOperand = createOperand(OperandSize.EMPTY, "");
		final ReilOperand secondOperand = createOperand(OperandSize.EMPTY, "");
		final ReilOperand thirdOperand = createOperand(OperandSize.EMPTY, "");

		return new ReilInstruction(new CAddress(offset), ReilHelpers.OPCODE_NOP, firstOperand, secondOperand, thirdOperand);
	}

	public static ReilOperand createOperand(final OperandSize size, final String value)
	{
		final ReilOperandNode root = new ReilOperandNode(size.toSizeString(), ExpressionType.SIZE_PREFIX);
		final ReilOperandNode child = new ReilOperandNode(value, getOperandType(value));

		ReilOperandNode.link(root, child);

		return new ReilOperand(root);
	}

	/**
	 * Creates a OR instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param secondSize The size of the second operand
	 * @param secondValue The value of the second operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createOr(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createTrinaryInstruction(ReilHelpers.OPCODE_OR, new CAddress(offset), firstSize, firstValue, secondSize, secondValue, thirdSize, thirdValue);
	}

	/**
	 * Creates a STM instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createStm(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createBinaryInstruction(ReilHelpers.OPCODE_STM, new CAddress(offset), firstSize, firstValue, thirdSize, thirdValue);
	}

	/**
	 * Creates a STR instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createStr(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createBinaryInstruction(ReilHelpers.OPCODE_STR, new CAddress(offset), firstSize, firstValue, thirdSize, thirdValue);
	}

	/**
	 * Creates a SUB instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param secondSize The size of the second operand
	 * @param secondValue The value of the second operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createSub(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createTrinaryInstruction(ReilHelpers.OPCODE_SUB, new CAddress(offset), firstSize, firstValue, secondSize, secondValue, thirdSize, thirdValue);
	}

	/**
	 * Creates an UNDEF instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param size The size of the value
	 * @param value The value to be undefined
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createUndef(final long offset, final OperandSize size, final String value)
	{
		final ReilOperand firstOperand = createOperand(OperandSize.EMPTY, "");
		final ReilOperand secondOperand = createOperand(OperandSize.EMPTY, "");
		final ReilOperand thirdOperand = createOperand(size, value);

		return new ReilInstruction(new CAddress(offset), ReilHelpers.OPCODE_UNDEF, firstOperand, secondOperand, thirdOperand);
	}

	/**
	 * Creates an UNKNOWN instruction.
	 *
	 * @param offset The offset of the instruction
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createUnknown(final long offset)
	{
		final ReilOperand firstOperand = createOperand(OperandSize.EMPTY, "");
		final ReilOperand secondOperand = createOperand(OperandSize.EMPTY, "");
		final ReilOperand thirdOperand = createOperand(OperandSize.EMPTY, "");

		return new ReilInstruction(new CAddress(offset), ReilHelpers.OPCODE_UNKNOWN, firstOperand, secondOperand, thirdOperand);
	}

	/**
	 * Creates a XOR instruction.
	 *
	 * @param offset The offset of the instruction
	 * @param firstSize The size of the first operand
	 * @param firstValue The value of the first operand
	 * @param secondSize The size of the second operand
	 * @param secondValue The value of the second operand
	 * @param thirdSize The size of the third operand
	 * @param thirdValue The value of the third operand
	 *
	 * @return The created instruction
	 */
	public static ReilInstruction createXor(final long offset, final OperandSize firstSize, final String firstValue, final OperandSize secondSize, final String secondValue, final OperandSize thirdSize, final String thirdValue)
	{
		return createTrinaryInstruction(ReilHelpers.OPCODE_XOR, new CAddress(offset), firstSize, firstValue, secondSize, secondValue, thirdSize, thirdValue);
	}

	public static ExpressionType getOperandType(final String value)
	{
		if (Convert.isDecString(value) || value.startsWith("-"))
		{
			return ExpressionType.Integer;
		}
		else if (value.startsWith("t"))
		{
			return ExpressionType.SYMBOL;
		}
		else if (OperandSize.isSizeString(value))
		{
			return ExpressionType.SIZE_PREFIX;
		}
		else
		{
			return ExpressionType.Register;
		}
	}

	/**
	 * Checks whether instructions with the given mnemonic use
	 * all three of their operands.
	 *
	 * @param mnemonic The mnemonic of the instruction.
	 *
	 * @return True, for ADD, SUB, MUL, DIV, BSH, AND, OR and XOR. False, otherwise.
	 */
	public static boolean isBinaryInstruction(final String mnemonic)
	{
		return mnemonic.equals(ReilHelpers.OPCODE_ADD) || mnemonic.equals(ReilHelpers.OPCODE_SUB) || mnemonic.equals(ReilHelpers.OPCODE_MUL) || mnemonic.equals(ReilHelpers.OPCODE_DIV) || mnemonic.equals(ReilHelpers.OPCODE_BSH) || mnemonic.equals(ReilHelpers.OPCODE_AND) || mnemonic.equals(ReilHelpers.OPCODE_OR) || mnemonic.equals(ReilHelpers.OPCODE_XOR);
	}

	/**
	 * Checks whether a given instruction is a conditional jump.
	 *
	 * @param instruction The instruction in question.
	 *
	 * @return True, if the given instruction is a conditional jump. False, otherwise.
	 */
	public static boolean isConditionalJump(final ReilInstruction instruction)
	{
		if (instruction == null)
		{
			throw new IllegalArgumentException("Error: Argument instruction can't be null");
		}

		return instruction.getMnemonic().equals(OPCODE_JCC) && instruction.getFirstOperand().getType() == OperandType.REGISTER;
	}

	public static boolean isNativeRegister(final ReilOperand operand)
	{
		return operand.getType() == OperandType.REGISTER && !isTemporaryRegister(operand);
	}

	public static boolean isTemporaryRegister(final ReilOperand operand)
	{
		return operand.getType() == OperandType.REGISTER && operand.getValue().startsWith("t");
	}

	public static boolean isTemporaryRegister(final String value)
	{
		return value.startsWith("t");
	}

	/**
	 * Checks whether the instructions with the given mnemonic
	 * use only their first and third operands.
	 *
	 * @param mnemonic The mnemonic of the instruction.
	 *
	 * @return True, for BISZ and STR. False, otherwise.
	 */
	public static boolean isUnaryInstruction(final String mnemonic)
	{
		return mnemonic.equals(ReilHelpers.OPCODE_BISZ) || mnemonic.equals(ReilHelpers.OPCODE_STR);
	}

	public static boolean setsValue(final ReilInstruction instruction, final String register)
	{
		return instruction.getThirdOperand().getValue().equals(register);
	}

	/**
	 * Converts a string that contains a subaddress into a
	 * long value with the same offset as the string.
	 *
	 * Attention: A valid subaddress of the form "number.number"
	 * must be passed to the function or an exception is thrown.
	 *
	 * @param subaddress
	 *
	 * @return
	 */
	public static long subAddressToLong(final String subaddress)
	{

		if (subaddress == null)
		{
			throw new IllegalArgumentException("Error: Argument subaddress can't be null");
		}

		if (subaddress.contains("."))
		{

			final String[] parts = subaddress.split("\\.");

			if (parts.length != 2)
			{
				throw new IllegalArgumentException("Error: Argument subaddress is not a valid subaddress");
			}

			// TODO: Add a check if the parts are really long numbers?

			final long firstPart = Long.parseLong(parts[0]);
			final long secondPart = Long.parseLong(parts[1]);

			return firstPart * 0x100 + secondPart;

		}
		else
		{
			throw new IllegalArgumentException("Error: Argument subaddress is not a valid subaddress");
		}
	}

	public static IAddress toNativeAddress(final IAddress address)
	{
		return new CAddress(address.toLong() / 0x100);
	}

	public static IAddress toReilAddress(final IAddress address)
	{
		return new CAddress(address.toLong() * 0x100);
	}

	public static boolean uses(final ReilInstruction instruction, final String register)
	{
		return instruction.getFirstOperand().getValue().equals(register) || instruction.getSecondOperand().getValue().equals(register);
	}

	/**
	 * Checks whether an instruction with the given mnemonic
	 * uses its first operand.
	 *
	 * @param mnemonic The mnemonic of the instruction.
	 *
	 * @return True, if the instruction uses its first operand. False, otherwise.
	 */
	public static boolean usesFirstOperand(final String mnemonic)
	{
		return !mnemonic.equals(ReilHelpers.OPCODE_NOP);
	}

	/**
	 * Checks whether an instruction with the given mnemonic
	 * uses its second operand.
	 *
	 * @param mnemonic The mnemonic of the instruction.
	 *
	 * @return True, if the instruction uses its first operand. False, otherwise.
	 */
	public static boolean usesSecondOperand(final String mnemonic)
	{
		return !mnemonic.equals(ReilHelpers.OPCODE_NOP)
		&& !mnemonic.equals(ReilHelpers.OPCODE_BISZ)
		&& !mnemonic.equals(ReilHelpers.OPCODE_STR)
		&& !mnemonic.equals(ReilHelpers.OPCODE_STM)
		&& !mnemonic.equals(ReilHelpers.OPCODE_LDM)
		&& !mnemonic.equals(ReilHelpers.OPCODE_JCC);
	}

	/**
	 * Checks whether an instruction with the given mnemonic
	 * uses its third operand.
	 *
	 * @param mnemonic The mnemonic of the instruction.
	 *
	 * @return True, if the instruction uses its first operand. False, otherwise.
	 */
	public static boolean usesThirdOperand(final String mnemonic)
	{
		return !mnemonic.equals(ReilHelpers.OPCODE_NOP);
	}

	public static List<String> useValues(final ReilInstruction instruction)
	{
		final List<String> values = new ArrayList<String>();

		if (instruction.getFirstOperand().getType() == OperandType.REGISTER)
		{
			values.add(instruction.getFirstOperand().getValue());
		}

		if (instruction.getSecondOperand().getType() == OperandType.REGISTER)
		{
			values.add(instruction.getSecondOperand().getValue());
		}

		return values;
	}

	public static boolean writesThirdOperand(final String mnemonic)
	{
		return CollectionHelpers.count(ListHelpers.list(OPCODE_ADD, OPCODE_AND, OPCODE_BISZ, OPCODE_BSH, OPCODE_DIV, OPCODE_DEFINE, OPCODE_LDM, OPCODE_MOD, OPCODE_MUL, OPCODE_OR, OPCODE_STR, OPCODE_SUB, OPCODE_XOR), mnemonic) != 0;
	}
}

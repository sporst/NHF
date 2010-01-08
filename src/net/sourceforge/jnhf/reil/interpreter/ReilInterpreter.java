package net.sourceforge.jnhf.reil.interpreter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.jnhf.helpers.Pair;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.OperandType;
import net.sourceforge.jnhf.reil.ReilHelpers;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.ReilOperand;

/**
 * Interpreter for REIL code
 */
public class ReilInterpreter
{
	private static final String SUB_PC = "sub_PC";

	/**
	 * CPU policy used by the interpreter
	 */
	private final ICpuPolicy cpuPolicy;

	/**
	 * Interpreter policy used by the interpreter
	 */
	private final IInterpreterPolicy interpreterPolicy;

	/**
	 * Registers currently in use by the interpreter
	 */
	private final HashMap<String, ReilRegister> registers = new HashMap<String, ReilRegister>();

	/**
	 * Memory currently in use by the interpreter
	 */
	private final ReilMemory memory;

//	private final ReilMemory stack = new ReilMemory(Endianness.LITTLE_ENDIAN);

	/**
	 * Creates a new REIL interpreter with the given options.
	 *
	 * @param endianness
	 *            Endianness of the memory layout of the interpreter
	 * @param cpuPolicy
	 *            CPU policy used by the interpreter
	 * @param interpreterPolicy
	 *            Interpreter policy used by the interpreter
	 */
	public ReilInterpreter(final Endianness endianness, final ICpuPolicy cpuPolicy, final IInterpreterPolicy interpreterPolicy)
	{
		if (endianness == null)
		{
			throw new IllegalArgumentException("Error: Argument endianness can't be null");
		}

		if (cpuPolicy == null)
		{
			throw new IllegalArgumentException("Error: Argument cpuPolicy can't be null");
		}

		if (interpreterPolicy == null)
		{
			throw new IllegalArgumentException("Error: Argument interpreterPolicy can't be null");
		}

		this.cpuPolicy = cpuPolicy;
		this.interpreterPolicy = interpreterPolicy;
		this.memory = new ReilMemory(endianness);
	}

	private static long getTruncateMask(final OperandSize targetSize)
	{
		switch (targetSize)
		{
		case BYTE: return 0xFFL;
		case DWORD: return 0xFFFFFFFFL;
		case QWORD: return 0xFFFFFFFFFFFFFFFFL;
		case WORD: return 0xFFFFL;
		default: throw new IllegalStateException("Error: Unknown target size for truncate mask");
		}
	}

	public static BigInteger nativeToReil(final BigInteger address)
	{
		return address.multiply(BigInteger.valueOf(0x100));
	}

	/**
	 * Interprets an ADD instruction.
	 *
	 * @param instruction
	 *            The ADD instruction to interpret
	 */
	private void interpretAdd(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());
		final Pair<Boolean, BigInteger> secondValue = loadLongValue(instruction.getSecondOperand());

		if (firstValue.first() && secondValue.first())
		{
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			final BigInteger result = firstValue.second().add(secondValue.second());

			final String targetRegister = instruction.getThirdOperand().getValue();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets an AND instruction.
	 *
	 * @param instruction
	 *            The AND instruction to interpret
	 */
	private void interpretAnd(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());
		final Pair<Boolean, BigInteger> secondValue = loadLongValue(instruction.getSecondOperand());

		if (firstValue.first() && secondValue.first())
		{
			final BigInteger result = firstValue.second().and(secondValue.second());
			final String targetRegister = instruction.getThirdOperand().getValue();
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets a BISZ instruction.
	 *
	 * @param instruction
	 *            The BISZ instruction to interpret
	 */
	private void interpretBisz(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());

		if (firstValue.first())
		{
			final BigInteger result = firstValue.second().equals(BigInteger.ZERO) ? BigInteger.ONE : BigInteger.ZERO;
			final String targetRegister = instruction.getThirdOperand().getValue();
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets a BSH instruction.
	 *
	 * @param instruction
	 *            The BSH instruction to interpret
	 */
	private void interpretBsh(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());
		final Pair<Boolean, BigInteger> secondValue = loadLongValue(instruction.getSecondOperand());

		if (firstValue.first() && secondValue.first())
		{
			BigInteger result = BigInteger.ZERO;
			if (secondValue.second().compareTo(BigInteger.ZERO) < 0)
			{
				result = firstValue.second().shiftRight(secondValue.second().negate().intValue());
			}
			else
			{
				result = firstValue.second().shiftLeft(secondValue.second().intValue());
			}

			final String targetRegister = instruction.getThirdOperand().getValue();
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets a DIV instruction.
	 *
	 * @param instruction
	 *            The DIV instruction to interpret
	 */
	private void interpretDiv(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());
		final Pair<Boolean, BigInteger> secondValue = loadLongValue(instruction.getSecondOperand());

		if (firstValue.first() && secondValue.first())
		{
			final BigInteger result = firstValue.second().divide(secondValue.second());
			final String targetRegister = instruction.getThirdOperand().getValue();
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets a single REIL instruction.
	 *
	 * @param instruction
	 *            The REIL instruction to interpret
	 * @param programCounter
	 *            The name of the program counter register
	 */
	private void interpretInstruction(final ReilInstruction instruction, final String programCounter)
	{
//		System.out.println("Interpreting " + instruction);

		final String mnemonic = instruction.getMnemonic();

		if (mnemonic.equals(ReilHelpers.OPCODE_ADD))
		{
			interpretAdd(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_AND))
		{
			interpretAnd(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_BISZ))
		{
			interpretBisz(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_BSH))
		{
			interpretBsh(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_DIV))
		{
			interpretDiv(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_JCC))
		{
			interpretJcc(instruction, programCounter);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_LDM))
		{
			interpretLdm(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_MOD))
		{
			interpretMod(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_MUL))
		{
			interpretMul(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_NOP))
		{
			// Do nothing
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_OR))
		{
			interpretOr(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_STM))
		{
			interpretStm(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_STR))
		{
			interpretStr(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_SUB))
		{
			interpretSub(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_UNDEF))
		{
			interpretUndef(instruction);
		}
		else if (mnemonic.equals(ReilHelpers.OPCODE_XOR))
		{
			interpretXor(instruction);
		}
	}

	/**
	 * Interprets a JCC instruction.
	 *
	 * @param instruction
	 *            The JCC instruction to interpret
	 * @param programCounter
	 *            The name of the program counter register
	 */
	private void interpretJcc(final ReilInstruction instruction, final String programCounter)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());

		if (!firstValue.second().equals(BigInteger.ZERO) && instruction.getThirdOperand().getType() == OperandType.SUB_ADDRESS)
		{
			final String[] parts = instruction.getThirdOperand().getValue().split("\\.");

			assert parts.length == 2;

			setRegister(programCounter, new BigInteger(parts[0]), OperandSize.DWORD, ReilRegisterStatus.DEFINED);
			setRegister(SUB_PC, new BigInteger(parts[1]), OperandSize.DWORD, ReilRegisterStatus.DEFINED);
		}
		else if (!firstValue.second().equals(BigInteger.ZERO))
		{
			final Pair<Boolean, BigInteger> secondValue = loadLongValue(instruction.getThirdOperand());

			setRegister(programCounter, secondValue.second(), OperandSize.DWORD, ReilRegisterStatus.DEFINED);
		}

	}

	/**
	 * Interprets a LDM instruction.
	 *
	 * @param instruction
	 *            The LDM instruction to interpret
	 */
	private void interpretLdm(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());

//		final ReilMemory targetMemory = instruction.getFirstOperand().getValue().equals("SP") ? stack : memory;

		if (firstValue.first())
		{
			final String target = instruction.getThirdOperand().getValue();
			final int targetSize = instruction.getThirdOperand().getSize().getIntegerValue();

			final BigInteger value = new BigInteger(String.valueOf(memory.load(firstValue.second().longValue(), targetSize)));

			setRegister(target, value, instruction.getThirdOperand().getSize(), ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets a MOD instruction.
	 *
	 * @param instruction
	 *            The MOD instruction to interpret
	 */
	private void interpretMod(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());
		final Pair<Boolean, BigInteger> secondValue = loadLongValue(instruction.getSecondOperand());

		if (firstValue.first() && secondValue.first())
		{
			final BigInteger result = firstValue.second().mod(secondValue.second());
			final String targetRegister = instruction.getThirdOperand().getValue();
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets a MUL instruction.
	 *
	 * @param instruction
	 *            The MUL instruction to interpret
	 */
	private void interpretMul(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());
		final Pair<Boolean, BigInteger> secondValue = loadLongValue(instruction.getSecondOperand());

		if (firstValue.first() && secondValue.first())
		{
			final BigInteger result = firstValue.second().multiply(secondValue.second());
			final String targetRegister = instruction.getThirdOperand().getValue();
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets a OR instruction.
	 *
	 * @param instruction
	 *            The OR instruction to interpret
	 */
	private void interpretOr(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());
		final Pair<Boolean, BigInteger> secondValue = loadLongValue(instruction.getSecondOperand());

		if (firstValue.first() && secondValue.first())
		{
			final BigInteger result = firstValue.second().or(secondValue.second());
			final String targetRegister = instruction.getThirdOperand().getValue();
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets a STM instruction.
	 *
	 * @param instruction
	 *            The STM instruction to interpret
	 */
	private void interpretStm(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> sourceValue = loadLongValue(instruction.getFirstOperand());
		final Pair<Boolean, BigInteger> targetValue = loadLongValue(instruction.getThirdOperand());

//		final ReilMemory targetMemory = instruction.getThirdOperand().getValue().equals("SP") ? stack : memory;

		if (sourceValue.first() && targetValue.first())
		{
			final int targetSize = instruction.getFirstOperand().getSize().getIntegerValue();

			memory.store(targetValue.second().longValue(), sourceValue.second().longValue(), targetSize);
		}
		else
		{
			System.out.println(sourceValue.toString());
			System.out.println(targetValue.toString());

			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets a STR instruction.
	 *
	 * @param instruction
	 *            The STR instruction to interpret
	 */
	private void interpretStr(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());

		if (firstValue.first())
		{
			final BigInteger result = firstValue.second();
			final String targetRegister = instruction.getThirdOperand().getValue();
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets a SUB instruction.
	 *
	 * @param instruction
	 *            The SUB instruction to interpret
	 */
	private void interpretSub(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());
		final Pair<Boolean, BigInteger> secondValue = loadLongValue(instruction.getSecondOperand());

		if (firstValue.first() && secondValue.first())
		{
			final OperandSize targetSize = instruction.getThirdOperand().getSize();

			final BigInteger result = firstValue.second().subtract(secondValue.second()).and(BigInteger.valueOf(getTruncateMask(targetSize)));
			final String targetRegister = instruction.getThirdOperand().getValue();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Interprets an UNDEF instruction.
	 *
	 * @param instruction
	 *            The UNDEF instruction to interpret
	 */
	private void interpretUndef(final ReilInstruction instruction)
	{
		registers.remove(instruction.getThirdOperand().getValue());
	}

	/**
	 * Interprets a XOR instruction.
	 *
	 * @param instruction
	 *            The XORinstruction to interpret
	 */
	private void interpretXor(final ReilInstruction instruction)
	{
		final Pair<Boolean, BigInteger> firstValue = loadLongValue(instruction.getFirstOperand());
		final Pair<Boolean, BigInteger> secondValue = loadLongValue(instruction.getSecondOperand());

		if (firstValue.first() && secondValue.first())
		{
			final BigInteger result = firstValue.second().xor(secondValue.second());
			final String targetRegister = instruction.getThirdOperand().getValue();
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			setRegister(targetRegister, result, targetSize, ReilRegisterStatus.DEFINED);
		}
		else if (instruction.getFirstOperand().getValue().equals(instruction.getSecondOperand().getValue()))
		{
			final String targetRegister = instruction.getThirdOperand().getValue();
			final OperandSize targetSize = instruction.getThirdOperand().getSize();
			setRegister(targetRegister, BigInteger.ZERO, targetSize, ReilRegisterStatus.DEFINED);
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * Loads the value of an operand into a long value.
	 *
	 * @param operand
	 *            The operand to load
	 *
	 * @return A pair made of a bool and a long value. The bool indicates
	 *         whether loading the value was succesful.
	 */
	private Pair<Boolean, BigInteger> loadLongValue(final ReilOperand operand)
	{
		final OperandType type = operand.getType();
		final String value = operand.getValue();

		if (type == OperandType.INTEGER_LITERAL)
		{
			return new Pair<Boolean, BigInteger>(true, new BigInteger(value));
		}
		else if (type == OperandType.REGISTER)
		{
			if (isDefined(value))
			{
				return new Pair<Boolean, BigInteger>(true, getVariableValue(value));
			}
			else
			{
				return new Pair<Boolean, BigInteger>(false, BigInteger.ZERO);
			}
		}
		else
		{
			return new Pair<Boolean, BigInteger>(false, BigInteger.ZERO);
		}
	}

	/**
	 * Searches the instruction at the next valid PC offset.
	 *
	 * @param pc
	 *            The current value of the program counter
	 * @param instructions
	 *            The instructions to interpret
	 * @param programCounter
	 *            The name of the program counter register
	 *
	 * @return True, if the next valid PC offset could be found. False, otherwise.
	 */
	private boolean searchNextPc(final BigInteger pc, final HashMap<BigInteger, List<ReilInstruction>> instructions, final String programCounter)
	{
		for (int i = 0; i < 10; i++)
		{
			final BigInteger current = pc.add(BigInteger.valueOf(i));

			if (instructions.containsKey(nativeToReil(current)))
			{
				setRegister(programCounter, current, OperandSize.DWORD, ReilRegisterStatus.DEFINED);
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the CPU policy of the interpreter.
	 *
	 * @return The CPU policy of the interpreter
	 */
	public ICpuPolicy getCpuPolicy()
	{
		return cpuPolicy;
	}

	public List<ReilRegister> getDefinedRegisters()
	{
		return new ArrayList<ReilRegister>(registers.values());
	}

	public ReilMemory getMemory()
	{
		return memory;
	}

	public long getMemorySize()
	{
		return memory.getAllocatedMemory();
	}

	/**
	 * Returns the current value of a register.
	 *
	 * @param register
	 *            The register in question
	 *
	 * @return The value of the register
	 *
	 * @throws IllegalArgumentException
	 *             Thrown if the register has no assigned value
	 */
	public BigInteger getVariableValue(final String register)
	{
		if (register == null || !registers.containsKey(register))
		{
			throw new IllegalArgumentException("Error: Register has no value");
		}

		return registers.get(register).getValue();
	}

	/**
	 * Interprets a list of instructions in the current REIL interpreter
	 *
	 * @param instructions
	 *            The instructions to interpret
	 * @param entryPoint
	 *            The offset of the first instruction to interpret
	 * @throws InterpreterException
	 */
	public void interpret(final HashMap<BigInteger, List<ReilInstruction>> instructions, final BigInteger entryPoint) throws InterpreterException
	{
		long index = 0;

		cpuPolicy.start(this);
		interpreterPolicy.start();

		final String programCounter = cpuPolicy.getProgramCounter();

		if (programCounter == null)
		{
			throw new InterpreterException("Error: CPU Policy returned an invalid program counter");
		}

		// Set the program counter to the entry point
		setRegister(programCounter, entryPoint, cpuPolicy.getRegisterSize(programCounter), ReilRegisterStatus.DEFINED);

		while (true)
		{
			BigInteger pc = nativeToReil(getVariableValue(programCounter));

			interpreterPolicy.nextInstruction(this);

			if (!instructions.containsKey(pc))
			{
				throw new InterpreterException(String.format("Error: Instruction at offset %X not found", pc));
			}

			final List<ReilInstruction> instructionList = instructions.get(pc);

			if (instructionList == null || instructionList.size() == 0)
			{
				throw new InterpreterException(String.format("Error: Instruction at offset %X has invalid REIL code",pc));
			}

			setRegister(SUB_PC, BigInteger.ZERO, OperandSize.DWORD,ReilRegisterStatus.DEFINED);

			int subPc = getVariableValue(SUB_PC).intValue();

			while (subPc < instructionList.size())
			{
				final ReilInstruction inst = instructionList.get(subPc);

				interpretInstruction(inst, programCounter);

				final int newSubPc = getVariableValue(SUB_PC).intValue();

				if (subPc == newSubPc)
				{
					subPc = newSubPc + 1;
				}
				else
				{
					subPc = newSubPc;
				}

				setRegister(SUB_PC, BigInteger.valueOf(subPc), OperandSize.DWORD, ReilRegisterStatus.DEFINED);
			}

			final BigInteger pcNew = getVariableValue(programCounter);

			if (pc.equals(nativeToReil(pcNew)))
			{
				pc = pcNew.add(BigInteger.ONE);
			}
			else
			{
				pc = pcNew;
			}

			if (pcNew.equals(BigInteger.valueOf(4294967295L)))
			{
				break;
			}

			if (!searchNextPc(pc, instructions, programCounter))
			{
				break;
			}

			index++;
		}

		interpreterPolicy.end();
	}

	/**
	 * Determines whether a register currently holds a value or not.
	 *
	 * @param register
	 *            The register in question
	 *
	 * @return True, if the register has a value. False, otherwise.
	 */
	public boolean isDefined(final String register)
	{
		return registers.containsKey(register);
	}

	public byte readMemoryByte(final long address)
	{
		return (byte) memory.load(address, 1);
	}

	public long readMemoryDword(final long address)
	{
		return memory.load(address, 4);
	}

	public long readMemoryWord(final long address)
	{
		return memory.load(address, 2);
	}

	/**
	 * Sets a value in the simulated memory.
	 *
	 * @param address
	 *            The address of the value
	 * @param value
	 *            The value to write to the address
	 * @param length
	 *            The number of bytes to write to the address
	 */
	public void setMemory(final long address, final long value, final int length)
	{
		memory.store(address, value, length);
	}

	/**
	 * Sets the value of a register.
	 *
	 * @param register
	 *            The register in question
	 * @param value
	 *            The new value of the register
	 * @param size
	 *            The size of the register
	 * @param status
	 *            The status of the register
	 */
	public void setRegister(final String register, final BigInteger value, final OperandSize size, final ReilRegisterStatus status)
	{
		final ReilRegister r = new ReilRegister(register, size, value);

		if (registers.containsKey(register))
		{
			registers.remove(register);
		}

		if (status == ReilRegisterStatus.DEFINED)
		{
			registers.put(register, r);
		}
	}
}

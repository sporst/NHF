package net.sourceforge.jnhf.disassembler;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.jnhf.fceux.tracefile.IllegalTraceLineException;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogLine;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogParser;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.InternalTranslationException;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilBlock;
import net.sourceforge.jnhf.reil.ReilGraph;
import net.sourceforge.jnhf.reil.ReilInstruction;
import net.sourceforge.jnhf.reil.ReilTranslator;
import net.sourceforge.jnhf.reil.interpreter.Endianness;
import net.sourceforge.jnhf.reil.interpreter.IInterpreterPolicy;
import net.sourceforge.jnhf.reil.interpreter.InterpreterException;
import net.sourceforge.jnhf.reil.interpreter.Policy6502;
import net.sourceforge.jnhf.reil.interpreter.ReilInterpreter;
import net.sourceforge.jnhf.reil.interpreter.ReilRegisterStatus;

public class Disassembler
{
	private static int getFlagValue(final String flagValues, final int i)
	{
		return Character.isUpperCase(flagValues.charAt(i)) ? 1 : 0;
	}

	private static int getFlagValue(final TraceLogLine traceLogLine, final int i)
	{
		return Character.isUpperCase(traceLogLine.getValueFlags().charAt(i)) ? 1 : 0;
	}

	private static List<ReilInstruction> getInstructions(final ReilGraph reilGraph)
	{
		final List<ReilInstruction> instructions = new FilledList<ReilInstruction>();

		for (final ReilBlock reilBlock : reilGraph)
		{
			instructions.addAll(reilBlock.getInstructions());
		}

		return instructions;
	}

	private static int mergeFlags(final String previousFlags)
	{
		return
			getFlagValue(previousFlags, 7) |
			getFlagValue(previousFlags, 6) << 1 |
			getFlagValue(previousFlags, 5) << 2 |
			getFlagValue(previousFlags, 4) << 3 |
			getFlagValue(previousFlags, 3) << 4 |
			getFlagValue(previousFlags, 2) << 5 |
			getFlagValue(previousFlags, 1) << 6 |
			getFlagValue(previousFlags, 0) << 7
		;
	}

	public static void main(final String[] args) throws IOException, IllegalTraceLineException, InternalTranslationException, InterpreterException
	{
		final IFilledList<TraceLogLine> result = TraceLogParser.parse(new File("F:\\@@\\tracelog.txt"));

		final ReilInterpreter interpreter = new ReilInterpreter(Endianness.LITTLE_ENDIAN, new Policy6502(), new FooPolicy());

		interpreter.setRegister("A", BigInteger.ONE, OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("X", BigInteger.ZERO, OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("Y", BigInteger.valueOf(4), OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("N", BigInteger.ONE, OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("V", BigInteger.ZERO, OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("U", BigInteger.ONE, OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("B", BigInteger.ZERO, OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("D", BigInteger.ZERO, OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("Z", BigInteger.ZERO, OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("I", BigInteger.ZERO, OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("C", BigInteger.ZERO, OperandSize.BYTE, ReilRegisterStatus.DEFINED);
		interpreter.setRegister("SP", BigInteger.valueOf(0x1FF), OperandSize.WORD, ReilRegisterStatus.DEFINED);

		interpreter.setMemory(0x0054, 0x9B00, 2);
		interpreter.setMemory(0x0056, 0x9C00, 2);
		interpreter.setMemory(0x0058, 0x9D00, 2);
		interpreter.setMemory(0x005A, 0x9E00, 2);
		interpreter.setMemory(0x005C, 0x9F00, 2);
		interpreter.setMemory(0x005E, 0xD1B7, 2);

		interpreter.setMemory(0x19C, 0x001C, 2);
		interpreter.setMemory(0x19E, 0xC410, 2);
		interpreter.setMemory(0x1DC, 0xFF12, 2);
		interpreter.setMemory(0x1DE, 0xE47B, 2);

		int previousI = 0;
		int previousOffset = 0;
		String previousFlags = null;

		for (int i=0;i<result.size();i++)
		{
			final TraceLogLine traceLogLine = result.get(i);
			final TraceLogLine nextLogLine = result.get(i + 1);

			if (Integer.valueOf(traceLogLine.getValueA(), 16) != interpreter.getVariableValue("A").intValue())
			{
				interpreter.getMemory().printMemory();
				System.out.println("STACK: " + interpreter.getVariableValue("SP").toString(16));
				System.out.println("Invalid A: " + interpreter.getVariableValue("A") + " (Expected: " + traceLogLine.getValueA());
				return;
			}

			if (Integer.valueOf(traceLogLine.getValueX(), 16) != interpreter.getVariableValue("X").intValue())
			{
				System.out.println("Invalid X: " + interpreter.getVariableValue("X"));
				return;
			}

			if (Integer.valueOf(traceLogLine.getValueY(), 16) != interpreter.getVariableValue("Y").intValue())
			{
				System.out.println("Invalid Y");
				return;
			}

			if (getFlagValue(traceLogLine, 0) != interpreter.getVariableValue("N").intValue())
			{
				System.out.println("Invalid N");
				interpreter.getMemory().printMemory();
				System.out.println("STACK: " + interpreter.getVariableValue("SP").toString(16));
				return;
			}
			if (getFlagValue(traceLogLine, 1) != interpreter.getVariableValue("V").intValue())
			{
				System.out.println("Invalid V");
				return;
			}
			if (getFlagValue(traceLogLine, 2) != interpreter.getVariableValue("U").intValue())
			{
				System.out.println("Invalid U");
				return;
			}
//			if (getFlagValue(traceLogLine, 3) != interpreter.getVariableValue("B").intValue())
//			{
//				System.out.println("Invalid B");
//				return;
//			}
			if (getFlagValue(traceLogLine, 4) != interpreter.getVariableValue("D").intValue())
			{
				System.out.println("Invalid D");
				return;
			}
//			if (getFlagValue(traceLogLine, 5) != interpreter.getVariableValue("I").intValue())
//			{
//				System.out.println("Invalid I");
//				return;
//			}
			if (getFlagValue(traceLogLine, 6) != interpreter.getVariableValue("Z").intValue())
			{
				System.out.println("Invalid Z: " + interpreter.getVariableValue("Z"));
				System.out.println(getFlagValue(traceLogLine, 6));
				System.out.println(traceLogLine.getValueFlags());
				return;
			}
			if (getFlagValue(traceLogLine, 7) != interpreter.getVariableValue("C").intValue())
			{
				System.out.println("Invalid C");
				return;
			}

			if (interpreter.getVariableValue("SP").intValue() < 0x100)
			{
				System.out.println("Invalid SP");

				return;
			}

			final int valueI = getFlagValue(traceLogLine, 5);

			if (previousI == 0 && valueI == 1)
			{

				final long newSp = interpreter.getVariableValue("SP").longValue() - 3;

				System.out.println("Correcting to " + String.format("%04X", newSp));

				interpreter.setRegister("SP", BigInteger.valueOf(newSp), OperandSize.WORD, ReilRegisterStatus.DEFINED);
				interpreter.setMemory(newSp + 3, previousOffset >> 8, 1);
				interpreter.setMemory(newSp + 2, previousOffset & 0xFF, 1);
				interpreter.setMemory(newSp + 1, mergeFlags(previousFlags), 1);
			}

			previousI = valueI;

			if (traceLogLine.getMemoryAddress() != null)
			{
				final int address = Integer.valueOf(traceLogLine.getMemoryAddress(), 16);

				try
				{
					interpreter.readMemoryByte(address);
				}
				catch(final IllegalArgumentException e)
				{
					interpreter.setMemory(address, Integer.valueOf(traceLogLine.getMemoryValue(), 16), 1);
				}
			}

			final Instruction instruction = InstructionDisassembler.disassemble(Integer.valueOf(traceLogLine.getAddress(), 16), traceLogLine.getData());

			if (instruction == null)
			{
				throw new IllegalStateException(traceLogLine.getData());
			}

			if (instruction.getMnemonic().equals("LDA") && "01FE".equals(traceLogLine.getMemoryAddress()))
			{
				interpreter.setMemory(0x01FE, Integer.valueOf(nextLogLine.getValueA(), 16), 1);
			}
			else if (instruction.getMnemonic().equals("LDA") && "2002".equals(traceLogLine.getMemoryAddress()))
			{
				interpreter.setMemory(0x2002, Integer.valueOf(nextLogLine.getValueA(), 16), 1);
			}
			else if (instruction.getMnemonic().equals("LDA") && "4016".equals(traceLogLine.getMemoryAddress()))
			{
				interpreter.setMemory(0x4016, Integer.valueOf(nextLogLine.getValueA(), 16), 1);
			}
			else if (instruction.getMnemonic().equals("LDA") && "4017".equals(traceLogLine.getMemoryAddress()))
			{
				interpreter.setMemory(0x4017, Integer.valueOf(nextLogLine.getValueA(), 16), 1);
			}

			System.out.printf("%08X\n", instruction.getAddress());

			final ReilGraph reilGraph = ReilTranslator.translate(instruction);

			final HashMap<BigInteger, List<ReilInstruction>> instructions = new HashMap<BigInteger, List<ReilInstruction>>();

			instructions.put(BigInteger.valueOf(instruction.getAddress() * 0x100), getInstructions(reilGraph));

			interpreter.interpret(instructions, BigInteger.valueOf(instruction.getAddress()));

//			System.out.println("STACK: " + interpreter.getVariableValue("SP").toString(16));

			previousOffset = Integer.valueOf(traceLogLine.getAddress(), 16) + traceLogLine.getData().length() / 2;
			previousFlags = traceLogLine.getValueFlags();

//			interpreter.getMemory().printMemory();

		}

		System.out.println("OK");
	}

	private static class FooPolicy implements IInterpreterPolicy
	{
		@Override
		public void end()
		{
		}

		@Override
		public void nextInstruction(final ReilInterpreter interpreter)
		{
		}

		@Override
		public void start()
		{
		}
	}
}

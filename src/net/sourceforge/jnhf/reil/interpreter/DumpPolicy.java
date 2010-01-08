package net.sourceforge.jnhf.reil.interpreter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import net.sourceforge.jnhf.reil.OperandSize;

public class DumpPolicy implements IInterpreterPolicy
{
	private BufferedWriter writer = null;

	public DumpPolicy(final String filename) throws IOException
	{
		writer = new BufferedWriter(new FileWriter(new File(filename)));
	}

	private void printRegisters(final ReilInterpreter interpreter, final ICpuPolicy cpuPolicy, final String[] registers, final String[] flags) throws IOException
	{
		for (final String register : registers)
		{
			final BigInteger value = interpreter.isDefined(register) ? interpreter.getVariableValue(register) : BigInteger.ZERO;

			final OperandSize registerSize = cpuPolicy.getRegisterSize(register);

			final String mask = "%0" + 2 * registerSize.getIntegerValue() + "X";

			writer.write(String.format("%s: " + mask + " ", register.toUpperCase(), value));
		}

		long value = 0;
		int i = 0;

		for (final String flag : flags)
		{
			final int flagValue = interpreter.isDefined(flag) ? interpreter.getVariableValue(flag).intValue() : 0;

			value = value + (flagValue << (31 - i));
			i++;
		}

		writer.write(String.format("%s: %08X ", "FOO", value));
		writer.write("\n");
	}

	public void end()
	{
		try
		{
			writer.close();
		}
		catch (final Exception e)
		{
			assert false;
		}
	}

	public void nextInstruction(final ReilInterpreter interpreter)
	{
		final ICpuPolicy cpuPolicy = interpreter.getCpuPolicy();

		final String[] registers = cpuPolicy.getRegisters();
		final String[] flags = cpuPolicy.getFlags();

		try
		{
			printRegisters(interpreter, cpuPolicy, registers, flags);
		}
		catch (final IOException e)
		{
			System.out.println(e);
			assert false;
		}
	}

	public void start()
	{
	}
}

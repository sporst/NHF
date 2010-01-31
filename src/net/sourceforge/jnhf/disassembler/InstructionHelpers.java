package net.sourceforge.jnhf.disassembler;

public class InstructionHelpers
{
	public static boolean isBranch(final Instruction instruction)
	{
		final String mnemonic = instruction.getMnemonic();

		return
			"BPL".equals(mnemonic) ||
			"BMI".equals(mnemonic) ||
			"BVC".equals(mnemonic) ||
			"BVS".equals(mnemonic) ||
			"BCC".equals(mnemonic) ||
			"BCS".equals(mnemonic) ||
			"BNE".equals(mnemonic) ||
			"BEQ".equals(mnemonic)
		;
	}
}

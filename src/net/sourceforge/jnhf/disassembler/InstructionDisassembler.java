package net.sourceforge.jnhf.disassembler;

/**
 * Disassembler for individual instructions.
 */
public final class InstructionDisassembler
{
	/**
	 * This array exists for performance reasons.
	 */
	private static String[] HEX_VALUES = new String[] {
		"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F",
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F",
		"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F",
		"30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F",
		"40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F",
		"50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F",
		"60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F",
		"70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F",
		"80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F",
		"90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
		"A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF",
		"B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF",
		"C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF",
		"D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF",
		"E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF",
		"F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF"
	};

	/**
	 * Disassembles a single instruction.
	 *
	 * @param address The address of the instruction in the data stream.
	 * @param data The data stream to disassemble.
	 *
	 * @return The disassembled instruction.
	 */
	public static Instruction disassemble(final int address, final byte[] data)
	{
		if (data == null)
		{
			throw new IllegalArgumentException("Error: Data argument can not be null");
		}

		String mnemonic = null;
		Operand operand = null;

		boolean indirectX = false;
		boolean zeroPage = false;
		boolean immediate = false;
		boolean absolute = false;
		boolean branch = false;
		boolean indirectY = false;
		boolean zeroPageX = false;
		boolean absoluteY = false;
		boolean absoluteX = false;
		boolean zeroPageY = false;
		boolean jump = false;
		boolean jumpIndirect = false;

		switch(data[0] & 0xFF)
		{
		case 0x00: mnemonic = "BRK"; break;
		case 0x08: mnemonic = "PHP"; break;
		case 0x0A: mnemonic = "ASL"; break;
		case 0x18: mnemonic = "CLC"; break;
		case 0x28: mnemonic = "PLP"; break;
		case 0x2A: mnemonic = "ROL"; break;
		case 0x38: mnemonic = "SEC"; break;
		case 0x40: mnemonic = "RTI"; break;
		case 0x48: mnemonic = "PHA"; break;
		case 0x4A: mnemonic = "LSR"; break;
		case 0x58: mnemonic = "CLI"; break;
		case 0x60: mnemonic = "RTS"; break;
		case 0x68: mnemonic = "PLA"; break;
		case 0x6A: mnemonic = "ROR"; break;
		case 0x78: mnemonic = "SEI"; break;
		case 0x88: mnemonic = "DEY"; break;
		case 0x8A: mnemonic = "TXA"; break;
		case 0x98: mnemonic = "TYA"; break;
		case 0x9A: mnemonic = "TXS"; break;
		case 0xA8: mnemonic = "TAY"; break;
		case 0xAA: mnemonic = "TAX"; break;
		case 0xB8: mnemonic = "CLV"; break;
		case 0xBA: mnemonic = "TSX"; break;
		case 0xC8: mnemonic = "INY"; break;
		case 0xCA: mnemonic = "DEX"; break;
		case 0xD8: mnemonic = "CLD"; break;
		case 0xE8: mnemonic = "INX"; break;
		case 0xEA: mnemonic = "NOP"; break;
		case 0xF8: mnemonic = "SED"; break;

		//(Indirect,X)
		case 0x01: mnemonic = "ORA"; indirectX  = true; break;
		case 0x21: mnemonic = "AND"; indirectX = true; break;
		case 0x41: mnemonic = "EOR"; indirectX = true; break;
		case 0x61: mnemonic = "ADC"; indirectX = true; break;
		case 0x81: mnemonic = "STA"; indirectX = true; break;
		case 0xA1: mnemonic = "LDA"; indirectX = true; break;
		case 0xC1: mnemonic = "CMP"; indirectX = true; break;
		case 0xE1: mnemonic = "SBC"; indirectX = true; break;

		//Zero Page
		case 0x05: mnemonic = "ORA"; zeroPage  = true; break;
		case 0x06: mnemonic = "ASL"; zeroPage = true; break;
		case 0x24: mnemonic = "BIT"; zeroPage = true; break;
		case 0x25: mnemonic = "AND"; zeroPage = true; break;
		case 0x26: mnemonic = "ROL"; zeroPage = true; break;
		case 0x45: mnemonic = "EOR"; zeroPage = true; break;
		case 0x46: mnemonic = "LSR"; zeroPage = true; break;
		case 0x65: mnemonic = "ADC"; zeroPage = true; break;
		case 0x66: mnemonic = "ROR"; zeroPage = true; break;
		case 0x84: mnemonic = "STY"; zeroPage = true; break;
		case 0x85: mnemonic = "STA"; zeroPage = true; break;
		case 0x86: mnemonic = "STX"; zeroPage = true; break;
		case 0xA4: mnemonic = "LDY"; zeroPage = true; break;
		case 0xA5: mnemonic = "LDA"; zeroPage = true; break;
		case 0xA6: mnemonic = "LDX"; zeroPage = true; break;
		case 0xC4: mnemonic = "CPY"; zeroPage = true; break;
		case 0xC5: mnemonic = "CMP"; zeroPage = true; break;
		case 0xC6: mnemonic = "DEC"; zeroPage = true; break;
		case 0xE4: mnemonic = "CPX"; zeroPage = true; break;
		case 0xE5: mnemonic = "SBC"; zeroPage = true; break;
		case 0xE6: mnemonic = "INC"; zeroPage = true; break;

		//#Immediate
		case 0x09: mnemonic = "ORA"; immediate = true; break;
		case 0x29: mnemonic = "AND"; immediate = true; break;
		case 0x49: mnemonic = "EOR"; immediate = true; break;
		case 0x69: mnemonic = "ADC"; immediate = true; break;
		case 0xA0: mnemonic = "LDY"; immediate = true; break;
		case 0xA2: mnemonic = "LDX"; immediate = true; break;
		case 0xA9: mnemonic = "LDA"; immediate = true; break;
		case 0xC0: mnemonic = "CPY"; immediate = true; break;
		case 0xC9: mnemonic = "CMP"; immediate = true; break;
		case 0xE0: mnemonic = "CPX"; immediate = true; break;
		case 0xE9: mnemonic = "SBC"; immediate = true; break;

		//Absolute
		case 0x0D:mnemonic = "ORA"; absolute = true; break;
		case 0x0E:mnemonic = "ASL"; absolute = true; break;
		case 0x2C:mnemonic = "BIT"; absolute = true; break;
		case 0x2D:mnemonic = "AND"; absolute = true; break;
		case 0x2E:mnemonic = "ROL"; absolute = true; break;
		case 0x4D:mnemonic = "EOR"; absolute = true; break;
		case 0x4E:mnemonic = "LSR"; absolute = true; break;
		case 0x6D:mnemonic = "ADC"; absolute = true; break;
		case 0x6E:mnemonic = "ROR"; absolute = true; break;
		case 0x8C:mnemonic = "STY"; absolute = true; break;
		case 0x8D:mnemonic = "STA"; absolute = true; break;
		case 0x8E:mnemonic = "STX"; absolute = true; break;
		case 0xAC:mnemonic = "LDY"; absolute = true; break;
		case 0xAD:mnemonic = "LDA"; absolute = true; break;
		case 0xAE:mnemonic = "LDX"; absolute = true; break;
		case 0xCC:mnemonic = "CPY"; absolute = true; break;
		case 0xCD:mnemonic = "CMP"; absolute = true; break;
		case 0xCE:mnemonic = "DEC"; absolute = true; break;
		case 0xEC:mnemonic = "CPX"; absolute = true; break;
		case 0xED:mnemonic = "SBC"; absolute = true; break;
		case 0xEE:mnemonic = "INC"; absolute = true; break;

		//branches
		case 0x10: mnemonic = "BPL"; branch = true; break;
		case 0x30: mnemonic = "BMI"; branch = true; break;
		case 0x50: mnemonic = "BVC"; branch = true; break;
		case 0x70: mnemonic = "BVS"; branch = true; break;
		case 0x90: mnemonic = "BCC"; branch = true; break;
		case 0xB0: mnemonic = "BCS"; branch = true; break;
		case 0xD0: mnemonic = "BNE"; branch = true; break;
		case 0xF0: mnemonic = "BEQ"; branch = true; break;

		//(Indirect),Y
		case 0x11: mnemonic = "ORA"; indirectY = true; break;
		case 0x31: mnemonic = "AND"; indirectY = true; break;
		case 0x51: mnemonic = "EOR"; indirectY = true; break;
		case 0x71: mnemonic = "ADC"; indirectY = true; break;
		case 0x91: mnemonic = "STA"; indirectY = true; break;
		case 0xB1: mnemonic = "LDA"; indirectY = true; break;
		case 0xD1: mnemonic = "CMP"; indirectY = true; break;
		case 0xF1: mnemonic = "SBC"; indirectY = true; break;

		//Zero Page,X
		case 0x15: mnemonic = "ORA"; zeroPageX = true; break;
		case 0x16: mnemonic = "ASL"; zeroPageX = true; break;
		case 0x35: mnemonic = "AND"; zeroPageX = true; break;
		case 0x36: mnemonic = "ROL"; zeroPageX = true; break;
		case 0x55: mnemonic = "EOR"; zeroPageX = true; break;
		case 0x56: mnemonic = "LSR"; zeroPageX = true; break;
		case 0x75: mnemonic = "ADC"; zeroPageX = true; break;
		case 0x76: mnemonic = "ROR"; zeroPageX = true; break;
		case 0x94: mnemonic = "STY"; zeroPageX = true; break;
		case 0x95: mnemonic = "STA"; zeroPageX = true; break;
		case 0xB4: mnemonic = "LDY"; zeroPageX = true; break;
		case 0xB5: mnemonic = "LDA"; zeroPageX = true; break;
		case 0xD5: mnemonic = "CMP"; zeroPageX = true; break;
		case 0xD6: mnemonic = "DEC"; zeroPageX = true; break;
		case 0xF5: mnemonic = "SBC"; zeroPageX = true; break;
		case 0xF6: mnemonic = "INC"; zeroPageX = true; break;

		//Absolute,Y
		case 0x19: mnemonic = "ORA"; absoluteY = true; break;
		case 0x39: mnemonic = "AND"; absoluteY = true; break;
		case 0x59: mnemonic = "EOR"; absoluteY = true; break;
		case 0x79: mnemonic = "ADC"; absoluteY = true; break;
		case 0x99: mnemonic = "STA"; absoluteY = true; break;
		case 0xB9: mnemonic = "LDA"; absoluteY = true; break;
		case 0xBE: mnemonic = "LDX"; absoluteY = true; break;
		case 0xD9: mnemonic = "CMP"; absoluteY = true; break;
		case 0xF9: mnemonic = "SBC"; absoluteY = true; break;

		//Absolute,X
		case 0x1D: mnemonic = "ORA"; absoluteX = true; break;
		case 0x1E: mnemonic = "ASL"; absoluteX = true; break;
		case 0x3D: mnemonic = "AND"; absoluteX = true; break;
		case 0x3E: mnemonic = "ROL"; absoluteX = true; break;
		case 0x5D: mnemonic = "EOR"; absoluteX = true; break;
		case 0x5E: mnemonic = "LSR"; absoluteX = true; break;
		case 0x7D: mnemonic = "ADC"; absoluteX = true; break;
		case 0x7E: mnemonic = "ROR"; absoluteX = true; break;
		case 0x9D: mnemonic = "STA"; absoluteX = true; break;
		case 0xBC: mnemonic = "LDY"; absoluteX = true; break;
		case 0xBD: mnemonic = "LDA"; absoluteX = true; break;
		case 0xDD: mnemonic = "CMP"; absoluteX = true; break;
		case 0xDE: mnemonic = "DEC"; absoluteX = true; break;
		case 0xFD: mnemonic = "SBC"; absoluteX = true; break;
		case 0xFE: mnemonic = "INC"; absoluteX = true; break;

		//jumps
		case 0x20: mnemonic = "JSR"; jump = true; break;
		case 0x4C: mnemonic = "JMP"; jump = true; break;

		// indirect jumps
		case 0x6C: mnemonic = "JMP"; jumpIndirect = true; break;

		//Zero Page,Y
		case 0x96: mnemonic = "STX"; zeroPageY = true; break;
		case 0xB6: mnemonic = "LDX"; zeroPageY = true; break;

		default: return null;
		}

		if (zeroPage)
		{
			final OperandExpression memoryExpression = new OperandExpression(null, "[");
			final OperandExpression addressExpression = new OperandExpression(memoryExpression, "00" + HEX_VALUES[data[1] & 0xFF]);

			operand = new Operand(memoryExpression);
		}
		else if (zeroPageX)
		{
			final OperandExpression memoryExpression = new OperandExpression(null, "[");
			final OperandExpression plusExpression = new OperandExpression(memoryExpression, "+");
			final OperandExpression addressExpression = new OperandExpression(plusExpression, "00" + HEX_VALUES[data[1] & 0xFF]);
			final OperandExpression xExpression = new OperandExpression(plusExpression, "X");

			operand = new Operand(memoryExpression);
		}
		else if (zeroPageY)
		{
			final OperandExpression memoryExpression = new OperandExpression(null, "[");
			final OperandExpression plusExpression = new OperandExpression(memoryExpression, "+");
			final OperandExpression addressExpression = new OperandExpression(plusExpression, "00" + HEX_VALUES[data[1] & 0xFF]);
			final OperandExpression xExpression = new OperandExpression(plusExpression, "Y");

			operand = new Operand(memoryExpression);
		}
		else if (absoluteX)
		{
			final OperandExpression memoryExpression = new OperandExpression(null, "[");
			final OperandExpression plusExpression = new OperandExpression(memoryExpression, "+");
			final OperandExpression addressExpression = new OperandExpression(plusExpression, HEX_VALUES[data[2] & 0xFF] + HEX_VALUES[data[1] & 0xFF]);
			final OperandExpression xExpression = new OperandExpression(plusExpression, "X");

			operand = new Operand(memoryExpression);
		}
		else if (absoluteY)
		{
			final OperandExpression memoryExpression = new OperandExpression(null, "[");
			final OperandExpression plusExpression = new OperandExpression(memoryExpression, "+");
			final OperandExpression addressExpression = new OperandExpression(plusExpression, HEX_VALUES[data[2] & 0xFF] + HEX_VALUES[data[1] & 0xFF]);
			final OperandExpression yExpression = new OperandExpression(plusExpression, "Y");

			operand = new Operand(memoryExpression);
		}
		else if (indirectY)
		{
			final OperandExpression memoryExpression = new OperandExpression(null, "[");
			final OperandExpression plusExpression = new OperandExpression(memoryExpression, "+");
			final OperandExpression indirectExpression = new OperandExpression(plusExpression, "(");
			final OperandExpression addressExpression = new OperandExpression(indirectExpression, "00" + HEX_VALUES[data[1] & 0xFF]);
			final OperandExpression xExpression = new OperandExpression(plusExpression, "Y");

			operand = new Operand(memoryExpression);
		}
		else if (indirectX)
		{
			final OperandExpression memoryExpression = new OperandExpression(null, "[");
			final OperandExpression plusExpression = new OperandExpression(memoryExpression, "+");
			final OperandExpression indirectExpression = new OperandExpression(plusExpression, "(");
			final OperandExpression addressExpression = new OperandExpression(indirectExpression, "00" + HEX_VALUES[data[1] & 0xFF]);
			final OperandExpression xExpression = new OperandExpression(plusExpression, "X");

			operand = new Operand(memoryExpression);
		}
		else if (immediate)
		{
			final OperandExpression immediateExpression = new OperandExpression(null, "00" + HEX_VALUES[data[1] & 0xFF]);

			operand = new Operand(immediateExpression);
		}
		else if (absolute)
		{
			final OperandExpression memoryExpression = new OperandExpression(null, "[");
			final OperandExpression immediateExpression = new OperandExpression(memoryExpression, HEX_VALUES[data[2] & 0xFF] + HEX_VALUES[data[1] & 0xFF]);

			operand = new Operand(memoryExpression);
		}
		else if (branch)
		{
			final int value = (address + data[1] + 2) & 0xFFFF;

			final OperandExpression immediateExpression = new OperandExpression(null, String.format("%04X", value));

			operand = new Operand(immediateExpression);
		}
		else if (jump)
		{
			final OperandExpression immediateExpression = new OperandExpression(null, HEX_VALUES[data[2] & 0xFF] + HEX_VALUES[data[1] & 0xFF]);

			operand = new Operand(immediateExpression);
		}
		else if (jumpIndirect)
		{
			final OperandExpression memoryExpression = new OperandExpression(null, "(");
			final OperandExpression immediateExpression = new OperandExpression(memoryExpression, HEX_VALUES[data[2] & 0xFF] + HEX_VALUES[data[1] & 0xFF]);

			operand = new Operand(memoryExpression);
		}

		return new Instruction(address, mnemonic, operand);
	}

	/**
	 * Disassembles a single instruction from a hex string stream.
	 *
	 * @param address The address of the instruction.
	 * @param hex The hexadecimal value stream that represents the instructions.
	 *
	 * @return The disassembled instruction.
	 */
	public static Instruction disassemble(final int address, final String hex)
	{
		final byte[] data = new byte[hex.length() / 2];

		for (int i=0;i<data.length;i++)
		{
			data[i] = (byte) Long.valueOf(hex.substring(2 * i, 2 * i + 2), 16).longValue();
		}


		return disassemble(address, data);
	}
}

package net.sourceforge.jnhf.reil;

public enum OperandSize
{
	EMPTY,
	BYTE,
	WORD,
	DWORD,
	QWORD,
	OWORD,
	ADDRESS;

	public static boolean isSizeString(final String value)
	{
		return "byte".equals(value) || "word".equals(value) || "dword".equals(value) || "qword".equals(value) || "oword".equals(value);
	}

	public static OperandSize sizeStringToValue(final String value)
	{
		if ("".equals(value))
		{
			return OperandSize.EMPTY;
		}
		else if ("byte".equals(value))
		{
			return OperandSize.BYTE;
		}
		else if ("word".equals(value))
		{
			return OperandSize.WORD;
		}
		else if ("dword".equals(value))
		{
			return OperandSize.DWORD;
		}
		else if ("qword".equals(value))
		{
			return OperandSize.QWORD;
		}
		else if ("oword".equals(value))
		{
			return OperandSize.OWORD;
		}
		else if ("address".equals(value))
		{
			return OperandSize.ADDRESS;
		}
		else
		{
			throw new IllegalStateException("Unknown operand string " + value);
		}
	}

	public int getBits()
	{
		return getIntegerValue() * 8;
	}

	public int getIntegerValue()
	{
		if (this == BYTE)
		{
			return 1;
		}
		else if (this == WORD)
		{
			return 2;
		}
		else if (this == DWORD)
		{
			return 4;
		}
		else if (this == QWORD)
		{
			// ESCA-JAVA0076:
			return 8;
		}
		else if (this == OWORD)
		{
			return 16;
		}
		else
		{
			return 0;
		}
	}

	public String toSizeString()
	{
		switch(this)
		{
		case BYTE: return "byte";
		case WORD: return "word";
		case DWORD: return "dword";
		case QWORD: return "qword";
		case OWORD: return "oword";
		case EMPTY: return "";
		case ADDRESS: return "address";
		default:throw new IllegalStateException("Error: Unknown size");
		}
	}
}

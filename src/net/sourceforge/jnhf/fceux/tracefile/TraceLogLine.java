package net.sourceforge.jnhf.fceux.tracefile;

public class TraceLogLine
{
	private final String m_address;
	private final String m_data;
	private final String m_instruction;
	private final String m_memoryAddress;
	private final String m_memoryValue;
	private final String m_valueA;
	private final String m_valueX;
	private final String m_valueY;
	private final String m_valueFlags;

	public TraceLogLine(final String address, final String data, final String instruction, final String memoryAddress, final String memoryValue, final String valueA, final String valueX, final String valueY, final String valueFlags)
	{
		if (address == null)
		{
			throw new IllegalArgumentException("Address argument can not be null");
		}

		if (data == null)
		{
			throw new IllegalArgumentException("Data argument can not be null");
		}

		if (instruction == null)
		{
			throw new IllegalArgumentException("Instruction argument can not be null");
		}

		// memoryAddress can be null

		// memoryValue can be null

		if (valueA == null)
		{
			throw new IllegalArgumentException("ValueA argument can not be null");
		}

		if (valueX == null)
		{
			throw new IllegalArgumentException("ValueB argument can not be null");
		}

		if (valueY == null)
		{
			throw new IllegalArgumentException("ValueC argument can not be null");
		}

		if (valueFlags == null)
		{
			throw new IllegalArgumentException("ValueD argument can not be null");
		}

		m_address = address;
		m_data = data;
		m_instruction = instruction;
		m_memoryAddress = memoryAddress;
		m_memoryValue = memoryValue;
		m_valueA = valueA;
		m_valueX = valueX;
		m_valueY = valueY;
		m_valueFlags = valueFlags;
	}

	public String getAddress()
	{
		return m_address;
	}

	public String getData()
	{
		return m_data;
	}

	public String getInstruction()
	{
		return m_instruction;
	}

	public String getMemoryAddress()
	{
		return m_memoryAddress;
	}

	public String getMemoryValue()
	{
		return m_memoryValue;
	}

	public String getValueA()
	{
		return m_valueA;
	}

	public String getValueFlags()
	{
		return m_valueFlags;
	}

	public String getValueX()
	{
		return m_valueX;
	}

	public String getValueY()
	{
		return m_valueY;
	}
}

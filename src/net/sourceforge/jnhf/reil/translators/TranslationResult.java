package net.sourceforge.jnhf.reil.translators;

import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.reil.OperandSize;
import net.sourceforge.jnhf.reil.ReilInstruction;

public class TranslationResult
{
	private final IFilledList<ReilInstruction> m_instructions;
	private final String m_register;
	private final long m_offset;
	private final OperandSize m_size;
	private final String m_memoryAddress;

	public TranslationResult(final long offset, final String register, final OperandSize size, final String memoryAddress, final IFilledList<ReilInstruction> instructions)
	{
		m_offset = offset;
		m_register = register;
		m_size = size;
		m_memoryAddress = memoryAddress;
		m_instructions = new FilledList<ReilInstruction>(instructions);
	}

	public IFilledList<ReilInstruction> getInstructions()
	{
		return new FilledList<ReilInstruction>(m_instructions);
	}

	public String getMemoryAddress()
	{
		return m_memoryAddress;
	}

	public long getNextOffset()
	{
		return m_offset;
	}

	public String getResultRegister()
	{
		return m_register;
	}

	public OperandSize getResultSize()
	{
		return m_size;
	}
}

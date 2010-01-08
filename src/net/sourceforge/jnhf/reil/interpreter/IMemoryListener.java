package net.sourceforge.jnhf.reil.interpreter;

/**
 * Interface that must be implemented by all classes that want to be notified
 * about changes in the simulated memory.
 */
public interface IMemoryListener
{
	/**
	 * Invoked after the content of the memory changed.
	 * 
	 * @param address The address of the first memory cell that changed.
	 * @param size Number of consecutive bytes that changed.
	 */
	void memoryChanged(long address, int size);

	/**
	 * Invoked after the content of the memory was cleared.
	 */
	void memoryCleared();
}

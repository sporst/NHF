package net.sourceforge.jnhf.helpers;

/**
 * This class contains functions for commonly used byte operations.
 */
public final class ByteHelpers
{
	/**
	 * Reads a Big Endian DWORD value from a byte array.
	 *
	 * @param data The byte array from which the DWORD value is read.
	 * @param offset The index of the array element where DWORD reading begins.
	 *
	 * @return The DWORD value read from the array.
	 */
	public static long readDwordBigEndian(final byte[] data, final int offset)
	{
		return
			(data[offset + 0] & 0xFFL) * 0x100 * 0x100 * 0x100 +
			(data[offset + 1] & 0xFFL) * 0x100 * 0x100 +
			(data[offset + 2] & 0xFFL) * 0x100 +
			(data[offset + 3] & 0xFFL);
	}

	/**
	 * Reads a Little Endian DWORD value from a byte array.
	 *
	 * @param data The byte array from which the DWORD value is read.
	 * @param offset The index of the array element where DWORD reading begins.
	 *
	 * @return The DWORD value read from the array.
	 */
	public static long readDwordLittleEndian(final byte[] data, final int offset)
	{
		return
			(data[offset + 3] & 0xFFL) * 0x100 * 0x100 * 0x100 +
			(data[offset + 2] & 0xFFL) * 0x100 * 0x100 +
			(data[offset + 1] & 0xFFL) * 0x100 +
			(data[offset + 0] & 0xFFL);
	}

	/**
	 * Reads a Big Endian QWORD value from a byte array.
	 *
	 * @param data The byte array from which the QWORD value is read.
	 * @param offset The index of the array element where QWORD reading begins.
	 *
	 * @return The QWORD value read from the array.
	 */
	public static long readQwordBigEndian(final byte[] data, final int offset)
	{
		return
			(data[offset + 0] & 0xFFL) * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 +
			(data[offset + 1] & 0xFFL) * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 +
			(data[offset + 2] & 0xFFL) * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 +
			(data[offset + 3] & 0xFFL) * 0x100 * 0x100 * 0x100 * 0x100 +
			(data[offset + 4] & 0xFFL) * 0x100 * 0x100 * 0x100 +
			(data[offset + 5] & 0xFFL) * 0x100 * 0x100 +
			(data[offset + 6] & 0xFFL) * 0x100 +
			(data[offset + 7] & 0xFFL);
	}

	/**
	 * Reads a Little Endian QWORD value from a byte array.
	 *
	 * @param data The byte array from which the QWORD value is read.
	 * @param offset The index of the array element where QWORD reading begins.
	 *
	 * @return The QWORD value read from the array.
	 */
	public static long readQwordLittleEndian(final byte[] data, final int offset)
	{
		return
			(data[offset + 7] & 0xFFL) * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 +
			(data[offset + 6] & 0xFFL) * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 +
			(data[offset + 5] & 0xFFL) * 0x100 * 0x100 * 0x100 * 0x100 * 0x100 +
			(data[offset + 4] & 0xFFL) * 0x100 * 0x100 * 0x100 * 0x100 +
			(data[offset + 3] & 0xFFL) * 0x100 * 0x100 * 0x100 +
			(data[offset + 2] & 0xFFL) * 0x100 * 0x100 +
			(data[offset + 1] & 0xFFL) * 0x100 +
			(data[offset + 0] & 0xFFL);
	}

	/**
	 * Reads a Big Endian WORD value from a byte array.
	 *
	 * @param data The byte array from which the WORD value is read.
	 * @param offset The index of the array element where WORD reading begins.
	 *
	 * @return The WORD value read from the array.
	 */
	public static long readWordBigEndian(final byte[] data, final int offset)
	{
		return
			(data[offset + 0] & 0xFFL) * 0x100 +
			(data[offset + 1] & 0xFFL);
	}

	/**
	 * Reads a Little Endian WORD value from a byte array.
	 *
	 * @param data The byte array from which the WORD value is read.
	 * @param offset The index of the array element where WORD reading begins.
	 *
	 * @return The WORD value read from the array.
	 */
	public static int readWordLittleEndian(final byte[] data, final int offset)
	{
		return
			(data[offset + 1] & 0xFF) * 0x100 +
			(data[offset + 0] & 0xFF);
	}

	public static byte[] toBigEndianDword(final long value)
	{
		return new byte[]
		{
				(byte) ((value & 0xFF000000L) >>> 24),
				(byte) ((value & 0x00FF0000L) >>> 16),
				(byte) ((value & 0x0000FF00L) >>> 8),
				(byte) ( value & 0x000000FFL)
		};
	}

	public static byte[] toBigEndianWord(final long value)
	{
		return new byte[]
	    {
				(byte) ((value & 0xFF00L) >>> 8),
				(byte) (value & 0xFF)
	    };
	}

	public static byte[] toLittleEndianDword(final long value)
	{
		return new byte[]
		{
				(byte) ( value & 0x000000FFL),
				(byte) ((value & 0x0000FF00L) >>> 8),
				(byte) ((value & 0x00FF0000L) >>> 16),
				(byte) ((value & 0xFF000000L) >>> 24)
		};
	}

	public static byte[] toLittleEndianWord(final long value)
	{
		return new byte[]
        {
			(byte) (value & 0xFF),
			(byte) ((value & 0xFF00L) >>> 8)
        };
	}
}
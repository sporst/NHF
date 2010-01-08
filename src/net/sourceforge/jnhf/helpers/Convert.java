package net.sourceforge.jnhf.helpers;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class Convert
{
	/**
	 * Converts an ASCII string into a hex string.
	 *
	 * Example: AAA => 414141
	 *
	 * @param asciiString The ASCII string to convert.
	 *
	 * @return The converted hex string.
	 */
	public static String asciiToHexString(final String asciiString)
	{
		final StringBuffer sb = new StringBuffer();

		for (final byte b : asciiString.getBytes())
		{
			sb.append(String.format("%X", b));
		}

		return sb.toString();
	}

	@Deprecated // Talk to sp if you want to use this
	public static String colorToHexString(final Color c)
    {
    	String cs = Integer.toHexString(c.getRGB());
    	cs = cs.substring(2);
    	return cs;
    }

	/**
	 * Converts a decimal string into a hexadecimal string.
	 *
	 * Note that the decimal string value must fit into a long value.
	 *
	 * @param decString The decimal string to convert.
	 *
	 * @return The hexadecimal string.
	 */
	public static String decStringToHexString(final String decString)
	{
		if (decString == null)
		{
			throw new IllegalArgumentException("Error: Decimal string can't be null");
		}

		return Long.toHexString(Long.valueOf(decString, 16));
	}

	/**
	 * Converts a hex to ASCII. If the hex string has an odd number of
	 * characters, a 0 is added at the end of the string.
	 *
	 * Example: 414141 is converted to AAA
	 *
	 * @param hexString The string to convert.
	 *
	 * @return The converted ASCII string.
	 */
	public static String hexStringToAsciiString(final String hexString)
	{
		final String realText = hexString.length() % 2 == 0 ? hexString : "0" + hexString;

		String asciiString = "";

		for (int i = 0; i < realText.length(); i += 2)
		{
			final char c1 = realText.charAt(i);
			final char c2 = realText.charAt(i + 1);

			if (!isHexCharacter(c1) || !isHexCharacter(c2))
			{
				throw new IllegalArgumentException("Error: Invalid hex string");
			}

			final char code = (char) ((Character.digit(c1, 16) << 4) + Character.digit(c2, 16));

			asciiString += isPrintableCharacter(code) ? code : ".";
		}

		return asciiString;
	}

	/**
	 * Converts a hex string to a byte array. If the hex string has an odd
	 * number of characters, a 0 is added at the end of the string.
	 *
	 * Example: 414141 => {0x41, 0x41, 0x41}
	 *
	 * @param hexString The hex string to convert.
	 *
	 * @return The converted byte array.
	 */
	public static byte[] hexStringToBytes(final String hexString)
	{
		final String realText = hexString.length() % 2 == 0 ? hexString : "0" + hexString;

		final byte[] data = new byte[realText.length() / 2];

		for (int i = 0; i < realText.length(); i += 2)
		{
			final char c1 = realText.charAt(i);
			final char c2 = realText.charAt(i + 1);

			if (!isHexCharacter(c1) || !isHexCharacter(c2))
			{
				throw new IllegalArgumentException("Error: Invalid hex string");
			}

			data[i / 2] = (byte) ((Character.digit(c1, 16) << 4) + Character.digit(c2, 16));
		}

		return data;
	}

	/**
	 * Converts an hex string to long.
	 *
	 * @param hex string to convert.
	 *
	 * @return The hex string.
	 */
	public static long hexStringToLong(final String hexString)
	{
		if (hexString == null)
		{
			throw new IllegalArgumentException("Error: Unicode string can't be null");
		}

		if (!isHexString(hexString))
		{
			throw new IllegalArgumentException(String.format("Error: Hex string '%s' is not a vaild hex string", hexString));
		}

		return Long.parseLong(hexString, 16);
	}

	/**
	 * Tests whether a given character is a valid decimal character.
	 *
	 * @param c The character to test.
	 *
	 * @return True, if the given character is a valid decimal character.
	 */
	public static boolean isDecCharacter(final char c)
	{
		return c >= '0' && c <= '9';
	}

	/**
	 * Tests whether a given string is a valid decimal string.
	 *
	 * @param string The string to check.
	 *
	 * @return True, if the string is a valid decimal string. False, otherwise.
	 */
	public static boolean isDecString(final String string)
	{
		if (string == null)
		{
			throw new IllegalArgumentException("Error: String argument can't be null");
		}

		for (int i = 0; i < string.length(); i++)
		{
			if (!isDecCharacter(string.charAt(i)))
			{
				return false;
			}
		}

		return string.length() != 0;
	}

	/**
	 * Tests whether a character is a valid character of a hexadecimal string.
	 *
	 * @param c The character to test.
	 *
	 * @return True, if the character is a hex character. False, otherwise.
	 */
	public static boolean isHexCharacter(final char c)
	{
		return isDecCharacter(c) || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}

	/**
	 * Tests whether a given string is a valid hexadecimal string.
	 *
	 * @param string The string to check.
	 *
	 * @return True, if the string is a valid hexadecimal string. False, otherwise.
	 */
	public static boolean isHexString(final String string)
	{
		if (string == null)
		{
			throw new IllegalArgumentException("Error: String argument can't be null");
		}

		for (int i = 0; i < string.length(); i++)
		{
			if (!isHexCharacter(string.charAt(i)))
			{
				return false;
			}
		}

		return string.length() != 0;
	}

	/**
	 * Tests whether a given string is a valid MD5 string.
	 *
	 * @param string The string to check.
	 *
	 * @return True, if the string is a valid MD5 string. False, otherwise.
	 */
	public static boolean isMD5String(final String string)
	{
		if (string == null)
		{
			throw new IllegalArgumentException("Error: String argument can't be null");
		}

		return string.length() == 32 && isHexString(string);
	}

	/**
	 * Tests whether a character is a printable ASCII character.
	 *
	 * @param c The character to test.
	 *
	 * @return True, if the character is a printable ASCII character. False,
	 *         otherwise.
	 */
	public static boolean isPrintableCharacter(final char c)
	{
	    final Character.UnicodeBlock block = Character.UnicodeBlock.of(c);

	    return (!Character.isISOControl(c)) &&
	            c != KeyEvent.CHAR_UNDEFINED &&
	            block != null &&
	            block != Character.UnicodeBlock.SPECIALS;
	}

	/**
	 * Tests whether a given string is a valid SHA1 string.
	 *
	 * @param string The string to check.
	 *
	 * @return True, if the string is a valid SHA1 string. False, otherwise.
	 */
	public static boolean isSha1String(final String string)
	{
		if (string == null)
		{
			throw new IllegalArgumentException("Error: String argument can't be null");
		}

		return string.length() == 40 && isHexString(string);
	}

	/**
	 * Converts an unicode string to a hex string.
	 *
	 * @param unicodeString The unicode string to convert.
	 *
	 * @return The hex string.
	 */
	public static String unicodeToHexString(final String unicodeString)
	{
		if (unicodeString == null)
		{
			throw new IllegalArgumentException("Error: Unicode string can't be null");
		}

		final StringBuffer sb = new StringBuffer();

		for (final byte b : unicodeString.getBytes())
		{
			sb.append(String.format("%X00", b));
		}

		return sb.toString();
	}
}

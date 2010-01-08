package net.sourceforge.jnhf.reil;

/**
 * Enumeration of possible operand types.
 *
 * @author sp
 *
 */
public enum OperandType {

	/**
	 * Operand does not exist
	 */
	EMPTY,

    /**
     * Operand is an integer literal.
     */
    INTEGER_LITERAL,

    /**
     * Operand is a subaddress.
     */
    SUB_ADDRESS,

    /**
     * Operand is a register.
     */
    REGISTER;

    /**
     * Checks whether a string is a valid integer number.
     *
     * @param value The string to check.
     * @return True or false, depending on whether the string is a number or not.
     */
    private static boolean isInteger(final String value) {
	for (int i = 0; i < value.length(); i++) {
	    final char character = value.charAt(i);

	    if (character != '-' && (character < '0' || character > '9')) {
		return false;
	    }
	}

	return true;
    }

    /**
     * Converts the string representation of an operand
     * to an OperandType value.
     *
     * @param value The string representation of an operand.
     * @return The converted OperandType value.
     */
    public static OperandType getOperandType(final String value) {

    	if ("".equals(value)) {
    		return OperandType.EMPTY;
    	}
		if (value.contains(".")) {
		    return OperandType.SUB_ADDRESS;
		}
		else if (isInteger(value)) {
		    return OperandType.INTEGER_LITERAL;
		}
		else {
		    return OperandType.REGISTER;
		}
    }
}

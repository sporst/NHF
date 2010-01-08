package net.sourceforge.jnhf.reil;

/**
 * This class is used for exceptions that are thrown when something
 * goes wrong that's probably a bug in the translator.
 * 
 * @author sp
 *
 */
public class InternalTranslationException extends Exception {

    /**
     * UID of the class
     */
    private static final long serialVersionUID = -7388260811571010342L;
    
    /**
     * Message of the exception
     */
    private String message;
    
    /**
     * Creates a new InternalTranslationException object.
     * 
     * @param message The exception message
     */
    public InternalTranslationException(final String message) {
	this.message = message;
    }
    
    /**
     * Returns the exception message.
     * 
     * @return The exception message
     */
    public String getMessage() {
	return message;
    }
}

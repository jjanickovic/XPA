package org.xpaframework;

/**
 * <p>This is the superclass exception for the exceptions
 * thrown by serialization and deserialization process.</p>
 * 
 * @author Jan Janickovic
 */
public class MappingException extends Exception {

	private static final long serialVersionUID = 5265691295574342359L;

	public MappingException() {
		super();
	}

	/**
	 * Creates the exception with specified <code>message</code> for
	 * the <code>cause</code>.
	 * 
	 * @param message
	 * @param cause
	 */
	public MappingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates the exception with specified <code>message</code>.
	 * 
	 * @param message
	 */
	public MappingException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Creates the exception with specified <code>cause</code>.
	 * 
	 * @param cause
	 */
	public MappingException(Throwable cause) {
		super(cause);
	}

}

package org.xpaframework;

/**
 * <p>Exception indicating the error during serialization process.</p>
 * 
 * @author Jan Janickovic
 *
 */
public class SerializationException extends MappingException {

	private static final long serialVersionUID = 4576523701065424490L;

	public SerializationException() {
		super();
	}

	/**
	 * Creates the exception with specified <code>message</code> for
	 * the <code>cause</code>.
	 * 
	 * @param message
	 * @param cause
	 */
	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates the exception with specified <code>message</code>.
	 * 
	 * @param message
	 */
	public SerializationException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Creates the exception with specified <code>cause</code>.
	 * 
	 * @param cause
	 */
	public SerializationException(Throwable cause) {
		super(cause);
	}

}

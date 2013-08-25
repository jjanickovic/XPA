package org.xpaframework;

/**
 * <p>Exception indicating the error during deserialization process.</p>
 * 
 * @author Jan Janickovic
 */
public class DeserializationException extends MappingException {

	private static final long serialVersionUID = 4576523701065424490L;

	public DeserializationException() {
		super();
	}

	/**
	 * Creates the exception with specified <code>message</code> for
	 * the <code>cause</code>.
	 * 
	 * @param message
	 * @param cause
	 */
	public DeserializationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates the exception with specified <code>message</code>.
	 * 
	 * @param message
	 */
	public DeserializationException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Creates the exception with specified <code>cause</code>.
	 * 
	 * @param cause
	 */
	public DeserializationException(Throwable cause) {
		super(cause);
	}

}

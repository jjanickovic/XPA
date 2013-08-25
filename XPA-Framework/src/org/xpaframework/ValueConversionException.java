package org.xpaframework;

/**
 * <p>Exception indicating conversion failure.</p>
 * 
 * @author Jan Janickovic
 */
public class ValueConversionException extends DeserializationException {

	private static final long serialVersionUID = -5052281031810345088L;

	public ValueConversionException() {
		super();
	}

	/**
	 * Creates the exception with specified <code>message</code> for
	 * the <code>cause</code>.
	 * 
	 * @param message
	 * @param cause
	 */
	public ValueConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates the exception with specified <code>message</code>.
	 * 
	 * @param message
	 */
	public ValueConversionException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Creates the exception with specified <code>cause</code>.
	 * 
	 * @param cause
	 */
	public ValueConversionException(Throwable cause) {
		super(cause);
	}
	
}

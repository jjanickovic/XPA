package org.xpaframework.xml;

import org.xpaframework.MappingException;

/**
 * <p>Exception indicating error during collecting metadata for target class.
 * </p>
 * 
 * @author Jan Janickovic
 */
public class MetaDataCreationException extends MappingException {

	private static final long serialVersionUID = 1512534883834747442L;

	public MetaDataCreationException() {
		super();
	}

	/**
	 * Creates the exception with specified <code>message</code> for
	 * the <code>cause</code>.
	 * 
	 * @param message
	 * @param cause
	 */
	public MetaDataCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates the exception with specified <code>message</code>.
	 * 
	 * @param message
	 */
	public MetaDataCreationException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Creates the exception with specified <code>cause</code>.
	 * 
	 * @param cause
	 */
	public MetaDataCreationException(Throwable cause) {
		super(cause);
	}

}

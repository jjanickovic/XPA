package org.xpaframework;

import java.nio.charset.Charset;
import java.util.Collection;

/**
 * <p>This interface implementation represents the object-to-data and
 * data-to object conversion. All classes that implements <code>this</code>
 * adapter are used during the serialization/deserialization process.
 * The conversion process is made by {@link ValueAdapterRegistry}
 * where all defined adapter must be registered.</p>
 * <p>The type <code>T</code> parameter of this adapter represents
 * class mapped to specified data type identified by {@link #getType()}
 * result. This indicates that {@link #getType()} method should return
 * the class of this type <code>T</code>. In code this means:
 * <pre>getType().equals(((T) type).getClass())</pre>
 * should return <code>true</code> to ensure that this adapter is
 * returned by {@link ValueAdapterRegistry#getAdapter(Class)}
 * method.</p>
 * 
 * @author Jan Janickovic
 *
 * @param <T> type of the object that represents target data type.
 * 
 * @see ValueAdapterRegistry
 */
public interface ValueAdapter<T> {
	
	public static final String DEFAULT_CHARSET = Charset.defaultCharset().name();
	
	/**
	 * <p>Returns the class representation of this object. The return result should be the
	 * a {@link Class} that is at least assignable from the <code>T</code> parameter, or
	 * like parameterized type, if the <code>T</code> is type of {@link Collection}.</p>
	 * 
	 * @return class type associated with this adapter.
	 */
	Class<?> getType();
	
	/**
	 * Converts the String representation of the element/attribute to the
	 * specified object.
	 * 
	 * @param value - string value to be converted
	 * 
	 * @return converted object
	 * 
	 * @throws Exception the method can throw exception of any type
	 */
	public T convertValue(String value) throws ValueConversionException;

	/**
	 * Converts the <code>T</code> element or attribute to its string representation.
	 * 
	 * @param target - the target object representing simple element or
	 * attribute.
	 * 
	 * @return string representation of the <code>target</code>
	 */
	public String toString(T target);
	
}

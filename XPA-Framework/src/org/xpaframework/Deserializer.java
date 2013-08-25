package org.xpaframework;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import org.xml.sax.InputSource;

import android.content.Context;

/**
 * <p>This deserializer interface providing input parsing and
 * mapping parsed data into the <code>T</code> object. This
 * interface defines the sources if the passed to the target
 * implementation of the deserialization process.</p>
 * 
 * <p>Based no the implementation, <code>null</code>
 * values may be either ignored or will throw exception, so
 * it's recommended to check if the input argument is initialized
 * before any of the implementation is called.</p>
 * 
 * @author Jan Janickovic
 *
 * @param <T> type of the result object.
 * 
 * @see AbstractDeserializer
 */
public interface Deserializer<T> {

	/**
	 * Sets specific {@link ValueAdapterRegistry} for this deserializer.
	 * 
	 * @param adapterHandler - the {@link ValueAdapterRegistry} object.
	 */
	public void setValueAdapterRegistry(ValueAdapterRegistry adapterHandler);
	
	/**
	 * 
	 * @param contex - context of the application/activity.
	 * @param fileName - fully qualified name of the file.
	 * 
	 * @throws MappingException if error during deserialization occurs.
	 */
	public void deserialize(Context contex, String fileName) throws MappingException;
	
	/**
	 * <p>Deserializes data from the <code>url</code>.</p>
	 * 
	 * @param url - request url value.
	 * 
	 * @throws MappingException if error during deserialization occurs.
	 */
	public void deserialize(URL url) throws MappingException;
	
	/**
	 * <p>Deserializes data from the <code>inputSource</code>.</p>
	 * 
	 * @param inputSource
	 * 
	 * @throws MappingException if error during deserialization occurs.
	 */
	public void deserialize(InputSource inputSource) throws MappingException;
	
	/**
	 * <p>Deserializes data from the <code>reader</code>.</p>
	 * 
	 * @param reader
	 * 
	 * @throws MappingException if error during deserialization occurs.
	 */
	public void deserialize(Reader reader) throws MappingException;
	
	/**
	 * <p>Deserializes data from the <code>inputStream</code>.</p>
	 * 
	 * @param inputStream - stream containing data.
	 * 
	 * @throws MappingException if error during deserialization occurs.
	 */
	public void deserialize(InputStream inputStream) throws MappingException;
	
	/**
	 * <p>Deserializes data from the <code>file</code>.</p>
	 * 
	 * @param file - {@link File} object to deserialize.
	 * 
	 * @throws MappingException if error during deserialization occurs.
	 */
	public void deserialize(File file) throws MappingException;
	
	/**
	 * @return single object or object hierarchy (depends on the method
	 * implementation) deserialized from the input data source.
	 */
	public T getValue();
	
}

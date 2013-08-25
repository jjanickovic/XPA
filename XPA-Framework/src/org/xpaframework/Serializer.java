package org.xpaframework;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * <p>This interface defines set of methods to write the serialized
 * object to.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see AbstractSerializer
 */
public interface Serializer {

	public void setValueAdapterRegistry(ValueAdapterRegistry adapterRegistry);
	
	/**
	 * Serializes this object into the specified <code>os</code>.
	 * 
	 * @param obj - object to serialize.
	 * @param os - target {@link OutputStream}.
	 * 
	 * @throws SerializationException if any error occurs
	 * during serialization process.
	 * 
	 * @throws IOException if the serialized object causes this type
	 * of exception during writing into this <code>os</code>.
	 */
	public void serialize(Object obj, OutputStream os) throws SerializationException, IOException;
	
	/**
	 * Serializes this object into the specified <code>url</code>.
	 * 
	 * @param obj - object to serialize.
	 * @param os - target {@link URL}.
	 * 
	 * @throws SerializationException if any error occurs
	 * during serialization process.
	 * 
	 * @throws IOException if the serialized object causes this type
	 * of exception during writing into this <code>url</code>.
	 */
	public void serialize(Object obj, URL url) throws SerializationException, IOException;

	/**
	 * Serializes this object into the specified <code>file</code>.
	 * 
	 * @param obj - object to serialize.
	 * @param file - target {@link File}.
	 * 
	 * @throws SerializationException if any error occurs
	 * during serialization process.
	 * 
	 * @throws IOException if the serialized object causes this type
	 * of exception during writing into this <code>file</code>.
	 */
	public void serialize(Object obj, File file) throws SerializationException, IOException;
}

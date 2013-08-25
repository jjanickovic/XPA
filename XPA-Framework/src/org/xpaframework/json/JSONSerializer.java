package org.xpaframework.json;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import org.json.JSONObject;
import org.xpaframework.SerializationException;
import org.xpaframework.Serializer;
import org.xpaframework.ValueAdapterRegistry;


/**
 * <p>Interface defining methods for JSON data format serialization.
 * The serialization may be processed over following types:
 * <ul>
 * <li>JSONObject</li>
 * <li>JSONElement</li>
 * <li>Map&lt;String, Object&gt;</li>
 * </ul>
 * by method, that serializes this object into appropriate data
 * source.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see JSONObject
 * @see JSONElement
 * @see Map
 */
public interface JSONSerializer extends Serializer {

	/**
	 * @return defined registry containing object responsible
	 * for object-&gt;string conversion.
	 */
	public ValueAdapterRegistry getAdapterRegistry();

	/**
	 * Serializes this <code>json</code> into the <code>os</code>.
	 * 
	 * @param json - {@link JSONObject} instance.
	 * @param os - target output stream.
	 * 
	 * @throws SerializationException if an error occurs during the serialization process.
	 * @throws IOException if any of the I/O exception is thrown when the data are written
	 * into the <code>os</code>.
	 */
	public void serialize(JSONObject json, OutputStream os) throws SerializationException, IOException;
	
	/**
	 * Serializes this <code>json</code> into the <code>url</code>.
	 * 
	 * @param json - {@link JSONObject} instance.
	 * @param url - url where this object will be sent. 
	 * 
	 * @throws SerializationException if an error occurs during the serialization process.
	 * @throws IOException if any of the I/O exception is thrown when the data are written
	 * into the <code>url</code>.
	 */
	public void serialize(JSONObject json, URL url) throws SerializationException, IOException;

	/**
	 * Serializes this <code>json</code> into the <code>file</code>.
	 * 
	 * @param json - {@link JSONObject} instance.
	 * @param file - target file to store JSON data.
	 * 
	 * @throws SerializationException if an error occurs during the serialization process.
	 * @throws IOException if any of the I/O exception is thrown when the data are written
	 * into the <code>file</code>.
	 */
	public void serialize(JSONObject json, File file) throws SerializationException, IOException;

	/**
	 * Serializes this <code>e</code> into the <code>os</code>.
	 * 
	 * @param e - {@link JSONElement} object instance.
	 * @param os - target output stream.
	 * 
	 * @throws SerializationException if an error occurs during the serialization process.
	 * @throws IOException if any of the I/O exception is thrown when the data are written
	 * into the <code>os</code>.
	 * 
	 * @see JSONElement
	 */
	public void serialize(JSONElement e, OutputStream os) throws SerializationException, IOException;
	
	/**
	 * Serializes this <code>e</code> into the <code>url</code>.
	 * 
	 * @param e - {@link JSONElement} object instance.
	 * @param url - url where this object will be sent. 
	 * 
	 * @throws SerializationException if an error occurs during the serialization process.
	 * @throws IOException if any of the I/O exception is thrown when the data are written
	 * into the <code>url</code>.
	 */
	public void serialize(JSONElement e, URL url) throws SerializationException, IOException;

	/**
	 * Serializes this <code>e</code> into the <code>file</code>.
	 * 
	 * @param e - {@link JSONElement} instance.
	 * @param file - target file to store JSON data.
	 * 
	 * @throws SerializationException if an error occurs during the serialization process.
	 * @throws IOException if any of the I/O exception is thrown when the data are written
	 * into the <code>file</code>.
	 */
	public void serialize(JSONElement e, File file) throws SerializationException, IOException;

	/**
	 * Serializes this <code>map</code> into the <code>os</code>.
	 * 
	 * @param map - map containing key-value pairs to serialization.
	 * @param os - target output stream.
	 * 
	 * @throws SerializationException if an error occurs during the serialization process.
	 * @throws IOException if any of the I/O exception is thrown when the data are written
	 * into the <code>os</code>.
	 */
	public void serialize(Map<String, Object> map, OutputStream os) throws SerializationException, IOException;
	
	/**
	 * Serializes this <code>map</code> into the <code>url</code>.
	 * 
	 * @param map - map containing key-value pairs to serialization.
	 * @param url - url where this object will be sent. 
	 * 
	 * @throws SerializationException if an error occurs during the serialization process.
	 * @throws IOException if any of the I/O exception is thrown when the data are written
	 * into the <code>map</code>.
	 */
	public void serialize(Map<String, Object> map, URL url) throws SerializationException, IOException;

	/**
	 * Serializes this <code>map</code> into the <code>file</code>.
	 * 
	 * @param map - map containing key-value pairs to serialization.
	 * @param file - target file to store JSON data.
	 * 
	 * @throws SerializationException if an error occurs during the serialization process.
	 * @throws IOException if any of the I/O exception is thrown when the data are written
	 * into the <code>file</code>.
	 */
	public void serialize(Map<String, Object> map, File file) throws SerializationException, IOException;
	
}

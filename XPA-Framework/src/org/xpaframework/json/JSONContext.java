package org.xpaframework.json;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xpaframework.AbstractSerializer;
import org.xpaframework.SerializationException;
import org.xpaframework.Serializer;
import org.xpaframework.ValueAdapterRegistry;


/**
 * <p>Context providing basic serialization and deserialization implementations
 * over JSON format.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see JSONObject
 */
public class JSONContext {

	public static final String CHARSET_UTF_8 = "UTF-8";
	
	/**
	 * Content type value of the JSON HTTP requests.
	 */
	public static final String CONTENT_TYPE_JSON = "application/json";

	private JSONSerializer serializer;
	
	/**
	 * <p>Creates singleton instance of the {@link Serializer} object.</p>
	 * 
	 * @return {@link Serializer} object.
	 */
	public JSONSerializer createSerializer() {
		return createSerializer(new JSONDataRegistry());
	}
	
	/**
	 * <p>Creates and returns the singleton instance of the
	 * {@link JSONSerializer} with custom adapter registry.</p>
	 * 
	 * @param adapterRegistry - custom adapter registry
	 * 
	 * @return instance of the {@link JSONSerializer} interface.
	 */
	public JSONSerializer createSerializer(ValueAdapterRegistry adapterRegistry) {
		if(this.serializer == null) {
			this.serializer = new JSONSerializerImpl();
		}
		
		if(!adapterRegistry.isInitialized()) {
			adapterRegistry.initialize();
		}
		
		this.serializer.setValueAdapterRegistry(adapterRegistry);
		
		return this.serializer;
	}

	private JSONParser deserializer;
	
	/**
	 * <p>Creates singleton instance of the {@link JSONParser} object.</p>
	 * 
	 * @return {@link JSONParser} object.
	 */
	public JSONParser createDeserializer() {
		if(this.deserializer == null) {
			this.deserializer = new JSONParser();
		}
		
		return this.deserializer;
	}
	
	private static JSONContext instance;

	/**
	 * Lazily creates and returns singleton instance of t<code>this</code>
	 * context.
	 * 
	 * @return singleton instance of <code>this</code> context.
	 */
	public static JSONContext getInstance() {
		if(instance == null) {
			instance = new JSONContext();
		}
		
		return instance;
	}
	
	protected JSONContext() {
	}

	/**
	 * <p>Default implementation of the {@link Serializer} interface providing
	 * serialization of the JSON objects.</p>
	 * <p>This serialization is provides by {@link Object#toString()} method
	 * invocation on the object argument. Each serialization method expects the
	 * {@link JSONObject} as the object argument, or any other object implementing
	 * {@link Object#toString()} method according the JSON format specification.</p>
	 * 
	 * @author Jan Janickovic
	 * 
	 * @see Serializer
	 * @see JSONObject
	 * @see Object#toString()
	 */
	private class JSONSerializerImpl extends AbstractSerializer implements JSONSerializer {
		
		private JSONSerializerImpl() {
		}

		@Override
		public void serialize(JSONElement json, OutputStream os)
				throws SerializationException, IOException {
			
			JSONValueAdapter<Object> adapter = getAdapter(json.getClass());
			String jsonString = adapter.toString(json);
			serialize(jsonString, os);
		}
		
		@Override
		public ValueAdapterRegistry getAdapterRegistry() {
			return super.getAdapterRegistry();
		}

		@Override
		public void serialize(JSONElement element, URL url)
				throws SerializationException, IOException {
			
			JSONValueAdapter<Object> adapter = getAdapter(element.getClass());

			try {
				JSONObject json = adapter.toJsonObject(element);
				serialize(json, url);
			} catch (JSONException e) {
				throw new SerializationException("JSON serialization error!", e);
			}
		}

		@Override
		public void serialize(JSONElement element, File file)
				throws SerializationException, IOException {
			
			JSONValueAdapter<Object> adapter = getAdapter(element.getClass());

			try {
				JSONObject json = adapter.toJsonObject(element);
				serialize(json, file);
			} catch (JSONException e) {
				throw new SerializationException("JSON serialization error!", e);
			}
		}

		@Override
		public void serialize(Map<String, Object> jsonMap, OutputStream os)
				throws SerializationException, IOException {
			
			JSONValueAdapter<Object> adapter = getAdapter(jsonMap.getClass());
			String jsonString = adapter.toString(jsonMap);
			serialize(jsonString, os);
		}

		@Override
		public void serialize(Map<String, Object> jsonMap, URL url)
				throws SerializationException, IOException {
			
			JSONValueAdapter<Object> adapter = getAdapter(jsonMap.getClass());
			
			try {
				JSONObject json = adapter.toJsonObject(jsonMap);
				serialize(json, url);
			} catch (JSONException e) {
				throw new SerializationException("JSON serialization error!", e);
			}
		}

		@Override
		public void serialize(Map<String, Object> jsonMap, File file)
				throws SerializationException, IOException {
			
			JSONValueAdapter<Object> adapter = getAdapter(jsonMap.getClass());
			
			try {
				JSONObject json = adapter.toJsonObject(jsonMap);
				serialize(json, file);
			} catch (JSONException e) {
				throw new SerializationException("JSON serialization error!", e);
			}
		}
		
		/**
		 * <p>Serializes json object to its string representation. The object
		 * conversion to its string representation is provided by
		 * {@link JSONObject#toString()} method.</p>
		 * 
		 * @throws SerializationException
		 * @throws IOException
		 * 
		 * @see #serialize(JSONObject, File)
		 * @see #serialize(JSONObject, URL)
		 * @see JSONObject
		 */
		@Override
		public void serialize(JSONObject json, OutputStream os)
				throws SerializationException, IOException {

			if(json == null) {
				throw new SerializationException(new NullPointerException("null object!"));
			}

			serialize(json.toString(), os);
		}

		@Override
		public void serialize(JSONObject json, URL url)
				throws SerializationException, IOException {
			
			serialize(json, url, CONTENT_TYPE_JSON);
		}

		@Override
		public void serialize(JSONObject json, File file)
				throws SerializationException, IOException {
			serialize(json.toString(), file);
		}

		/**
		 * <p>Serializes object to its string representation into the <code>os</code>
		 * according to JSON format requirements.</p>
		 * <p>The object conversion to its string representation is provided by
		 * {@link Object#toString()} method. It is recommended that this
		 * <code>obj</code> should be of {@link JSONObject} type. If object of
		 * any other type is passed to this method, it must override {@link Object#toString()}
		 * method according to the JSON format.</p>
		 * 
		 * @throws SerializationException
		 * @throws IOException
		 * 
		 * @see #serialize(Object, File)
		 * @see #serialize(Object, URL)
		 * @see JSONObject
		 * @see Object#toString()
		 */
		@Override
		public void serialize(Object obj, OutputStream os)
				throws SerializationException, IOException {
			
			JSONValueAdapter<Object> adapter = getAdapter(obj.getClass());
			
			try {
				JSONObject json = adapter.toJsonObject(obj);
				serialize(json, os);
			} catch (JSONException e) {
				throw new SerializationException("JSON serialization error!", e);
			}
		}

		/**
		 * Serializes this <code>obj</code> to its string representation and passes this
		 * string as the url's content.
		 * 
		 * @throws SerializationException
		 * @throws IOException

		 * @see #serialize(Object, File)
		 * @see #serialize(Object, OutputStream)
		 */
		@Override
		public void serialize(Object obj, URL url)
				throws SerializationException, IOException {

			JSONValueAdapter<Object> adapter = getAdapter(obj.getClass());
			
			try {
				JSONObject json = adapter.toJsonObject(obj);
				serialize(json, url);
			} catch (JSONException e) {
				throw new SerializationException("JSON serialization error!", e);
			}
		}

		@SuppressWarnings("unchecked")
		private JSONValueAdapter<Object> getAdapter(Class<?> adapterClass) {
			return (JSONValueAdapter<Object>) getAdapterRegistry().getAdapter(adapterClass);
		}

		private void serialize(String jsonString, OutputStream os)
				throws SerializationException, IOException {
			byte[] data = jsonString.getBytes(CHARSET_UTF_8);
			DataOutputStream dataOutputStream = null;
			
			try {
				dataOutputStream = new DataOutputStream(os);
				dataOutputStream.write(data);
				dataOutputStream.flush();
			} finally {
				if(dataOutputStream != null) {
					dataOutputStream.close();
				}
			}
		}

	}
}

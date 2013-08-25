package org.xpaframework;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xpaframework.xml.util.Logger;


/**
 * <p>The abstract implementation of the serialization process.
 * This class defines the default target serialization handling
 * to write the serialized object to.</p>
 * 
 * @author Jan Janickovic
 */
public abstract class AbstractSerializer implements Serializer {

	private Logger logger = Logger.getLogger(AbstractSerializer.class);
	
	/**
	 * The name of the Content type request property.
	 */
	protected static final String PROPERTY_CONTENT_TYPE = "Content-Type";

	private ValueAdapterRegistry adapterRegistry;

	protected ValueAdapterRegistry getAdapterRegistry() {
		return adapterRegistry;
	}

	@Override
	public void setValueAdapterRegistry(ValueAdapterRegistry adapterRegistry) {
		this.adapterRegistry = adapterRegistry;
	}
	
	@Override
	public void serialize(Object obj, File file) throws SerializationException, IOException {
		if(!file.exists()) {
			file.createNewFile();
		}
		
		OutputStream os = new FileOutputStream(file);
		serialize(obj, os);
		os.close();
	}

	/**
	 * Serializes this <code>obj</code> into the <code>url</code>.
	 * This serialized request is specifying also content-type
	 * request header to specify the serialized format.
	 * 
	 * @param obj - object to serialize.
	 * @param url - the request url.
	 * @param contentType - the serialized object's content-type header value.
	 * 
	 * @throws IOException
	 * @throws SerializationException
	 */
	protected void serialize(Object obj, URL url, String contentType)
			throws IOException, SerializationException {
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setChunkedStreamingMode(0);
		connection.setRequestProperty(PROPERTY_CONTENT_TYPE, contentType);
		connection.connect();
		
		BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
		serialize(obj, bos);
		
		this.logger.v("response code: " + connection.getResponseCode() +
				", response message: " + connection.getResponseMessage());
		
		connection.disconnect();
	}

}

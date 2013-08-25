package org.xpaframework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.xml.sax.InputSource;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * <p>The abstract implementation of deserializer providing defining
 * base access to the data sources and deserialization process.</p>
 * 
 * @author Jan Janickovic
 *
 * @param <T> - object to be mapped.
 */
public abstract class AbstractDeserializer<T> implements Deserializer<T> {

	private ValueAdapterRegistry adapterRegistry;

	public ValueAdapterRegistry getAdapterRegistry() {
		return adapterRegistry;
	}

	/**
	 * Sets the adapter responsible for data to object mapping.
	 * 
	 * @param adapterRegistry - registry instance. This value is based
	 * on format which <code>this</code> deserializer processes.
	 */
	@Override
	public void setValueAdapterRegistry(ValueAdapterRegistry adapterRegistry) {
		this.adapterRegistry = adapterRegistry;
	}
	
	/**
	 * Deserializes the application resource specified by
	 * <code>fileName</code>.
	 */
	@Override
	public void deserialize(Context context, String fileName) throws MappingException {
		Resources resources = context.getResources();
		AssetManager assetManager = resources.getAssets();
		
		try {
			AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
			
			if(fileDescriptor.getLength() <= 0) {
				return;
			}
			
			deserialize(fileDescriptor.createInputStream());
		} catch (IOException e) {
			throw new MappingException("Deseiralization failure!", e);
		}
	}

	/**
	 * Deserializes the url content.
	 */
	@Override
	public void deserialize(URL url) throws MappingException {
		try {
			URLConnection connection = url.openConnection();
			deserialize(connection.getInputStream());
		} catch (IOException e) {
			throw new MappingException("Deseiralization failure!", e);
		}
	}

	/**
	 * Deserializes input from the <code>reader</code> argument.
	 * 
	 * @param reader - the reader delivering the input format.
	 */
	@Override
	public void deserialize(Reader reader) throws MappingException {
		deserialize(new InputSource(reader));
	}
	
	@Override
	public void deserialize(InputSource inputSource) throws MappingException {
		deserialize(inputSource.getByteStream()); 
	}
	
	@Override
	public void deserialize(File file) throws MappingException {
		try {
			deserialize(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new MappingException("Deseiralization failure! File: " + file, e);
		}
	}
}

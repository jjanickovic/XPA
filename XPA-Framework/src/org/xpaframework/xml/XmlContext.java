package org.xpaframework.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xpaframework.AbstractDeserializer;
import org.xpaframework.AbstractSerializer;
import org.xpaframework.Deserializer;
import org.xpaframework.MappingException;
import org.xpaframework.SerializationException;
import org.xpaframework.Serializer;


/**
 * <p>he context for the XML processing provides full logic of the format
 * objects serialization and deserialization. The serialization and
 * deserialization is provided by objects created within this context by
 * calling <code>createSerializer</code> or <code>createDeserializer</code>
 * methods.</p>
 * <p>The context is created by {@link XmlContextFactory} class by calling
 * <code>XmlContextFactory.createSerializer()</code> method. The serialization
 * and deserialization processes are configured by {@link ContextConfiguration}
 * class.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see XmlContextFactory
 * @see ContextConfiguration
 */
public class XmlContext {

	private ContextConfiguration configuration;

	private Hashtable<Class<?>, MetaDataInterceptor> interceptors =
			new Hashtable<Class<?>, MetaDataInterceptor>();
	
	/**
	 * <p>Creates context instance configured by the <code>options</code>
	 * parameter.</p>
	 * 
	 * @param options - context options
	 */
	protected XmlContext(ContextConfiguration options) {
		this.configuration = options;
	}
	
	private Serializer serializer;
	
	/**
	 * <p>Creates and returns singleton serializer object.</p>
	 * 
	 * @return - serializer responsible for objects serialization.
	 * 
	 * @see Serializer
	 */
	public Serializer createSerializer() {
		if(this.serializer == null) {
			this.serializer = new SerializationWrapper(this.configuration);
			this.serializer.setValueAdapterRegistry(this.configuration.getAdapterRegistry());
		}

		return this.serializer;
	}

	/**
	 * <p>Creates deserializer object for the specified root. This root must be
	 * represented by this <code>clazz</code> object created according to
	 * deserialization specification.</p>
	 * 
	 * @param clazz - class representing root element object.
	 * @return {@link Deserializer} object responsible for document
	 * deserialization.
	 * 
	 * @throws NullPointerException if <code>clazz</code> is <code>null</code>.
	 */
	public <T> Deserializer<T> createDeserializer(Class<T> clazz) throws NullPointerException {
		if(clazz == null) {
			throw new NullPointerException("Root class: null");
		}
		
		MetaDataInterceptor metaDataInterceptor = getMetaDataInterceptor(clazz);
		DeserializationWrapper<T> deserializer = new DeserializationWrapper<T>(
				metaDataInterceptor, this.configuration);
		return deserializer;
	}

	/**
	 * Creates and returns threat resolving meta-data for target <code>clazz</code>.
	 * If the meta-data were created already, the method simply returns created
	 * object.
	 * 
	 * @param clazz - target class for the meta-data creation.
	 * 
	 * @return - started thread creating meta-data or the finished thread,
	 * if object was created for target <code>clazz</code> before.
	 * 
	 * @see Thread#start()
	 */
	private MetaDataInterceptor getMetaDataInterceptor(Class<?> clazz) {
		MetaDataInterceptor interceptor = this.interceptors.get(clazz);
		
		if(interceptor == null) {
			interceptor = new MetaDataInterceptor(clazz, this.configuration);
			this.interceptors.put(clazz, interceptor);
			interceptor.start();
		}
		
		return interceptor;
	}
	
	//Serializer/Deserializer implementation
	
	/**
	 * <p>Class wrapping serialization process.</p>
	 * 
	 * @author Jan Janickovic
	 */
	private class SerializationWrapper extends AbstractSerializer {

		private ContextConfiguration contextConfig;
		
		private SerializationWrapper(ContextConfiguration config) {
			this.contextConfig = config;
		}
		
		@Override
		public void serialize(Object obj, URL url) throws SerializationException, IOException {
			serialize(obj, url, XmlUtils.CONTENT_TYPE_XML);
		}

		@Override
		public void serialize(Object obj, OutputStream os)
				throws NullPointerException, SerializationException, IOException {
			
			if(obj == null) {
				String detailMessage = "Serialization can not be processed on null object!";
				NullPointerException cause = new NullPointerException(detailMessage);
				throw new SerializationException("XML serrialization error", cause);
			}
			
			try {
				MetaDataInterceptor interceptor = getMetaDataInterceptor(obj.getClass());
				XmlSerializer serializer = new XmlSerializer(interceptor, getAdapterRegistry());
				String encoding = this.contextConfig.getEncoding();
				boolean standalone = this.contextConfig.isStandalone();
				serializer.serialize(obj, os, encoding, standalone);
				os.flush();
			} catch (IllegalArgumentException e) {
				throw new SerializationException(e);
			} catch (IllegalStateException e) {
				throw new SerializationException(e);
			} finally {
//				if(os != null) {
//					os.close();
//				}
			}
		}
	}
	
	/**
	 * <p>Class wrapping deserialization process.</p>
	 * 
	 * @author Jan Janickovic
	 */
	private class DeserializationWrapper<T> extends AbstractDeserializer<T> {

		private XmlParser<T> parser;
		
		private DeserializationWrapper(MetaDataInterceptor metaDataInterceptor, 
				ContextConfiguration config) {
			
			this.parser = new XmlParser<T>(metaDataInterceptor, config.getAdapterRegistry());
			parser.setPrimitiveTypeInitializer(config.getPrimitiveTypeInitializer());
		}
		
		@Override
		public void deserialize(InputStream inputStream) throws MappingException {
			try {
				SAXParserFactory parserFactory = SAXParserFactory.newInstance();
				parserFactory.setValidating(false);
				SAXParser parser = parserFactory.newSAXParser();
				parser.parse(inputStream, this.parser);
			} catch (SAXException e) {
				throw new MappingException(e);
			} catch (IOException e) {
				throw new MappingException(e);
			} catch (ParserConfigurationException e) {
				throw new MappingException(e);
			}
		}

		@Override
		public T getValue() {
			return this.parser.getValue();
		}
	}

}

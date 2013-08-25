package org.xpaframework.xml;

import java.io.File;

import org.xpaframework.ValueAdapterRegistry;
import org.xpaframework.xml.util.Logger;
import org.xpaframework.xml.util.Logger.Level;


/**
 * <p>Class containing options for the XML data transformation. The XML
 * processing options are defined by this object.</p>
 * 
 * @author Jan Janickovic
 */
public final class ContextConfiguration {

	public static final String DEFAULT_ENCODING = "UTF-8";
	
	private boolean cachingEnabled = false;
	private File cacheDirectory = null;
	private ValueAdapterRegistry adapterRegistry;
	private String encoding = DEFAULT_ENCODING;
	private boolean standalone = true;
	
	//not configurable options
	
	private PrimitiveTypeInitializer primitiveTypeInitializer;
	
	/**
	 * <p>Not implemented yet.</p>
	 */
	@Deprecated
	protected boolean isCachingEnabled() {
		return cachingEnabled;
	}

	/**
	 * <p>Not implemented yet.</p>
	 */
	@Deprecated
	public void setCachingEnabled(boolean cachingEnabled) {
		this.cachingEnabled = cachingEnabled;
	}

	/**
	 * <p>Not implemented yet.</p>
	 */
	@Deprecated
	protected File getCacheDirectory() {
		return cacheDirectory;
	}

	/**
	 * <p>Not implemented yet.</p>
	 */
	@Deprecated
	public void setCacheDirectory(File cacheDirectory) {
		this.cacheDirectory = cacheDirectory;
	}

	protected ValueAdapterRegistry getAdapterRegistry() {
		return adapterRegistry;
	}

	/**
	 * <p>Sets the custom value adapter registry for XML processing.</p>
	 * 
	 * @param adapterRegistry - custom registry containing specified adapters.
	 * 
	 * @see ValueAdapterRegistry
	 */
	public void setAdapterRegistry(ValueAdapterRegistry adapterRegistry) {
		this.adapterRegistry = adapterRegistry;
	}

	protected String getEncoding() {
		return encoding;
	}
	
	/**
	 * <p>Sets the XML encoding property.</p>
	 * 
	 * @param encoding - encoding. By default {@link #DEFAULT_ENCODING} is set.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	protected boolean isStandalone() {
		return standalone;
	}
	
	/**
	 * <p>Sets if the serialized document is defined as standalone.</p>
	 * 
	 * @param standalone - <code>true</code> if standalone, <code>false</code>
	 * otherwise.
	 */
	public void setStandalone(boolean standalone) {
		this.standalone = standalone;
	}
	
	protected PrimitiveTypeInitializer getPrimitiveTypeInitializer() {
		return primitiveTypeInitializer;
	}
	
	protected ContextConfiguration() {
		this.primitiveTypeInitializer = PrimitiveTypeInitializer.getInitializer();
		this.adapterRegistry = new SimpleTypeRegistry();
		this.adapterRegistry.initialize();
		
		//Enable/disable logging
		Logger.getConfig().setLoggingEnabled(true);
		Logger.getConfig().setLevel(Level.INFO);
	}
	
}

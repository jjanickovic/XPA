package org.xpaframework.xml;

/**
 * <p>The factory class providing {@link XmlContext} creation.<p>
 * 
 * <p>It's recommended to create {@link XmlContext} object only once
 * and share it within the application.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see XmlContext
 */
public final class XmlContextFactory {

	/**
	 * <p>Singleton factory instance.</p>
	 */
	private static XmlContextFactory instance = new XmlContextFactory();
	
	/**
	 * @return singleton instance of this {@link XmlContextFactory} class.
	 */
	public static XmlContextFactory getInstance() {
		return instance;
	}

	/**
	 * <p>Creates fresh new {@link XmlContext} object using default
	 * {@link ContextConfiguration}.</p>
	 * 
	 * @return new {@link XmlContext} object.
	 * 
	 * @see XmlContextFactory#createXmlContext(ContextConfiguration)
	 * @see #defaultConfiguration()
	 */
	public synchronized XmlContext createXmlContext() {
		return createXmlContext(defaultConfiguration());
	}
	
	/**
	 * <p>Creates fresh new {@link XmlContext} object using custom
	 * {@link SimpleTypeRegistry}.</p>
	 * 
	 * @param valueAdapterResolver
	 * 
	 * @return new {@link XmlContext} object.
	 * 
	 * @see #createXmlContext();
	 */
	public synchronized XmlContext createXmlContext(ContextConfiguration configuration) {
		return new XmlContext(configuration);
	}
	
	/**
	 * @return default configuration object used for context creation.
	 */
	public ContextConfiguration defaultConfiguration() {
		return new ContextConfiguration();
	}
	
	private XmlContextFactory() {
	}

}

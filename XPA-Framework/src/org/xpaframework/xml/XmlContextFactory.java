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
	 * Creates fresh new {@link XmlContext} object using default {@link SimpleTypeRegistry}.
	 * 
	 * @return new {@link XmlContext} object.
	 * 
	 * @see XmlContextFactory#createXmlContext(SimpleTypeRegistry)
	 */
	public synchronized XmlContext createXmlContext() {
		return createXmlContext(defaultConfiguration());
	}
	
	/**
	 * Creates fresh new {@link XmlContext} object using custom {@link SimpleTypeRegistry}.
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
	
	public ContextConfiguration defaultConfiguration() {
		return new ContextConfiguration();
	}
	
	private static XmlContextFactory instance = new XmlContextFactory();

	/**
	 * @return singleton instance of this {@link XmlContextFactory} class.
	 */
	public static XmlContextFactory getInstance() {
		return instance;
	}

	private XmlContextFactory() {
	}

}

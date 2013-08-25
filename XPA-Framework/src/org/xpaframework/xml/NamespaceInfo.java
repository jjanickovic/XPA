package org.xpaframework.xml;

/**
 * <p>Class containing metadata of specified namespace.</p>
 * 
 * @author Jan Janickovic
 */
public class NamespaceInfo {

	protected static final NamespaceInfo NO_NAMESPACE = new NamespaceInfo("", "");
	
	private String prefix = "";
	private String namespace = "";

	public String getPrefix() {
		return prefix;
	}

	public String getNamespace() {
		return namespace;
	}

	protected NamespaceInfo(String prefix, String namespace) {
		this.prefix = prefix;
		this.namespace = namespace;
	}
}

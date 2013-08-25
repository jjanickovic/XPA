package org.xpaframework.xml;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * <p>Class containing metadata information of specified XML attribute.</p>
 * 
 * @author Jan Janickovic
 */
public class AttributeInfo {

	/**
	 * Name of the XML attribute.
	 */
	private String name;
	
	/**
	 * Attribute's namespace.
	 */
	private NamespaceInfo namespace = NamespaceInfo.NO_NAMESPACE;
	
	/**
	 * Field of the class that represents attribute of the XML element.
	 */
	private Field field;

	/**
	 * Creates instance of the attribute information containing specified name,
	 * field and namespace. Every parameter of this constructor must be
	 * initialized to not null/not empty value, except the <code>namespace
	 * </code> attribute.
	 * 
	 * @param name - name of the attribute according the XML document.
	 * @param field - field holding the attribute's value.
	 * @param namespace - attribute's namespace. This parameter can be set to
	 * <code>null</code>.
	 * 
	 * @throws NullPointerException
	 */
	protected AttributeInfo(String name, Field field, NamespaceInfo namespace)
			throws NullPointerException {
		
		if(name == null || name.isEmpty()) {
			throw new NullPointerException("Attribute name not initialized!");
		}
		
		if(field == null) {
			throw new NullPointerException("Field parameter not initialized (value = null)!");
		}
		
		this.name = name;
		this.field = field;

		if(namespace != null) {
			this.namespace = namespace;
		}
	}
	
	/**
	 * @return the name of the XML attribute.
	 */
	protected String getName() {
		return name;
	}
	
	/**
	 * @return the namespace (if set) associated with targeted attribute.
	 */
	protected NamespaceInfo getNamespace() {
		return namespace;
	}

	/**
	 * @return the field associated with the XML attribute.
	 */
	protected Field getField() {
		return field;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof AttributeInfo)) {
			return false;
		}
		
		AttributeInfo obj = (AttributeInfo) o;
		return this.name.equals(obj.getName());
	}

	@Override
	public int hashCode() {
		return this.name.toUpperCase(Locale.US).hashCode();
	}
	
}

package org.xpaframework.xml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.xpaframework.xml.annotation.XmlAncestor;
import org.xpaframework.xml.annotation.XmlType;
import org.xpaframework.xml.annotation.XmlValue;


/**
 * <p>Class containing metadata of specified XML element.</p>
 * 
 * @author Jan Janickovic
 */
public class ElementInfo {

	protected static final String KEY_DELIMITER = "@";
	
	/**
	 * Name or type of the element.
	 */
	private String name;
	
	/**
	 * @return the name or type of the element bound to this meta data.
	 */
	protected String getName() {
		return name;
	}
	
	/**
	 * The target namespace of the element. This value is by default set
	 * to empty string.
	 */
	private NamespaceInfo namespace = NamespaceInfo.NO_NAMESPACE;

	/**
	 * @return target namespace of this element.
	 */
	protected NamespaceInfo getNamespace() {
		return namespace;
	}

	/**
	 * @return the unique identifier for this object. The identifier is made
	 * from the {@link #namespace} and {@link #name} of the
	 * element.
	 */
	protected String getIdentifier() {
		return this.namespace.getNamespace() + KEY_DELIMITER + this.name;
	}
	
	/**
	 * Class associated with the XML element.
	 */
	private Class<?> mappingClass;

	/**
	 * @return {@link Class} to be bound with this element.
	 */
	protected Class<?> getMappingClass() {
		return mappingClass;
	}
	
	/**
	 * The map of field injections where this element information has to be used
	 * to bind this object.
	 */
	private Map<Class<?>, Field> injectionMap = new Hashtable<Class<?>, Field>();
	
	/**
	 * <p>Adds the <code>field</code> to the {@link #injectionMap} where the key
	 * is {@link Field#getDeclaringClass()} method result.</p>
	 * <p>Check the return result of this method to avoid multiple injection for
	 * the same field. This multiple injection can cause stack overflow.</p>
	 * 
	 * @param field - field where the data has to be bound.
	 * 
	 * @return <code>true</code> if there was no previous injection for
	 * field <code>field</code>, <code>false</code> if the field has been
	 * mapped already.
	 * 
	 * @see Field#getDeclaringClass()
	 */
	protected boolean addInjection(Field field) {
		return this.injectionMap.put(field.getDeclaringClass(), field) == null;
	}
	
	protected Field getInjection(Class<?> clazz) {
		return this.injectionMap.get(clazz);
	}
	
	private Collection<Field> children = new HashSet<Field>();
	
	/**
	 * <p>Adds child element of this element if any exist.</p>
	 * <p>The exception of adding this element is that the child is
	 * defined by <code>&lt;xsd:sequence/&gt;</code> schema definition.
	 * These elements are mapped by {@link XmlType#order()} value and
	 * are placed into the {@link #order} field.</p>
	 * 
	 * @param child - a field representing child element.
	 * @return <code>true</code> if the <code>child</code> was added, otherwise
	 * <code>false</code>.
	 * 
	 * @throws NullPointerException - if <code>child</code> is <code>null</code>.
	 * 
	 * @see #getOrder()
	 * @see XmlType
	 */
	protected boolean addChild(Field child) throws NullPointerException {
		if(child == null) {
			throw new NullPointerException("Null child!");
		}
		
		if(this.order.contains(child)) {
			return false;
		}
		
		return this.children.add(child);
	}
	
	/**
	 * <p>Returns the list of fields representing child elements. These
	 * fields can be serialized in any order.</p>
	 * <p></p>
	 * @return the list of fields representing child elements.
	 * 
	 * @see #getOrder()
	 * @see XmlType
	 */
	protected Collection<Field> getChildren() {
		return children;
	}
	
	/**
	 * List holding the order for nested elements.
	 * 
	 * @see XmlType#order()
	 */
	private Collection<Field> order = new ArrayList<Field>();
	
	/**
	 * <p>Sets the order for nested elements.</p>
	 * <p><b>Note:</b> This method should be called before any {@link #addChild(Field)}
	 * method invocation to ensure that no child is added if has been set within this
	 * ordering.</p>
	 * 
	 * @param values - the property names representing the appropriate
	 * order for nested elements.
	 */
	protected void addOrder(Collection<Field> values) {
		this.order.addAll(values);
	}
	
	/**
	 * <p>Returns sorted list of properties.</p>
	 * <p>These properties are mapped to this {@link #mappingClass} fields
	 * to specify the order of nested elements. This is set by {@link XmlType#order()}
	 * annotation property.</p>
	 * <p>Method also returns mapping fields from superclass, if the
	 * {@link #mappingClass}'s superclass is an ancestor of this
	 * class.</p>
	 * 
	 * @return the sorted collection of mapping strings that represent
	 * property names.
	 * 
	 * @see XmlType#order()
	 * @see XmlAncestor
	 */
	protected Collection<Field> getOrder() {
		return order;
	}
	
	protected ElementInfo(String name, Class<?> mapping, NamespaceInfo namespace) {
		this.name = name;
		this.mappingClass = mapping;
		
		if(namespace != null) {
			this.namespace = namespace;
		}
	}
	
	private Set<AttributeInfo> attributeInfos = new HashSet<AttributeInfo>();

	protected Collection<AttributeInfo> getAttributeInformations() {
		return attributeInfos;
	}

	/**
	 * Adds attribute information for this element.
	 * 
	 * @param attributeInfo - attribute information object.
	 * 
	 * @see AttributeInfo
	 */
	protected void addAttribute(AttributeInfo attributeInfo) {
		this.attributeInfos.add(attributeInfo);
	}
	
	private Field valueField;
	
	/**
	 * @return the value field or <code>null</code> if no value is defined.
	 */
	protected Field getValueField() {
		return valueField;
	}

	/**
	 * Sets the field for the element value. This method can be invoked only for
	 * one field, the IllegalStateException is thrown for overwriting original
	 * value.
	 * 
	 * @param valueField - field that contains element's value.
	 * 
	 * @throws IllegalStateException if the field representing XML value
	 * was set already.
	 * 
	 * @see XmlValue
	 */
	protected void setValueField(Field valueField) throws IllegalStateException {
		if(this.valueField != null) {
			String message = "Field for XML value set! Original field: " + valueField.getName() +
					", owner class: " + valueField.getDeclaringClass().getName();
			
			throw new IllegalStateException(message);
		}
		
		this.valueField = valueField;
	}
	
	/**
	 * Determines if this element can contain value.
	 * 
	 * @return <code>true</code> if does, <code>false</code> otherwise.
	 */
	protected boolean hasValue() {
		return this.valueField != null;
	}

	/**
	 * <p>Overrides {@link #toString()} method by returning result in format:
	 * <pre>{@link #getIdentifier()} + "/[" + {@link #mappingClass}.getName()</pre>
	 * </p>
	 */
	@Override
	public String toString() {
		return getIdentifier() + "/[" + this.mappingClass.getName() + "]" ;
	}

}

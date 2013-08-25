package org.xpaframework.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.xml.XMLConstants;

import org.xpaframework.SerializationException;
import org.xpaframework.ValueAdapter;
import org.xpaframework.ValueAdapterRegistry;
import org.xpaframework.xml.annotation.XmlRootElement;
import org.xpaframework.xml.annotation.XmlValue;
import org.xpaframework.xml.util.Logger;

import android.util.Xml;

/**
 * <p>Object to XML serialization class using
 * {@link org.xmlpull.v1.XmlSerializer} object to create XML output.</p>
 * 
 * @author Jan Janickovic
 */
public class XmlSerializer {
	
	private final Logger logger = Logger.getLogger(getClass());

	private org.xmlpull.v1.XmlSerializer serializer;
	
	private MetaDataInterceptor metaDataInterceptor;
	private ValueAdapterRegistry adapterRegistry;
	
	private Map<String, ElementInfo> serializationInfo;
	
	public XmlSerializer(MetaDataInterceptor metaDataInterceptor, ValueAdapterRegistry adapterRegistry)
			throws IOException {
		
		this.metaDataInterceptor = metaDataInterceptor;
		this.adapterRegistry = adapterRegistry;
		this.serializer = Xml.newSerializer();
		
		try {
			this.serializationInfo = this.metaDataInterceptor.getSerializationInfo();
			this.logger.v( "meta-data:\r\n " + this.serializationInfo);
		} catch (InterruptedException e) {
			throw new RuntimeException("Metadata processing failure!", e);
		}
	}
	
	/**
	 * Serializes this <code>obj</code> to the specified output.
	 * 
	 * @param obj - the root element object. This object must be annotated by
	 * {@link XmlRootElement} annotation.
	 * @param encoding - the XML output encoding.
	 * @param standalone - the standalone flag.
	 * 
	 * @throws NullPointerException if <code>obj</code> is <code>null</code>.
	 * @throws SerializationException if any other exception caused by
	 * serialization occurs.
	 */
	public void serialize(Object obj, OutputStream os, String encoding, Boolean standalone)
			throws NullPointerException, SerializationException {
		
		if(obj == null) {
			throw new NullPointerException("obj argument is null!");
		}

		XmlUtils.validateRoot(obj.getClass());
		ElementInfo elementInfo = getElementInfo(obj.getClass(), null);
		
		try {
			this.serializer.setOutput(os, encoding);
			this.serializer.startDocument(encoding, standalone);
			
			serialize(obj, elementInfo);
			
			this.serializer.endDocument();
			this.serializer.flush();
		} catch (IllegalArgumentException e) {
			throw new SerializationException("Serialization error!", e);
		} catch (IllegalStateException e) {
			throw new SerializationException("Serialization error!", e);
		} catch (IOException e) {
			throw new SerializationException("Serialization error!", e);
		}
	}
	
	/**
	 * <p>Serializes this object to XML format. This object's data are represented
	 * by the <code>elementInfo</code> argument that holds all meta-data required
	 * for XML serialization.</p>
	 * <p>This method is used for standard serialization where the
	 * {@link ElementInfo#getName()} represents name of the XML element. The non-standard
	 * serialization is made by {@link #serialize(Object, ElementInfo, String, String)}
	 * method.</p>
	 * 
	 * @param obj - object to serialize.
	 * @param elementInfo - element information required for serialization.
	 * 
	 * @throws IllegalArgumentException defined by {@link org.xmlpull.v1.XmlSerializer}
	 * object or if no meta data information is found for currently serializing object.
	 * 
	 * @throws IllegalArgumentException defined by {@link org.xmlpull.v1.XmlSerializer} object.
	 * @throws IllegalStateException defined by {@link org.xmlpull.v1.XmlSerializer} object.
	 * @throws IOException defined by {@link org.xmlpull.v1.XmlSerializer} object.
	 * 
	 * @see #serialize(Object, ElementInfo, String, String)
	 */
	void serialize(Object obj, ElementInfo elementInfo)
			throws IllegalArgumentException, IllegalStateException, IOException {
		
		serialize(obj, elementInfo, elementInfo.getName(), elementInfo.getNamespace());
	}
	
	/**
	 * <p>Serializes this object to XML format. This object's data are represented
	 * by the <code>elementInfo</code> argument that holds all meta-data required
	 * for XML serialization.</p>
	 * <p>The arguments <code>elementName</code> and <code>namespace</code> can
	 * differ from those held in the {@link ElementInfo} object. This can happen
	 * in inheritance, where the {@link ElementInfo#getName()} doesn't represent
	 * XML element's name, but its type.</p>
	 * 
	 * @param obj - object to serialize.
	 * @param elementInfo - element information required for serialization.
	 * @param elementName - the name of the XML element.
	 * @param targetNamespace - the XML element's namespace.
	 * 
	 * @throws IllegalArgumentException defined by {@link org.xmlpull.v1.XmlSerializer}
	 * object or if no meta data information is found for currently serializing object.
	 * 
	 * @throws IllegalArgumentException defined by {@link org.xmlpull.v1.XmlSerializer} object.
	 * This exception is also thrown when the <code>elementInfo</code> is <code>null</code>.
	 * @throws IllegalStateException defined by {@link org.xmlpull.v1.XmlSerializer} object.
	 * @throws IOException defined by {@link org.xmlpull.v1.XmlSerializer} object.
	 * 
	 * @see #serialize(Object, ElementInfo)
	 */
	@SuppressWarnings("unchecked")
	void serialize(Object obj, ElementInfo elementInfo, String elementName, NamespaceInfo namespace)
			throws IllegalArgumentException, IllegalStateException, IOException {
		this.logger.v("Serializing object: " + obj + " of class: " + obj.getClass());
		
		if(elementInfo == null) {
			String massageFormat = "[Object: %s, element: %s:%s]: ElementInfo object = null!";
			String message = String.format(massageFormat, obj, namespace, elementName);
			throw new IllegalArgumentException(message);
		}

		/*
		 * 1 - Start element
		 * 2 - bind all attribute of target object (including super class attributes).
		 * 		The order is not important.
		 * 3 - bind value if any.
		 * 4 - create list of meta-objects containing: element name, target namespace,
		 * 			object reference
		 * 5 - based on XmlType order serialize and remove elements from the list, then
		 * 		add remaining elements. This step calls recursively steps 1-5.
		 */
		
		//1
		this.serializer.setPrefix(namespace.getPrefix(), namespace.getNamespace());
		this.serializer.startTag(namespace.getNamespace(), elementName);

		/*
		 * Simple types are supposed to have only its value, so they are serialized
		 * into the plain string. Serializing values of simple type provides different way
		 * so they need to be processed separately.
		 */
		if(XmlUtils.isSimpleType(obj.getClass(), true)) {
			ValueAdapter<Object> adapter = (ValueAdapter<Object>) this.adapterRegistry.getAdapter(
					obj.getClass());
			this.serializer.text(adapter.toString(obj));
		} else {//complex type serialization
			//2
			serializeAttributes(obj, elementInfo);
			
			//3 serialize value
			if(elementInfo.hasValue()) {
				serializeValue(obj, elementInfo.getValueField());
			}
			
			//4 (5 is implemented by recursive call of this method)
			Class<?> mappingClass = elementInfo.getMappingClass();
			NamespaceInfo targetNamespace = elementInfo.getNamespace();
			serializeElements(obj, mappingClass, elementInfo.getOrder(), targetNamespace);
			serializeElements(obj, mappingClass, elementInfo.getChildren(), targetNamespace);
		}
		
		this.serializer.endTag(namespace.getNamespace(), elementName);
	}
	
	/**
	 * <p>Serializes attributes of the <code>obj</code>.</p>
	 *  
	 * @param obj
	 * @param elementInfo
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	void serializeAttributes(Object obj, ElementInfo elementInfo)
			throws IllegalArgumentException, IllegalStateException, IOException {

		if(XmlUtils.hasAncestor(obj.getClass())) {
			String type = XmlUtils.getElementType(obj.getClass());
			
			this.serializer.attribute(
					XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI,
					XmlUtils.ATTRIBUTE_TYPE, type);
		}

		for(AttributeInfo attributeInfo : elementInfo.getAttributeInformations()) {
			Field field = attributeInfo.getField();
			Method getter = this.metaDataInterceptor.getGetterMethod(field);
			Object value = XmlUtils.invoke(getter, obj);
			
			if(value == null) {//skipping null values
				continue;
			}
			
			ValueAdapter<Object> adapter = (ValueAdapter<Object>) this.adapterRegistry.getAdapter(value.getClass());
			String stringValue = adapter.toString(value);
			this.serializer.attribute(attributeInfo.getNamespace().getPrefix(),
					attributeInfo.getName(), stringValue);
		}
	}
	
	/**
	 * <p>Serializes nested elements of the target <code>obj</code>.</p>
	 * 
	 * @param obj
	 * @param target
	 * @param fields
	 * @param namespace
	 * @throws IOException
	 */
	private void serializeElements(Object obj, Class<?> target, Collection<Field> fields, NamespaceInfo namespace)
			throws IOException {
		
		for(Field field : fields) {
			Method getter = this.metaDataInterceptor.getGetterMethod(field);
			Object value = XmlUtils.invoke(getter, obj);
			
			if(value == null) {//skipping null values
				continue;
			}
			
			String elementName = XmlUtils.getElementName(field);
			Class<? extends Object> valueClass = value.getClass();
			
			if(Collection.class.isAssignableFrom(valueClass)) {
				serializeElements((Collection<?>) value, field, elementName);
			} else {
				ElementInfo elementInfo = getElementInfo(valueClass, field);
				NamespaceInfo targetNamespace = this.metaDataInterceptor.getNamespace(field);
				
				if(targetNamespace != null) {
					namespace = targetNamespace;
				}
				
				serialize(value, elementInfo);
			}

		}
	}

	/**
	 * 
	 * @param collection - collection to serialize.
	 * 
	 * @throws IllegalArgumentException thrown by {@link org.xmlpull.v1.XmlSerializer} object.
	 * @throws IllegalStateException thrown by {@link org.xmlpull.v1.XmlSerializer} object.
	 * @throws IOException thrown by {@link org.xmlpull.v1.XmlSerializer} object.
	 */
	void serializeElements(Collection<?> collection, Field owner, String elementName)
			throws IllegalArgumentException, IllegalStateException, IOException {

		if(collection == null || collection.isEmpty()) {
			return;
		}

		for(Object obj : collection) {
			ElementInfo elementInfo = getElementInfo(obj.getClass(), owner);
			serialize(obj, elementInfo, elementName, elementInfo.getNamespace());
		}
	}
	
	/**
	 * <p>Serializes value of the <code>obj</code> representing XML complex type.</p>
	 * <p>This serialization method serves only for complex type elements that contain
	 * field annotated with {@link XmlValue} annotation.</p>
	 * 
	 * @param obj - object representing XML element.
	 * @param field - the value owner. This field is to create appropriate getter
	 * method to retrieve value on the <code>obj</code>.
	 * 
	 * @throws IllegalArgumentException thrown by {@link org.xmlpull.v1.XmlSerializer#
	 * text(String)} method.
	 * @throws IllegalStateException thrown by {@link org.xmlpull.v1.XmlSerializer#
	 * text(String)} method.
	 * @throws IOException thrown by {@link org.xmlpull.v1.XmlSerializer#
	 * text(String)} method.
	 * 
	 * @see {@link XmlValue}
	 */
	void serializeValue(Object obj, Field field)
			throws IllegalArgumentException, IllegalStateException, IOException {

		Method getter = this.metaDataInterceptor.getGetterMethod(field);
		Object value = XmlUtils.invoke(getter, obj);
		
		if(value != null) {
			this.serializer.text(value.toString());
		}
		
	}
	
	private ElementInfo getElementInfo(Class<?> clazz, Field field) throws NullPointerException {
		/*
		 * This condition is created according following requirements:
		 *  -	implementation of abstract classes representing simple type.
		 *  -	null field represent root elements that are considered as complex types.
		 *  -	if type of field and class argument are the same, there is no need to
		 *  	recursively	call method - result will be the same (avoiding stack overflow). 
		 */
		if(field != null && XmlUtils.isSimpleType(field) && !clazz.equals(field.getType())) {
			return getElementInfo(field.getType(), field);
		}
		
		String key = this.metaDataInterceptor.createIdentifier(clazz, field);
		ElementInfo elementInfo = this.serializationInfo.get(key);
		
		if(elementInfo == null) {
			String message = "No element info!\r\n" +
					"\tclass: " + clazz + "\r\n\tField: " + field + "\r\n\tKey: " + key;
			throw new NullPointerException(message);
		}
		
		return elementInfo;
	}
	
}

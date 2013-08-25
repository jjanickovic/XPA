package org.xpaframework.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.XMLConstants;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xpaframework.ValueAdapter;
import org.xpaframework.ValueAdapterRegistry;
import org.xpaframework.ValueConversionException;
import org.xpaframework.xml.annotation.XmlAncestor;
import org.xpaframework.xml.annotation.XmlRootElement;
import org.xpaframework.xml.util.Logger;


/**
 * <p>Class providing XML input parsing to given object. This parsing
 * is provided by SAX implementation of the whole XML document.<p>
 * 
 * @author Jan Janickovic
 *
 * @param <T> - type of the root element.
 * 
 * @see DefaultHandler
 * @see XmlRootElement
 */
public class XmlParser<T extends Object> extends DefaultHandler {

	private final Logger logger = Logger.getLogger(getClass());
	
	private MetaDataInterceptor metaDataInterceptor;
	private Map<String, ElementInfo> metaData;
	
	private PrimitiveTypeInitializer primitiveTypeInitializer;
	private ValueAdapterRegistry adapterRegistry;
	
	private Stack<ObjectInfo> objectStack;
	private T value;
	
	public void setPrimitiveTypeInitializer(PrimitiveTypeInitializer initializer) {
		this.primitiveTypeInitializer = initializer;
	}

	public XmlParser(MetaDataInterceptor metaDataInterceptor, ValueAdapterRegistry adapterRegistry) {
		this.metaDataInterceptor = metaDataInterceptor;
		this.adapterRegistry = adapterRegistry;
	}
	
	public T getValue() throws IllegalStateException {
		return this.value;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		this.logger.v("startDocument()");
		
		try {
			this.metaData = metaDataInterceptor.getParsingInfo();
		} catch (InterruptedException e) {
			throw new RuntimeException("Metadata processing failure!", e);
		}
		
		this.objectStack = new Stack<ObjectInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		this.logger.v("startElement() - name: " + localName);
		String elementKey = getElementKey(localName, uri, attributes);

		ElementInfo elementInfo = this.metaData.get(elementKey);
		
		if(elementInfo == null) {
			return;
		}

		ObjectInfo element = createObject(elementKey, elementInfo);
		Collection<AttributeInfo> attributeInfos = elementInfo.getAttributeInformations();

		//if there are no attributes, the element is (or should be) represented by the String object.
		if(!elementInfo.getAttributeInformations().isEmpty()) { 
			bindAttributes(element.getObject(), attributes, attributeInfos);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		this.logger.v("endElement() - name: " + localName);

		ObjectInfo objectInfo = this.objectStack.pop();
		ElementInfo elementInfo = this.metaData.get(objectInfo.getElementKey());
		
		if(elementInfo == null) {
			return;
		}

		if(objectInfo.hasValue()) {
			try {
				Class<?> binding = elementInfo.getMappingClass();

				//FIXME resolve if the simple type is enumeration with its own adapter!
				if(XmlUtils.isSimpleType(binding, false)) {
					ValueAdapter<?> adapter = this.adapterRegistry.getAdapter(binding);
					Object value = adapter.convertValue(objectInfo.getElementValue());
					objectInfo.setObject(value);
				} else if(binding.isEnum()) {
					@SuppressWarnings("rawtypes")
					Class<Enum> enumType = (Class<Enum>) binding;
					Object value = Enum.valueOf(enumType, objectInfo.getElementValue());
					objectInfo.setObject(value);
				} else {//default implementation of XmlValue annotation
					Field valueField = elementInfo.getValueField();
					Method setter = this.metaDataInterceptor.getSetterMethod(valueField);
					ValueAdapter<?> adapter = this.adapterRegistry.getAdapter(valueField.getType());
					XmlUtils.invoke(setter, objectInfo.getObject(),
							adapter.convertValue(objectInfo.getElementValue()));
				}

			} catch (ValueConversionException e) {
				throw new SAXException("Value conversion error!", e);
			}
		}

		if(this.objectStack.isEmpty()) {//reached the root element
			this.value = (T) objectInfo.getObject();
			return;
		}
		
		ObjectInfo parentObjectInfo = this.objectStack.peek();
		Class<? extends Object> parentClass = parentObjectInfo.getObject().getClass();
		Field owner = getInjectionField(elementInfo, parentClass);

		if(List.class.isAssignableFrom(owner.getType())) {
			Method getter = this.metaDataInterceptor.getGetterMethod(owner);
			List<Object> list = XmlUtils.invokeList(parentObjectInfo.getObject(), getter);
			list.add(objectInfo.getObject());
		} else {
			Method setter = this.metaDataInterceptor.getSetterMethod(owner);
			XmlUtils.invoke(setter, parentObjectInfo.getObject(), objectInfo.getObject());
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		this.logger.v("endDocument() - object:" + this.value);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		String value = new String(ch, start, length);
		
		if(value == null || value.trim().isEmpty()) {
			return;
		}
		
		this.logger.v("characters() - value: " + value);
		ObjectInfo elementInfo = this.objectStack.peek();
		elementInfo.setElementValue(value);
	}

	private String getElementKey(String localName, String namespace, Attributes attributes) {
		String elementKey = this.metaDataInterceptor.createIdentifier(localName, namespace);
		String type = attributes.getValue(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "type");
		
		if(type != null && type.contains(":")) {
			type = type.substring(type.indexOf(":") + 1);
			elementKey = this.metaDataInterceptor.createIdentifier(type, namespace);
		}
		
		return elementKey;
	}

	/**
	 * Finds appropriate field for the data injection.
	 * 
	 * @param elementInfo - {@link ElementInfo} object of the target XML element. 
	 * @param clazz - parent class object or its ancestor
	 * @return the field representing the injection for this element or <code>null</code> if
	 * was found.
	 * @throws NullPointerException if <code>clazz</code> is <code>null</code>.
	 */
	private Field getInjectionField(ElementInfo elementInfo, Class<? extends Object> clazz)
			throws NullPointerException {
		
		if(clazz == null) {
			throw new NullPointerException();
		}
		
		Field injection = elementInfo.getInjection(clazz);
		
		if(injection == null && clazz.getSuperclass().isAnnotationPresent(XmlAncestor.class)) {
			injection = getInjectionField(elementInfo, clazz.getSuperclass());
		}
		
		return injection;
	}
	
	private Object bindAttributes(Object element, Attributes attributes, Collection<AttributeInfo> attrInfos)
			throws SAXException {
		
		for(AttributeInfo attributeInfo : attrInfos) {
			Field attributeField = attributeInfo.getField();
			Class<?> valueType = attributeField.getType();
			
			try {
				String namespace = attributeInfo.getNamespace().getNamespace();
				String attributeValue = attributes.getValue(namespace,	attributeInfo.getName());
				
				if(attributeValue == null) {
					continue;
				}
				
				this.logger.v("Attribute [name: " + attributeInfo.getName() +
						", value: " + attributeValue + "]");
				
				ValueAdapter<?> adapter = this.adapterRegistry.getAdapter(valueType);
				Object valueObject = adapter.convertValue(attributeValue);
				Method setter = this.metaDataInterceptor.getSetterMethod(attributeField);
				
				XmlUtils.invoke(setter, element, valueObject);
			} catch (ValueConversionException e) {
				throw new SAXException("Value conversion error! Attribute: " + attributeInfo.getName(), e);
			} catch (Exception e) {
				throw new SAXException("Value conversion error! Element: " + element.getClass().getName() +
						", Attribute: " + attributeInfo.getName(), e);
			}
			
		}
		
		return element;
	}

	/**
	 * Creates and stores object into the object pseudo-stack.
	 * 
	 * @param elementInfo - the meta-data required for object creation.
	 * 
	 * @return new object instance.
	 */
	private ObjectInfo createObject(String elementKey, ElementInfo elementInfo) {
		
		Class<?> binding = elementInfo.getMappingClass();
		Object element = null;
		
		if(binding.isPrimitive()) {
			element = this.primitiveTypeInitializer.getObject(binding.getCanonicalName());
		} else if(!XmlUtils.isSimpleType(binding, true)) {//simple types can be not initialized
			element = XmlUtils.newInstance(binding);
		}
		
		return this.objectStack.push(new ObjectInfo(elementKey, element));
	}
	
	private class ObjectInfo {
		
		private String elementKey;
		
		public String getElementKey() {
			return elementKey;
		}

		private Object object;

		public void setObject(Object object) {
			this.object = object;
		}

		public Object getObject() {
			return object;
		}

		private ObjectInfo(String elementKey, Object object) {
			this.elementKey = elementKey;
			this.object = object;
		}
		
		private StringBuffer elementValue;

		public String getElementValue() {
			return elementValue.toString();
		}

		public void setElementValue(String value) {
			if(this.elementValue == null) {
				this.elementValue = new StringBuffer();
			}
			
			this.elementValue.append(value);
		}
		
		public boolean hasValue() {
			return this.elementValue != null;
		}
		
	}
	
}

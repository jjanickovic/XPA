package org.xpaframework.xml;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import org.xpaframework.ValueAdapter;
import org.xpaframework.ValueAdapterRegistry;
import org.xpaframework.xml.annotation.XmlAncestor;
import org.xpaframework.xml.annotation.XmlAttribute;
import org.xpaframework.xml.annotation.XmlNamespace;
import org.xpaframework.xml.annotation.XmlType;
import org.xpaframework.xml.annotation.XmlValue;
import org.xpaframework.xml.annotation.XmlValueAdapter;
import org.xpaframework.xml.util.Logger;


/**
 * <p>The class implementation creates, holds and delivers meta information
 * between XML data and the objects. Each processes of serialization and
 * deserialization is represented by the map of element information objects
 * holding all data required for the data transformation.</p>
 * </p>This metadata is created and delivered in its own thread to increase
 * performance.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see ElementInfo
 * @see AttributeInfo
 * @see NamespaceInfo
 */
public final class MetaDataInitializer extends Thread {

	private Logger logger = Logger.getLogger(MetaDataInitializer.class); 
	
	private PrimitiveTypeInitializer primitiveTypeInitializer;
	private ValueAdapterRegistry valueAdapterRegistry;
	
	private Class<?> rootClass;
	private Map<String, ElementInfo> parsingInfo;
	private Map<String, ElementInfo> serializationInfo;
	
	/*
	 * These maps are created for some performance issues over
	 * reflection. 
	 */
	private Map<Field, Method> getterMap;
	private Map<Field, Method> setterMap;
	
	protected MetaDataInitializer(Class<?> root, ContextConfiguration config) {
		this.rootClass = root;
		
		this.parsingInfo = new Hashtable<String, ElementInfo>();
		this.serializationInfo = new Hashtable<String, ElementInfo>();
		
		this.getterMap = new Hashtable<Field, Method>();
		this.setterMap = new Hashtable<Field, Method>();
		
		this.primitiveTypeInitializer = config.getPrimitiveTypeInitializer();
		this.valueAdapterRegistry = config.getAdapterRegistry();
	}
	
	/**
	 * <p>Starts the metadata creation for the specified class.</p>
	 * 
	 * @throws IllegalStateException if the root class is not validated.
	 */
	@Override
	public void run() throws IllegalStateException {
		XmlUtils.validateRoot(this.rootClass);

		try {
			NamespaceInfo namespace = getNamespace(this.rootClass);
			createMetaData(this.rootClass, null, namespace);
		} catch (MetaDataCreationException e) {
			throw new IllegalStateException("Meta data creation failed!", e);
		}
	}
	
	/**
	 * <p>Returns created meta-data for specified class. The metadata are used
	 * for XML document deserialization.</p>
	 * <p>The creation process is invoked asynchronously by
	 * {@link Thread#start()} method.</p>
	 * 
	 * @return the meta data for target class.
	 * 
	 * @throws InterruptedException may be thrown by {@link Thread#join()}
	 * method.
	 * 
	 * @see Thread#join()
	 */
	protected Map<String, ElementInfo> getParsingInfo() throws InterruptedException {
		if(isAlive()) {
			join();
		}
		
		this.logger.i("deserialization info thread attached.");
		return this.parsingInfo;
	}
	
	/**
	 * <p>Returns created meta-data for specified class. The metadata are used
	 * for XML document serialization.</p>
	 * <p>The creation process is invoked asynchronously by
	 * {@link Thread#start()} method.</p>
	 * 
	 * @return the meta data for target class.
	 * 
	 * @throws InterruptedException may be thrown by {@link Thread#join()}
	 * method.
	 * 
	 * @see Thread#join()
	 */
	protected Map<String, ElementInfo> getSerializationInfo() throws InterruptedException {
		if(isAlive()) {
			join();
		}

		this.logger.i("serialization info thread attached.");
		return this.serializationInfo;
	}
	
	/**
	 * @return getter method for the specified <code>field</code>.
	 */
	protected Method getGetterMethod(Field field) {
		return this.getterMap.get(field);
	}

	/**
	 * @return setter method for the specified <code>field</code>.
	 */
	protected Method getSetterMethod(Field field) {
		return this.setterMap.get(field);
	}

	/**
	 * <p>Creates id string for the object mapping. This key contains case-sensitive
	 * element name and the element's namespace.</p>
	 * 
	 * @param elementName - name of the XML element.
	 * @param targetNamespace - XML element's namespace.
	 * 
	 * @return The key used for getting appropriate element information.
	 * 
	 * @see #getParsingInfo()
	 */
	protected String createIdentifier(String elementName, String targetNamespace) {
		return targetNamespace + ElementInfo.KEY_DELIMITER + elementName;
	}
	
	/**
	 * <p>Creates key used to retireve element infomation required for object serialization.</p>
	 * 
	 * @param type - {@link Class} representing target object. This <code>type</code> is usually
	 * retrieved by {@link Object#getClass()} method.
	 * 
	 * @param field - the target object's owner field. The only exception is the root element
	 * that has no field to own it.
	 * 
	 * @return String for getting appropriate {@link ElementInfo} object.
	 * 
	 * @see #getSerializationInfo()
	 */
	protected String createIdentifier(Class<?> type, Field field) {
		type = this.primitiveTypeInitializer.convertType(type);
		String identifier = type.getCanonicalName();
		
		if(field != null) {
			identifier += ElementInfo.KEY_DELIMITER +
					field.getDeclaringClass().getCanonicalName() + "." +
					field.getName();
		}
		
		return identifier;
	}
	
	/**
	 * <p>Returns the target namespace for this {@link AnnotatedElement}
	 * <code>element</code>.</p>
	 * 
	 * @param element - the {@link AnnotatedElement} representing XML element.
	 * @return the target namespace for mapping or <code>null</code> if
	 * no namespace is defined.
	 * @throws NullPointerException if <code>clazz</code> is <code>null</code>.
	 * 
	 * @see XmlSchema
	 */
	protected NamespaceInfo getNamespace(AnnotatedElement element) throws NullPointerException {
		if(element == null) {
			throw new NullPointerException("clazz = null!");
		} else if(!element.isAnnotationPresent(XmlNamespace.class)) {
			return null;
		}
		
		XmlNamespace xmlNamespace = element.getAnnotation(XmlNamespace.class);
		return new NamespaceInfo(xmlNamespace.prefix(), xmlNamespace.value());
	}
	
	private void createMetaData(Class<?> clazz, Field owner, NamespaceInfo namespace)
			throws MetaDataCreationException {
		
		//Creating meta data from declared fields.
		if(XmlUtils.isAncestor(clazz)) {
			XmlAncestor ancestor = clazz.getAnnotation(XmlAncestor.class);
			Class<?>[] inheritanceArray = ancestor.inheritanceTypes();
			
			for(Class<?> inheritance : inheritanceArray) {
				createMetaData(inheritance, owner, namespace);
			}
		}
		
		/*
		 * Exclude abstract classes. The XmlUtils.isAbstract(clazz) method
		 * can not be evaluated for primitive types.
		 */
		if(!XmlUtils.isSimpleType(clazz, true) && XmlUtils.isAbstract(clazz)) {
			return;
		}
		
		ElementInfo elementInfo = getElementInfo(clazz, owner, namespace);

		/*
		 * Avoiding stack overflow. This can be caused when one of the child
		 * element's is the same object as its owner. For example:
		 * 
		 * class Elem {
		 * 	private Elem child;
		 * 
		 * 	public Elem getChild() {
		 * 		return child;
		 * 	}
		 * 
		 * 	public void setChild(String child) {
		 * 		this.child = child;
		 * 	}
		 * }
		 */
		if(owner != null && !elementInfo.addInjection(owner)) {
			return;
		}
		
		//avoiding parsing of simple or primitive types
		if(!XmlUtils.isSimpleType(clazz, true)) {
			applyOrder(elementInfo, clazz);

			createMetaData(elementInfo, clazz, namespace);
		}
	}

	private void createMetaData(ElementInfo elementInfo, Class<?> clazz, NamespaceInfo namespace)
			throws MetaDataCreationException {
		
		if(XmlUtils.hasAncestor(clazz)) {
			if(clazz.isAnnotationPresent(XmlNamespace.class)) {
				String message = "Inherited types are not allowed to define namespace!";
				throw new MetaDataCreationException(message);
			}
			
			Class<?> superClass = clazz.getSuperclass();
			
			if(superClass.isAnnotationPresent(XmlNamespace.class)) {
				namespace = getNamespace(XmlNamespace.class);
			}
			
			createMetaData(elementInfo, clazz.getSuperclass(), namespace);
		}
		
		for(Field field : clazz.getDeclaredFields()) {
			NamespaceInfo fieldNamespace = getNamespace(field);
			if(fieldNamespace == null) {
				fieldNamespace = namespace;
			}
			
			if(XmlUtils.isTransient(field)) {
				continue;
			} else if(field.isAnnotationPresent(XmlAttribute.class)) {
				attributeBinding(elementInfo, field, fieldNamespace);
			} else if(field.isAnnotationPresent(XmlValue.class)) {
				elementInfo.setValueField(field);
			} else {
				elementBidning(elementInfo, field, fieldNamespace);
				elementInfo.addChild(field);
			}

			/*
			 * Registers adapter for this field (if any) and getter/setter
			 * methods. Adapters can be registered for elements and attributes.
			 */
			registerAdapter(field);
			registerInjectionMethod(field);
		}
	}

	private void attributeBinding(ElementInfo elementInfo, Field field, NamespaceInfo namespace) {
		XmlAttribute xmlAttribute = field.getAnnotation(XmlAttribute.class);
		String name = xmlAttribute.name().isEmpty() ? field.getName() : xmlAttribute.name();
		NamespaceInfo targetNamespace = getNamespace(field);

		if(targetNamespace == null) {
			targetNamespace = namespace;
		}
		
		//Log.d(TAG, "Adding attribute: '" + name + "' for element: " + elementInfo.getName());
		AttributeInfo attributeInfo = new AttributeInfo(name, field, targetNamespace);
		elementInfo.addAttribute(attributeInfo);
		
	}
	
	//if field represent complex type of the element
	private void elementBidning(ElementInfo elementInfo, Field field, NamespaceInfo namespace)
			throws MetaDataCreationException {
		
		Class<?> classType = field.getType();
		NamespaceInfo elementNamespace = getNamespace(field);
		
		if(elementNamespace == null) {
			elementNamespace = namespace;
		}

		if(Collection.class.isAssignableFrom(classType)) {
			classType = XmlUtils.getCollectionType(field);
		}

		createMetaData(classType, field, elementNamespace);
	}

	/**
	 * <p>Creates sequence of elements that must be mapped in appropriate order.</p>
	 * 
	 * @param elementInfo - element information object representing this class.
	 * @param clazz - the target class or its ancestor.
	 */
	private void applyOrder(ElementInfo elementInfo, Class<?> clazz) {
		if(XmlUtils.hasAncestor(clazz)) {
			applyOrder(elementInfo, clazz.getSuperclass());
		}
		
		XmlType xmlType = clazz.getAnnotation(XmlType.class);
		
		if(xmlType == null) {
			return;
		}

		Collection<Field> fields = new ArrayList<Field>();
		for(String fieldName : xmlType.order()) {
			Field field = XmlUtils.getDeclaredField(fieldName, clazz);
			fields.add(field);
		}
		
		elementInfo.addOrder(fields);
	}
	
	private ElementInfo getElementInfo(Class<?> clazz, Field owner, NamespaceInfo namespace)
			throws MetaDataCreationException {
		
		String elementName = (owner != null) ? XmlUtils.getElementName(owner) :
			XmlUtils.getRootElementName(clazz);
		
		XmlType xmlType = clazz.getAnnotation(XmlType.class);
		elementName = createElementName(clazz, elementName, xmlType);
		
		String targetNamespace = namespace == null ? "" : namespace.getNamespace(); 
		String deserializationKey = createIdentifier(elementName, targetNamespace);
		ElementInfo elementInfo = this.parsingInfo.get(deserializationKey);
		
		if(elementInfo == null) {
			elementInfo = new ElementInfo(elementName, clazz, namespace);
			this.logger.d("Creating meta data for element: " + deserializationKey);
			this.parsingInfo.put(elementInfo.getIdentifier(), elementInfo);
		}

		/*
		 * Serialization is provided for all fields that are not transient - means
		 * that meta-data must be created on each field.
		 */
		String serializationKey = createIdentifier(clazz, owner);
		this.serializationInfo.put(serializationKey, elementInfo);

		return elementInfo;
	}

	/**
	 * <p>Creates correct name of target element according to the inheritance
	 * configuration. The element name is update only if this <code>clazz</code>
	 * has an ancestor.</p>
	 * <p>The update is performed according the {@link XmlType#name()} value
	 * of target {@link XmlType} annotation.</p>
	 * 
	 * @param clazz - class specifying this element.
	 * @param elementName - original element name.
	 * @param xmlType - {@link XmlType} annotation of this <code>clazz</code>
	 * if any present.
	 * 
	 * @return <code>elementName</code> or updated this name, if necessary. 
	 * 
	 * @throws MetaDataCreationException if this <code>clazz</code> has ancestor
	 * and the type of this field is not present.
	 * 
	 * @see XmlType
	 */
	private String createElementName(Class<?> clazz, String elementName, XmlType xmlType)
			throws MetaDataCreationException {
		
		if(!XmlUtils.hasAncestor(clazz)) {
			return elementName;
		}
			
		if(xmlType == null) {
			String message = "Class " + clazz.getName() + "must have " +
					XmlType.class.getName() + " annotation!";

			throw new MetaDataCreationException(message);
		}

		String type = !XmlType.NAME_DEFAULT.equals(xmlType.name()) ? xmlType.name() :
			clazz.getSimpleName().toLowerCase(Locale.US);

		elementName = type;
		return elementName;
	}
	
	/*
	 * Injects getter/setter methods as additional meta-data information. This
	 * method is providing huge performance increase for the XML data
	 * processing.
	 */
	private void registerInjectionMethod(Field field) {
		Method getter = XmlUtils.createGetter(field);
		this.getterMap.put(field, getter);
		Method setter = XmlUtils.createSetter(field);
		this.setterMap.put(field, setter);
	}
	
	/**
	 * <p>Registers {@link ValueAdapter} if any present for this <code>field</code>.</p>
	 * 
	 * @param clazz - Class containing {@link XmlValueAdapter} annotation.
	 * 
	 * @throws IllegalStateException if the <code>field</code> doesn't implement
	 * {@link ValueAdapter} interface.
	 * 
	 * @see ValueAdapter
	 * @see XmlValueAdapter
	 * @see ValueAdapterRegistry
	 */
	private void registerAdapter(Field field) throws IllegalStateException {
		if(!field.isAnnotationPresent(XmlValueAdapter.class)) {
			return;
		}

		XmlValueAdapter xmlValueAdapter = field.getAnnotation(XmlValueAdapter.class);
		Class<?> adapterClass = Void.class.equals(xmlValueAdapter.adapter()) ?
				field.getType() : xmlValueAdapter.adapter();
		
		if(!ValueAdapter.class.isAssignableFrom(adapterClass)) {
			String message = "Adapter: " + adapterClass.getName() + " must implement " +
					ValueAdapter.class.getName() + "interface!";
			
			throw new IllegalStateException(message);
		}
		
		ValueAdapter<?> adapter = (ValueAdapter<?>) XmlUtils.newInstance(adapterClass);
		this.valueAdapterRegistry.registerAdapter(adapter);
	}

}

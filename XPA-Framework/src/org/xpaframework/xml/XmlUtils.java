package org.xpaframework.xml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.xpaframework.xml.annotation.XmlAncestor;
import org.xpaframework.xml.annotation.XmlElement;
import org.xpaframework.xml.annotation.XmlRootElement;
import org.xpaframework.xml.annotation.XmlTransient;
import org.xpaframework.xml.annotation.XmlType;


/**
 * Utility class containing support methods for XML data mapping.
 * 
 * @author Jan Janickovic
 */
public class XmlUtils {//TODO split and move to .util package

	/**
	 * The attribute referencing XML schema instance attribute "type".
	 * The xml representation of this attribute is:
	 * <pre>&lt;elem
	 * 	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	 * 	xsi:type="value"/&gt;</pre>
	 */
	public static final String ATTRIBUTE_TYPE = "type";
	
	/**
	 * <p>Field representing content type of the XML request.</p>
	 * <p>Value: {@value #CONTENT_TYPE_XML}</p>
	 */
	public static final String CONTENT_TYPE_XML = "application/xml";
	
	/**
	 * <p>Creates and returns the XML element string for specified
	 * <code>field</code>.</p>
	 * <ul>The process of the retrieving the value is:
	 * <li>if the <code>name</code> property of the {@link XmlElement}
	 * annotation is present, then this value is used, but only if the default
	 * value is overwritten.</li>
	 * <li>if the <code>name</code> property of the {@link XmlElement}
	 * annotation is not present, then the {@link Field#getName()} method
	 * result is used.</li>
	 * </ul>
	 * 
	 * @param field - field representing XML element tag.
	 * @return the XML element name for specified field.
	 * 
	 * @see XmlElement#name()
	 * @see Field#getName()
	 */
	public static String getElementName(Field field) {
		String elementName = field.getName();
		
		if(field.isAnnotationPresent(XmlElement.class)) {
			XmlElement xmlElement = field.getAnnotation(XmlElement.class);
			
			if(!XmlElement.NAME_DEFAULT.equals(xmlElement.name())) {
				return xmlElement.name();
			}
		}
		
		return elementName;
	}
	
	/**
	 * Searches for the element type set by {@link XmlType} annotation.
	 * 
	 * @param clazz - target class
	 * @param recursive - determines if the search should be recursive. If
	 * <code>true</code> then the top level XML element type is returned.
	 * @return the XML element type associated with this <code>clazz</code>.
	 * 
	 * @throws IllegalArgumentException if the {@link XmlType} annotation is
	 * missing on any of the {@link XmlAncestor} annotated super classes
	 * (if the search is recursive).
	 * 
	 * @see XmlAncestor
	 * @see XmlType
	 * @see XmlUtils#getElementName(Class)
	 * @see XmlUtils#hasAncestor(Class)
	 */
	public static String getElementType(Class<?> clazz) throws IllegalArgumentException {
		if(!clazz.isAnnotationPresent(XmlType.class)) {
			String massage = "XmlType annotation not present for class: " + clazz.getName();
			throw new IllegalArgumentException(massage);
		}

		XmlType xmlType = clazz.getAnnotation(XmlType.class);
		return XmlType.NAME_DEFAULT.equals(xmlType.name()) ? clazz.getSimpleName().toLowerCase(Locale.US) : xmlType.name();
	}
	
	public static String getRootElementName(Class<?> rootClass) {
		String rootElementName = rootClass.getSimpleName().toLowerCase(Locale.US);

		if(!rootClass.isAnnotationPresent(XmlRootElement.class)) {
			return rootElementName;
		}
		
		XmlRootElement rootElement = rootClass.getAnnotation(XmlRootElement.class);
		return XmlRootElement.NAME_DEFAULT.equals(rootElement.name()) ? rootElementName : rootElement.name();
	}

	public static boolean isAbstract(Class<?> clazz) {
		return (clazz.getModifiers() & Modifier.ABSTRACT) != 0;
	}
	
	/**
	 * <p>Determines if this <code>field</code> should be used in the XML
	 * processing.</p>
	 * 
	 * @param field - field to detect if is transient
	 * @return <code>true</code> if this <code>field</code> should be used
	 * (means that is annotated by {@link XmlTransient} annotation or contains
	 * <code>transient</code> modifier), otherwise <code>false</code>.
	 * 
	 * @see XmlTransient
	 */
	public static boolean isTransient(Field field) {
		boolean nativeTransient = (field.getModifiers() & Modifier.TRANSIENT) == Modifier.TRANSIENT;
		return (field.isAnnotationPresent(XmlTransient.class) || nativeTransient);
	}
	
	/**
	 * <p>Returns the field specified with its <code>fieldName</code> for
	 * declared in <code>declaringClass</code>.</p>
	 * <p>This method wraps the {@link Class#getDeclaredField(String)}
	 * method and throws {@link IllegalArgumentException} if eny of the
	 * exceptions of wrapped method is thrown.</p> 
	 * 
	 * @param fieldName - case-sensitive name of the field
	 * @param declaringClass - class that declares this field.
	 * 
	 * @return {@link Field} object
	 * 
	 * @throws IllegalArgumentException if any of
	 * {@link Class#getDeclaredField(String)}
	 * exceptions is thrown. Exception is thrown with the original cause.
	 * 
	 * @see Class#getDeclaredField(String)
	 */
	public static Field getDeclaredField(String fieldName, Class<?> declaringClass)
			throws IllegalArgumentException{
		
		try {
			return declaringClass.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e);
		} catch (NoSuchFieldException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param field - class field to create getter for.
	 * 
	 * @return getter method for this <code>field</code>.
	 * 
	 * @throws IllegalStateException if any of the exceptions by
	 * {@link Class#getMethod(String, Class...)} is thrown.
	 * 
	 * @see #createSetter(Field)
	 */
	public static Method createGetter(Field field) throws IllegalStateException {
		Class<?> clazz = field.getDeclaringClass();
		String fieldName = field.getName();
		String methodName = "get" + 
				Character.toUpperCase(fieldName.toCharArray()[0]) + fieldName.substring(1);
		try {
			return clazz.getMethod(methodName, new Class<?>[]{});
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			String message = "No method named [" + methodName + "] for object: " + clazz.getName();
			throw new IllegalStateException(message);
		}
	}

	/**
	 * @param field - class field to create setter for.
	 * 
	 * @return setter method for this <code>field</code>.
	 * 
	 * @throws IllegalStateException if any of the exceptions by
	 * {@link Class#getMethod(String, Class...)} is thrown.
	 * 
	 * @see #createGetter(Field)
	 */
	public static Method createSetter(Field field) throws IllegalStateException {
		Class<?> clazz = field.getDeclaringClass();
		String fieldName = field.getName();
		String methodName = "set" + 
				Character.toUpperCase(fieldName.toCharArray()[0]) + fieldName.substring(1);
		try {
			return clazz.getMethod(methodName, field.getType());
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			String message = "No method named [" + methodName + "] for object: " + clazz.getName();
			throw new IllegalStateException(message, e);
		}
		
	}
	
	/**
	 * <p>Invokes method with no arguments. This can be called only for methods
	 * with no arguments (like getter methods, etc.).</p>
	 * <p>This method wraps {@link #invoke(Method, Object, Object...)} method
	 * and handles its throwing exceptions.</p>
	 * 
	 * @param method - method to invoke
	 * @param receiver - the object on which to call this method 
	 * @return result of {@link Method#invoke(Object, Object...)}
	 * 
	 * @throws RuntimeException defined by
	 * {@link #invoke(Method, Object, Object...)} method.
	 * 
	 * @see #invoke(Method, Object, Object...)
	 * @see Method#invoke(Object, Object...)
	 */
	public static Object invoke(Method method, Object receiver) throws RuntimeException {
		return invoke(method, receiver, new Object[]{});
	}
	
	public static Object invoke(Method method, Object receiver, Object... args)
			throws RuntimeException {
		
		try {
			return method.invoke(receiver, args);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Method invocation failed!", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Method invocation failed!", e);
		}
	}
	
	/**
	 * Invokes getter method that returns object of type {@link List}.
	 * 
	 * @return object of type {@link List}.
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> invokeList(Object obj, Method getter) {
		try {
			return (List<Object>) getter.invoke(obj, new Object[]{});
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Method invocation failed!", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Method invocation failed!", e);
		}
	}
	
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Instantiation failed!", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Instantiation failed!", e);
		}
	}
	
	public static boolean hasAncestor(Class<?> clazz) throws NullPointerException {
		if(clazz == null) {
			throw new NullPointerException("null class!");
		}
		
		Class<?> superClass = clazz.getSuperclass();
		return superClass != null && superClass.isAnnotationPresent(XmlAncestor.class);
	}
	
	public static boolean isAncestor(Class<?> clazz) {
		return clazz.isAnnotationPresent(XmlAncestor.class);
	}
	
	/**
	 * <p>Determines if the <code>binding</code> class represents simple type.
	 * <ul>The simple type can be represented by following conditions:
	 * <li>{@link Class#isEnum()}</li>
	 * <li>{@link Class#isPrimitive()}</li>
	 * <li><code>Number.class.equals(binding)</code></li>
	 * <li><code>String.class.equals(binding)</code></li>
	 * </ul>
	 * </p>
	 * <p>The only exception may be enumeration types. Due access to
	 * enumeration type in different way like to the others, this condition can
	 * be excluded by calling method {@link #isSimpleType(Class, boolean)} with
	 * arguments:
	 * <pre>XmlUtils.isSimpleType(class, false)</pre></p>
	 * 
	 * @param clazz - the type class of the field.
	 * @return <code>true</code> if the type represents simple type,
	 * <code>false</code> otherwise.
	 * 
	 * @see #isSimpleType(Class, boolean)
	 * @see Class#isEnum()
	 * @see Class#isPrimitive()
	 * @see Number
	 * @see Object#equals(Object)
	 * @see String
	 */
	public static boolean isSimpleType(Field field) {
		return isSimpleType(field.getType(), true);
	}
	
	/**
	 * <p>Determines if the <code>binding</code> class represents simple type.
	 * <ul>The simple type can be represented by following conditions:
	 * <li>{@link Class#isPrimitive()}</li>
	 * <li><code>Class.class.equals(binding)</code></li>
	 * <li><code>Number.class.equals(binding)</code></li>
	 * <li><code>String.class.equals(binding)</code></li>
	 * <li><code>URL.class.equals(binding)</code></li>
	 * </ul>
	 * </p>
	 * <p>The only exception are enumeration types, that are determined
	 * by {@link Class#isEnum()} method.</p>
	 * 
	 * @param clazz - the type class of the field.
	 * @param includeEnums - if <code>true</code>, enumeration types will
	 * be included into the condition, if <code>false</code> they will be
	 * excluded.
	 * 
	 * @return <code>true</code> if the type represents simple type,
	 * <code>false</code> otherwise.
	 * 
	 * @see #isSimpleType(Class)
	 * @see Class#isEnum()
	 * @see Class#isPrimitive()
	 * @see Number
	 * @see Object#equals(Object)
	 * @see String
	 */
	public static boolean isSimpleType(Class<?> clazz, boolean includeEnums) {
		return clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || Boolean.class.equals(clazz) ||
				String.class.equals(clazz) || Class.class.equals(clazz) || Character.class.equals(clazz) ||
				URL.class.equals(clazz) || Byte.class.equals(clazz) || (includeEnums && clazz.isEnum()) ||
				XMLGregorianCalendar.class.isAssignableFrom(clazz) || Duration.class.isAssignableFrom(clazz) ||
				QName.class.isAssignableFrom(clazz);
	}

	/**
	 * @param field - scanning {@link Field}.
	 * @return parameter type of the {@link Collection}.
	 * @throws IllegalArgumentException if this <code>field</code> is not a
	 * {@link Collection}.
	 */
	public static Class<?> getCollectionType(Field field) throws IllegalArgumentException {
		Class<?> type = field.getType();
		
		if(!Collection.class.isAssignableFrom(type)) {
			throw new IllegalArgumentException("Field " + field.getDeclaringClass() + "." +
					field.getName() + "is not subclass of " + Collection.class.getName());
		}
		
		ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
		return (Class<?>) stringListType.getActualTypeArguments()[0];
	}
	
	/**
	 * Validates root element class against the serialization/deserialization
	 * rules.
	 * 
	 * @param clazz - the XML root element class respresentation. 
	 * @throws NullPointerException if <code>class</code> is <code>null</code>.
	 * @throws IllegalArgumentException if the {@link XmlRootElement}
	 * annotation is not present on this <code>clazz</code>.
	 * @throws IllegalStateException if this root has an ancestor class.
	 * 
	 * @see XmlAncestor
	 * @see XmlRootElement
	 * @see XmlType
	 * @see #hasAncestor(Class)
	 */
	public static void validateRoot(Class<?> clazz) 
			throws NullPointerException, IllegalArgumentException, IllegalStateException {
		
		if(clazz == null) {
			throw new NullPointerException("'obj' parameter is null!");
		}
		
		if(!clazz.isAnnotationPresent(XmlRootElement.class)) {
			String message = clazz.getName() + " is not suitable for xml binding";
			throw new IllegalArgumentException(message);
		}
		
		if(hasAncestor(clazz)) {
			throw new IllegalStateException("The root element object can not have ancestor!");
		}
	}
	
}

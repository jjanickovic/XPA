package org.xpaframework.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The annotation representing field mapping to specified XML element. This
 * mapping is made by either name of this field or the {@link #name()}
 * property. When this property is set, this value is considered as the name
 * of the XML element that is represented by this field.</p>
 * <p>If this element is bound to specific namespace, the {@link XmlNamespace}
 * annotation can define element's namespace.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see XmlNamespace
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlElement {

	public static final String NAME_DEFAULT = "#DEFAULT#";
	public static final String NAMESPACE_DEFAULT = "#DEFAULT#";
	
	/**
	 * <p>Type of the element that is bound to the annotated class. This
	 * property is used for object to XML serialization as the name of the
	 * target element.</p>
	 */
	String name() default NAME_DEFAULT;
	
	/**
	 * <p>The target namespace for this type. The default value is empty
	 * string.</p>
	 */
	String targetNamespace() default NAMESPACE_DEFAULT;
	
}

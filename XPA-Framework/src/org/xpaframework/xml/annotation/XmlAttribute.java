package org.xpaframework.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Annotation defining the target field as the XML attribute property. The
 * annotated field is then mapped to specified XML attribute.</p>
 * <p>The class fields are allowed to have different names of the fields than
 * the XML attribute names. For this type, the {@link #name()} property
 * can be set to define the target XML attribute.</p>
 * <p>According to attribute namespace prefix, the attribute field must be
 * annoatated with {@link XmlNamespace} to define specific namespace of this
 * attribute.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see XmlNamespace
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlAttribute {

	/**
	 * Name of the XML attribute value. Default value is empty string.
	 */
	String name() default "";
	
}

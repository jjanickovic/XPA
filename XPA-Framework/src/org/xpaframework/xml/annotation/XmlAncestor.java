package org.xpaframework.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Annotation declaring ancestor for the mapped object. The ancestors can
 * declare shared properties and attributes for various types of objects. The
 * inherited classed used for XML mapping must be explicitly defined by
 * {@link #inheritanceTypes()} property.</p>
 * <p>Note, that if this annotation is used on parent class, the inherited
 * class must be annotated by {@link XmlType} annotation.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see XmlType
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlAncestor {
	
	/**
	 * Inherited classed used for XML-object processing.
	 */
	public Class<?>[] inheritanceTypes();
}

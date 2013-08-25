package org.xpaframework.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Annotation representing schema type attribute. This annotation is used
 * for annotating classes that are mapped to complex element that represents
 * an abstract part of the document.</p>
 * <p>This annotation is part of inheritance where this type determines
 * superclass's type if the annotated class's superclass is marked as
 * {@link XmlAncestor}.</p>
 * <p>The type also represents the order of the nested fields according to the
 * XML schema sequence property.</p>
 *  
 * @author Jan Janickovic
 * 
 * @see XmlAncestor
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlType {
	
	/**
	 * Default name for this type.
	 */
	public static final String NAME_DEFAULT = "#DEFAULT#";
	
	/**
	 * <p>Type of the element that is bound to the annotated class. Default
	 * value: {@value #NAME_DEFAULT}</p>
	 */
	String name() default NAME_DEFAULT;
	
	/**
	 * <p>Attribute determining the order of child elements for target object's
	 * nested elements. The array values must be case-sensitive field names
	 * representing nested elements. The default value is empty array.</p>
	 */
	String[] order() default {};
}

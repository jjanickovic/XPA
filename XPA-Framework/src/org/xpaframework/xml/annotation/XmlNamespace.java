package org.xpaframework.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation represents the namespace of the element or attribute.
 * Within the object hierarchy, there can be specified more than one namespace
 * representing schema or instance of the target field, so there are allowed
 * to define more namespaces. If the class or field is annotated, all target
 * class's properties are using this namespace as its own.</p>
 * <p>The namespace information can be declared for bioth classes and field.
 * The namespace information (if any) is retrieved by following order:
 * <ul>
 * <li>Highest priority have the field declaration of the namespace.</li>
 * <li>If the field has not set the namespace property, then the class
 * namespace is set as the field's namespace property.</li>
 * <li>If no namespace is defined by the field's class type, then the value
 * is retieved from the field owner.</li>
 * </ul>
 * This process is implemented by collecting metadata information of the
 * objects representing target XML document structure.
 * </p>
 * 
 * @author Jan Janickovic
 * 
 * @see XmlElement
 * @see XmlAttribute
 */
@Target(value = {ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlNamespace {

	/**
	 * <p>The namespace prefix property. By default is set to empty string
	 * which indicates that the prefix will be randomly generated.</p>
	 */
	String prefix() default "";
	
	/**
	 * <p>The property defining the target namespace.</p>
	 */
	String value();
}

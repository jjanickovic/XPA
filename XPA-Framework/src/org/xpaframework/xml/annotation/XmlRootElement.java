package org.xpaframework.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation specifies the XML root element. The root element object
 * must be annotated with this annotation to declare object as root for XML
 * processing.</p>
 * <p>This annotation is processed the same way as the {@link XmlElement}
 * annotation. The base meaning of this annotation is to separate root element
 * form the others. Root elements have no parents that is important information
 * of the objects tranformation.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see XmlElement
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlRootElement {

	public static final String NAME_DEFAULT = "#DEFAULT#";

	String name() default NAME_DEFAULT;
}

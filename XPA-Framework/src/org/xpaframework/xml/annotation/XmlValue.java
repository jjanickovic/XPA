package org.xpaframework.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Annotation that can be used to mark field as XML mapping value.
 * It can be used on any object representing <code>complexType</code>
 * but can be used only once per class.</p>
 * 
 * <p>If the annotated class is part of inheritance, make sure that
 * the superclass has no field annotated with this value. More than
 * one field annotated by this annotation will throw an exception.</p>
 * 
 * @author Jan Janickovic
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlValue {
}

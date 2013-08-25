package org.xpaframework.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The annotated identifying the field to be skipped from the XML mapping
 * process.</p>
 * 
 * @author Jan Janickovic
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlTransient {

}

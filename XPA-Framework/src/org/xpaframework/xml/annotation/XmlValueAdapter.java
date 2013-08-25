package org.xpaframework.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xpaframework.ValueAdapter;
import org.xpaframework.xml.SimpleTypeRegistry;


/**
 * <p>This annotation is marking customized object creation that should be
 * created by its own adapter. Each annotated object must either
 * specify the target adapter class or implement it itself.</p>
 * 
 * <p>Note: {@linkplain ValueAdapter} implementations should not be implemented
 * by enumerations due class instantiation issues. Instead of create the own
 * adapter class that implement the {@link ValueAdapter} interface specifying
 * the enumneration class as type.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see SimpleTypeRegistry
 * @see org.xpaframework.ValueAdapter
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlValueAdapter {
	
	/**
	 * <p>The class using for element-object mapping. This class must implement
	 * {@link ValueAdapter} interface to provide correct initialization and
	 * serialization.</p>
	 * <p>The default value of this property is set to {@link Void} class which
	 * means that the mapped object implements the adapter itself.</p>
	 */
	Class<?> adapter() default Void.class;
	
}

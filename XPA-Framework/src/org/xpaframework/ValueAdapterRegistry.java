package org.xpaframework;

import java.util.Hashtable;
import java.util.Map;

/**
 * <p>The class registering all conversion adapters. These adapters
 * are used for serialization/deserialization processing to convert
 * data representation into the specified data type.</p>
 * <p>This conversion is handled by {@link ValueAdapter} interface
 * by implementing {@link ValueAdapter#convertValue(String)}
 * method for derserialization and {@link ValueAdapter#toString(Object)}
 * method to serialize object's value.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see ValueAdapter
 */
public abstract class ValueAdapterRegistry {

	/*
	 * Map's key must be of string type due the primitive type object.
	 * They are not able to be implicitly converted into their object
	 * representation.
	 */
	private Map<String, ValueAdapter<? extends Object>> map =
			new Hashtable<String, ValueAdapter<? extends Object>>();

	protected ValueAdapterRegistry() {
	}
	
	/**
	 * Initializes handler by its own values.
	 */
	public abstract void initialize();
	
	/**
	 * Determines if this registry is initialized.
	 * @return
	 */
	public abstract boolean isInitialized();
	
	/**
	 * @return map of registered adapter for this handler.
	 */
	protected Map<String, ValueAdapter<? extends Object>> getRegisteredAdapters() {
		return this.map;
	}
	
	/**
	 * <p>Registers the {@link ValueAdapter} object within this context.</p>
	 * <p>The method also provides the check if the adapter is correctly configured. If not,
	 * {@link NullPointerException} is thrown according this method exception rules.</p>
	 * 
	 * @param adapter - the {@link ValueAdapter} object to be registered within this context.
	 * @throws NullPointerException if adapter is <code>null</code> or the result of {@link #getTarget()}
	 * is <code>null</code>.
	 * 
	 * @see ValueAdapter#getTarget()
	 * @see ValueAdapter#getType()
	 */
	public void registerAdapter(ValueAdapter<? extends Object> adapter)
			throws NullPointerException {
		
		if(adapter == null) {
			throw new NullPointerException("null adapter can not be registered in the context!");
		}
		
		this.map.put(adapter.getType().getCanonicalName(), adapter);
	}
	
	
	/**
	 * <p>Returns value adapter for given <code>target</code>.
	 * 
	 * @param target - the target class.
	 * 
	 * @return the {@link ValueAdapter} object.
	 * 
	 * @throws NullPointerExceptionl if <code>target</code> is <code>null</code>.
	 * @throws IllegalArgumentException if no adapter found for given <code>target</code>.
	 * 
	 * @see Class#getCanonicalName()
	 */
	public ValueAdapter<?> getAdapter(Class<?> target)
			throws NullPointerException, IllegalArgumentException {
		
		if(target == null) {
			throw new NullPointerException("null target class!");
		}
		
		ValueAdapter<?> adapter = this.map.get(target.getCanonicalName());
		
		if(adapter == null) {
			throw new IllegalArgumentException("No adapter registered for class: " + target.getName());
		}
		
		return adapter;
	}
	
}

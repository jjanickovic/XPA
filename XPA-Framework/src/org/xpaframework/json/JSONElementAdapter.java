package org.xpaframework.json;

/**
 * This class is not used. It will be deleted in next release.
 * 
 * @author Jan Janickovic
 *
 * @param <T> type of the object extended by the {@link JSONElement}
 * interface
 * 
 * @see JSONElement
 */
@Deprecated
public abstract class JSONElementAdapter<T extends JSONElement> extends JSONValueAdapter<T> {

	private Class<T> targetClass;

	@Override
	public Class<?> getType() {
		return this.targetClass;
	}

	protected JSONElementAdapter(Class<T> targetClass) {
		this.targetClass = targetClass;
	}
}

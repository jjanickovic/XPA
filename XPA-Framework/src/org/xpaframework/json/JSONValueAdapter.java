package org.xpaframework.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.xpaframework.ValueAdapter;


/**
 * Abstract value adapter converting various data types to
 * {@link JSONObject}.
 * 
 * @author Jan Janickovic
 *
 * @param <T> - type representing data for JSON.
 */
public abstract class JSONValueAdapter<T> implements ValueAdapter<T> {

	/**
	 * Converts this string value to {@link JSONObject}.
	 * 
	 * @param value - value representing data for JSON object.
	 * @return new instance of {@link JSONObject}
	 * 
	 * @throws JSONException if {@link JSONObject} instance creation throws
	 * {@link JSONException}
	 * 
	 * @see JSONObject
	 */
	public abstract JSONObject toJsonObject(T value) throws JSONException;
	
}

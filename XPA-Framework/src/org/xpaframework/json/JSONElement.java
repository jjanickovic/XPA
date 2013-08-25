package org.xpaframework.json;

import org.json.JSONObject;

/**
 * <p>Interface declaring class as JSON object representation. By
 * implementing methods {@link #toJsonString()} and {@link #create(String)}
 * on the implemented object the JSON format is serialized/deserialized.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see JSONObject
 */
public interface JSONElement {

	public String toJsonString();
	
	public JSONElement create(String json);
}

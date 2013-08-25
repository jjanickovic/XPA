package org.xpaframework.json;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xpaframework.ValueAdapter;
import org.xpaframework.ValueAdapterRegistry;
import org.xpaframework.ValueConversionException;


/**
 * <p>Registry handling JSON string to {@link JSONObject} or {@link Map}
 * conversion.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see ValueAdapterRegistry
 */
public class JSONDataRegistry extends ValueAdapterRegistry {

	private boolean initialized;
	
	@Override
	public void initialize() {
		registerAdapter(TYPE_JSON_MAP);
		registerAdapter(TYPE_JSON_OBJECT);
		this.initialized = true;
	}
	
	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public JSONValueAdapter<?> getAdapter(Class<?> target)
			throws NullPointerException, IllegalArgumentException {
		
		ValueAdapter<?> adapter = super.getAdapter(target);
		
		if(!JSONValueAdapter.class.isAssignableFrom(adapter.getClass())) {
			throw new IllegalArgumentException("Target class " + target.getCanonicalName() +
					" is not instance of " + JSONValueAdapter.class.getCanonicalName());
		}
		
		return (JSONValueAdapter<?>) adapter;
	}
	
	/**
	 * Adapter responsible for JSON-Object -&gt; string and
	 * string -&gt; JSONObject conversion. 
	 * 
	 * @see JSONObject
	 */
	private final ValueAdapter<JSONObject> TYPE_JSON_OBJECT = new JSONValueAdapter<JSONObject>() {
		
		@Override
		public Class<?> getType() {
			return JSONObject.class;
		}
		
		@Override
		public JSONObject convertValue(String value) throws ValueConversionException {
			if(value == null) {
				return null;
			}
			
			try {
				return new JSONObject(value);
			} catch (JSONException e) {
				throw new ValueConversionException("JSON object instantiation error!", e);
			}
		}

		@Override
		public String toString(JSONObject target) {
			if(target == null) {
				return null;
			}
			
			return target.toString();
		}

		@Override
		public JSONObject toJsonObject(JSONObject value) throws JSONException {
			return value;
		}
	};

	/**
	 * Adapter responsible for Map -&gt; string and
	 * string -&gt; Map conversion. 
	 * 
	 * @see JSONObject
	 */
	private final ValueAdapter<Map<String, Object>> TYPE_JSON_MAP = new JSONValueAdapter<Map<String, Object>>() {
		
		@Override
		public Class<?> getType() {
			return Map.class;
		}
		
		@Override
		public Map<String, Object> convertValue(String value) throws ValueConversionException {
			if(value == null) {
				return null;
			}
			
			return null;
		}

		@Override
		public String toString(Map<String, Object> target) {
			if(target == null) {
				return null;
			}
			
			try {
				JSONObject json = toJsonObject(target);
				return json.toString();
			} catch (JSONException e) {
				throw new IllegalArgumentException(e);
			}
		}

		@Override
		public JSONObject toJsonObject(Map<String, Object> value) throws JSONException {
			JSONObject json = new JSONObject();
			
			for(String key : value.keySet()) {
				json.put(key, value.get(key));
			}
			
			return json;
		}
	};
	
}

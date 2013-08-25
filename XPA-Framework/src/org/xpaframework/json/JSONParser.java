package org.xpaframework.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xpaframework.AbstractDeserializer;
import org.xpaframework.DeserializationException;
import org.xpaframework.Deserializer;
import org.xpaframework.MappingException;


/**
 * <p>Deserializer implementation over JSON format. This implementation
 * converts the input data source into the {@link JSONObject}.</p>
 * 
 * @author Jan Janickovic
 * 
 * @see Deserializer
 * @see JSONObject
 */
public class JSONParser extends AbstractDeserializer<JSONObject> {

	private JSONObject target;

	protected JSONParser() {
	}

	@Override
	public void deserialize(Reader reader) throws DeserializationException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		StringBuffer stringBuffer = new StringBuffer();

		try {
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}

			this.target = new JSONObject(stringBuffer.toString());
		} catch (IOException e) {
			throw new DeserializationException("JSON object creation error!", e);
		} catch (JSONException e) {
			throw new DeserializationException("JSON object creation error!", e);
		}
	}

	@Override
	public void deserialize(InputStream inputStream) throws MappingException {
		deserialize(new InputStreamReader(inputStream));
	}

	@Override
	public JSONObject getValue() {
		return this.target;
	}
	
}

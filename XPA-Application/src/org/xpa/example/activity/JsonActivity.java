package org.xpa.example.activity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xpa.example.ApplicationContext;
import org.xpa.example.R;
import org.xpaframework.MappingException;
import org.xpaframework.SerializationException;
import org.xpaframework.Serializer;
import org.xpaframework.json.JSONContext;
import org.xpaframework.json.JSONParser;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class JsonActivity extends Activity {

	private final String TAG = getClass().getSimpleName();
	private final String JSON_URL = "http://api.androidhive.info/contacts/";
	
	private Button receive, send;
	private JSONContext jsonContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_json);
		
		this.jsonContext = getApplicationContext().getJsonContext();

		this.receive = (Button) findViewById(R.id.button_json_receive);
		this.send = (Button) findViewById(R.id.button_json_send);
		
		this.receive.setOnClickListener(new ReceiveListener());
		this.send.setOnClickListener(new SendListener());
	}
	
	@Override
	public ApplicationContext getApplicationContext() {
		return (ApplicationContext) super.getApplicationContext();
	}
	
	private class ReceiveListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			JSONParser parser = (JSONParser) jsonContext.createDeserializer();
			
			try {
				URL url = new URL(JSON_URL);
				parser.deserialize(url);
				
				JSONObject jsonObject = parser.getValue();
				
				Log.i(TAG, "Received data:\n" + jsonObject.toString());
				Toast.makeText(JsonActivity.this, R.string.msg_toast_result_ok,
						Toast.LENGTH_LONG).show();
			} catch (MalformedURLException e) {
				Toast.makeText(JsonActivity.this, R.string.msg_toast_result_failed,
						Toast.LENGTH_LONG).show();
				throw new RuntimeException(e);
			} catch (MappingException e) {
				Toast.makeText(JsonActivity.this, R.string.msg_toast_result_failed,
						Toast.LENGTH_LONG).show();
				throw new RuntimeException(e);
			}
		}
		
	}
	
	private class SendListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			try {
				JSONObject jsonObject = createJsonObject(6);
				File dir = Environment.getExternalStorageDirectory();
				Serializer serializer = jsonContext.createSerializer();
				serializer.serialize(jsonObject, new File(dir, "output-contacts.json"));
				
				Log.i(TAG, "Sent data:\n" + jsonObject.toString());
				Toast.makeText(JsonActivity.this, R.string.msg_toast_result_ok,
						Toast.LENGTH_LONG).show();
			} catch (JSONException e) {
				Toast.makeText(JsonActivity.this, R.string.msg_toast_result_failed,
						Toast.LENGTH_LONG).show();
				throw new RuntimeException(e);
			} catch (SerializationException e) {
				Toast.makeText(JsonActivity.this, R.string.msg_toast_result_failed,
						Toast.LENGTH_LONG).show();
				throw new RuntimeException(e);
			} catch (IOException e) {
				Toast.makeText(JsonActivity.this, R.string.msg_toast_result_failed,
						Toast.LENGTH_LONG).show();
				throw new RuntimeException(e);
			}
			
		}
		
		private JSONObject createJsonObject(int size)
				throws JSONException {
			JSONObject jsonObject = new JSONObject();
			JSONArray array = new JSONArray();
			
			for(int i = 0; i < size; i++) {
				JSONObject o = new JSONObject();
				o.put("name", "Name-" + i);
				o.put("surname", "Surname-" + i);
				
				array.put(o);
			}
			
			jsonObject.put("people", array);
			return jsonObject;
		}
	}
	
}

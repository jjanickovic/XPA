package org.xpa.example.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.xpa.example.ApplicationContext;
import org.xpa.example.R;
import org.xpa.example.data.orders.Orders;
import org.xpa.example.data.shop.Shop;
import org.xpa.example.data.values.Values;
import org.xpa.example.util.IOUtils;
import org.xpaframework.Deserializer;
import org.xpaframework.MappingException;
import org.xpaframework.Serializer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class XPAActivity extends Activity {

	private final String TAG = getClass().getSimpleName();
	private static final int INPUT_FILE_ORDERS = R.raw.orders;
	private static final int INPUT_FILE_VALUES = R.raw.values;
	private static final int INPUT_FILE_SHOP= R.raw.shop;

	private static final String OUTPUT_FILE_ORDERS = "output-xpa-orders.xml";
	private static final String OUTPUT_FILE_VALUES = "output-xpa-values.xml";
	private static final String OUTPUT_FILE_SHOP = "output-xpa-shop.xml";

	private Button connectionButton;
	private Button sendButton;
	private Spinner type, data;

	private Orders orders;
	private Values values;
	private Shop shop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xpa);
		
		this.type = (Spinner) findViewById(R.id.spinner_xml_input);
		this.data = (Spinner) findViewById(R.id.spinner_xml_data);
		
		this.connectionButton = (Button) findViewById(R.id.button_xpa_receive);
		this.connectionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int msgError = R.string.msg_toast_result_failed;
				
				try {
					int dataIndex = getData();
					switch (dataIndex) {
					case 0:
						Log.d(TAG, "loadOrders");
						loadOrders();
						break;
					case 1:
						Log.d(TAG, "loadValues");
						loadValues();
						break;
					case 2:
						Log.d(TAG, "loadShop");
						loadShop();
						break;
					default:
						Toast.makeText(XPAActivity.this, "Invalid data index!", Toast.LENGTH_LONG)
								.show();
						break;
					}

					int msgId = R.string.msg_toast_result_ok;
					Toast.makeText(XPAActivity.this, msgId, Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Toast.makeText(XPAActivity.this, msgError, Toast.LENGTH_LONG).show();
					Log.e(TAG, "Request error!", e);
				} catch (MappingException e) {
					Toast.makeText(XPAActivity.this, msgError, Toast.LENGTH_LONG).show();
					Log.e(TAG, "Request error!", e);
				}
			}
		});

		this.sendButton = (Button) findViewById(R.id.button_xpa_send);
		this.sendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int msgError = R.string.msg_toast_result_failed;

				try {
					int dataIndex = getData();
					switch (dataIndex) {
					case 0:
						validateOutput(XPAActivity.this.orders);
						Log.d(TAG, "storeOrders");
						storeOrders(XPAActivity.this.orders);
						break;
					case 1:
						validateOutput(XPAActivity.this.values);
						Log.d(TAG, "storeValues");
						storeValues(XPAActivity.this.values);
						break;
					case 2:
						validateOutput(XPAActivity.this.shop);
						Log.d(TAG, "storeShop");
						storeShop(XPAActivity.this.shop);
						break;
					default:
						Toast.makeText(XPAActivity.this, "Invalid data index!", Toast.LENGTH_LONG)
								.show();
						break;
					}
					int msgId = R.string.msg_toast_result_ok;
					Toast.makeText(XPAActivity.this, msgId, Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Toast.makeText(XPAActivity.this, msgError, Toast.LENGTH_LONG).show();
					Log.e(TAG, "Serialization error!", e);
				} catch (MappingException e) {
					Toast.makeText(XPAActivity.this, msgError, Toast.LENGTH_LONG).show();
					Log.e(TAG, "Serialization error!", e);
				}
			}
			
			private boolean validateOutput(Object output) {
				if(output == null) {
					int msgId = R.string.msg_toast_no_data;
					Toast.makeText(XPAActivity.this, msgId, Toast.LENGTH_LONG).show();
					return false;
				}
				
				return true;
			}
		});

	}

	private int getData() {
		return this.data.getSelectedItemPosition();//0 - orders, 1 - values, 2 - shop
	}
	
	private int getType() {
		return this.type.getSelectedItemPosition();//0 - HTTP, 1 - file
	}
	
	private void loadOrders() throws IOException, MappingException {
		Deserializer<Orders> deserializer = getApplicationContext()
				.getXmlContext().createDeserializer(Orders.class);
		
		if(0 == getType()) {
			InputStream input = getResources().openRawResource(INPUT_FILE_ORDERS);
			deserializer.deserialize(input);
		} else {
			URL url = IOUtils.createXmlOrdersUrl(this);
			Log.d(TAG, "Creating request for " + url);
			deserializer.deserialize(url);
		}

		this.orders = deserializer.getValue();
		Log.d(TAG, "Response object: " + this.orders);
	}

	private void storeOrders(Object obj) throws MappingException, IOException {
		if(1 == getType()) {
			Log.w(TAG, "HTTP not supported");
			Toast.makeText(this, "HTTP not supported", Toast.LENGTH_LONG).show();
			return;
		}

		Serializer serializer = getApplicationContext()
				.getXmlContext().createSerializer();

		if(0 == getType()) {
			File storageDir = Environment.getExternalStorageDirectory();
			File outputFile = new File(storageDir, OUTPUT_FILE_ORDERS);
			serializer.serialize(obj, outputFile);
		} else {
			URL url = IOUtils.createXmlOrdersUrl(this);
			serializer.serialize(obj, url);
		}
	}
	
	private void loadValues() throws MalformedURLException, MappingException {
		if(1 == getType()) {//HTTP not supported
			Log.w(TAG, "HTTP not supported");
			Toast.makeText(this, "HTTP not supported", Toast.LENGTH_LONG).show();
			return;
		}
		
		Deserializer<Values> deserializer = getApplicationContext()
				.getXmlContext().createDeserializer(Values.class);

		InputStream input = getResources().openRawResource(INPUT_FILE_VALUES);
		deserializer.deserialize(input);

		this.values = deserializer.getValue();
		Log.d(TAG, "Response object: " + this.values);
	}
	
	private void storeValues(Object obj) throws MappingException, IOException {
		Serializer serializer = getApplicationContext()
				.getXmlContext().createSerializer();

		if(0 == getType()) {
			File storageDir = Environment.getExternalStorageDirectory();
			File outputFile = new File(storageDir, OUTPUT_FILE_VALUES);
			serializer.serialize(obj, outputFile);
		} else {
			URL url = IOUtils.createXmlValuesUrl(this);
			serializer.serialize(obj, url);
		}
	}
	
	private void loadShop() throws MappingException {
		if(1 == getType()) {//HTTP not supported
			Log.w(TAG, "HTTP not supported");
			Toast.makeText(this, "HTTP not supported", Toast.LENGTH_LONG).show();
			return;
		}
		
		Deserializer<Shop> deserializer = getApplicationContext()
				.getXmlContext().createDeserializer(Shop.class);
		InputStream input = getResources().openRawResource(INPUT_FILE_SHOP);
		deserializer.deserialize(input);
		this.shop = deserializer.getValue();
	}
	
	public void storeShop(Object obj) throws MappingException, IOException {
		Serializer serializer = getApplicationContext()
				.getXmlContext().createSerializer();

		if(0 == getType()) {
			File storageDir = Environment.getExternalStorageDirectory();
			File outputFile = new File(storageDir, OUTPUT_FILE_SHOP);
			serializer.serialize(obj, outputFile);
		} else {
			URL url = IOUtils.createXmlValuesUrl(this);
			serializer.serialize(obj, url);
		}
	}
	
	@Override
	public ApplicationContext getApplicationContext() {
		return (ApplicationContext) super.getApplicationContext();
	}

}

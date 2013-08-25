package org.xpa.example.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.xpa.example.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class IOUtils {

	private static final String HTTP_XML_ORDERS = "/XPA/orders";
	private static final String HTTP_XML_VALUES = "/XPA/values";

	private static final String HTTP_JSON = "http://api.androidhive.info/contacts/";
	
	private static final String PREFERENCE_KEY = "server_address";
	
	public static URL createXmlOrdersUrl(Context context) {
		return createUrl(context, HTTP_XML_ORDERS);
	}
	
	public static URL createXmlValuesUrl(Context context) {
		return createUrl(context, HTTP_XML_VALUES);
	}
	
	public static URL createJsonUrl(Context context) {
		return createUrl(context, HTTP_JSON);
	}
	
	private static URL createUrl(Context context, String path) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String address = preferences.getString(PREFERENCE_KEY, null);
		
		if(address == null) {
			Toast.makeText(context, R.string.msg_toast_invalid_address, Toast.LENGTH_LONG).show();
			return null;
		}
		
		try {
			return new URL(address + path);
		} catch (MalformedURLException e) {
			Toast.makeText(context, R.string.msg_toast_invalid_address, Toast.LENGTH_LONG).show();
			return null;
		}
	}
}

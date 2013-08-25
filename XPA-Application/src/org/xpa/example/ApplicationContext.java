package org.xpa.example;

import org.xpa.example.data.orders.Currency;
import org.xpaframework.json.JSONContext;
import org.xpaframework.xml.XmlContext;
import org.xpaframework.xml.XmlContextFactory;

import android.app.Application;
import android.util.Log;

public class ApplicationContext extends Application {

	private XmlContext xmlContext;
	private JSONContext jsonContext;

	@Override
	public void onCreate() {
		super.onCreate();
		
		this.jsonContext = JSONContext.getInstance();
		this.xmlContext = XmlContextFactory.getInstance().createXmlContext();
		
		Class<?> enumSuperclass = Currency.class.getSuperclass();
		Log.d("AppContext", "Enum superclass: " + enumSuperclass);
	}
	
	public XmlContext getXmlContext() {
		return this.xmlContext;
	}
	
	public JSONContext getJsonContext() {
		return this.jsonContext;
	}
	
}

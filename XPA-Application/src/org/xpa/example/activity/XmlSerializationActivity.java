package org.xpa.example.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;
import org.xpa.example.ApplicationContext;
import org.xpa.example.R;
import org.xpa.example.data.people.People;
import org.xpa.example.data.people.Person;
import org.xpa.example.util.Result;
import org.xpa.example.util.ResultSet;
import org.xpaframework.SerializationException;
import org.xpaframework.Serializer;
import org.xpaframework.xml.XmlContext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class XmlSerializationActivity extends Activity {

	private static final String TAG = XmlSerializationActivity.class.getSimpleName();
	
	private Button dom, xmlPull, xpa;
	private Spinner type, repeatCount;
	
	private XmlContext xmlContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xml_serialization);
		
		this.xmlContext = getApplicationContext().getXmlContext();
		
		this.dom = (Button) findViewById(R.id.button_xml_serialization_dom);
		this.xmlPull = (Button) findViewById(R.id.button_xml_serialization_xml_pull);
		this.xpa = (Button) findViewById(R.id.button_xml_serialization_xpa);

		this.type = (Spinner) findViewById(R.id.spinner_xml_type);
		this.repeatCount = (Spinner) findViewById(R.id.spinner_xml_repeats);
		
		this.dom.setOnClickListener(new DOMListener("DOM"));
		this.xmlPull.setOnClickListener(new XmlPullListener("XmlPull"));
		this.xpa.setOnClickListener(new XPAListener("XPA", this.xmlContext));
	}

	private int getXmlSize() {
		return type.getSelectedItemPosition() == 0 ? 2 : 76;
	}
	
	private int getRepeats() {
		String item = repeatCount.getSelectedItem().toString();
		return Integer.valueOf(item);
	}
	
	private abstract class ButtonListener implements View.OnClickListener {

		private String tag;
		
		protected ButtonListener(String tag) {
			this.tag = tag;
		}
		
		protected abstract void create(People people, OutputStream os);
		
		private OutputStream getOutput() {
			try {
				File storageDir = Environment.getExternalStorageDirectory();
				String fileName = "output-" + this.tag + "-" + getXmlSize() +  "-items.xml";
				FileOutputStream os = new FileOutputStream(new File(storageDir, fileName));
				return os;
			} catch (FileNotFoundException e) {
				new RuntimeException("Output stream error!", e);
			}
			
			return null;
		}
		
		@Override
		public void onClick(View v) {
			Toast.makeText(XmlSerializationActivity.this,
					R.string.msg_starting, Toast.LENGTH_LONG).show();
			
			int repeats = getRepeats();
			List<Long> durationList = new ArrayList<Long>();
			
			for(int i = 0; i < repeats; i++) {
				long millis = System.currentTimeMillis();
				
				People people = People.createPeople(getXmlSize());
				OutputStream os = getOutput();
				create(people, os);
				
				long diff = System.currentTimeMillis() - millis;
				durationList.add(diff);
				Log.i(TAG, String.format("[%s-run %d] %d items in %s ms.",
						this.tag, (i+1), people.getCount(), diff));
				
				try {
					int timeout = new Random().nextInt(9000) + 1000;
					Log.d(TAG, "Waiting " + timeout + " milliseconds");
					Thread.sleep(timeout);
				} catch (InterruptedException e) {
					Log.e(TAG, "Can not wait!", e);
				}
			}

			Log.i(TAG, "Summary: " + durationList);
			Log.i(TAG, "Deserialization process completed.");

			ResultSet resultSet = Result.createResult(durationList);
			Intent intent = new Intent(XmlSerializationActivity.this,
					PerformanceResultActivity.class);
			intent.putExtra(ResultSet.KEY, resultSet);
			startActivity(intent);
		}
		
	}
	
	private class DOMListener extends ButtonListener {

		protected DOMListener(String tag) {
			super(tag);
		}

		@Override
		protected void create(People people, OutputStream os) {
			try {
				Document doc = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder().newDocument();
				
				Element root = doc.createElement("people");
				doc.appendChild(root);
				
				for(Person person : people.getPeople()) {
					Element ePerson = doc.createElement("person");
					ePerson.setAttribute("gender", person.getGender().getValue());
					root.appendChild(ePerson);
					
					Element eName = doc.createElement("name");
					eName.setTextContent(person.getName());
					ePerson.appendChild(eName);
					
					Element eSurname = doc.createElement("surname");
					eSurname.setTextContent(person.getSurname());
					ePerson.appendChild(eSurname);
				}
				
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				StreamResult result = new StreamResult(os);
				transformer.transform(new DOMSource(root), result);
			} catch (ParserConfigurationException e) {
				new RuntimeException("DOM serialization error!", e);
			} catch (TransformerConfigurationException e) {
				new RuntimeException("DOM serialization error!", e);
			} catch (TransformerFactoryConfigurationError e) {
				new RuntimeException("DOM serialization error!", e);
			} catch (TransformerException e) {
				new RuntimeException("DOM serialization error!", e);
			}
		}
	}
	
	private class XmlPullListener extends ButtonListener {

		protected XmlPullListener(String tag) {
			super(tag);
		}

		@Override
		protected void create(People people, OutputStream os) {
			try {
				XmlSerializer serializer = Xml.newSerializer();
				String encoding = "UTF-8";
				serializer.setOutput(os, encoding);
				serializer.startDocument(encoding, true);
				
				serializer.startTag("", "people");
				
				for(Person person : people.getPeople()) {
					serializer.startTag("", "person");
					serializer.attribute("", "gender", person.getGender().getValue());
					
					serializer.startTag("", "name");
					serializer.text(person.getName());
					serializer.endTag("", "name");
					
					serializer.startTag("", "surname");
					serializer.text(person.getSurname());
					serializer.endTag("", "surname");

					serializer.endTag("", "person");
				}
				
				serializer.endTag("", "people");
				
				serializer.endDocument();
				serializer.flush();
				os.close();
			} catch (IllegalArgumentException e) {
				new RuntimeException("XmlPull serialization error!", e);
			} catch (IllegalStateException e) {
				new RuntimeException("XmlPull serialization error!", e);
			} catch (IOException e) {
				new RuntimeException("XmlPull serialization error!", e);
			}
		}
	}
	
	private class XPAListener extends ButtonListener {

		private XmlContext xmlContext;
		
		protected XPAListener(String tag, XmlContext xmlContext) {
			super(tag);
			
			this.xmlContext = xmlContext;
		}

		@Override
		protected void create(People people, OutputStream os) {
			try {
				Serializer serializer = this.xmlContext.createSerializer();
				serializer.serialize(people, os);
			} catch (SerializationException e) {
				new RuntimeException("XPA serialization error!", e);
			} catch (IOException e) {
				new RuntimeException("XPA serialization error!", e);
			}
		}
		
	}
	
	@Override
	public ApplicationContext getApplicationContext() {
		return (ApplicationContext) super.getApplicationContext();
	}
}

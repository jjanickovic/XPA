package org.xpa.example.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xpa.example.ApplicationContext;
import org.xpa.example.R;
import org.xpa.example.data.people.People;
import org.xpa.example.data.people.Person;
import org.xpa.example.data.people.Person.Gender;
import org.xpa.example.util.Result;
import org.xpa.example.util.ResultSet;
import org.xpaframework.Deserializer;
import org.xpaframework.MappingException;
import org.xpaframework.xml.XmlContext;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.util.Xml.Encoding;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class XmlDeserializationActivity extends Activity {

	private final String TAG = XmlDeserializationActivity.class.getSimpleName();

	private Button dom, sax, xmlPull, xpa;
	private Spinner type, repeatCount;
	
	private XmlContext xmlContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xml_deserialization);

		this.xmlContext = getApplicationContext().getXmlContext();
		
		this.dom = (Button) findViewById(R.id.button_xml_deserialization_dom);
		this.sax = (Button) findViewById(R.id.button_xml_deserialization_sax);
		this.xmlPull = (Button) findViewById(R.id.button_xml_deserialization_xml_pull);
		this.xpa = (Button) findViewById(R.id.button_xml_deserialization_xpa);
		
		this.type = (Spinner) findViewById(R.id.spinner_xml_type);
		this.repeatCount = (Spinner) findViewById(R.id.spinner_xml_repeats);

		this.dom.setOnClickListener(new ButtonListener("DOM") {

			@Override
			List<Person> getData(InputStream input) {
				return parseDom(input);
			}
		});

		this.sax.setOnClickListener(new ButtonListener("SAX") {

			@Override
			List<Person> getData(InputStream input) {
				return parseSax(input);
			}
		});

		this.xmlPull.setOnClickListener(new ButtonListener("XML Pull Parser") {

			@Override
			List<Person> getData(InputStream input) {
				return parseXmlPull(input);
			}
		});
		
		this.xpa.setOnClickListener(new ButtonListener("XPA") {

			@Override
			List<Person> getData(InputStream input) {
				return parseXpa(input);
			}
			
		});
		
	}

	private abstract class ButtonListener implements View.OnClickListener {

		private String tag;
		
		private ButtonListener(String tag) {
			this.tag = tag;
		}
		
		abstract List<Person> getData(InputStream input);

		@Override
		public void onClick(View v) {
			Toast.makeText(XmlDeserializationActivity.this,
					R.string.msg_starting, Toast.LENGTH_LONG).show();
			v.setEnabled(false);
			int repeats = getRepeats();
			List<Long> durationList = new ArrayList<Long>();

			for(int i = 0; i < repeats; i++) {
				Long start = System.currentTimeMillis();
				InputStream input = getResources().openRawResource(getXmlResourceId());
				People people = new People();
				people.setPeople(getData(input));
				
				long diff = System.currentTimeMillis() - start;
				durationList.add(diff);
				Log.i(TAG, String.format("[%s-run %d] %d items in %s ms.",
						this.tag, (i+1), people.getCount(), diff));
				
				if(i == (repeats - 1)) {
					break;
				}
				
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
			v.setEnabled(true);
			
			ResultSet resultSet = Result.createResult(durationList);
			Intent intent = new Intent(XmlDeserializationActivity.this,
					PerformanceResultActivity.class);
			intent.putExtra(ResultSet.KEY, resultSet);
			startActivity(intent);
		}

		private int getXmlResourceId() {
			return type.getSelectedItemPosition() == 0 ?
					R.raw.people_small :  R.raw.people_big;
		}
		
		private int getRepeats() {
			String item = repeatCount.getSelectedItem().toString();
			return Integer.valueOf(item);
		}
		
	}
	
	private List<Person> parseDom(InputStream input) {
		List<Person> people = new ArrayList<Person>();

		try {
			DocumentBuilderFactory documentBuilderFactory =
					DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder =
					documentBuilderFactory.newDocumentBuilder();


			Document document = documentBuilder.parse(input);

			NodeList nodeList = document.getElementsByTagName("person");


			for(int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if(node.getNodeType() == Node.ELEMENT_NODE) {

					Element e = (Element) node;
					Person person = new Person();
					String attributeValue = e.getAttribute("gender");
					Gender gender = Gender.fromValue(attributeValue);
					person.setGender(gender);

					Node name = e.getElementsByTagName("name").item(0);
					person.setName(name.getTextContent());
					Node surname = e.getElementsByTagName("surname").item(0);
					person.setSurname(surname.getTextContent());

					people.add(person);
				}

			}

			return people;
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Person> parseSax(InputStream input) {

		try {
			SaxHandler handler = new SaxHandler();
			Xml.parse(input, Encoding.UTF_8, handler);
			return handler.getValue().getPeople();
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
	}

	private class SaxHandler extends DefaultHandler {

		private boolean isName, isSurname;
		private People people;
		private Person person;

		private int count = 0;

		@Override
		public void startDocument() throws SAXException {
			this.people = new People();
		}

		@Override
		public void startElement(String uri, String localName,
				String qName, Attributes attributes) throws
				SAXException {

			if("person".equals(localName)) {
				this.person = new Person();
				String s = attributes.getValue("gender");
				Gender gender = Gender.fromValue(s);
				this.person.setGender(gender);
			}

			this.isName = "name".equals(localName);
			this.isSurname = "surname".equals(localName);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {

			if("person".equals(localName)) {
				this.people.getPeople().add(this.person);
				this.count++;//counting only
			}
		}

		@Override
		public void endDocument() throws SAXException {
			Log.i(TAG, "XML contains " +this.count + "people.");
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {

			String s = new String(ch, start, length).trim();

			if(s.isEmpty()) {
				return;
			}

			if(this.isName) {
				this.person.setName(s);
			} else if(this.isSurname) {
				this.person.setSurname(s);
			}
		}

		public People getValue() {
			return this.people;
		}
	}

	private List<Person> parseXmlPull(InputStream input) {
		List<Person> people = new ArrayList<Person>();
		XmlPullParser p = Xml.newPullParser();

		try {
			p.setInput(input, null);
			int eventType = p.getEventType();
			Person person = null;

			while(eventType != XmlPullParser.END_DOCUMENT) {
				String elem = p.getName();

				switch (eventType) {
				case XmlPullParser.START_TAG:
					if("person".equalsIgnoreCase(elem)) {
						person = new Person();
						String s = p.getAttributeValue(null, "gender");
						person.setGender(Gender.fromValue(s));
					} else if("name".equalsIgnoreCase(elem)) {
						person.setName(p.nextText());
					} else if("surname".equalsIgnoreCase(elem)) {
						person.setSurname(p.nextText());
					}

					break;
				case XmlPullParser.END_TAG:
					if("person".equalsIgnoreCase(elem)) {
						people.add(person);
					}

					break;

				default:
					break;
				}

				eventType = p.next();
			}
		} catch (XmlPullParserException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return people;
	}
	
	private List<Person> parseXpa(InputStream input) {
		try {
			Deserializer<People> deserializer = this.xmlContext.createDeserializer(People.class);
			deserializer.deserialize(input);
			People people = deserializer.getValue();
			
			return people.getPeople();
		} catch (NullPointerException e) {
			throw new RuntimeException(e);
		} catch (MappingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ApplicationContext getApplicationContext() {
		return (ApplicationContext) super.getApplicationContext();
	}
}

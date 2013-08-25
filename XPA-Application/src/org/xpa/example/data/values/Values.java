package org.xpa.example.data.values;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.xpaframework.xml.annotation.XmlRootElement;

@XmlRootElement
public class Values {

	private Primitives primitives;
	private Objects objects;
	
	public Primitives getPrimitives() {
		return primitives;
	}

	public void setPrimitives(Primitives primitives) {
		this.primitives = primitives;
	}

	public Objects getObjects() {
		return objects;
	}

	public void setObjects(Objects objects) {
		this.objects = objects;
	}

	/**
	 * Used only for initial data creation.
	 */
	@Deprecated
	public static Values createValues() throws DatatypeConfigurationException, MalformedURLException {
		Objects o = new Objects();
		o.setBigDecimal(new BigDecimal("12.00"));
		o.setBigInteger(new BigInteger("145619465982"));
		o.setDuration(DatatypeFactory.newInstance().newDuration(916594));
		o.setGregorianCalendar(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(2013, 1, 1, 0));
		o.setOBoolean(Boolean.TRUE);
		o.setOByte(Byte.decode("0x42"));	//represents 'B' character
		o.setOChar('C');
		o.setODouble(123456.789);
		o.setOFloat(123456.78f);
		o.setOInteger(123);
		o.setOLong(9876543210L);
		o.setOShort((short) -100);
		o.setQName(new QName("http://www.namespace.sk", "ns"));
		o.setString("string value, etc.");
		o.setUrl(new URL("http://www.android.com"));
		
		Primitives p = new Primitives();
		p.setPrimitiveBoolean(false);
		p.setPrimitiveByte(Byte.decode("0x62"));
		p.setPrimitiveChar('c');
		p.setPrimitiveDouble(123456.789);
		p.setPrimitiveFloat(123456.78f);
		p.setPrimitiveInt(123);
		p.setPrimitiveLong(92846528347L);
		p.setPrimitiveShort((short) 12);
		
		Values values = new Values();
		values.setObjects(o);
		values.setPrimitives(p);
		return values;
	}
	
}

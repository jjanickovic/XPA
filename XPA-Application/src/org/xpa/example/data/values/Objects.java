package org.xpa.example.data.values;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.xpaframework.xml.annotation.XmlElement;


public class Objects {

	@XmlElement(name = "big-decimal")
	private BigDecimal bigDecimal;
	@XmlElement(name = "big-integer")
	private BigInteger bigInteger;
	@XmlElement(name = "boolean")
	private Boolean oBoolean;
	@XmlElement(name = "byte")
	private Byte oByte;
	@XmlElement(name = "char")
	private Character oChar;
	@XmlElement(name = "double")
	private Double oDouble;
	private Duration duration;
	@XmlElement(name = "float")
	private Float oFloat;
	@XmlElement(name = "calendar")
	private XMLGregorianCalendar gregorianCalendar;
	@XmlElement(name = "int")
	private Integer oInteger;
	@XmlElement(name = "long")
	private Long oLong;
	@XmlElement(name = "q-name")
	private QName qName;
	@XmlElement(name = "short")
	private Short oShort;
	private String string;
	private URL url;

	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}

	public void setBigDecimal(BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}

	public BigInteger getBigInteger() {
		return bigInteger;
	}

	public void setBigInteger(BigInteger bigInteger) {
		this.bigInteger = bigInteger;
	}

	public Boolean getOBoolean() {
		return oBoolean;
	}

	public void setOBoolean(Boolean oBoolean) {
		this.oBoolean = oBoolean;
	}

	public Byte getOByte() {
		return oByte;
	}

	public void setOByte(Byte oByte) {
		this.oByte = oByte;
	}

	public Character getOChar() {
		return oChar;
	}

	public void setOChar(Character oChar) {
		this.oChar = oChar;
	}

	public Double getODouble() {
		return oDouble;
	}

	public void setODouble(Double oDouble) {
		this.oDouble = oDouble;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public Float getOFloat() {
		return oFloat;
	}

	public void setOFloat(Float oFloat) {
		this.oFloat = oFloat;
	}

	public XMLGregorianCalendar getGregorianCalendar() {
		return gregorianCalendar;
	}

	public void setGregorianCalendar(XMLGregorianCalendar gregorianCalendar) {
		this.gregorianCalendar = gregorianCalendar;
	}

	public Integer getOInteger() {
		return oInteger;
	}

	public void setOInteger(Integer oInteger) {
		this.oInteger = oInteger;
	}

	public Long getOLong() {
		return oLong;
	}

	public void setOLong(Long oLong) {
		this.oLong = oLong;
	}

	public QName getQName() {
		return qName;
	}

	public void setQName(QName qName) {
		this.qName = qName;
	}

	public Short getOShort() {
		return oShort;
	}

	public void setOShort(Short oShort) {
		this.oShort = oShort;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
}


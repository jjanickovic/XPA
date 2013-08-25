package org.xpa.example.data.values;

import org.xpaframework.xml.annotation.XmlElement;


public class Primitives {
	
	@XmlElement(name = "boolean")
	private boolean primitiveBoolean;
	@XmlElement(name = "byte")
	private byte primitiveByte;
	@XmlElement(name = "char")
	private char primitiveChar;
	@XmlElement(name = "short")
	private short primitiveShort;
	@XmlElement(name = "int")
	private int primitiveInt;
	@XmlElement(name = "long")
	private long primitiveLong;
	@XmlElement(name = "float")
	private float primitiveFloat;
	@XmlElement(name = "double")
	private double primitiveDouble;
	
	public boolean isPrimitiveBoolean() {
		return primitiveBoolean;
	}
	
	@Deprecated //for XP-API only
	public boolean getPrimitiveBoolean() {
		return primitiveBoolean;
	}
	
	public void setPrimitiveBoolean(boolean primitiveBoolean) {
		this.primitiveBoolean = primitiveBoolean;
	}
	
	public byte getPrimitiveByte() {
		return primitiveByte;
	}
	
	public void setPrimitiveByte(byte primitiveByte) {
		this.primitiveByte = primitiveByte;
	}
	
	public char getPrimitiveChar() {
		return primitiveChar;
	}
	
	public void setPrimitiveChar(char primitiveChar) {
		this.primitiveChar = primitiveChar;
	}
	
	public short getPrimitiveShort() {
		return primitiveShort;
	}
	
	public void setPrimitiveShort(short primitiveShort) {
		this.primitiveShort = primitiveShort;
	}
	
	public int getPrimitiveInt() {
		return primitiveInt;
	}
	
	public void setPrimitiveInt(int primitiveInt) {
		this.primitiveInt = primitiveInt;
	}
	
	public long getPrimitiveLong() {
		return primitiveLong;
	}
	
	public void setPrimitiveLong(long primitiveLong) {
		this.primitiveLong = primitiveLong;
	}
	
	public float getPrimitiveFloat() {
		return primitiveFloat;
	}
	
	public void setPrimitiveFloat(float primitiveFloat) {
		this.primitiveFloat = primitiveFloat;
	}
	
	public double getPrimitiveDouble() {
		return primitiveDouble;
	}
	
	public void setPrimitiveDouble(double primitiveDouble) {
		this.primitiveDouble = primitiveDouble;
	}
	
}


package org.xpa.example.data.orders;

import java.math.BigInteger;

import org.xpaframework.xml.annotation.XmlAttribute;
import org.xpaframework.xml.annotation.XmlElement;
import org.xpaframework.xml.annotation.XmlType;

@XmlType(order = {"name", "count", "price"})
public class Product {

//	@XmlElement
    private String name;
	
//	@XmlElement
    private int count;
	
	@XmlElement
    private Price price;
	
	@XmlAttribute
    private BigInteger id;
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Price getPrice() {
		return price;
	}
	public void setPrice(Price price) {
		this.price = price;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}

}

package org.xpa.example.data.orders;

import java.math.BigInteger;

import org.xpaframework.xml.annotation.XmlAncestor;
import org.xpaframework.xml.annotation.XmlAttribute;
import org.xpaframework.xml.annotation.XmlElement;
import org.xpaframework.xml.annotation.XmlType;

@XmlAncestor(inheritanceTypes = {Person.class, Company.class})
@XmlType(order = {"address"})
public abstract class Client {

	@XmlElement
    private Address address;
	
	@XmlAttribute
    private BigInteger id;
    
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}

}

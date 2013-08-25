package org.xpa.example.data.orders;

import org.xpaframework.xml.annotation.XmlElement;


public class Address {

	@XmlElement
    private String country;
	
	@XmlElement
    private String province;
	
	@XmlElement
    private String city;
	
	@XmlElement
    private String street;
	
	@XmlElement(name = "street-number")
    private String streetNumber;
	
	@XmlElement
    private String zip;
	
    public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

}

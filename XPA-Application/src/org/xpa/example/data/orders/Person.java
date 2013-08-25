package org.xpa.example.data.orders;

import org.xpaframework.xml.annotation.XmlElement;
import org.xpaframework.xml.annotation.XmlType;


@XmlType(name = "person", order={"name", "surname"})
public class Person extends Client {

	@XmlElement
    private String name;
	@XmlElement
    private String surname;
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

}

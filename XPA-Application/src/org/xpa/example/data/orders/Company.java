package org.xpa.example.data.orders;

import org.xpaframework.xml.annotation.XmlElement;
import org.xpaframework.xml.annotation.XmlType;


@XmlType(name = "company", order={"name", "ico"})
public class Company extends Client {

	@XmlElement
    private String name;
	
	@XmlElement
    private String ico;
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIco() {
		return ico;
	}
	public void setIco(String ico) {
		this.ico = ico;
	}

}

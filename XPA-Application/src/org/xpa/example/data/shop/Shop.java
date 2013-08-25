package org.xpa.example.data.shop;

import java.util.ArrayList;
import java.util.List;

import org.xpaframework.xml.annotation.XmlElement;
import org.xpaframework.xml.annotation.XmlRootElement;

@XmlRootElement(name = "shop")
public class Shop {

	@XmlElement(name="category")
	private List<Category> categoriers = new ArrayList<Category>();

	public List<Category> getCategoriers() {
		return categoriers;
	}

	public void setCategoriers(List<Category> categoriers) {
		this.categoriers = categoriers;
	}
}

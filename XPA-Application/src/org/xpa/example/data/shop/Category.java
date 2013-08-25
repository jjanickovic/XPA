package org.xpa.example.data.shop;

import java.util.ArrayList;
import java.util.List;

import org.xpaframework.xml.annotation.XmlAttribute;
import org.xpaframework.xml.annotation.XmlElement;

public class Category {

	@XmlAttribute
	private String name;
	
	@XmlElement(name="category")
	private List<Category> subCategories;
	
	@XmlElement(name="product")
	private List<Product> products;
	
	public Category() {
		this.subCategories = new ArrayList<Category>();
		this.products = new ArrayList<Product>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<Category> subCategories) {
		this.subCategories = subCategories;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}

package org.xpa.example.data.orders;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.xpaframework.xml.annotation.XmlAttribute;
import org.xpaframework.xml.annotation.XmlType;

@XmlType(order = {"client", "product"})
public class Order {

	private Client client;

    private List<Product> product = new ArrayList<Product>();
	
	@XmlAttribute(name = "order-id")
    private BigInteger orderId;
	
	@XmlAttribute(name = "order-date")
    private XMLGregorianCalendar orderDate;
	
    public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public List<Product> getProduct() {
		return product;
	}
	public void setProduct(List<Product> product) {
		this.product = product;
	}
	public BigInteger getOrderId() {
		return orderId;
	}
	public void setOrderId(BigInteger orderId) {
		this.orderId = orderId;
	}
	public XMLGregorianCalendar getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(XMLGregorianCalendar orderDate) {
		this.orderDate = orderDate;
	}

}

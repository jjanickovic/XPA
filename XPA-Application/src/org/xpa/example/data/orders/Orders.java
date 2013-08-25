package org.xpa.example.data.orders;

import java.util.ArrayList;
import java.util.List;

import org.xpaframework.xml.annotation.XmlElement;
import org.xpaframework.xml.annotation.XmlNamespace;
import org.xpaframework.xml.annotation.XmlRootElement;

@XmlRootElement
@XmlNamespace("http://www.graham.sk/orders")
public class Orders {

	@XmlElement
    private List<Order> order = new ArrayList<Order>();

	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

}

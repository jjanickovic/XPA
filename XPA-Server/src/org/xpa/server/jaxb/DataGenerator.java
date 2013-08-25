package org.xpa.server.jaxb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.xpa.server.jaxb.orders.Address;
import org.xpa.server.jaxb.orders.Company;
import org.xpa.server.jaxb.orders.ObjectFactory;
import org.xpa.server.jaxb.orders.Order;
import org.xpa.server.jaxb.orders.Orders;
import org.xpa.server.jaxb.orders.Person;
import org.xpa.server.jaxb.orders.PriceType;
import org.xpa.server.jaxb.orders.Product;


public class DataGenerator {

	private static ObjectFactory objectFactory = new ObjectFactory();
	
	public Orders generate() {
		Orders orders = objectFactory.createOrders();
		
		for(int i = 0; i < 3; i++) {
			orders.getOrder().add(createOrder());
		}
		
		return orders;
	}
	
	private Order createOrder() {
		Order order = objectFactory.createOrder();
		order.setOrderId(randomId());
		order.setOrderDate(createDate());
		
		Boolean random = new Random().nextBoolean();
		order.setClient(random ? createCompany() : createPerson());
		
		//int range = new Random().nextInt(12) + 1;
		int range = 2;
		for(int i = 0; i < range; i++) {
			order.getProduct().add(createProduct());
		}
		
		return order;
	}

	private Company createCompany() {
		Company company = objectFactory.createCompany();
		company.setName("Company s.r.o.");
		company.setIco("ICO-4856389");
		company.setId(new BigInteger("11023010"));
		company.setAddress(createAddress());
		return company;
	}
	
	private Person createPerson() {
		Person person = objectFactory.createPerson();
		person.setId(randomId());
		person.setAddress(createAddress());
		person.setName("Meno");
		person.setSurname("Priezvisko");
		return person;
	}
	
	private Address createAddress() {
		Address address = objectFactory.createAddress();
		address.setCountry("Slovenska republika");
		address.setProvince(null);
		address.setCity("Bratislava");
		address.setStreet("Dolnozemska cesta");
		address.setStreetNumber("1/A");
		address.setZip("98765");
		return address;
	}
	
	private Product createProduct() {
		Product product = objectFactory.createProduct();
		product.setCount(new Random().nextInt(6) + 1);
		product.setId(randomId());
		product.setName("Produkt - nazov");
		product.setPrice(randomPrice());
		
		return product;
	}

	private XMLGregorianCalendar createDate() {
		try {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String[] currency = {"EUR", "USD", "GBP"};
	
	private PriceType randomPrice() {
		PriceType price = objectFactory.createPriceType();
		BigDecimal value = new BigDecimal(new Random().nextDouble() * 1000);
		value = value.setScale(2, RoundingMode.HALF_UP);
		
		price.setValue(value);
		price.setCurrency(this.currency[new Random().nextInt(currency.length)]);
		return price;
	}

	private BigInteger randomId() {
		int id = new Random().nextInt(100000) + 100000;
		return new BigInteger(id + "");
	}
	
}

package org.xpa.server.jaxb;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.xpa.server.jaxb.orders.Orders;


public class TestRunner {

	public void execute(DataGenerator generator) throws JAXBException {
		Orders orders = generator.generate();
		
		JAXBContext jaxbContext = JAXBContext.newInstance("sk.graham.shared.jaxb.data");
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(orders, new File("xml-output.xml"));
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new TestRunner().execute(new DataGenerator());
	}

}

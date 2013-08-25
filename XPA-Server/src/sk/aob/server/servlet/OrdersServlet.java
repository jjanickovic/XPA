package sk.aob.server.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.xpa.server.jaxb.DataGenerator;
import org.xpa.server.jaxb.orders.Orders;


/**
 * Servlet implementation class OrdersServlet
 */
@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(getClass().getSimpleName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrdersServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("OrdersServlet.doGet()");
        
        try {
        	OutputStream output = response.getOutputStream();
			execute(new DataGenerator(), output);
			response.setContentType("application/xml");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("OrdersServlet.doPost()");
		logger.info("Content-type: " + request.getContentType());
		
		StringWriter stringWriter = new StringWriter();
		IOUtils.copy(request.getInputStream(), stringWriter);
		
		logger.info("Content:");
		logger.info(stringWriter.toString());
	}

	private void execute(DataGenerator generator, OutputStream output) throws JAXBException {
		Orders orders = generator.generate();
		
		JAXBContext jaxbContext = JAXBContext.newInstance("org.xpa.server.jaxb.orders");
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(orders, output);
	}
}

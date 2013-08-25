package sk.aob.server.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * Servlet implementation class ValuesServlet
 */
@WebServlet("/values")
public class ValuesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(getClass().getSimpleName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ValuesServlet() {
        super();
        logger.setLevel(Level.FINE);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.warning("ValuesServlet.doGet() not supported!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("ValuesServlet.doPost()");
		logger.info("Content-type: " + request.getContentType());
		
		StringWriter stringWriter = new StringWriter();
		IOUtils.copy(request.getInputStream(), stringWriter);
		
		logger.info("Content:");
		logger.info(stringWriter.toString());

	}

}

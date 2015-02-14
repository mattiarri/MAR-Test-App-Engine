package com.mar.it;

import java.io.IOException; import javax.servlet.http.*;
import java.util.logging.Logger;

public class LoggingServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(LoggingServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	log.finest("finest level");
	log.finer("finer level");
	log.fine("fine level");
	log.config("config level");
	log.info("info level");
	log.warning("warning level");
	log.severe("severe level");

	System.out.println("stdout level"); // INFO
	System.err.println("stderr level"); // WARNING 
	}
}
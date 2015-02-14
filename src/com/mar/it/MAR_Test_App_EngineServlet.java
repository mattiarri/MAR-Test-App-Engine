package com.mar.it;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class MAR_Test_App_EngineServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		final String runNumGenPage = "runNumGen.html";
		final String loginNoGoogle = "login.html";

	        
			UserService userService = UserServiceFactory.getUserService();
	        String thisURL = req.getRequestURI();
			User user = userService.getCurrentUser();
			String pathVar = getServletContext().getRealPath("css/")+"style.css";
			PrintWriter out=resp.getWriter();
			out.println("<link rel='stylesheet' type='text/css' href= '" + pathVar + "' />");
			out.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css'>");
			out.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css'>");
			
	 
			resp.setContentType("text/html");
			out.println("<div class='well' style='text-align:center'> <h1>LOGIN tramite Google Account <img src='images/googleLogo.jpg' class='img-circle'></h1></div>");
	 
			if (user != null) {
				out.println("<h3><p style='color:#5E86C1;'>Benvenuto, " + user.getNickname() + "</p></h3>");				
				out.println(
						"<h1><a class='btn btn-success' type='submit' href='"
						+ runNumGenPage
						+ "'> Accedi alla pagina di lancio <span class='glyphicon glyphicon-stats' aria-hidden='true'></span></a></h1>");
				
				out.println(
						"<a class='btn btn-danger' type='submit' href='"
							+ userService.createLogoutURL(thisURL)
							+ "'> LogOut <span class='glyphicon glyphicon-off' aria-hidden='true'></span></a>");

			} else {

				out.println("<p><a class='btn btn-success' type='submit' href='"
						+ userService.createLoginURL(thisURL)
						+ "'>Prosegui con il login</a></p>");
				
				out.println(
						"<p> <a class='btn btn-warning' type='submit' href='"
						+ loginNoGoogle
						+ "'> Effettua il Login senza un Google Account</a></p>");
				
				out.println();
	 
			}
	}
}

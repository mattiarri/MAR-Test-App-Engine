package com.mar.it;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class RandomNumGeneration extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 348972690265422682L;
	public static long lStartTime = 0; // start time
	public static long lEndTime = 0; //end time
	public static String s_user = "";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		lStartTime = new Date().getTime(); // update the start time
		ArrayList<Integer> allNumbers = new ArrayList<Integer>();	
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    s_user = "";
	    if(user!=null){
	    	s_user = user.toString();
	    }else{
	    	s_user = req.getParameter("username");
	    }

		String strCallResult = "";
		resp.setContentType("text/plain");
		try {
			Queue myQueue = QueueFactory.getQueue("default");
			DatastoreService myDatastore = DatastoreServiceFactory.getDatastoreService();			
			Key k_user = KeyFactory.createKey("utente", s_user);
			Entity e_user = new Entity(k_user);
			e_user.setProperty("login", s_user);
			myDatastore.put(e_user);
			for(int x=1; x<=100; x++){
				String s_key = x+""+s_user;
				myQueue.add(TaskOptions.Builder.withUrl("/generatebatch").param("chiaveInc", s_key));
				resp.getWriter().println(strCallResult);
			}
		
		}
		catch (Exception ex) {
			strCallResult = "Fail: " + ex.getMessage();
			resp.getWriter().println(strCallResult);
		}		
			RequestDispatcher rd = req.getRequestDispatcher("/graph.html");
			req.setAttribute("allNumbers", allNumbers);
	        rd.forward(req, resp);		
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
	doGet(req, resp);
	}

}

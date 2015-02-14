package com.mar.it;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class GenerateBatch extends HttpServlet {
	private static final Logger log = Logger.getLogger(GenerateBatch.class.getName());
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	
	String strCallResult = "";
	resp.setContentType("text/plain");
	resp.getWriter().println("Creo task");
	
	ArrayList<Integer> theArray = new ArrayList<Integer>();
	for(int i=0; i<=999; i++){
		theArray.add(randInt(0,50));
	}
	DatastoreService myDataStore = DatastoreServiceFactory.getDatastoreService();
	String[] chiave = req.getParameterValues("chiaveInc");
	
	Key k_numList = KeyFactory.createKey("GeneratedNumbers", chiave[0]);
	Entity numList = new Entity(k_numList);
	numList.setProperty("myIntArray", theArray);	
	String s_numListKey = KeyFactory.keyToString(k_numList);
	myDataStore.put(numList);
	System.out.println("<p>Created an entity with a system ID, key: " +
			s_numListKey + "</p>");
	try{
		strCallResult = "SUCCESS: task done";
	}
	catch(Exception ex){
		strCallResult = "FAIL: task not created : " + ex.getMessage();
	}

	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
	doGet(req, resp);
	}
	
	//NUMERI CASUALI DEVIAZIONE NORMALE
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

}
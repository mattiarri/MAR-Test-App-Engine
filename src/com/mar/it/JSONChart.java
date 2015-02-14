package com.mar.it;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.*;



public class JSONChart extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2817830478431882870L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		ArrayList<Long> allNumbers = new ArrayList<Long>();
		HashMap<Long,Long> hm_occurances = new HashMap<Long,Long>();
		DatastoreService myDatastore = DatastoreServiceFactory.getDatastoreService();
		Entity e_user = null;
		try {
			Key k_user = KeyFactory.createKey("utente", RandomNumGeneration.s_user);
			e_user = myDatastore.get(k_user);
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String s_user = (String) e_user.getProperty("login");
		for(int x=1; x<=100; x++){
			String s_key = x+""+s_user.toString();
			Entity numList;
			Key k_numList = KeyFactory.createKey("GeneratedNumbers", s_key);
			try {
				numList = myDatastore.get(k_numList);
				ArrayList<Long> theArray = (ArrayList<Long>) numList.getProperty("myIntArray");
				theArray.size();
				allNumbers.addAll(theArray);
				System.out.println("tot array:" +allNumbers.size());
				allNumbers.get(0);

			}
			catch (EntityNotFoundException e) {
			    // Entities don't exists
				System.out.println("Entity with key "+s_key+ " Notfound");
			}
		}
		
		//calcolo l'hashmap dove K è il numero e V è quante volte si è ripetuto, poi lo converto in JSON
		for(int i=0; i<allNumbers.size(); i++){
			Long occorrenza = allNumbers.get(i);
			if(hm_occurances.containsKey(occorrenza)){
	    		hm_occurances.put(occorrenza,hm_occurances.get(occorrenza)+1);
	    	}else{
	    		hm_occurances.put(occorrenza,(long) 1);
	    	}
		}
		
		//valore di quanti numeri differenti
		int numeriDifferenti = hm_occurances.size();
		
		//array con i tre numeri con più occorrenze
		HashMap<Long, Long> hm_occurancesSorted = sortByValues(hm_occurances); 
	    Iterator it = hm_occurancesSorted.entrySet().iterator();
	    int contatore = 0;
	    HashMap<Long, Long> hm_topThree = new HashMap<Long,Long>();
		
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        hm_topThree.put((Long)pairs.getKey(),(Long)pairs.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	        contatore++;
	        if(contatore>2){
	        	break;
	        }
	    }
		
		//creo l'oggetto JSON e aggiunto le proprietà richieste
		JsonObject obj = new JsonObject();
		obj.addProperty("numeriDifferenti", numeriDifferenti);
		System.out.println(RandomNumGeneration.lStartTime);
		RandomNumGeneration.lEndTime = new Date().getTime();
		System.out.println(RandomNumGeneration.lEndTime );
		long executionTime = (RandomNumGeneration.lEndTime - RandomNumGeneration.lStartTime);
		obj.addProperty("tempo", executionTime);
		
		//definisco un nuovo oggetto Gson per le conversioni automatiche
		Gson gs = new Gson();

		JsonElement hmocc;
		hmocc = gs.toJsonTree(hm_topThree);
		obj.add("maggioriricorrenze", hmocc);

		JsonElement hmel;
		hmel = gs.toJsonTree(hm_occurances);
		obj.add("percentuali", hmel);

		resp.setContentType("application/json");
		resp.getWriter().write(obj.toString());

			
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
	doGet(req, resp);
	}
	
	//metodo generico per hm
	private static HashMap sortByValues(HashMap map) { 
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
			int result = ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			return result * -1;
		}
		});
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }

}

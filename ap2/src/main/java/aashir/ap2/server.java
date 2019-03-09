package aashir.ap2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
//import java.util.ArrayList;

import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.service.ServiceRegistry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class server {
	
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	private static Session currSession ;
	private static Transaction t;
	
	@SuppressWarnings({ "resource", "unchecked", "unused"})
	public static void main(String[] args) throws IOException {
		
		ServerSocket welcomeSocket = new ServerSocket(7025);
		
		Configuration cfg = new Configuration();
		cfg.configure();
		cfg.addAnnotatedClass(person.class);
		
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
		sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		currSession = sessionFactory.openSession();
		
		  while (true) {
			  
		  Socket connectionSocket = welcomeSocket.accept();
		  System.out.println("Server: Connection accepted");
		  BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
		  DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		  
		  
	      
		  String command = inFromClient.readLine();
	      int condition = 5;
	      String commands[] = {"post:","put:","delete:","get:"};
	      person person = new person();
	      
	      //register a person
	      if (command.startsWith(commands[0])) {
	    	  condition = 0;
	    	  command = command.substring(5,command.length());
	    	  person = person.convertPerson(command);
			    	  
	    	  List<person> list;
	    	  t = currSession.beginTransaction();
	    	  list = (List<person>)currSession.createSQLQuery("SELECT * FROM person").addEntity(person.class).list();
	    	  
	    	  boolean isThere = false;
	    	  
	    	  for(int i = 0; i < list.size() ; i++) {
	    		if( list.get(i).getUsername() == person.getUsername()) {
	    			isThere = true;
	    		}
	    	  }
	    	  System.out.println("Adding username: " + person.getUsername());
	    	  if(!isThere) {
	  			currSession.save(person);
	  			t.commit();
	  			if (currSession.getTransaction().getStatus() == TransactionStatus.COMMITTED)
	  			{
	  				System.out.println("Record successfully added.");
	  				outToClient.writeBytes("success :{" + person.getUsername() +"} sucessfully registered \n");
	  			}
	    	  }
	    	  else {
	    		  System.out.println("Username already registered");
	    		  outToClient.writeBytes("failed :{" + person.getUsername() +"} already registered \n");
	    	  }
	      }
	      
	      //update a person
	      else if (command.startsWith(commands[1])){
	    	  condition = 1;
	    	  command = command.substring(4,command.length());
	    	  person = person.convertPerson(command);
	    	  
	    	  person temp = new person();
	    	  System.out.println("Updating username: " + person.getUsername()+ "\n");
	    	  t = currSession.beginTransaction();
	    	  temp = currSession.get(person.class, person.getUsername());
	    	  //System.out.println(temp.getFirstName());
	    	  
	    	  if(temp != null) {
	    		  
	    		  	currSession.delete(temp);
	    		  	t.commit();
	    		  	t = currSession.beginTransaction();
	    		    currSession.saveOrUpdate(person);
		  			t.commit();
		  			if (currSession.getTransaction().getStatus() == TransactionStatus.COMMITTED)
		  			{
		  				System.out.println("Record successfully updated.");
		  				outToClient.writeBytes("success :{" + person.getUsername() +"} sucessfully updated \n");
		  			}
	    	  }
	    	  else {
		    		  System.out.println("Username already registered");
		    		  outToClient.writeBytes("failed :{" + person.getUsername() +"} already registered \n");
	    	  }
	    		  
	      }
	      
	      //delete a person
	      else if (command.startsWith(commands[2])){
	    	  condition = 2;
	    	  command = command.substring(7,command.length());
	    	  
	    	  
	    	  t = currSession.beginTransaction();
	    	  person temp = new person();
	    	  temp = currSession.get(person.class, command);
	    	  
	    	  System.out.println("Deleting: " + temp.getFirstName());

	    	  currSession.remove(temp);
	    	  t.commit();
	    	  
	    	  
	    	  if (currSession.getTransaction().getStatus() == TransactionStatus.COMMITTED)
	  			{
	  				System.out.println("Record successfully deleted.");
	  				outToClient.writeBytes("success :{" + temp.getUsername() +"} sucessfully deleted\n");
	  			}
	    	  else {
	    		  	System.out.println("Record deletion failure.");
	  				outToClient.writeBytes("fail :{" + temp.getUsername() +"} not deleted\n");
	    		  
	    	  }
	    	  
	      }
	      
	      
	      //retreive a person
	      else if (command.startsWith(commands[3])){
	    	  condition = 3;
	    	  command = command.substring(4,command.length());
	    	  
	    	  System.out.println("Getting Data of: "+command);
	    	  
	    	  t = currSession.beginTransaction();
	    	  person temp = currSession.get(person.class, command);
	    	  
	    	  System.out.println("user fetched: "+temp.getFirstName());

	    	 
	   		if (temp!= null)
	   		{
			
				String fetch = "success:" + temp.convertJson(temp);
				outToClient.writeBytes(temp.toString());
	   		}
	   		
	   		else 
	   			
	   		{
	   			outToClient.writeBytes("error : user {"+command+"} not found \n");
	   		}

	    	  
	      }
	      	        
		}//while

     }//main

}//class
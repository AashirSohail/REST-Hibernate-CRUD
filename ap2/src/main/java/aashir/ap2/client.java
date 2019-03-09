package aashir.ap2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

//import com.fasterxml.jackson.core.JsonProcessingException;


public class client {

	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) throws IOException {
		
		
		String Fname;
		String Lname; 
		String username;
		String password; 
		String mobile; 
		String access;  
		String reply;
		String jsonString;
		
		person person = null;
		
		//Socket connection request to server
		Socket clientSocket = new Socket ("127.0.0.1", 7025);
		
		//DataOutputStream to send data to sever
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
        
        //BUfferedReader to recieve data from server
        BufferedReader inFromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
		
		
		Scanner Input = new Scanner(System.in);
		
		int choice = 5;
		
		while (choice !=1 && choice !=2 && choice!=-1 && choice != 3 && choice != 4)
		{
			System.out.print("1 to Enter Record, 2 to Update Record\n3 to Delete Record, 4 to View a Record\nEnter -1 to exit:");
			choice = Input.nextInt();
			Input.nextLine(); 
		}
		
		while (choice != -1)
		{
		
		switch (choice)
		{
			
		case 1:
			person = new person ();
			
			System.out.print("First Name: ");
			person.setFirstName(Input.nextLine());
			System.out.print("Last Name: ");
			person.setLastName(Input.nextLine());
			System.out.print("Username: ");
			person.setUsername(Input.nextLine());
			System.out.print("Password: ");
			person.setPassword(Input.nextLine());
			System.out.print("Mobile: ");
			person.setMobile(Input.nextLine());
			System.out.print("Access Level: ");
			person.setAccessLevel(Input.nextLine());
			
			
			jsonString = "post:" + person.convertJson(person) + "\n";
			System.out.println(jsonString);
			outToServer.writeBytes(jsonString);
			reply = inFromServer.readLine();
			System.out.println("Client : " + reply + "\n");
			break;
			
		case 2:
			person = new person ();
			System.out.println("Enter username to update: ");
			person.setUsername(Input.nextLine());
			System.out.print("First Name: ");
			person.setFirstName(Input.nextLine());
			System.out.print("Last Name: ");
			person.setLastName(Input.nextLine());
			System.out.print("Password: ");
			person.setPassword(Input.nextLine());
			System.out.print("Mobile: ");
			person.setMobile(Input.nextLine());
			System.out.print("Access Level: ");
			person.setAccessLevel(Input.nextLine());
			
			
			jsonString = "put:" + person.convertJson(person) + "\n";
			System.out.println(jsonString);
			outToServer.writeBytes(jsonString);
			
			reply = inFromServer.readLine();
			System.out.println("Client : " + reply + "\n");
			break;
			
		case 3:
			System.out.println("Enter username to delete: ");
			username = Input.nextLine();
			jsonString = "delete:" + username;
			outToServer.writeBytes(jsonString);
			
			reply = inFromServer.readLine();
			System.out.println("Client : " + reply + "\n");

			break;
			
		case 4:
			System.out.println("Enter username to get Info: ");
			username = Input.nextLine();
			jsonString = "get:" + username;
			outToServer.writeBytes(jsonString);
			
			String fetch = inFromServer.readLine();
			if (fetch.startsWith("success:"))
			{
				fetch=fetch.substring(8, fetch.length());
				System.out.println(fetch);
				person = person.convertPerson(fetch);
				System.out.println("Client : " + fetch);
			}
			else 
			{
				System.out.println(fetch);
			}
			break;	
		}
			
		System.out.print("1 to Enter Record, 2 to Update Record\n3 to Delete Record, 4 to View a Record\nEnter -1 to exit:");
		choice = Input.nextInt();
		Input.nextLine(); 
		while (choice !=1 && choice !=2 && choice!=-1 && choice != 3 && choice != 4)
		{
			System.out.print("1 to Enter Record, 2 to Update Record\n3 to Delete Record, 4 to View a Record\nEnter -1 to exit:");
			choice = Input.nextInt();
			Input.nextLine(); 
		}	
		
		}
		 

	}

}
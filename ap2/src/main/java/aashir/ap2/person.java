package aashir.ap2;

import javax.persistence.*;
import javax.persistence.Table;

//import org.hibernate.annotations.GenericGenerator;

import java.io.IOException;
//import java.util.*;

//import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table (name="person")
public class person {
	@Id   
	private String username;
	@Column
	private String lastName;
	@Column
	private String mobile;
	@Column
	private String password;
	@Column
	private String accessLevel;
	@Column
	private String firstName;
	
	public person()
	{
		
	}
	
	@Override
	public String toString() {
		return "Username: " + this.username  + " FirstName: " + this.firstName +  " LastName: " + this.lastName + " Mobile: " + this.mobile + " Password: "
				+ this.password + " AccessLevel: " + this.accessLevel;
	}

	String convertJson (person person) throws JsonProcessingException
	{
		 ObjectMapper mapper = new ObjectMapper();
		 String jsonString = mapper.writeValueAsString(person);
		 //System.out.println(jsonString);
		 return jsonString;
	}
	
	person convertPerson (String jsonString) throws JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		person person = mapper.readValue(jsonString, person.class);
		//System.out.println(person.getAccessLevel());
		return person;
		
	}

	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	
	
	
}
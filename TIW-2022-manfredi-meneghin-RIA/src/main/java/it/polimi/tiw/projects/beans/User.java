package it.polimi.tiw.projects.beans;

/**
 * Java bean for the User information
 */
public class User {
	
	private int    id;
	private String name;
	private String surname;
	private String email;
	private String username;
	
	/**
	 * Getter for the field 'id'
	 * @return the user's id
	 */
	public int getId() { return id; }

	/**
	 * Getter for the field 'id'
	 * @return the user's id
	 */
	public void setId(int id) { this.id = id; }

	
	/**
	 * Getter for the field 'name'
	 * @return the user's name
	 */
	public String getName() { return name; }
	
	/**
	 * Setter for the field 'name'
	 * @param name the name to be set
	 */
	public void setName(String name) { this.name = name; }
	
	/**
	 * Getter for the field 'surname'
	 * @return the user's surname
	 */
	public String getSurname() { return surname; }
	
	/**
	 * Setter for the field 'surname'
	 * @param surname the surname to be set
	 */
	public void setSurname(String surname) { this.surname = surname; }
	
	/**
	 * Getter for the field 'email'
	 * @return the user's email
	 */
	public String getEmail() { return email; }
	
	/**
	 * Setter for the field 'email'
	 * @param email the email to be set
	 */
	public void setEmail(String email) { this.email = email; }
	
	/**
	 * Getter for the field 'username'
	 * @return the user's username
	 */
	public String getUsername() { return username; }
	
	/**
	 * Setter for the field 'username'
	 * @param username the username to be set
	 */
	public void setUsername(String username) { this.username = username; }
}
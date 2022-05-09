package it.polimi.tiw.projects.beans;

public class User {
	private String name;
	private String surname;
	private String username;
	private String password;
	
	public User(String name, String surname, String username, String password) {
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
	}
	
	/*
	 * Getter for the field 'name'
	 * @return the user's name
	 */
	public String getName() { return name; }
	
	/*
	 * Setter for the field 'name'
	 * @param name the name to be set
	 */
	public void setName(String name) { this.name = name; }
	
	/*
	 * Getter for the field 'surname'
	 * @return the user's surname
	 */
	public String getSurname() { return surname; }
	
	/*
	 * Setter for the field 'surname'
	 * @param surname the surname to be set
	 */
	public void setSurname(String surname) { this.surname = surname; }
	
	/*
	 * Getter for the field 'username'
	 * @return the user's username
	 */
	public String getUsername() { return username; }
	
	/*
	 * Setter for the field 'username'
	 * @param username the username to be set
	 */
	public void setUsername(String username) { this.username = username; }
	
	/*
	 * Getter for the field 'password'
	 * @return the user's password
	 */
	public String getPassword() { return password; }
	
	/*
	 * Setter for the field 'password'
	 * @param password the password to be set
	 */
	public void setPassword(String password) { this.password = password; }
}

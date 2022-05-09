package it.polimi.tiw.projects.beans;

public class Account {
	private int code;
	private String username;
	private int balance;
	
	public Account(int code, String username, int balance) { 
		this.code = code;
		this.username = username;
		this.balance = balance;
	}
	
	/*
	 * Getter for the field 'code'
	 * @return the account's code 
	 */
	public int getCode() { return code; }
	
	/*
	 * Setter for the field 'code'
	 * @param code the code to be set
	 */
	public void setCode(int code) { this.code = code; }
	
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
	 * Getter for the field 'balance'
	 * @return the account's balance 
	 */
	public int getBalance() { return balance; }
	
	/*
	 * Setter for the field 'balance'
	 * @param balance the balance to be set
	 */
	public void setBalance(int balance) { this.balance = balance; }
}
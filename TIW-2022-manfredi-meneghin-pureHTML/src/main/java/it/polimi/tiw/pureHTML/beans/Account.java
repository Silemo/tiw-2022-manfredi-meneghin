package it.polimi.tiw.pureHTML.beans;

import java.math.BigDecimal;

/**
 * Java bean for the Account information
 */
public class Account {
	
	private int        code;
	private int        user_id;
	private BigDecimal balance;

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
	 * Getter for the field 'user_id'
	 * @return the account's user_id 
	 */
	public int getUserId() { return user_id; }
	
	/*
	 * Setter for the field 'user_id'
	 * @param user_id the user_id to be set
	 */
	public void setUserId(int user_id) { this.user_id = user_id; }
	
	/*
	 * Getter for the field 'balance'
	 * @return the account's balance 
	 */
	public BigDecimal getBalance() { return balance; }
	
	/*
	 * Setter for the field 'balance'
	 * @param balance the balance to be set
	 */
	public void setBalance(BigDecimal balance) { this.balance = balance; }
}

package it.polimi.tiw.projects.beans;

import java.sql.Date;

public class Transfer {
	private int id;
	private Date date;
	private int amount;
	private String reason;
	private String orderer;
	private String beneficiary;
	
	/*
	 * Getter for the field 'id'
	 * @return the transfer's id 
	 */
	public int getId() { return id; }
	
	/*
	 * Setter for the field 'id'
	 * @param id the id to be set
	 */
	public void setId(int id) { this.id = id; }
	
	/*
	 * Getter for the field 'date'
	 * @return the transfer's date 
	 */
	public Date getDate() { return date; }
	
	/*
	 * Setter for the field 'date'
	 * @param date the date to be set
	 */
	public void setDate(Date date) { this.date = date; }
	
	/*
	 * Getter for the field 'amount'
	 * @return the transfer's amount
	 */
	public int getAmount() { return amount; }
	
	/*
	 * Setter for the field 'amount'
	 * @param amount the amount to be set
	 */
	public void setAmount(int amount) { this.amount = amount; }
	
	/*
	 * Getter for the field 'reason'
	 * @return the transfer's reason
	 */
	public String getReason() { return reason; }
	
	/*
	 * Setter for the field 'reason'
	 * @param reason the reason to be set
	 */
	public void setReason(String reason) { this.reason = reason; }
	
	/*
	 * Getter for the field 'orderer'
	 * @return the transfer's orderer
	 */
	public String getOrderer() { return orderer; }
	
	/*
	 * Setter for the field 'orderer'
	 * @param orderer the orderer to be set
	 */
	public void setOrderer(String orderer) { this.orderer = orderer; }
	
	/*
	 * Getter for the field 'beneficiary'
	 * @return the transfer's beneficiary
	 */
	public String getBeneficiary() { return beneficiary; }
	
	/*
	 * Setter for the field 'beneficiary'
	 * @param beneficiary the beneficiary to be set
	 */
	public void setBeneficiary(String beneficiary) { this.beneficiary = beneficiary; }
}

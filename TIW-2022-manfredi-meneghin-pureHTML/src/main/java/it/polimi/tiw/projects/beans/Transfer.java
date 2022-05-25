package it.polimi.tiw.projects.beans;

import java.math.BigDecimal;
import java.sql.Date;

public class Transfer {
	
	private int id;
	private Date timestamp;
	private BigDecimal amount;
	private String reason;
	private int id_account_orderer;
	private int id_account_beneficiary;
	
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
	public Date getDate() { return timestamp; }
	
	/*
	 * Setter for the field 'date'
	 * @param date the date to be set
	 */
	public void setDate(Date timestamp) { this.timestamp = timestamp; }
	
	/*
	 * Getter for the field 'amount'
	 * @return the transfer's amount
	 */
	public BigDecimal getAmount() { return amount; }
	
	/*
	 * Setter for the field 'amount'
	 * @param amount the amount to be set
	 */
	public void setAmount(BigDecimal amount) { this.amount = amount; }
	
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
	 * Getter for the field 'id_account_orderer'
	 * @return the transfer's id_account_orderer
	 */
	public int getIdAccountOrderer() { return id_account_orderer; }
	
	/*
	 * Setter for the field 'id_account_orderer'
	 * @param id_account_orderer the id_account_orderer to be set
	 */
	public void setIdAccountOrderer(int id_account_orderer) { this.id_account_orderer = id_account_orderer; }
	
	/*
	 * Getter for the field 'id_account_beneficiary'
	 * @return the transfer's id_account_beneficiary
	 */
	public int getIdAccountBeneficiary() { return id_account_beneficiary; }
	
	/*
	 * Setter for the field 'id_account_beneficiary'
	 * @param id_account_beneficiary the id_account_beneficiary to be set
	 */
	public void setIdAccountBeneficiary(int id_account_beneficiary) { this.id_account_beneficiary = id_account_beneficiary; }
}

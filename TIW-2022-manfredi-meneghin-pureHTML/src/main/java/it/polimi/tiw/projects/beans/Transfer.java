package it.polimi.tiw.projects.beans;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Java bean for the Transfer information
 */
public class Transfer {
	
	private int        id;
	private Date       timestamp;
	private int        account_code_orderer;
	private int        account_code_beneficiary;
	private BigDecimal amount;
	private String     reason;
	
	/**
	 * Getter for the field 'id'
	 * @return the transfer's id 
	 */
	public int getId() { return id; }
	
	/**
	 * Setter for the field 'id'
	 * @param id the id to be set
	 */
	public void setId(int id) { this.id = id; }
	
	/**
	 * Getter for the field 'timestamp'
	 * @return the transfer's timestamp 
	 */
	public Date getTimestamp() { return timestamp; }
	
	/**
	 * Setter for the field 'timestamp'
	 * @param timestamp the timestamp to be set
	 */
	public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
	
	/**
	 * Getter for the field 'account_code_orderer'
	 * @return the transfer's account_code_orderer
	 */
	public int getAccountCodeOrderer() { return account_code_orderer; }
	
	/**
	 * Setter for the field 'account_code_orderer'
	 * @param code_account_orderer the account_code_orderer to be set
	 */
	public void setAccountCodeOrderer(int account_code_orderer) { this.account_code_orderer = account_code_orderer; }
	
	/**
	 * Getter for the field 'account_code_beneficiary'
	 * @return the transfer's account_code_beneficiary
	 */
	public int getAccountCodeBeneficiary() { return account_code_beneficiary; }
	
	/**
	 * Setter for the field 'account_code_beneficiary'
	 * @param code_account_beneficiary the account_code_beneficiary to be set
	 */
	public void setAccountCodeBeneficiary(int account_code_beneficiary) { this.account_code_beneficiary = account_code_beneficiary; }
	
	/**
	 * Getter for the field 'amount'
	 * @return the transfer's amount
	 */
	public BigDecimal getAmount() { return amount; }
	
	/**
	 * Setter for the field 'amount'
	 * @param amount the amount to be set
	 */
	public void setAmount(BigDecimal amount) { this.amount = amount; }
	
	/**
	 * Getter for the field 'reason'
	 * @return the transfer's reason
	 */
	public String getReason() { return reason; }
	
	/**
	 * Setter for the field 'reason'
	 * @param reason the reason to be set
	 */
	public void setReason(String reason) { this.reason = reason; }
}

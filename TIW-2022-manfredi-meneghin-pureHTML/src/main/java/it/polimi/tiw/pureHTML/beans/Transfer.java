package it.polimi.tiw.pureHTML.beans;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Java bean for the Transfer information
 */
public class Transfer {
	
	private int        id;
	private Date       timestamp;
	private int        code_account_orderer;
	private int        code_account_beneficiary;
	private BigDecimal amount;
	private String     reason;
	
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
	 * Getter for the field 'timestamp'
	 * @return the transfer's timestamp 
	 */
	public Date getTimestamp() { return timestamp; }
	
	/*
	 * Setter for the field 'timestamp'
	 * @param timestamp the timestamp to be set
	 */
	public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
	
	/*
	 * Getter for the field 'code_account_orderer'
	 * @return the transfer's code_account_orderer
	 */
	public int getCodeAccountOrderer() { return code_account_orderer; }
	
	/*
	 * Setter for the field 'code_account_orderer'
	 * @param code_account_orderer the code_account_orderer to be set
	 */
	public void setCodeAccountOrderer(int code_account_orderer) { this.code_account_orderer = code_account_orderer; }
	
	/*
	 * Getter for the field 'code_account_beneficiary'
	 * @return the transfer's code_account_beneficiary
	 */
	public int getCodeAccountBeneficiary() { return code_account_beneficiary; }
	
	/*
	 * Setter for the field 'code_account_beneficiary'
	 * @param code_account_beneficiary the code_account_beneficiary to be set
	 */
	public void setCodeAccountBeneficiary(int code_account_beneficiary) { this.code_account_beneficiary = code_account_beneficiary; }
	
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
}

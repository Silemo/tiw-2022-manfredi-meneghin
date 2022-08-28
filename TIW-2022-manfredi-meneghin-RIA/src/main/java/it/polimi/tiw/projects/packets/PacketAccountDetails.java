package it.polimi.tiw.projects.packets;

import java.util.List;

import it.polimi.tiw.projects.beans.Account;
import it.polimi.tiw.projects.beans.Transfer;

/**
 * Support class for JSON serialization - collects the informations shown in the AccountDetails
 * Used in GetAccountDetails
 */
public class PacketAccountDetails {
	
	private Account account;
	private List<Transfer> transfers;
	
	/**
	 * Constructor of the class
	 * 
	 * @param account
	 * @param transfers
	 */
	public PacketAccountDetails(Account account, List<Transfer> transfers){
		
		this.account = account;
		this.transfers = transfers;
	}
	
	/**
	 * Gets the field "account"
	 * 
	 * @return the account
	 */
	public Account getAccount(){ return this.account; }

	/**
	 * Gets the field "transfers"
	 * 
	 * @return the transfers
	 */
	public List<Transfer> getTransfers(){ return this.transfers; }
}

package it.polimi.tiw.projects.packets;

import it.polimi.tiw.projects.beans.Account;
import it.polimi.tiw.projects.beans.Transfer;

/**
 * Support class for JSON serialization - collects the informations regarding a Transfer
 * Used in MakeTransfer
 */
public class PacketTransferInfo {
	
	private Account sourceAccount;
	private Account destAccount;
	private Transfer transfer;
	
	/**
	 * Constructor of the class
	 * 
	 * @param sourceAccount
	 * @param destAccount
	 * @param transfer
	 */
	public PacketTransferInfo (Account sourceAccount, Account destAccount, Transfer transfer) {
		this.sourceAccount = sourceAccount;
		this.destAccount = destAccount;
		this.transfer = transfer;
	}

	/**
	 * Gets the field "sourceAccount"
	 * 
	 * @return the sourceAccount
	 */
	public Account getSourceAccount() { return sourceAccount; }

	/**
	 * Gets the field "destAccount"
	 * 
	 * @return the destAccount
	 */
	public Account getDestAccount() { return destAccount; }

	/**
	 * Gets the field "transfer"
	 * 
	 * @return the transfer
	 */
	public Transfer getTransfer() { return transfer; }
}

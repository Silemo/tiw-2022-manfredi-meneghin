package it.polimi.tiw.projects.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddressBook {
	
	// The owner of the address book (or list of contacts)
	private int ownerId;
	
	// A Map of user_id (owners of the account in our contact) and a SET of account_code (which correspond to the contact_account)
	// This type of structure was necessary to ease the access to the contact list. 
	// In the DB there is the contact entity which is composed of who owns the contact and an accountCode. So to avoid unnecessary JOINS
	// this structure of owner of the account and map(user_id, set<address_code>) was created
	private Map<Integer, Set<Integer>> contacts = new HashMap<>();
		
	/*
	 * Getter for the field 'ownerId'
	 * 
	 * @return the user id of the owner
	 */
	public int getOwnerId() { return ownerId; }

	/*
	 * Setter for the field 'ownerId'
	 * 
	 * @param ownerId the id of the owner to be set
	 */
	public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

	/*
	 * Getter for the field 'contacts'
	 * 
	 * @return a map of user_id -> set of account codes defining the contacts of the addressBook
	 */
	public Map<Integer, Set<Integer>> getContacts() { return new HashMap<>(contacts); }

	/*
	 * Puts a user and an account in the contacts map
	 * 
	 * @param user_id the account owner
	 * @param account_code the code of the account (contact_account)
	 */
	public void putContact(int user_id, int account_code) {
		
		if(contacts.containsKey(user_id)) {
			
			contacts.get(user_id).add(account_code);
			
		} else {
			
			Set<Integer> set = new HashSet<>();
			set.add(account_code);
			contacts.put(user_id, set);
		}

	}
}

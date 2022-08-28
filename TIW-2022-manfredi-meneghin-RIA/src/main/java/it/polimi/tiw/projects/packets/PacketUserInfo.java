package it.polimi.tiw.projects.packets;

/**
 * Support class for JSON serialization - represents the name and id of a User. 
 * Used in the Login event
 */
public class PacketUserInfo {
	
	private int id;
	private String name;
	private String username;
	
	/**
	 * Constructor of the class
	 * 
	 * @param name
	 * @param id
	 * @param username
	 */
	public PacketUserInfo(int id, String name, String username) {
		
		this.id = id;
		this.name = name;
		this.username = username;
	}
	
	/**
	 * Gets the field "id" 
	 * 
	 * @return the id
	 */
	public int getId() { return id; }
	
	/**
	 * Gets the filed "name"
	 * 
	 * @return the name
	 */
	public String getName() { return name; }
	
	/**
	 * Gets the filed "username"
	 * 
	 * @return the username
	 */
	public String getUsername() { return username; }
}

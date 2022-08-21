package it.polimi.tiw.projects.packets;

/**
 * Support class for JSON serialization - represents the name and id of a User. 
 * Used in the Login event
 */
public class PacketUserInfo {
	
	private int id;
	private String name;
	
	/**
	 * Constructor of the class
	 * 
	 * @param name
	 * @param id
	 */
	public PacketUserInfo(String name, int id) {
		
		this.id = id;
		this.name = name;
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
}

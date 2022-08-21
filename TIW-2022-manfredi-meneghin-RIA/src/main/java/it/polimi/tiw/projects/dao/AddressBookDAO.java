package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.projects.beans.AddressBook;


public class AddressBookDAO {

	private Connection connection;
	
	public AddressBookDAO(Connection connection) {
		
		this.connection = connection;
	}
	
	/**
	 * Finds the addressBook of the specified ownerId by a JOIN between the account and contact tables
	 * 
	 * @param ownerId the owner of the addressBook
	 * @return the AddressBook
	 * @throws SQLException
	 */
	public AddressBook findAddressBookByOwnerId(int ownerId) throws SQLException{

		AddressBook addressBook = new AddressBook();
		addressBook.setOwnerId(ownerId);

		String performedAction = " constructing an addressbook by owner id (finding his contacts) ";
		String query = "SELECT a.contact_account, b.user_id FROM contact AS a JOIN account AS b ON a.contact_account = b.code WHERE a.owner_id = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, ownerId);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				addressBook.putContact(resultSet.getInt("user_id"), resultSet.getInt("contact_account"));
			}
			
		} catch(SQLException e) {
			
			throw new SQLException("Error accessing the DB when" + performedAction + "[ " + e.getMessage() + " ]");
		
		} finally {
			
			try {
				
				resultSet.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the result set when" + performedAction + "[ " + e.getMessage() + " ]");
			}
			
			try {
				
				preparedStatement.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction + "[ " + e.getMessage() + " ]");
			}
		}
		
		return addressBook;
	}
	
	/**
	 * Checks if a specified contact (owner_id and contact_account) exists, if so returns true, otherwise false
	 * 
	 * @param ownerId the specified owner_id of the contact
	 * @param contactAccount the account_code of the contact
	 * @return true if the contact exists, false otherwise
	 * @throws SQLException
	 */
	public boolean doesContactExist(int ownerId, int contactAccount) throws SQLException{

		boolean result = false;

		String performedAction = " determining if a contact already exists ";
		String query = "SELECT * FROM contact WHERE owner_id = ? AND contact_account = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, ownerId);
			preparedStatement.setInt(2, contactAccount);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				
				result = true;
			}
			
		} catch(SQLException e) {
			
			throw new SQLException("Error accessing the DB when" + performedAction + "[ " + e.getMessage() + " ]");
			
		} finally {
			
			try {
				
				resultSet.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the result set when" + performedAction + "[ " + e.getMessage() + " ]");
			}
			
			try {
				
				preparedStatement.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction + "[ " + e.getMessage() + " ]");
			}
		}
		
		return result;
	}
	
	/**
	 * Creates a new Contact in the bank database.
	 * In case of error raises an SQLException
	 * 
	 * @param ownerId the specified owner of the contact
	 * @param contactAccount the specified account of the contact
	 * @throws SQLException
	 */
	public void createContact(int ownerId, int contactAccount) throws SQLException {
		
		String performedAction = " adding a new entry in an address book in the database ";
		String queryAddUser = "INSERT INTO contact (owner_id, contact_account) VALUES(?,?)";
		PreparedStatement preparedStatementAddUser = null;	
		
		try {
			
			preparedStatementAddUser = connection.prepareStatement(queryAddUser);
			preparedStatementAddUser.setInt(1, ownerId);
			preparedStatementAddUser.setInt(2, contactAccount);
			preparedStatementAddUser.executeUpdate();
			
		}catch(SQLException e) {
			
			throw new SQLException("Error accessing the DB when" + performedAction + "[ " + e.getMessage() + " ]");
			
		} finally {
			
			try {
				
				preparedStatementAddUser.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction + "[ " + e.getMessage() + " ]");
			}
		}
	}
}

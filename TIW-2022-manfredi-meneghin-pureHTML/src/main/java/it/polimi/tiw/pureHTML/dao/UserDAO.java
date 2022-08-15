package it.polimi.tiw.pureHTML.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.pureHTML.beans.User;

/**
 * Data Access Object for the User table in the mySQL server
 */
public class UserDAO {
	
	private Connection connection;

	public UserDAO(Connection connection) {
		
		this.connection = connection;
	}
	
	/**
	 * Finds and returns a User from the bankDB taking their email and password as input
	 * In case of error raises an SQLException
	 * 
	 * @param email the user's email (UNIQUE)
	 * @param password the user's password
	 * @return the User
	 * @throws SQLException
	 */
	public User findUser(String email, String password) throws SQLException{

		User user = null;
		String performedAction = " finding a user by email and password";
		String query           = "SELECT * FROM bankDB.user WHERE email = ? AND password = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				
				user = new User();
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("name"));
				user.setSurname(resultSet.getString("surname"));
				user.setEmail(resultSet.getString("email"));
				user.setUsername(resultSet.getString("username"));
			}
			
		} catch(SQLException e) {
			
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		} finally {
			
			try {
				
				resultSet.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the result set when" + performedAction);
			}
			
			try {
				
				preparedStatement.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		
		return user;
	}
	
	/**
	 * Finds and returns a User from the bankDB taking their id as input
	 * In case of error raises an SQLException
	 * 
	 * @param id the user's id
	 * @return the User
	 * @throws SQLException
	 */
	public User findUserById(int id) throws SQLException{

		User user = null;
		String performedAction = " finding a user by id";
		String query = "SELECT * FROM bankDB.user WHERE id = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				
				user = new User();
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("name"));
				user.setSurname(resultSet.getString("surname"));
				user.setEmail(resultSet.getString("email"));
				user.setUsername(resultSet.getString("username"));
			}
			
		} catch(SQLException e) {
			
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		} finally {
			
			try {
				
				resultSet.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the result set when" + performedAction);
			}
			
			try {
				
				preparedStatement.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		
		return user;
	}
	
	/**
	 * Finds and returns a User from the bankDB taking their email as input
	 * In case of error raises an SQLException
	 * 
	 * @param email the user's email (UNIQUE)
	 * @return the User
	 * @throws SQLException
	 */
	public User findUserByEmail(String email) throws SQLException{

		User user = null;
		String performedAction = " finding a user by email";
		String query = "SELECT * FROM bankDB.user WHERE email = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				
				user = new User();
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("name"));
				user.setSurname(resultSet.getString("surname"));
				user.setEmail(resultSet.getString("email"));
				user.setUsername(resultSet.getString("username"));
			}
			
		} catch(SQLException e) {
			
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		} finally {
			
			try {
				
				resultSet.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the result set when" + performedAction);
			}
			
			try {
				
				preparedStatement.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		
		return user;
	}
	
	/**
	 * Finds and returns a User from the bankDB taking their username as input
	 * In case of error raises an SQLException
	 * 
	 * @param username the user's username (UNIQUE)
	 * @return the User
	 * @throws SQLException
	 */
	public User findUserByUsername(String username) throws SQLException{

		User user = null;
		String performedAction = " finding a user by username";
		String query = "SELECT * FROM bankDB.user WHERE username = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				
				user = new User();
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("name"));
				user.setSurname(resultSet.getString("surname"));
				user.setEmail(resultSet.getString("email"));
				user.setUsername(resultSet.getString("username"));
			}
			
		} catch(SQLException e) {
			
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		} finally {
			
			try {
				
				resultSet.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the result set when" + performedAction);
			}
			
			try {
				
				preparedStatement.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		
		return user;
	}
	
	/**
	 * Creates a new User in the bank database
	 * In case of error raises an SQLException
	 * 
	 * @param name the user's name
	 * @param surname the user's surname
	 * @param email the user's email (UNIQUE)
	 * @param username the user's username (UNIQUE)
	 * @param password the user's password
	 * @throws SQLException
	 */
	public void createUser(String name, String surname, String email, String username, String password) throws SQLException {
		
		String performedAction = " creating a new user in the database";
		String queryAddUser = "INSERT INTO bankDB.user (name,surname,email,username,password) VALUES(?,?,?,?,?)";
		PreparedStatement preparedStatementAddUser = null;	
		
		try {
			
			preparedStatementAddUser = connection.prepareStatement(queryAddUser);
			preparedStatementAddUser.setString(1, name);
			preparedStatementAddUser.setString(2, surname);
			preparedStatementAddUser.setString(3, email);
			preparedStatementAddUser.setString(4, username);
			preparedStatementAddUser.setString(5, password);
			preparedStatementAddUser.executeUpdate();
			
		} catch(SQLException e) {
			
			throw new SQLException("Error accessing the DB when" + performedAction);
		
		} finally {
			
			try {
				
				preparedStatementAddUser.close();
			
			} catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
	}
}

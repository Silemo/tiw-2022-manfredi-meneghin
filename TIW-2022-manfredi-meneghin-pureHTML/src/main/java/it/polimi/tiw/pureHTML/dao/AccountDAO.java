package it.polimi.tiw.pureHTML.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.pureHTML.beans.Account;

/**
 * Data Access Object for the Account table in the mySQL server
 */
public class AccountDAO {
	
	private Connection connection;

	public AccountDAO(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Finds and returns an Account from the bankDB taking its code as input
	 * In case of error raises an SQLException
	 * 
	 * @param code the account's code
	 * @return the Account
	 * @throws SQLException
	 */
	public Account findAccountByCode(int code) throws SQLException{

		Account account = null;
		String performedAction = " finding an account by code";
		String query = "SELECT * FROM bankDB.account WHERE code = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, code);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				
				account = new Account();
				account.setCode(resultSet.getInt("code"));
				account.setUserId(resultSet.getInt("user_id"));
				account.setBalance(resultSet.getBigDecimal("balance"));
				
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
		
		return account;
	}
	
	/**
	 * Finds and returns all the Accounts from the bankDB which have the same user_id 
	 * i.e. all the accounts of the specified user
	 * In case of error raises an SQLException
	 * 
	 * @param user_id the id of the user
	 * @return a List of all user's accounts
	 * @throws SQLException
	 */
	public List<Account> findAccountsByUserId(int user_id) throws SQLException{

		List<Account> accounts = new ArrayList<>();
		String performedAction = " finding accounts by user_id";
		String query = "SELECT * FROM bankDB.account WHERE user_id = ? ORDER BY id ASC";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, user_id);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				
				Account account = new Account();
				account.setCode(resultSet.getInt("code"));
				account.setUserId(resultSet.getInt("user_id"));
				account.setBalance(resultSet.getBigDecimal("balance"));
				accounts.add(account);
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
		
		return accounts;
	}
	
	/**
	 * Creates a new Account in the bank database
	 * In case of error raises an SQLException
	 * 
	 * @param user_id the id of the account owner (user)
	 * @throws SQLException
	 */
	public void createAccount(int user_id) throws SQLException {
		
		String performedAction = " creating a new bank account in the database";
		String queryAddUser = "INSERT INTO bankDB.account (user_id) VALUES(?)";
		PreparedStatement preparedStatementAddUser = null;	
		
		try {
			
			preparedStatementAddUser = connection.prepareStatement(queryAddUser);
			preparedStatementAddUser.setInt(1, user_id);
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

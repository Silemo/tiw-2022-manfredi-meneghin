package it.polimi.tiw.pureHTML.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.pureHTML.beans.Transfer;

/**
 * Data Access Object for the Transfer table in the mySQL server
 */
public class TransferDAO {
	
	private Connection connection;

	public TransferDAO(Connection connection) {
		
		this.connection = connection;
	}
	
	/**
	 * Finds and returns all the Transfers from the bankDB which have a specified account code as ordered or beneficiary 
	 * i.e. all the transaction on the specified account
	 * In case of error raises an SQLException
	 * 
	 * @param code the code of the specified account
	 * @return a List of Transfer on specified account
	 * @throws SQLException
	 */
	public List<Transfer> findTransfersByAccountCode(int code) throws SQLException{

		List<Transfer> transfers = new ArrayList<>();
		String performedAction = " finding transfers by account code";
		String query = "SELECT * FROM bankDB.transfer WHERE code_account_orderer = ? OR code_account_beneficiary = ? ORDER BY timestamp DESC";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, code);
			preparedStatement.setInt(2, code);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				
				Transfer transfer = new Transfer();
				transfer.setCodeAccountOrderer(resultSet.getInt("code_account_orderer"));
				transfer.setCodeAccountBeneficiary(resultSet.getInt("code_account_beneficiary"));
				transfer.setTimestamp(new Date(resultSet.getTimestamp("timestamp").getTime()));
				transfer.setAmount(resultSet.getBigDecimal("amount"));
				transfer.setReason(resultSet.getString("reason"));
				transfers.add(transfer);
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
		
		return transfers;
	}
	
	/**
	 * Creates a new Transfer in the bank database - TRANSACTION BASED OPERATION
	 * In case of error raises an SQLException
	 * 
	 * @param code_account_orderer the account from where the transfer starts
	 * @param code_account_beneficiary the account which receives the transfer
	 * @param amount the quantity of money being exchanged
	 * @param reason a small description of the transfer reason
	 * @throws SQLException
	 */
	public void createTransfer(int code_account_orderer, int code_account_beneficiary, BigDecimal amount, String reason) throws SQLException{

		String performedAction = " creating a new transfer";
		String transactionInsert       = "INSERT INTO bankDB.transfer (code_account_orderer, code_account_beneficiary, amount, reason) VALUES(?,?,?,?)";
		String transactionUpdateSource = "UPDATE bankDB.account SET balance = balance - ? WHERE code = ?";
		String transactionUpdateDest   = "UPDATE bankDB.account SET balance = balance + ? WHERE code = ?";
		
		PreparedStatement preparedStatementInsert       = null;	
		PreparedStatement preparedStatementUpdateSource = null;	
		PreparedStatement preparedStatementUpdateDest   = null;	
		
		try {
			// Deactivate auto-commit to ensure an atomic transaction
			connection.setAutoCommit(false);
			
			preparedStatementInsert       = connection.prepareStatement(transactionInsert);
			preparedStatementUpdateSource = connection.prepareStatement(transactionUpdateSource);
			preparedStatementUpdateDest   = connection.prepareStatement(transactionUpdateDest);
			
			// First operation - Creating the transfer in bankDB.transfer table
			preparedStatementInsert.setInt(1, code_account_orderer);
			preparedStatementInsert.setInt(2, code_account_beneficiary);
			preparedStatementInsert.setBigDecimal(3, amount);
			preparedStatementInsert.setString(4, reason);
			preparedStatementInsert.executeUpdate();
			
			// Second operation - Removing the amount from the orderer's account
			preparedStatementUpdateSource.setBigDecimal(1, amount);
			preparedStatementUpdateSource.setInt(2, code_account_orderer);
			preparedStatementUpdateSource.executeUpdate();
			
			// Third and last operation - Adding the amount to the beneficiary's account
			preparedStatementUpdateDest.setBigDecimal(1, amount);
			preparedStatementUpdateDest.setInt(2, code_account_orderer);
			preparedStatementUpdateDest.executeUpdate();
			
			// Commit the whole transaction
			connection.commit();
			
		} catch(SQLException e) {
			// If an SQLException is raised a roll-back to the last commit is needed
			connection.rollback();
			
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		} finally {
			// When the transaction is completed (successfully or not) we need to resume the auto-commit
			connection.setAutoCommit(true);
			
			try {
				
				preparedStatementInsert.close();
				preparedStatementUpdateSource.close();
				preparedStatementUpdateDest.close();
				
			} catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
	}
}

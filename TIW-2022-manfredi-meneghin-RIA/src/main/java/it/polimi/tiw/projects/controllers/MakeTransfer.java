package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import it.polimi.tiw.projects.beans.*;
import it.polimi.tiw.projects.dao.AccountDAO;
import it.polimi.tiw.projects.dao.TransferDAO;
import it.polimi.tiw.projects.packets.PacketTransferInfo;
import it.polimi.tiw.projects.utils.*;

/**
 * Servlet implementation class MakeTransfer
 */
@WebServlet("/MakeTransfer")
@MultipartConfig
public class MakeTransfer extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private int sourceAccountCode;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeTransfer() {   
    	super();
    	// TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	
    	ServletContext servletContext = getServletContext();
		this.connection = ConnectionHandler.getConnection(servletContext);
    }

    @Override
    public void destroy() {
		
    	try {
    		
			ConnectionHandler.closeConnection(connection);
		
    	} catch (SQLException e) {
			
    		e.printStackTrace();
		}
	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Gets the transfer parameters and checks them (input sanitization)
		String destUserIdString        = request.getParameter("destUserId");
		String destAccountCodeString   = request.getParameter("destAccountCode");
		String sourceAccountCodeString = request.getParameter("sourceAccountCode");
		String amountString            = request.getParameter("amount");
		String reason                  = request.getParameter("reason");
		
		// Checks if the parameters are null, if so set the BAD_REQUEST
		if(destAccountCodeString == null || destUserIdString == null || sourceAccountCodeString == null || amountString == null || reason == null) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("One or more data requested is null, when making a transfer");
			return;
		}
		
		int destAccountCode;
		int destUserId;
		BigDecimal amount;
		// Parses and saves the parameter verifying if the numberFormat is correct
		try {
			
			sourceAccountCode = Integer.parseInt(sourceAccountCodeString);
			destAccountCode   = Integer.parseInt(destAccountCodeString);
			destUserId        = Integer.parseInt(destUserIdString);
			amount            = new BigDecimal(amountString.replace(",","."));
			
		} catch (NumberFormatException e) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Some values requested are not numbers, when making a transfer");
			return;
		}
		
		// Gets the user currently logged in
		HttpSession session = request.getSession(false);
		User currentUser    = (User)session.getAttribute("currentUser");
			
		// Gets the accounts between which the transaction will occur
		AccountDAO accountDAO = new AccountDAO(connection);
		Account sourceAccount;
		Account destAccount;
	
		try {
			
			sourceAccount = accountDAO.findAccountByCode(sourceAccountCode);
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}
		
		try {
			
			destAccount = accountDAO.findAccountByCode(destAccountCode);
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}
		
		// A series of preconditions to the transaction being checked, if these conditions are not met
		// the correct error message is set
		if(sourceAccount == null) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("Source account does not exist");
			return;
		}
		
		if(destAccount == null) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("Destination account does not exist");
			return;
		}
		
		if(sourceAccount.getUserId() != currentUser.getId()) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("Source account does not belong to the current user");
			return;
		}
		
		if(destAccount.getUserId() != destUserId) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("Destination account does not belong to the selected destination user");
			return;
		}
		
		if(sourceAccountCode == destAccountCode) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("Cannot make transfers from an account onto the same account");
			return;
		}
		
		if(amount.compareTo(new BigDecimal(0)) != 1) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("Transfer amount must be greater than 0");
			return;
		}
		
		if(sourceAccount.getBalance().subtract(amount).compareTo(new BigDecimal(0)) == -1) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("You don't have enough money on this account to make this transfer");
			return;
		}
		
		
		// After all preconditions are checked the transfer is executed
		TransferDAO transferDAO = new TransferDAO(connection);
		
		try {
			
			transferDAO.createTransfer(sourceAccountCode, destAccountCode, amount, reason);
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;		
		}
		
		// Gets the source and destination accounts to save them in the session
		try {
			
			sourceAccount = accountDAO.findAccountByCode(sourceAccountCode);
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}
		
		try {
			
			destAccount = accountDAO.findAccountByCode(destAccountCode);
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}
		
		// Creates a new transfer (java bean) with the informations just gathered 
		Transfer transfer = new Transfer();
		transfer.setAccountCodeOrderer(sourceAccountCode);
		transfer.setAccountCodeBeneficiary(destAccountCode);
		transfer.setAmount(amount);
		transfer.setReason(reason);
		
		String json = new Gson().toJson(new PacketTransferInfo(sourceAccount, destAccount, transfer));
		
		response.setStatus(HttpServletResponse.SC_OK);	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
}

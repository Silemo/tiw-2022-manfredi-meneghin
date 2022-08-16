package it.polimi.tiw.pureHTML.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.pureHTML.beans.Account;
import it.polimi.tiw.pureHTML.beans.Transfer;
import it.polimi.tiw.pureHTML.beans.User;
import it.polimi.tiw.pureHTML.dao.AccountDAO;
import it.polimi.tiw.pureHTML.dao.TransferDAO;
import it.polimi.tiw.pureHTML.utils.ConnectionHandler;
import it.polimi.tiw.pureHTML.utils.PathHelper;
import it.polimi.tiw.pureHTML.utils.TemplateHandler;

/**
 * Servlet implementation class MakeTransfer
 */
@WebServlet("/MakeTransfer")
public class MakeTransfer extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
	private int sourceAccountCode;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeTransfer() {
        
    	super();
    }
    
    @Override
    public void init() throws ServletException {
    	
    	ServletContext servletContext = getServletContext();
		this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
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
		
		// Checks if the parameters are null, if so redirects to an ErrorPage
		if(destAccountCodeString == null || destUserIdString == null || sourceAccountCodeString == null || amountString == null || reason == null) {
			
			forwardToErrorPage(request, response, "One or more data requested is null, when making a transfer");
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
			
			forwardToErrorPage(request, response, "Some values requested are not numbers, when making a transfer");
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
			
			forwardToErrorPage(request, response, e.getMessage());
			return;
		}
		
		try {
			
			destAccount = accountDAO.findAccountByCode(destAccountCode);
			
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;
		}
		
		// A series of preconditions to the transaction being checked, if these conditions are not met
		// redirects to "TransferFailedPage" with an errorMessage
		if(sourceAccount == null) {
			
			forwardToTransferFailedPage(request, response, "Source account does not exist");
			return;
		}
		
		if(destAccount == null) {
			
			forwardToTransferFailedPage(request, response, "Destination account does not exist");
			return;
		}
		
		if(sourceAccount.getUserId() != currentUser.getId()) {
			
			forwardToTransferFailedPage(request, response, "Source account does not belong to the current user");
			return;
		}
		
		if(destAccount.getUserId() != destUserId) {
			
			forwardToTransferFailedPage(request, response, "Destination account does not belong to the selected destination user");
			return;
		}
		
		if(sourceAccountCode == destAccountCode) {
			
			forwardToTransferFailedPage(request, response, "Cannot make transfers from an account onto the same account");
			return;
		}
		
		if(amount.compareTo(new BigDecimal(0)) != 1) {
			
			forwardToTransferFailedPage(request, response, "Transfer amount must be greater than 0");
			return;
		}
		
		if(sourceAccount.getBalance().subtract(amount).compareTo(new BigDecimal(0)) == -1) {
			
			forwardToTransferFailedPage(request, response, "You don't have enough money on this account to make this transfer");
			return;
		}
		
		
		// After all preconditions are checked the transfer is executed
		TransferDAO transferDAO = new TransferDAO(connection);
		
		try {
			
			transferDAO.createTransfer(sourceAccountCode, destAccountCode, amount, reason);
			
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;		
		}
		
		// Gets the source and destination accounts to save them in the session
		try {
			
			sourceAccount = accountDAO.findAccountByCode(sourceAccountCode);
			
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;
		}
		
		try {
			
			destAccount = accountDAO.findAccountByCode(destAccountCode);
			
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;
		}
		
		// Creates a new transfer (java bean) with the informations just gathered 
		Transfer transfer = new Transfer();
		transfer.setAccountCodeOrderer(sourceAccountCode);
		transfer.setAccountCodeBeneficiary(destAccountCode);
		transfer.setAmount(amount);
		transfer.setReason(reason);
		
		// TODO: I don't like saving this many arguments in the session but I don't know if there is any other option
		session.setAttribute("transfer", transfer);
		session.setAttribute("sourceAccount", sourceAccount);
		session.setAttribute("destAccount", destAccount);
		
		// TODO: do we need this??
		session.setAttribute("transferInfoShown", false);
		
		response.sendRedirect(getServletContext().getContextPath() + PathHelper.goToTransferConfirmedServletPath);
	}

	/**
	 * Forwards to the TransferFailedPage
	 * 
	 * @param request
	 * @param response
	 * @param error
	 * @throws ServletException
	 * @throws IOException
	 */
	private void forwardToTransferFailedPage(HttpServletRequest request, HttpServletResponse response, String failureReason) throws ServletException, IOException{
		
		request.setAttribute("reason", failureReason);
		request.setAttribute("accountCode", sourceAccountCode);
		forward(request, response, PathHelper.pathToTransferFailedPage);
		return;
	}
	
	/**
	 * Forwards to the ErrorPage
	 * 
	 * @param request
	 * @param response
	 * @param error
	 * @throws ServletException
	 * @throws IOException
	 */
	private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{
		
		request.setAttribute("error", error);
		forward(request, response, PathHelper.pathToErrorPage);
		return;
	}
	
	/**
	 * Forwards to the specified path
	 * 
	 * @param request
	 * @param response
	 * @param path
	 * @throws ServletException
	 * @throws IOException
	 */
	private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
		
	}
}

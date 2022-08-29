package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import it.polimi.tiw.projects.beans.Account;
import it.polimi.tiw.projects.beans.Transfer;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.AccountDAO;
import it.polimi.tiw.projects.dao.TransferDAO;
import it.polimi.tiw.projects.packets.PacketAccountDetails;
import it.polimi.tiw.projects.utils.ConnectionHandler;

/**
 * Servlet implementation class GetAccountDetails
 */
@WebServlet("/GetAccountDetails")
@MultipartConfig
public class GetAccountDetails extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAccountDetails() {
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
		
		String accountCodeString = request.getParameter("accountCode");
		// Verifies if the string is null, if so sets a BAD_REQUEST response
		if (accountCodeString == null) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Missing parameter in the request to get the accountDetails");
			return;
		}
		
		int accountCode;
		// Tries to parse the accountCode number. If unable sets a BAD_REQUEST response
		try {
			
			accountCode = Integer.parseInt(accountCodeString);
		
		} catch (NumberFormatException e) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Parameter in get account details request is not an integer");
			return;
		}
		// Gets the user from the session
		HttpSession session = request.getSession(false);
		User currentUser    = (User)session.getAttribute("currentUser");
		
		AccountDAO accountDAO = new AccountDAO(connection);
		Account account;
		// Gets the account by its code
		try {
			
			account = accountDAO.findAccountByCode(accountCode);
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;	
		}
		// Verifies if the account is not null and belongs to the user in the session, if otherwise sets an UNAUTHORIZED response
		if (account == null || account.getUserId() != currentUser.getId()) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("The account doesn't exist or is not yours");
			return;
		}
		
		List<Transfer> transfers;
		TransferDAO transferDAO = new TransferDAO(connection);
		// Finds all the transfers related to the selected account
		try {
			
			transfers = transferDAO.findTransfersByAccountCode(accountCode);
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;	
		}
		// Sends the PacketAccountDetails to the client, serializing it using JSON
		String json = new Gson().toJson(new PacketAccountDetails(account, transfers));
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

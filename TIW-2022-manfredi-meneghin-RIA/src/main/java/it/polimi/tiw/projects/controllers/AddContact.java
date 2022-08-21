package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.projects.beans.Account;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.AccountDAO;
import it.polimi.tiw.projects.dao.AddressBookDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

/**
 * Servlet implementation class AddContact
 */
@WebServlet("/AddContact")
public class AddContact extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddContact() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String contactIdString = request.getParameter("contactId");
		String contactAccountCodeString = request.getParameter("contactAccountCode");
		
		if(contactAccountCodeString == null || contactIdString == null) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Missing parameter in addContact request");
			return;
		}
		
		int contactId;
		int contactAccountCode;
		
		try {
			
			contactId = Integer.parseInt(contactIdString);
			contactAccountCode = Integer.parseInt(contactAccountCodeString);
			
		} catch (NumberFormatException e) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Parameters in addContact are not integers");
			return;
		}
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		
		AccountDAO accountDAO = new AccountDAO(connection);
		Account account;
		
		try {
			
			account = accountDAO.findAccountByCode(contactAccountCode);
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;	
		}
		
		if (account == null || account.getUserId() != contactId) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("The chosen account does not exists or does not belong to the specified user, retry");
			return;
		}
		
		AddressBookDAO addressBookDAO = new AddressBookDAO(connection);
		boolean alreadyPresent = false;
		
		try {
			
			alreadyPresent = addressBookDAO.doesContactExist(currentUser.getId(), contactAccountCode);
		
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;	
		}
		
		if(alreadyPresent) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);		
			response.getWriter().println("Contact entry already exists");
			return;
		}
		
		try {
			
			addressBookDAO.createContact(currentUser.getId(), contactAccountCode);
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_OK);	
	}
}

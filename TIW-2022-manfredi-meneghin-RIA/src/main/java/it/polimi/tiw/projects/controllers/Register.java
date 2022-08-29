package it.polimi.tiw.projects.controllers;

import java.io.IOException;
// import java.math.BigDecimal;
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

import it.polimi.tiw.projects.utils.*;
import it.polimi.tiw.projects.dao.AccountDAO;
import it.polimi.tiw.projects.dao.UserDAO;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.beans.Account;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
@MultipartConfig
public class Register extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		
		// Tries to register the user, if the function returns null ends (registerUser already set an error if it's the case)
		if (registerUser(request, response) == null) {
			return;
		}
		
		// Once the user is registered an OK status is set
		response.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Verifies the input and if it is correct creates the user in the DB and an account of the user. 
	 * If the operation is successful returns 0, else returns null
	 * 
	 * @param request
	 * @param response
	 * @return User
	 * @throws ServletException
	 * @throws IOException
	 */
	private Integer registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Gets the parameters of the request and verifies if they are in the correct format (length and syntax)
		String name       = request.getParameter("name");
		String surname    = request.getParameter("surname");
		String email      = request.getParameter("email");
		String username   = request.getParameter("username");
		String password   = request.getParameter("password");
		String repeat_pwd = request.getParameter("repeat_pwd");
				
		// Verifies if all parameters are not null
		if(name == null || surname == null || email == null || username == null || password == null || repeat_pwd == null) {
					
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Register module missing some data");
			return null;
		}
				
		// Checks if the inserted string (NAME) is of the correct length (1-45)
		if (name.length() <= 0 || name.length() > 45) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Chosen name invalid (a valid name has more than one character and less than 45)!");
			return null;
		}
				
		// Checks if the inserted string (SURNAME) is of the correct length (1-45)
		if (surname.length() <= 0 || surname.length() > 45) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Chosen surname invalid (a valid surname has more than one character and less than 45)!");
			return null;
		}
				
		// Checks if the inserted string (EMAIL) matches with an e-mail syntax (RCF5322 e-mail) by using a RegEx
		String emailRegEx = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";
				
		// If the string does not match the the user is redirected to the register page with an error message
		if (!email.matches(emailRegEx)) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Chosen email invalid!");
			return null;
		}
				
		// Checks if the inserted string (USERNAME) is of the correct length (1-45)
		if (username.length() <= 0 || username.length() > 45) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Chosen username invalid (a valid username has more than one character and less than 45)!");
			return null;
		}
				
		// Checks if the inserted strings (PASSWORD and REPEAT_PWD) are of the correct length (1-45) and equal
		if (password.length() <= 0 || password.length() > 45 || !password.equals(repeat_pwd)) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Chosen password invalid (a valid password has more than one character and less than 45)!");
			return null;
		}
				
		if (!password.equals(repeat_pwd)) {
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
			response.getWriter().println("Password and repeat password field not equal!");
			return null;
		}
				
		// CHECKS that the submitted user for the registration has not the SAME EMAIL
		// of another user in the DB.
		// If another user with the same email is present redirects to the to the RegisterPage
		// with a warning and error message
		UserDAO userDAO = new UserDAO(connection);
		User user = null;
				
		try {
					
			user = userDAO.findUserByEmail(email);
					
		} catch (SQLException e) {
					
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return null;	
		}
				
		if(user != null) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Chosen email already in use!");
			return null;
		}
				
		// CHECKS that the submitted user for the registration has not the SAME USERNAME
		// of another user in the DB.
		// If another user with the same username is present redirects to the to the RegisterPage
		// with a warning and error message
		try {
					
			user = userDAO.findUserByUsername(username);
					
		} catch (SQLException e) {
					
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return null;	
		}
				
		if(user != null) {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Chosen username already in use!");
			return null;
		}
				
		// TODO: remove BigDecimal and balance (account should be initialized at 0)
		// BigDecimal balance = new BigDecimal(10);
		
		// Creates the submitted user in the DB
		// If error are generated everything is forwarded to an errorPage
		try {
					
			//userDAO.registerUser(name, surname, email, username, password, balance);
			userDAO.registerUser(name, surname, email, username, password);
			
		} catch (SQLException e) {
					
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return null;	
		}
				
		// Gets the created user in the DB to verify it has been correctly created in the DB,
		// else if an Exception is raised forwards to the ErrorPage
		try {
					
			user = userDAO.findUserByEmail(email);
				
		} catch (SQLException e) {
					
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return null;	
		}
		
		if(user == null) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error: user non correctly created - registerPage!");
			return null;
		}
		
		// Gets the created account in the DB to verify it has been correctly created in the DB,
		// else if an Exception is raised forwards to the ErrorPage
		List<Account> accounts = null;
		AccountDAO accountDAO = new AccountDAO(connection);
		
		try {
									
			accounts = accountDAO.findAccountsByUserId(user.getId());
								
		} catch (SQLException e) {
									
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return null;	
		}
				
		if (accounts == null) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error: account non correctly created - registerPage!");
			return null;
		}
		
		return 0;
	}
}

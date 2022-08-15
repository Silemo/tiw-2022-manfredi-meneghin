package it.polimi.tiw.pureHTML.controllers;

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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.pureHTML.utils.*;
import it.polimi.tiw.pureHTML.dao.UserDAO;
import it.polimi.tiw.pureHTML.beans.User;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        
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
		
		// Gets the parameters of the request and verifies if they are in the correct format (length and syntax)
		String name       = request.getParameter("name");
		String surname    = request.getParameter("surname");
		String email      = request.getParameter("email");
		String username   = request.getParameter("username");
		String password   = request.getParameter("password");
		String repeat_pwd = request.getParameter("repeat_pwd");
		
		// Verifies if all parameters are not null
		if(name == null || surname == null || email == null || username == null || password == null || repeat_pwd == null) {
					
			forwardToErrorPage(request, response, "Register module missing some data");
			return;
		}
		
		// Checks if the inserted string (NAME) is of the correct length (1-45)
		if (name.length() <= 0 || name.length() > 45) {
			
			request.setAttribute("warning", "Chosen name invalid (a valid name has more than one character and less than 45)!");
			forward(request, response, PathHelper.pathToRegisterPage);
			return;
		}
		
		// Checks if the inserted string (SURNAME) is of the correct length (1-45)
		if (surname.length() <= 0 || surname.length() > 45) {
			
			request.setAttribute("warning", "Chosen surname invalid (a valid surname has more than one character and less than 45)!");
			forward(request, response, PathHelper.pathToRegisterPage);
			return;
		}
		
		// Checks if the inserted string (EMAIL) matches with an e-mail syntax (RCF2822 e-mail) by using a RegEx
		String emailRegEx = "^([a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?){3,320}$";
		
		// If the string does not match the the user is redirected to the register page with an error message
		if (!email.matches(emailRegEx)) {
			
			request.setAttribute("warning", "Chosen email invalid!");
			forward(request, response, PathHelper.pathToRegisterPage);
			return;
		}
		
		// Checks if the inserted string (USERNAME) is of the correct length (1-45)
		if (username.length() <= 0 || username.length() > 45) {
			
			request.setAttribute("warning", "Chosen username invalid (a valid username has more than one character and less than 45)!");
			forward(request, response, PathHelper.pathToRegisterPage);
			return;
		}
		
		// Checks if the inserted strings (PASSWORD and REPEAT_PWD) are of the correct length (1-45) and equal
		if (password.length() <= 0 || password.length() > 45 || !password.equals(repeat_pwd)) {
			
			request.setAttribute("warning", "Chosen password invalid (a valid password has more than one character and less than 45)!");
			forward(request, response, PathHelper.pathToRegisterPage);
			return;
		}
		
		if (!password.equals(repeat_pwd)) {
			
			request.setAttribute("warning", "Password and repeat password field not equal!");
			forward(request, response, PathHelper.pathToRegisterPage);
			return;
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
			
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
		
		if(user != null) {
			
			request.setAttribute("warning", "Chosen email already in use!");
			forward(request, response, PathHelper.pathToRegisterPage);
			return;
		}
		
		// CHECKS that the submitted user for the registration has not the SAME USERNAME
		// of another user in the DB.
		// If another user with the same username is present redirects to the to the RegisterPage
		// with a warning and error message
		try {
			
			user = userDAO.findUserByUsername(username);
			
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
		
		if(user != null) {
			
			request.setAttribute("warning", "Chosen username already in use!");
			forward(request, response, PathHelper.pathToRegisterPage);
			return;
		}
		
		// Creates the submitted user in the DB
		// If error are generated everything is forwarded to an errorPage
		try {
			
			userDAO.createUser(name, surname, email, username, password);
		
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
		
		// Gets the created user in the DB and adds it to the session and then redirects to HOME page,
		// else if an Exception is raised forwards to the ErrorPage
		try {
			
			user = userDAO.findUserByEmail(email);
		
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
	
		HttpSession session = request.getSession();
		session.setAttribute("currentUser", user);
		response.sendRedirect(getServletContext().getContextPath() + PathHelper.goToHomeServletPath);
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

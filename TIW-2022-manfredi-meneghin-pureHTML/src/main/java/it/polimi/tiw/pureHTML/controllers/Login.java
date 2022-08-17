package it.polimi.tiw.pureHTML.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.pureHTML.beans.User;
import it.polimi.tiw.pureHTML.dao.UserDAO;
import it.polimi.tiw.pureHTML.utils.*;

/**
 * Servlet implementation class Login
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"}, loadOnStartup = 1)
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
    	
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
	
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		// Verify if the given argument are null and if so forward to errorPage
		if(email == null || password == null) {
			
			forwardToErrorPage(request, response, "Null email or password");
			return;
		}
		
		// Query DB to authenticate user 
		// If user not present, forward to ErrorPage
		UserDAO userDAO = new UserDAO(connection);
		User user = null;
		
		try {
			
			user = userDAO.findUser(email, password);
			
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;
		}
		
		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message
		if(user == null) {
			
			request.setAttribute("warning", "Email or password incorrect!");
			forward(request, response, PathHelper.pathToLoginPage);
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
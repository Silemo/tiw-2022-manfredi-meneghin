package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.projects.beans.Account;
import it.polimi.tiw.projects.beans.Transfer;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.AccountDAO;
import it.polimi.tiw.projects.dao.TransferDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;
import it.polimi.tiw.projects.utils.PathHelper;
import it.polimi.tiw.projects.utils.TemplateHandler;

/**
 * Servlet implementation class GoToAccountStatus
 */
@WebServlet("/GoToAccountStatus")
public class GoToAccountStatus extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToAccountStatus() {
    	super();
    	// TODO Auto-generated constructor stub
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
		// Verifies if the given accountCode is valid, if not redirects to an error page
		String accountCodeString = request.getParameter("accountCode");
		
		if(accountCodeString == null) {
			
			forwardToErrorPage(request, response, "Null account code, when accessing account details");
		}
		
		int accountCode;
		
		try {
			
			accountCode = Integer.parseInt(accountCodeString);
			
		} catch (NumberFormatException e) {
			
			forwardToErrorPage(request, response, "Chosen account code is not a number, when accessing account details");
			return;
		}
		
		// Gets the currentUser from the session
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		
		// Gets the account selected by its code and then verifies if it exists and belongs to the current user
		AccountDAO accountDAO = new AccountDAO(connection);
		Account account;
		
		try {
			
			account = accountDAO.findAccountByCode(accountCode);
			
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;		
		}
		
		if(account == null || account.getUserId() != currentUser.getId()) {
			
			forwardToErrorPage(request, response, "Account not existing or not yours");
			return;
		}
		
		// Gets the list of transfers on the account, if the operation is successful saves them in the request and redirects
		List<Transfer> transfers;
		TransferDAO transferDAO = new TransferDAO(connection);
		
		try {
			
			transfers = transferDAO.findTransfersByAccountCode(accountCode);
			
		} catch (SQLException e) {
			
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
		
		request.setAttribute("account", account);
		request.setAttribute("transfers", transfers);
		forward(request, response, PathHelper.pathToAccountStatusPage);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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

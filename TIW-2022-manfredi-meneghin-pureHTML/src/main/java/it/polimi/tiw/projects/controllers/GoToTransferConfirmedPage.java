package it.polimi.tiw.projects.controllers;

import java.io.IOException;

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
import it.polimi.tiw.projects.utils.PathHelper;
import it.polimi.tiw.projects.utils.TemplateHandler;

/**
 * Servlet implementation class GoToTransferConfirmedPage
 */
@WebServlet("/GoToTransferConfirmedPage")
public class GoToTransferConfirmedPage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToTransferConfirmedPage() {
    	super();
    	// TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	
    	ServletContext servletContext = getServletContext();
		this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Gets the parameters of the transaction and verifies if they are null or not, if so redirects to an errorPage
		HttpSession session = request.getSession(false);
		Account  sourceAccount     = (Account) session.getAttribute("sourceAccount");
		Account  destAccount       = (Account) session.getAttribute("destAccount");
		Transfer transfer          = (Transfer)session.getAttribute("transfer");
		Boolean  transferInfoShown = (Boolean) session.getAttribute("transferInfoShown");
		
		if(sourceAccount == null || destAccount == null || transfer == null || transferInfoShown == null) {
			
			forwardToErrorPage(request, response, "No transfer to show");
			return;
		}
		
		// if all parameters are not null puts a "infoShownFlag" in the request and sets the informations as shown in the session
		request.setAttribute("transferInfoShown", transferInfoShown);
		
		if(!transferInfoShown) {
			
			session.setAttribute("transferInfoShown", true);
		}
		
		// then forwards to ConfirmedPage
		forward(request, response, PathHelper.pathToTransferConfirmedPage);
	
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

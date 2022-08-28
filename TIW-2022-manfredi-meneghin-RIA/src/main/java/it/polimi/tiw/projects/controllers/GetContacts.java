package it.polimi.tiw.projects.controllers;

import java.io.IOException;
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

import it.polimi.tiw.projects.beans.AddressBook;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.AddressBookDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

/**
 * Servlet implementation class GetContacts
 */
@WebServlet("/GetContacts")
@MultipartConfig
public class GetContacts extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private Connection connection;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetContacts() {
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
		
		HttpSession session = request.getSession(false);
		User currentUser    = (User) session.getAttribute("currentUser");
		
		AddressBookDAO addressBookDAO = new AddressBookDAO(connection);
		AddressBook addressBook;
		// Gets the addressBook from the DB
		try {
			
			addressBook = addressBookDAO.findAddressBookByOwnerId(currentUser.getId());
			
		} catch (SQLException e) {
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;
		}
		// Sends the addressBook to the client, serializing it using JSON
		String json = new Gson().toJson(addressBook.getContacts());
		
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

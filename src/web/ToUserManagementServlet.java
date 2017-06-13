package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Role;
import service.UserService;
import utils.AppException;

/**
 * Login Servlet
 */
public class ToUserManagementServlet extends HttpServlet {

	/**
	 *  Process the POST login request
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set request's character encoding
		request.setCharacterEncoding("UTF-8");
		//  Get login information
		String message = null;
		/*
		 *  Call methods in business logic layer to process business logic 
		 */
		//try {
		request.setAttribute("message", message);
		request.getRequestDispatcher("/login.jsp").forward(request,
						response);
				
		 // Save prompt message into request
				// Save user name into request	
				// Forward to login page				
		 /*catch (AppException e) {
			e.printStackTrace();
			// Redirect to exception page
			response.sendRedirect("toError");
		}*/
	}

	/**
	 * Process GET requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Call doPost() to process request
		this.doPost(request, response);
	}
}
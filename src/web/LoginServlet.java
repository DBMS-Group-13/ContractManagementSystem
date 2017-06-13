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
public class LoginServlet extends HttpServlet {

	/**
	 *  Process the POST login request
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set request's character encoding
		request.setCharacterEncoding("UTF-8");
		//  Get login information
		String name = request.getParameter("name");
		String password = request.getParameter("password");

		// Initialize userId
		int userId = -1;
		// Initialize prompt message
		String message = "";
		/*
		 *  Call methods in business logic layer to process business logic 
		 */
		try {
			// Initialize the user business logic class
			UserService userService = new UserService();
			// Call business logic layer for user login
			userId = userService.login(name, password);
			
			if (userId > 0) { // login successfully  
				//  Declare session
				HttpSession session = null;
				// Get session by using request
				session = request.getSession();
				// Save userId and user name into session
				session.setAttribute("userId", userId);
				session.setAttribute("userName", name);
				// Declare role
				Role role = null;
				//Call business logic layer to get role's information
				role = userService.getUserRole(userId);
				
				// Process page jump according to the user's role
				if ( role == null) {
					//Redirect to new user page					
					response.sendRedirect("toNewUser");
				} else if (role.getName().equals("admin")) {
					//Redirect to administrator page
					response.sendRedirect("toAdmin");
				} else if (role.getName().equals("operator")) {
					//Redirect to operator page 
					response.sendRedirect("toOperator");
				}
			} else {// Login failed
				// Set prompt message
				message = "Incorrect user name or password!";
				request.setAttribute("message", message); // Save prompt message into request
				// Save user name into request
				request.setAttribute("userName", name);	
				// Forward to login page
				request.getRequestDispatcher("/login.jsp").forward(request,
						response);
			}
		} catch (AppException e) {
			e.printStackTrace();
			// Redirect to exception page
			response.sendRedirect("toError");
		}
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
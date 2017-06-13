package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import service.UserService;
import utils.AppException;

/**
 * Login Servlet
 */
public class UserDeleteServlet extends HttpServlet {

	/**
	 *  Process the POST login request
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set request's character encoding
		request.setCharacterEncoding("UTF-8");
		String message = null;
		/*
		 *  Call methods in business logic layer to process business logic 
		 */
		try{
			List<User> users = new ArrayList<User>();
			UserService us = new UserService();
			int userId = (int) request.getAttribute("userId");
			if(us.deleteUser(userId)){
				users= us.getUsers();
				request.setAttribute("users", users);
				message = "Delete successfully";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/userManagement.jsp").forward(request,
						response);
				}
			else{
				users= us.getUsers();
				request.setAttribute("users", users);
				message = "Delete failed";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/userManagement.jsp").forward(request,
						response);
			}
		// Save prompt message into request
		// Save user name into request	
		// Forward to login page
		}
		 catch (AppException e) {
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
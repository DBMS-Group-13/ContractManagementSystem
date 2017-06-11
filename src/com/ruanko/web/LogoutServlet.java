package com.ruanko.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet that process user logout
 */
public class LogoutServlet extends HttpServlet{

	/**
	 * Process POST logout request
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Declare session
		HttpSession session = null;
		// Get session by using request object
		session = request.getSession();
		// Delete use information from session
		session.removeAttribute("userId");
		session.removeAttribute("userName");
		// Redirect to login page
		response.sendRedirect("toLogin");
	}

	/**
	 * Process the GET requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Call doPost() to process request
		this.doPost(request, response);
	}
	
}

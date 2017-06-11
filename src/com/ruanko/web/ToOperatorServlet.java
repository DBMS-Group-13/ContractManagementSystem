package com.ruanko.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for accessing contract operator page
 */
public class ToOperatorServlet extends HttpServlet {

	/**
	 * Jump to contract operator page
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set character set of request to "UTF-8"
		request.setCharacterEncoding("UTF-8");
		
		// Declare session
		HttpSession session = null;
		// Get session by using request object
		session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		// If the user is not logged in, then jump to the login page
		if (userId == null) {
			response.sendRedirect("toLogin");
		}else {
			// Forward to the contract operator page
			request.getRequestDispatcher("/frame2.jsp").forward(request, response);
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

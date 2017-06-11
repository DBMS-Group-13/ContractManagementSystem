package com.ruanko.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for accessing Registration page
 */
public class ToRegisterServlet extends HttpServlet {

	/**
	 * Process the POST requests, jump to Registration page
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// call doGet() to process request
		doGet(request, response);
	}

	/**
	 * Process the GET requests, jump to Registration page
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Forward to the registration page
		request.getRequestDispatcher("/register.jsp")
				.forward(request, response);
	}
}
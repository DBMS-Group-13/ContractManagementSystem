package com.ruanko.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for accessing exception page
 */
public class ToErrorServlet extends HttpServlet {

	/**
	 * Process POST requests, jump to exception page
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Call doGet() to process request
		doGet(request, response);
	}

	/**
	 * Process GET requests, jump to exception page
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Forward to exception page
		request.getRequestDispatcher("/error.jsp")
				.forward(request, response);
	}
}
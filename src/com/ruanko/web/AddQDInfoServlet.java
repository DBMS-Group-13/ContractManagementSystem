package com.ruanko.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruanko.model.ConProcess;
import com.ruanko.service.ContractService;
import com.ruanko.utils.AppException;
import com.ruanko.utils.Constant;

/**
 * Servlet for signing contract
 */
public class AddQDInfoServlet extends HttpServlet {

	/**
	 * Process Post requests of signing contract
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set the request's character encoding
		request.setCharacterEncoding("UTF-8");

		// Declare session
		HttpSession session = null;
		// Get session by using request
		session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		// If user is not login, jump to login page
		if (userId == null) {
			response.sendRedirect("toLogin");
		} else {

			// Get contract id
			int conId = Integer.parseInt(request.getParameter("conId"));
			// Get sign opinion
			String content = request.getParameter("content");

			// Instantiation conProcess object to encapsulate sign information 
			ConProcess conProcess = new ConProcess();
			conProcess.setConId(conId);
			conProcess.setUserId(userId);
			conProcess.setContent(content);

			/*
			 * Call business logic layer corresponding method to process business logic
			 */
			try {
				//  Initialize contractService
				ContractService contractService = new ContractService();
				// Call business logic layer to sign contract
				contractService.sign(conProcess);

				// After signed contract,redirect to page of contract to be signed
				response.sendRedirect("toDqdhtList");
			} catch (AppException e) {
				e.printStackTrace();
				// Redirect to the exception page
				response.sendRedirect("toError");
			}
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
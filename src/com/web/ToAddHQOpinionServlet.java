package com.ruanko.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruanko.model.Contract;
import com.ruanko.service.ContractService;
import com.ruanko.utils.AppException;

/**
 * Servlet for accessing countersign page
 */
public class ToAddHQOpinionServlet extends HttpServlet {

	/**
	 * Jump to countersign page
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set the request's character encoding
		request.setCharacterEncoding("UTF-8");
		
		// Declare session
		HttpSession session = null;
		// Get session by using request
		session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		// If user is not login, jump to login page
		if (userId == null) {
			response.sendRedirect("toLogin");
		} else {

			// Get contract id
			int conId = Integer.parseInt(request.getParameter("conId"));

			try {
				// Initialize contractService
				ContractService contractService = new ContractService();
				// Query contract information according to contract id
				Contract contract = contractService.getContract(conId);

				// Save contract to request
				request.setAttribute("contract", contract);
				//  Forward to countersign page
				request.getRequestDispatcher("/addHQOpinion.jsp").forward(
						request, response);
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

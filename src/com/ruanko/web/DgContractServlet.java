package com.ruanko.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruanko.model.Contract;
import com.ruanko.service.ContractService;
import com.ruanko.utils.AppException;

/**
 * Servlet for finalize contract
 */
public class DgContractServlet extends HttpServlet {

	/**
	 * Process Post requests of finalize contract
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
			// Get data information of contract
			int conId = Integer.parseInt(request.getParameter("conId"));
			String name = request.getParameter("name");
			String customer = request.getParameter("customer");
			String beginTime = request.getParameter("beginTime");
			String endTime = request.getParameter("endTime");
			String content = request.getParameter("content");

			// Instantiate begin and end of java.util.Date type,for accepting
			// transformed beginTime and endTime
			Date begin = new Date();
			Date end = new Date();

			// Define a date format object, transform the time of String type
			// into java.util.Date data type
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				begin = dateFormat.parse(beginTime);
				end = dateFormat.parse(endTime);

				// Build a Contract object and assign value for the object's
				// attribute
				Contract contract = new Contract();
				contract.setId(conId);
				contract.setName(name);
				contract.setCustomer(customer);
				contract.setBeginTime(begin);
				contract.setEndTime(end);
				contract.setContent(content);
				contract.setUserId(userId);

				// Initialize contractService
				ContractService contractService = new ContractService();
				// Call business logic layer to finalize contract
				contractService.finalize(contract);

				// After finalized contract,redirect to pending contract page
				response.sendRedirect("toDdghtList");
			} catch (ParseException e) {
				e.printStackTrace();
				// Initialize prompt message
				String message = "";
				message = "Please enter the correct date format!";
				// Save message to request
				request.setAttribute("message", message);
				// Forward to finalized contract page
				request.getRequestDispatcher("/dgContract.jsp").forward(
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

package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Customer;
import service.UserService;
import utils.AppException;

/**
 * Login Servlet
 */
public class CustomerDeleteServlet extends HttpServlet {

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
			List<Customer> customers = new ArrayList<Customer>();
			UserService us = new UserService();
			int customerId =  Integer.parseInt(request.getParameter("delCustomerId"));
			if(us.deleteCustomer(customerId)){
				customers= us.getCustomers();
				request.setAttribute("customers", customers);
				message = "Delete successfully";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/customerManagement.jsp").forward(request,
						response);
				}
			else{
				customers= us.getCustomers();
				request.setAttribute("customers", customers);
				message = "Delete failed";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/customerManagement.jsp").forward(request,
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
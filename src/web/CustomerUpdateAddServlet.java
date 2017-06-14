package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Customer;
import model.User;
import service.UserService;
import utils.AppException;

/**
 * Login Servlet
 */
public class CustomerUpdateAddServlet extends HttpServlet {

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
			Customer cs = new Customer();
			UserService us = new UserService();
			cs.setId(Integer.parseInt(request.getParameter("id")));
			cs.setNum(request.getParameter("num"));
			cs.setName(request.getParameter("name"));
			cs.setAddress(request.getParameter("address"));
			cs.setTel(request.getParameter("tel"));
			cs.setFax(request.getParameter("fax"));
			cs.setCode(request.getParameter("code"));
			cs.setBank(request.getParameter("bank"));
			cs.setAccount(request.getParameter("accout"));
			if(cs.getId() == -1)
				us.add(cs);
			else
				us.update(cs);
			customers= us.getCustomers();
			request.setAttribute("customers", customers);
			message = "Update successfully";
			request.setAttribute("message", message);
			request.getRequestDispatcher("/customerManagement.jsp").forward(request,
						response);			
			
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
package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import service.UserService;
import utils.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for accessing draft contract page
 */
public class ToDraftServlet extends HttpServlet {

	/**
	 * Jump to draft contract page
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//Set the request's character encoding
		request.setCharacterEncoding("UTF-8");
		
		// Declare session
		HttpSession session = null;
		// Get session by using request object
		session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		// If user is not login, jump to login page
		if (userId == null) {
			response.sendRedirect("toLogin");
		}else {
			// Forward to draft page
			List<Customer> customers = new ArrayList<Customer>();
			UserService us = new UserService();
			try {
				customers = us.getCustomers();
				request.setAttribute("customers",customers);
				request.getRequestDispatcher("/addContract.jsp").forward(request, response);
			} catch (AppException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Process GET requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// call doPost() to process request
		this.doPost(request, response);
	}

}

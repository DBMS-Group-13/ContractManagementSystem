package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ConBusiModel;
import model.Role;
import service.ContractService;
import service.UserService;
import utils.AppException;

/**
 * Servlet for accessing page of contract to be distributed 
 */
public class ToDfphtListServlet extends HttpServlet{

	/**
	 * Jump to page of contract to be distributed 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		// Set output content's type
		response.setContentType("text/html");
		// Set the response's character encoding
		response.setCharacterEncoding("UTF-8");
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
		}else {
			try {
				// Initialize userService
				UserService userService = new UserService();
				// Call business logic layer to get information corresponding to the user's role
				Role role = userService.getUserRole(userId);
				
				// Process page jump according to user's role
				if ( role == null) {
					//  Redirect to new user page
					response.sendRedirect("toNewUser");
				}else {
						// Initialize contractService
						ContractService contractService = new ContractService();
						// Initialize contractList
						List<ConBusiModel> contractList = new ArrayList<ConBusiModel>();
						// Call business logic layer to get list of contract to be distributed 
						contractList = contractService.getDfphtList();
						// Save contractList to request
						request.setAttribute("contractList", contractList);
						// Forward to page of contract to be distributed
						request.getRequestDispatcher("/dfphtList.jsp").forward(request, response);
					}
				
			} catch (AppException e) {
				e.printStackTrace();
				//Redirect to the exception page
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

package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ConBusiModel;
import service.ContractService;
import utils.AppException;

/**
 * Access page of contract to be countersigned
 */
public class ToSignedServlet extends HttpServlet{

	/**
	 * Jump to page of contract to be countersigned
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
		}else {
			
			try {
				// Initialize contractService
				ContractService contractService = new ContractService();
				// Initialize contractList
				List<ConBusiModel> contractList = new ArrayList<ConBusiModel>();
				// Call business logic layer to get list of contract to be approved 
				contractList = contractService.getProcess_SignedList(userId);
				// Save contractList to request
				request.setAttribute("contractList", contractList);
				// Forward to page of contract to be approved
				request.getRequestDispatcher("/approved.jsp").forward(request, response);
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

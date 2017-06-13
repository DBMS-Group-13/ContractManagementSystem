package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Contract;
import service.ContractService;
import utils.AppException;

/**
 * Servlet of accessing sign page
 */
public class ToAddQDInfoServlet extends HttpServlet {

	/**
	 * Jump to sign page
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set the request's character encoding
		request.setCharacterEncoding("UTF-8");
		
		// Declare session
		HttpSession session = null;
		// Get session by using request object
		session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		// If the user is not login, then jump to login page
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
				// Forward to sign page
				request.getRequestDispatcher("/addQDInformation.jsp").forward(
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

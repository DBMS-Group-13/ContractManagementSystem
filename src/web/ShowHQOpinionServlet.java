package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.CSignatureOpinion;
import service.ContractService;
import utils.AppException;

/**
 * Servlet for display countersign opinion
 */
public class ShowHQOpinionServlet extends HttpServlet {

	/**
	 * Process Post requests of displaying countersign opinion
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
			//  Get contract id
			int conId = Integer.parseInt(request.getParameter("conId"));
			
			try {
				// Initialize contractService
				ContractService contractService = new ContractService();
				//CSignatureOpinion CSignatureOpinion = new CSignatureOpinion();
				// Initialize csOpinionList
				List<CSignatureOpinion> csOpinionList = new ArrayList<CSignatureOpinion>();
				// Call business logic layer to get countersign opinion
				csOpinionList = contractService.showHQOpinion(conId);
				// Save conProcessList to request
				request.setAttribute("csOpinionList", csOpinionList);
				// Forward to countersign opinion page
				request.getRequestDispatcher("/showHQOpinion.jsp").forward(request,
						response);
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
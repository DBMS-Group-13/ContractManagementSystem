package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ConProcess;
import service.ContractService;
import utils.AppException;
import utils.Constant;

/**
 * Servlet for approve contract
 */
public class AddSHPOpinionServlet extends HttpServlet {

	/**
	 * Process Post requests of approving contract
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set the request's character encoding
		request.setCharacterEncoding("UTF-8");

		// Declare session
		HttpSession session = null;
		//  Get session by using request
		session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		// If user is not login, jump to login page
		if (userId == null) {
			response.sendRedirect("toLogin");
		} else {

			//  Get contract id
			int conId = Integer.parseInt(request.getParameter("conId"));
			// Get approval opinion
			String content = request.getParameter("content");
			// Get approval operation
			String approve = request.getParameter("approve");

			// Instantiation conProcess object to encapsulate approval information 
			ConProcess conProcess = new ConProcess();
			conProcess.setConId(conId);
			conProcess.setUserId(userId);
			conProcess.setContent(content);
			
			// Set value to state of approve type according to approving operation
			if (approve.equals("true")) { // Approve type is "pass"
				// Set "PROCESS_APPROVE" type corresponding state to "DONE"
				conProcess.setState(Constant.DONE);
			} else { //  Approve type is "refuse"
				// Set "PROCESS_APPROVE" type corresponding state to "VETOED"
				conProcess.setState(Constant.VETOED);
			}

			/*
			 * Call business logic layer corresponding method to process business logic
			 */
			try {
				//  Initialize contractService
				ContractService contractService = new ContractService();
				// Call business logic layer to approve contract
				contractService.approve(conProcess);

				// After approved contract,redirect to page of contract to be approved
				response.sendRedirect("toDshphtList");
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
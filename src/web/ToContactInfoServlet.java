package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ConDetailBusiModel;
import model.Customer;
import service.ContractService;
import service.UserService;
import utils.AppException;

/**
 * Login Servlet
 */
public class ToContactInfoServlet extends HttpServlet {

	/**
	 *  Process the POST login request
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set request's character encoding
		request.setCharacterEncoding("UTF-8");
		
		/*
		 *  Call methods in business logic layer to process business logic 
		 */
		try{
			ContractService cs = new ContractService();
			List<ConDetailBusiModel> conds = new ArrayList<ConDetailBusiModel>();
			conds= cs.getConBusis();
			request.setAttribute("conds", conds);
			request.getRequestDispatcher("/contractInfo.jsp").forward(request,
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
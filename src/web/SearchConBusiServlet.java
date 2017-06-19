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
import model.User;
import service.ContractService;
import service.UserService;
import utils.AppException;
import utils.Constant;

/**
 * Servlet for assigning contract
 */
public class SearchConBusiServlet extends HttpServlet {

	/**
	 * Process result of assign contrct
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set the request's character encoding
		request.setCharacterEncoding("UTF-8");
		ContractService cs = new ContractService();
		List<ConBusiModel> contracts = new ArrayList<ConBusiModel>();
		List<ConBusiModel> contractsRes = new ArrayList<ConBusiModel>();
		HttpSession session = null;
		// Get session by using request
		session = request.getSession();
		contracts = (List<ConBusiModel>) session.getAttribute("contractList");
		String name = request.getParameter("searchname");
		String jsp = request.getParameter("jspname");
		contractsRes = cs.SearchConBusiModel(contracts, name);
		// Save user name into request
		request.setAttribute("contractList", contractsRes);	
		System.out.println(jsp);
		// Forward to login page
		request.getRequestDispatcher(jsp).forward(request,
				response);
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

package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import service.ContractService;
import service.UserService;
import utils.AppException;
import utils.Constant;

/**
 * Servlet for assigning contract
 */
public class SearchListServlet extends HttpServlet {

	/**
	 * Process result of assign contrct
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set the request's character encoding
		request.setCharacterEncoding("UTF-8");
		ContractService cs = new ContractService();
		List<User> users = new ArrayList<User>();
		List<User> usersRes = new ArrayList<User>();
		users = (List<User>) request.getAttribute("users");
		String name =  (String) request.getAttribute("name");
		usersRes = cs.SearchUser(users, name);
		// Save user name into request
		request.setAttribute("users", usersRes);	
		// Forward to login page
		request.getRequestDispatcher("/userManagement.jsp").forward(request,
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

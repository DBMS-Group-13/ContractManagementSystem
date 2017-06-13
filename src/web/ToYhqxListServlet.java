package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.PermissionBusiModel;
import service.UserService;
import utils.AppException;

/**
 * Access page of user permission list
 */
public class ToYhqxListServlet extends HttpServlet{

	/**
	 * Jump to page of user permission list
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		// Set character set of request to "UTF-8"
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
				// Initialize permissionList
				List<PermissionBusiModel> permissionList = new ArrayList<PermissionBusiModel>();
				// Call business logic layer to get user permission list
				permissionList = userService.getYhqxList();
				// Save newUserList to request
				request.setAttribute("permissionList", permissionList);
				// Forward to page of user permission list
				request.getRequestDispatcher("/yhqxList.jsp").forward(request, response);
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

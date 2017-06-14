package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Role;
import model.User;
import service.UserService;
import utils.AppException;

/**
 * Login Servlet
 */
public class RoleDeleteServlet extends HttpServlet {

	/**
	 *  Process the POST login request
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set request's character encoding
		request.setCharacterEncoding("UTF-8");
		String message = null;
		/*
		 *  Call methods in business logic layer to process business logic 
		 */
		try{
			List<Role> roles = new ArrayList<Role>();
			UserService us = new UserService();
			int roleID =  Integer.parseInt(request.getParameter("delRoleId"));
			if(us.deleteRole(roleID)){
				roles= us.getRoleList();
				request.setAttribute("roles", roles);
				message = "Delete successfully";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/roleManagement.jsp").forward(request,
						response);
				}
			else{
				roles= us.getRoleList();
				request.setAttribute("roles", roles);
				message = "Delete failed";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/roleManagement.jsp").forward(request,
						response);
			}
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
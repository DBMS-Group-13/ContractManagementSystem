package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Role;
import service.UserService;
import utils.AppException;

/**
 * Login Servlet
 */
public class RoleUpdateAddSerlet extends HttpServlet {

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
			Role r = new Role();
			UserService us = new UserService();
			r.setId(Integer.parseInt(request.getParameter("id")));
			r.setName(request.getParameter("name"));
			r.setDescription(request.getParameter("description"));
			String[] funcs = request.getParameterValues("funcIds");
			String funcids = null;
			for(int i = 0;i<funcs.length;i++){
				if(i == funcs.length -1)
					funcids = funcids+funcs[i];
				funcids = funcids+funcs[i]+",";
			}
			r.setFuncIds(funcids);
			if(r.getId() == -1)
				us.addRole(r);
			else
				us.updateRole(r);
			roles= us.getRoleList();
			request.setAttribute("roles", roles);
			message = "Update successfully";
			request.setAttribute("message", message);
			request.getRequestDispatcher("/roleManagement.jsp").forward(request,
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
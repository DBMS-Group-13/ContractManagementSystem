package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for accessing new user page 
 */
public class ToNewUserServlet extends HttpServlet{
	
	/**
	 * Jump to new user page
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// SSet request's character encoding
		request.setCharacterEncoding("UTF-8");
		
		// Declare session
		HttpSession session = null;
		// Get session by using request object
		session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		// If user is not login,jump to login page
		if (userId == null) {
			response.sendRedirect("toLogin");
		}else {
			// Forward to the user page
			System.out.println(2);
			request.getRequestDispatcher("/newUser.jsp").forward(request, response);
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
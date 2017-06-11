package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for accessing administrator page
 */
public class ToAdminServlet extends HttpServlet {

	/**
	 * Jump to administrator page
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
		}else {
			// Forwarded to contract administrator page
			request.getRequestDispatcher("/frame1.jsp").forward(request, response);
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

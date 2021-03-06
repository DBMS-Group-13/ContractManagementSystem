package web;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import service.UserService;
import utils.AppException;
import utils.MailUtil;

/**
 * Servlet for registration
 */
public class ToActivateUserServlet extends HttpServlet {

	/**
	 * Process the register request
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
		String message = null;
		UserService user = new UserService();
		String email = request.getParameter("email");
		String token = request.getParameter("token");
		Long time = System.currentTimeMillis();
		User u = user.loadByEmail(email);
		if (u != null) {
			if (u.getStatus() == 0 && u.getActivateTime() != 0) {
				if (u.getActivateTime() < time) {
					// 过期--激活失败
					u.setActivateTime(Long.parseLong("-1"));
					// 重新发送激活邮件
					u = MailUtil.activateMail(u);
					// 重新设置了有效时间和token激活码
					user.updateUser(u);
				} else if (u.getActivateTime() > time) {
					// 在时间内
					u.setActivateTime(Long.parseLong("1"));
					if (u.getToken().equals(token)) {
						// 在时间内且激活码通过，激活成功
						u.setStatus(1);
						u.setCreateDate(new Date().toString());
						// 重新设置token防止被禁用的用户利用激活
						u.setToken(token.replace("1", "c"));
						user.updateUser(u);
						// resp.getWriter().write(JsonUtil.toJson(u));
						message = "Activate successfully！";
						request.setAttribute("message", message);
						request.getRequestDispatcher("/result.jsp").forward(request,
								response);
					} else {
						// 在时间内但是激活码错误
						message = "The activation code is wrong！";
						request.setAttribute("message", message);
						request.getRequestDispatcher("/result.jsp").forward(request,
								response);
					}
				}
				// u.getStatus()!=1判断结束
			} else if (u.getStatus() == 1) {
				// 已经被激活的重复点链接
				message = "The user has been activated！";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/result.jsp").forward(request,
						response);
			}
			// u为空
		} else if (u == null) {
			message = "Wrong user！";
			request.setAttribute("message", message);
			request.getRequestDispatcher("/result.jsp").forward(request,
					response);
		}
		}
		catch(AppException | NoSuchAlgorithmException | MessagingException e){
			e.printStackTrace();
			// Redirect to exception page
			response.sendRedirect("toError");
		}
	}

	/**
	 * Process the GET requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Call doPost() to process request
		this.doPost(request, response);
	}
}
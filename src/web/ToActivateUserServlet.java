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
			if (u.getStatus() == 0 && u.getActivateTime() != 1) {
				if (u.getActivateTime() < time) {
					// ����--����ʧ��
					u.setActivateTime(Long.parseLong("-1"));
					// ���·��ͼ����ʼ�
					u = MailUtil.activateMail(u);
					// ������������Чʱ���token������
					user.updateUser(u);
				} else if (u.getActivateTime() > time) {
					// ��ʱ����
					u.setActivateTime(Long.parseLong("1"));
					if (u.getToken().equals(token)) {
						// ��ʱ�����Ҽ�����ͨ��������ɹ�
						u.setStatus(1);
						u.setCreateDate(new Date().toString());
						// ��������token��ֹ�����õ��û����ü���
						u.setToken(token.replace("1", "c"));
						user.updateUser(u);
						// resp.getWriter().write(JsonUtil.toJson(u));
					} else {
						// ��ʱ���ڵ��Ǽ��������
						message = "The activation code is wrong��";
						request.setAttribute("message", message);
						request.getRequestDispatcher("/error.jsp").forward(request,
								response);
					}
				}
				// u.getStatus()!=1�жϽ���
			} else if (u.getStatus() == 1) {
				// �Ѿ���������ظ�������
				message = "The user has been activated��";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/error.jsp").forward(request,
						response);
			}
			// uΪ��
		} else if (u == null) {
			message = "Wrong user��";
			request.setAttribute("message", message);
			request.getRequestDispatcher("/error.jsp").forward(request,
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
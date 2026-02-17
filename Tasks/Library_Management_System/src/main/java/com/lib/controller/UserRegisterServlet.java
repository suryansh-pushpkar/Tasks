package com.lib.controller;

import java.io.IOException;

import javax.management.RuntimeErrorException;

import com.lib.dao.UserDao;
import com.lib.entity.User;
import com.lib.util.EmailUtil;
import com.lib.util.UserIdAssigner;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserRegisterServlet extends HttpServlet {

	private UserDao dao = new UserDao();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

		String name = request.getParameter("name");
		String mail = request.getParameter("mail");
		String password = request.getParameter("password");
		User user = new User();
		user.setName(name);
		user.setMail(mail);
		user.setPassword(password);

		user.setMembershipNo(UserIdAssigner.assignUniqueId(user));
		dao.saveUser(user);
		EmailUtil.sendWelcomeEmail(user.getMail(), user.getName(), user.getMembershipNo(), user.getPassword());

		HttpSession session = request.getSession();
		session.setAttribute("currentUser", user);
		session.setAttribute("role", user.getRole());
		session.setMaxInactiveInterval(60 * 60 * 6);
		response.sendRedirect("userDashboard.jsp");
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}

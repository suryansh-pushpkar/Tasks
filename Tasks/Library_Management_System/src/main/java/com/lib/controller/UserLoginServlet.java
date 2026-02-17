package com.lib.controller;

import java.io.IOException;

import com.lib.dao.UserDao;
import com.lib.entity.User;
import com.lib.util.EmailUtil;
import com.lib.util.UserIdAssigner;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserLoginServlet extends HttpServlet {
	UserDao dao = new UserDao();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

		String membershipNo = request.getParameter("membershipNo");
		String password = request.getParameter("password");
		
		User user = dao.userLogin(membershipNo, password);
		
		if(user != null) {
		HttpSession session = request.getSession();
		session.setAttribute("currentUser", user.getMembershipNo());
		session.setAttribute("currentEmail", user.getMail());
		session.setAttribute("role", user.getRole());
		session.setMaxInactiveInterval(60 * 60 * 6);
		response.sendRedirect("userDashboard.jsp");
		}else {
			response.sendRedirect("index.jsp");
		}
		}catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("index.jsp");
		}
	}

}

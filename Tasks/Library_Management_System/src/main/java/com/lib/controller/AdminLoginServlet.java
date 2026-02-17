package com.lib.controller;

import com.lib.dao.AdminDao;
import com.lib.entity.Admin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;


public class AdminLoginServlet extends HttpServlet {

	private AdminDao adminDao;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String membershipNo = request.getParameter("membershipNo");
		String password = request.getParameter("password");

		Admin admin = adminDao.login(membershipNo, password);

		if (admin != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("currentAdmin", admin);
			session.setAttribute("role", "ADMIN");

			response.sendRedirect("admindashboard.jsp");
		} else {
			response.sendRedirect("adminlogin.jsp");
		}
	}
}
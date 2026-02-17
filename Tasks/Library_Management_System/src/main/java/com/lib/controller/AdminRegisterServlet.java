package com.lib.controller;

import java.io.IOException;

import com.lib.dao.AdminDao;
import com.lib.entity.Admin;
import com.lib.util.AdminIdAssigner;
import com.lib.util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminRegisterServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter("name");
		String email = request.getParameter("mail");
		String password = request.getParameter("password");
		String address = request.getParameter("address");

		Admin admin = new Admin();
		admin.setName(name);
		admin.setMail(email);
		admin.setPassword(password);
		admin.setAddress(address);

		try {
			AdminIdAssigner.assignUniqueId(admin);
			EmailUtil.sendWelcomeEmail(admin.getMail(), admin.getName(), admin.getMembershipNo(), admin.getPassword());

			AdminDao dao = new AdminDao();
			if (dao.register(admin)) {

				HttpSession session = request.getSession();
				session.setAttribute("currentAdmin", admin);
				session.setAttribute("role", "ADMIN");
				session.setMaxInactiveInterval(60 * 60*6);
				response.sendRedirect("createlib.jsp");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("register.jsp?error=RegistrationFailed");
		}
	}
}

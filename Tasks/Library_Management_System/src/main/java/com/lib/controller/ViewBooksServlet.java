package com.lib.controller;

import java.io.IOException;
import java.util.List;

import com.lib.dao.BookDao;
import com.lib.entity.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ViewBooksServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		Admin currentAdmin = (Admin) (session != null ? session.getAttribute("currentAdmin") : null);

		if (currentAdmin == null) {
			response.sendRedirect("adminlogin.jsp");
			return;
		}

		try {
			int libId = currentAdmin.getLibrary().getId();

			BookDao bookDao = new BookDao();
			List<Object[]> groupedBooks = bookDao.getBooksWithQuantities(libId);

			request.setAttribute("groupedBooks", groupedBooks);
			request.getRequestDispatcher("viewBooks.jsp").forward(request, response);

		} catch (Exception e) {
			response.sendRedirect("admindashboard.jsp?error=Data fetch failed");
		}
	}
}
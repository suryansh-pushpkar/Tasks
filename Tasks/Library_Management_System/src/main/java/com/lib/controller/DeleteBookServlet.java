package com.lib.controller;

import java.io.IOException;

import com.lib.dao.BookDao;
import com.lib.entity.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DeleteBookServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id= Integer.parseInt(request.getParameter("id"));

		HttpSession session = request.getSession();
		Admin admin = (Admin) session.getAttribute("currentAdmin");

		if (id !=0) {
			BookDao bDao = new BookDao();
			try {
				bDao.deleteBook(id);
				response.sendRedirect("viewBooks.jsp?msg=Books deleted successfully");
			} catch (Exception e) {
				response.sendRedirect("viewBooks.jsp?error=Cannot delete books currently issued.");
			}
		}
	}
}
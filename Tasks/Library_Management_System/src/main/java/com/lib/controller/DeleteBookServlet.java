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
		String name = request.getParameter("name");
		String author = request.getParameter("author");

		HttpSession session = request.getSession();
		Admin admin = (Admin) session.getAttribute("currentAdmin");

		if (admin != null && name != null && author != null) {
			BookDao bDao = new BookDao();
			try {
				bDao.deleteBooksByNameAndAuthor(name, author, admin.getLibrary().getId());
				response.sendRedirect("ViewBooksServlet?msg=Books deleted successfully");
			} catch (Exception e) {
				response.sendRedirect("ViewBooksServlet?error=Cannot delete books currently issued.");
			}
		}
	}
}
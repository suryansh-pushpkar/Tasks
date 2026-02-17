package com.lib.controller;

import java.io.IOException;
import java.util.List;

import com.lib.dao.BookDao;
import com.lib.entity.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserDashboardServlet extends HttpServlet {
	private BookDao bookDao = new BookDao(); // Assuming your getBooks method is here

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int page = 1;
		int pageSize = 10;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}

		List<Book> books = bookDao.getBooks(page, pageSize);

		// 3. Set data as Request Attributes
		request.setAttribute("bookList", books);
		request.setAttribute("currentPage", page);

		// 4. Forward to the JSP (Forward keeps the data alive, Redirect kills it)
		request.getRequestDispatcher("userDashboard.jsp").forward(request, response);
	}
}
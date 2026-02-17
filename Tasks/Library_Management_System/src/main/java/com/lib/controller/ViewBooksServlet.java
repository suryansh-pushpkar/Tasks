package com.lib.controller;

import java.io.IOException;
import java.util.List;
import com.lib.dao.BookDao;
import com.lib.entity.Library;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ViewBooksServlet extends HttpServlet {

	private final BookDao bookDao = new BookDao();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		Library currentLibrary = (Library) (session != null ? session.getAttribute("currentLibrary") : null);

		if (currentLibrary == null) {
			response.sendRedirect("admindashboard.jsp?error=access_denied");
			return;
		}

		try {
			List<Object[]> groupedBooks = (List<Object[]>) bookDao.getBooksWithQuantities(currentLibrary.getId());

			request.setAttribute("groupedBooks", groupedBooks);

			request.getRequestDispatcher("viewBooks.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("adminDashboard.jsp?error=data_fetch_error");
		}
	}
}
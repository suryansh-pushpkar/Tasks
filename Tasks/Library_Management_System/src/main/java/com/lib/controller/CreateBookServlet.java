package com.lib.controller;

import java.io.IOException;
import com.lib.dao.BookDao;
import com.lib.entity.Book;
import com.lib.entity.Library;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CreateBookServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		Library currentLibrary = (Library) (session != null ? session.getAttribute("currentLibrary") : null);

		if (currentLibrary == null) {
			response.sendRedirect("admindashboard.jsp?error=No library found in session");
			return;
		}

		String name = request.getParameter("name");
		String author = request.getParameter("author");
		String isbn = request.getParameter("isbn");

		Book book = new Book();
		book.setName(name);
		book.setAuthor(author);
		book.setIsbn(isbn);
		book.setLibrary(currentLibrary);

		try {
			BookDao dao = new BookDao();
			Book dbbook = dao.saveBook(book);
			response.sendRedirect("admindashboard.jsp?msg=Book added successfully");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("addBook.jsp?error=Database error occurred");
		}
	}
}
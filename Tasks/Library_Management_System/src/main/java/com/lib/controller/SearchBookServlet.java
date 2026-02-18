package com.lib.controller;

import com.lib.dao.BookDao;
import com.lib.entity.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SearchBookServlet extends HttpServlet {
	private final BookDao bookDao = new BookDao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String query = request.getParameter("query");
		List<Book> books = bookDao.searchBooks(query);

		StringBuilder html = new StringBuilder();
		if (books.isEmpty()) {
			html.append("<p class='text-muted p-3'>No books found matching '" + query + "'</p>");
		} else {
			for (Book b : books) {
				html.append("<div class='list-group-item d-flex justify-content-between align-items-center'>")
						.append("<div><strong>" + b.getName() + "</strong><br><small>" + b.getAuthor()
								+ "</small></div>")
						.append("<a href='issueBook.jsp?bookId=" + b.getId()
								+ "' class='btn btn-sm btn-primary'>Issue</a>")
						.append("</div>");
			}
		}
		response.setContentType("text/html");
		response.getWriter().write(html.toString());
	}
}
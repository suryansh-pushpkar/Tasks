package com.lib.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.lib.dao.BookDao;
import com.lib.entity.Book;
import java.io.IOException;

public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookDao bookDAO;

	@Override
	public void init() {
		bookDAO = new BookDao();
	}

	// Displays the Edit Form (Called when Admin clicks "Edit" in view-books.jsp)
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idStr = request.getParameter("id");
		if (idStr != null) {
			try {
				Long id = Long.parseLong(idStr);
				Book existingBook = bookDAO.findById(Integer.parseInt(idStr));

				request.setAttribute("book", existingBook);
				request.getRequestDispatcher("editBook.jsp").forward(request, response);
			} catch (NumberFormatException e) {
				response.sendRedirect("viewBooks?error=InvalidID");
			}
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("bookId"));
		String name = request.getParameter("bookName");
		String author = request.getParameter("author");
		String edition = request.getParameter("edition");
		Long quantity = Long.parseLong(request.getParameter("quantity"));

		Book bookToUpdate = new Book();
		bookToUpdate.setId(id);
		bookToUpdate.setName(name);
		bookToUpdate.setAuthor(author);
		bookToUpdate.setEdition(edition);
		bookToUpdate.setQuantity(quantity);

		bookDAO.updateBook(bookToUpdate);

		response.sendRedirect("viewBooks.jsp?msg=Book Updated Successfully");
	}
}
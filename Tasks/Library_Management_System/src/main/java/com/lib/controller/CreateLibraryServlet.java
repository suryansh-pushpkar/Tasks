package com.lib.controller;

import com.lib.dao.AdminDao;
import com.lib.dao.LibraryDao;
import com.lib.entity.Admin;
import com.lib.entity.Library;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class CreateLibraryServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		Admin currentAdmin = (Admin) (session != null ? session.getAttribute("currentAdmin") : null);

		if (currentAdmin == null) {
			response.sendRedirect("adminlogin.jsp?error=unauthorized");
			return;
		}

		String libName = request.getParameter("libraryName");

		Library library = new Library();
		library.setName(libName);

		library.setOwner(currentAdmin);

		try {
			LibraryDao dao = new LibraryDao();
			Library dblib = dao.createLib(library);
			if (dblib != null) {

				session.setAttribute("currentLibrary", library);

				response.sendRedirect("admindashboard.jsp");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("createLibrary.jsp?error=creation_failed");
		}
	}
}
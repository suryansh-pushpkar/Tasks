package com.lib.controller;

import com.lib.dao.BookDao;
import com.lib.dao.IssueRecordDao;
import com.lib.entity.IssueRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReturnBookServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idStr = request.getParameter("id");
		if (idStr == null) {
			response.sendRedirect("admindashboard.jsp");
			return;
		}

		int id = Integer.parseInt(idStr);
		IssueRecordDao dao = new IssueRecordDao();
		IssueRecord record = new IssueRecord();

		try {
			String resultMessage = dao.returnBook(id);
			BookDao bDao = new BookDao();
			bDao.updateQuantity(record.getBook().getId(), 1);
			dao.update(record);
			response.sendRedirect("admindashboard.jsp?msg=Book returned and stock restored");
			response.sendRedirect("admindashboard.jsp?msg=" + resultMessage);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("admindashboard.jsp?error=Return processing failed");
		}
	}
}
package com.lib.controller;

import com.lib.dao.BookDao;
import com.lib.dao.IssueRecordDao;
import com.lib.entity.Book;
import com.lib.entity.IssueRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class ApproveIssueServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			IssueRecordDao dao = new IssueRecordDao();
			IssueRecord record = dao.findById(id);

			if (record == null) {
				response.sendRedirect("admindashboard.jsp?error=Record Not Found");
				return; 
			}

			BookDao bDao = new BookDao();
			Book book = record.getBook();

			if (book.getQuantity() > 0) {
				bDao.updateQuantity(book.getId(), -1);

				record.setStatus("ISSUED");
				dao.update(record);

				response.sendRedirect("admindashboard.jsp?msg=Request Approved and stock updated");
				return; 
			} else {
				response.sendRedirect("admindashboard.jsp?error=Out of stock!");
				return; 
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (!response.isCommitted()) {
				response.sendRedirect("admindashboard.jsp?error=Approval Failed");
			}
		}
	}
}
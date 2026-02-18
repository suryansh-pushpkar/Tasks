package com.lib.controller;

import com.lib.dao.IssueRecordDao;
import com.lib.entity.IssueRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class ApproveRenewalServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idStr = request.getParameter("id");
		if (idStr == null) {
			response.sendRedirect("admindashboard.jsp");
			return;
		}

		try {
			int recordId = Integer.parseInt(idStr);
			IssueRecordDao irDao = new IssueRecordDao();
			IssueRecord record = irDao.findById(recordId);

			if (record != null) {
				if (irDao.isBookRequestedByOthers(record.getBook().getId())) {
					response.sendRedirect("admindashboard.jsp?error=Book is reserved by another member.");
				} else {
					boolean success = irDao.updateRenewalDate(recordId, 14);
					if (success) {
						response.sendRedirect("admindashboard.jsp?msg=Renewal approved!");
					} else {
						response.sendRedirect("admindashboard.jsp?error=Update failed.");
					}
				}
			}
		} catch (Exception e) {
			response.sendRedirect("admindashboard.jsp?error=Server error");
		}
	}
}
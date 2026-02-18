package com.lib.controller;

import java.io.IOException;

import com.lib.dao.IssueRecordDao;
import com.lib.entity.IssueRecord;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RenewBooServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int recordId = Integer.parseInt(request.getParameter("id"));
		IssueRecordDao dao = new IssueRecordDao();
		IssueRecord record = dao.findById(recordId);
		if (record == null) {
			response.sendRedirect("admindashboard.jsp?error=Record Not Found");
			return;
		}

		boolean success = dao.updateRenewalDate(recordId, 14);
		if (success) {
			response.sendRedirect("admindashboard.jsp?msg=Book Renewed");
		} else {
			response.sendRedirect("admindashboard.jsp?msg=Something went wrongs");
		}
	}
}

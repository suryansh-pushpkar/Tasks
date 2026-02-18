package com.lib.controller;

import com.lib.dao.IssueRecordDao;
import com.lib.entity.IssueRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RejectRenewalServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));
		IssueRecordDao irDao = new IssueRecordDao();

		try {
			IssueRecord record = irDao.findById(id);
			if (record != null) {
				record.setStatus("ISSUED");
				irDao.update(record);
				response.sendRedirect("admindashboard.jsp?msg=Renewal Request Declined");
			}
		} catch (Exception e) {
			response.sendRedirect("admindashboard.jsp?error=Action Failed");
		}
	}
}
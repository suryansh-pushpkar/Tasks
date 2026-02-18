package com.lib.controller;

import com.lib.dao.IssueRecordDao;
import com.lib.entity.IssueRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApproveIssueServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));
		IssueRecordDao dao = new IssueRecordDao();

		try {
			IssueRecord record = dao.findById(id); 
			if (record != null) {
				record.setStatus("ISSUED");
				dao.update(record); 
				response.sendRedirect("admindashboard.jsp?msg=Request Approved");
			}
		} catch (Exception e) {
			response.sendRedirect("admindashboard.jsp?error=Approval Failed");
		}
	}
}
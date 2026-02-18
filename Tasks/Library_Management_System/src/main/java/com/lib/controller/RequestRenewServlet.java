package com.lib.controller;

import com.lib.dao.IssueRecordDao;
import com.lib.entity.IssueRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestRenewServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));
		IssueRecordDao dao = new IssueRecordDao();

		try {
			IssueRecord record = dao.findById(id);
			if (record != null && "ISSUED".equals(record.getStatus())) {
				record.setStatus("RENEW_REQUESTED");
				dao.update(record);
				response.sendRedirect("userDashboard.jsp?msg=Renewal request sent to admin.");
			} else {
				response.sendRedirect("userDashboard.jsp?error=Cannot request renewal at this time.");
			}
		} catch (Exception e) {
			response.sendRedirect("userDashboard.jsp?error=Request failed.");
		}
	}
}
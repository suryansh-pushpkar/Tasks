package com.lib.controller;

import com.lib.dao.IssueRecordDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RejectIssueServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));
		IssueRecordDao dao = new IssueRecordDao();

		try {
			dao.delete(id); 
			response.sendRedirect("admindashboard.jsp?msg=Request Rejected");
		} catch (Exception e) {
			response.sendRedirect("admindashboard.jsp?error=Rejection Failed");
		}
	}
}
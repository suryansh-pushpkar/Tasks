package com.lib.controller;

import com.lib.dao.BookDao;
import com.lib.dao.IssueRecordDao;
import com.lib.entity.Book;
import com.lib.entity.IssueRecord;
import com.lib.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.temporal.ChronoUnit;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ConfirmIssueServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("User") == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		User user = (User) session.getAttribute("User");

		try {
			int bookId = Integer.parseInt(request.getParameter("bookId"));
			String startDateStr = request.getParameter("startDate");
			String endDateStr = request.getParameter("endDate");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = sdf.parse(startDateStr);
			Date endDate = sdf.parse(endDateStr);

			long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
			long diffDays = ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant());
			if (endDate.before(startDate) || diffDays > 60) {
				response.sendRedirect("issueBook.jsp?bookId=" + bookId + "&error=Invalid date range selection.");
				return;
			}

			IssueRecordDao irDao = new IssueRecordDao();
			long activeCount = irDao.getActiveBookCount(user.getId());

			if (activeCount >= 5) {
				response.sendRedirect("userDashboard.jsp?error=Limit Reached! Maximum 5 active requests allowed.");
				return;
			}

			BookDao bDao = new BookDao();
			Book book = bDao.findById(bookId);

			IssueRecord record = new IssueRecord();
			record.setBook(book);
			record.setUser(user);
			record.setStartDate(startDate);
			record.setEndDate(endDate);
			record.setStatus("PENDING");

			irDao.save(record);

			response.sendRedirect("userDashboard.jsp?msg=Request sent successfully for " + book.getName());

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("userDashboard.jsp?error=Process failed: " + e.getMessage());
		}
	}
}
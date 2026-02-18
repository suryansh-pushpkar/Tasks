package com.lib.controller;

import com.lib.dao.BookDao;
import com.lib.dao.IssueRecordDao;
import com.lib.dao.UserDao;
import com.lib.entity.Book;
import com.lib.entity.IssueRecord;
import com.lib.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmIssueServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		// Safety check for session
		if (session == null || session.getAttribute("currentUser") == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		String mNo = (String) session.getAttribute("currentUser");
		String mail = (String) session.getAttribute("currentEmail");

		try {
			int bookId = Integer.parseInt(request.getParameter("bookId"));
			String endDateStr = request.getParameter("endDate");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date endDate = sdf.parse(endDateStr);

			UserDao uDao = new UserDao();
			User user = uDao.getUserByMnoAndEmail(mNo, mail);

			IssueRecordDao irDao = new IssueRecordDao();

			long activeCount = irDao.getActiveBookCount(user.getId());
			if (activeCount >= 5) {
				response.sendRedirect(
						"userdashboard.jsp?error=Limit Reached! You cannot have more than 5 active books or pending requests.");
				return;
			}

			BookDao bDao = new BookDao();
			Book book = bDao.findById(bookId);

			IssueRecord record = new IssueRecord();
			record.setBook(book);
			record.setUser(user);
			record.setStartDate(new Date());
			record.setEndDate(endDate);
			record.setStatus("PENDING");

			irDao.save(record);

			response.sendRedirect("userDashboard.jsp?msg=Request sent! Please wait for Admin approval.");

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("userDashboard.jsp?error=Request failed: " + e.getMessage());
		}
	}
}
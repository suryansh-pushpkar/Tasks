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
	    
	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        
	        Date userStart = sdf.parse(request.getParameter("startDate"));
	        Date userEnd = sdf.parse(request.getParameter("endDate"));
	        
	        Date serverToday = new Date(); 
	        serverToday = sdf.parse(sdf.format(serverToday)); 

	        if (userStart.before(serverToday)) {
	            response.sendRedirect("issueBook.jsp?error=Cannot pick a date in the past!");
	            return;
	        }

	        long diff = userEnd.getTime() - userStart.getTime();
	        long days = diff / (1000 * 60 * 60 * 24);
	        
	        if (days > 60) {
	            response.sendRedirect("issueBook.jsp?error=Duration exceeds 60 days limit!");
	            return;
	        }

	        IssueRecord record = new IssueRecord();
	        record.setBook(new BookDao().findById(Integer.parseInt(request.getParameter("bookId"))));
	        record.setUser((User) request.getSession().getAttribute("currentUser"));
	        record.setStartDate(userStart);
	        record.setEndDate(userEnd);
	        record.setStatus("PENDING");

	        new IssueRecordDao().save(record);
	        response.sendRedirect("userDashboard.jsp?msg=Request submitted for approval!");

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendRedirect("userDashboard.jsp?error=Processing error.");
	    }
	}
}
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.lib.entity.User"%>
<%@ page import="com.lib.entity.Book"%>
<%@ page import="com.lib.dao.UserDao"%>
<%@ page import="com.lib.dao.BookDao"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<%
    // 1. Session Check
    String mNo = (String) session.getAttribute("currentUser");
    String mail = (String) session.getAttribute("currentEmail");

    if (mNo == null || mail == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // 2. Fetch User Details
    UserDao uDao = new UserDao();
    User user = uDao.getUserByMnoAndEmail(mNo, mail);
    
    // 3. Handle Pagination & Fetch Books
    int currentPage = 1;
    int pageSize = 5; 
    
    if(request.getParameter("page") != null) {
        try {
            currentPage = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) {
            currentPage = 1;
        }
    }

    BookDao bDao = new BookDao();
    // Ensure getBooks uses LEFT JOIN FETCH in its HQL to avoid Lazy Loading errors
    List<Book> books = bDao.getBooks(currentPage, pageSize);
    
    // 4. Set attributes for JSTL
    pageContext.setAttribute("bookList", books);
    pageContext.setAttribute("currentPage", currentPage);
%>

<!DOCTYPE html>
<html>
<head>
<title>Library Dashboard</title>
<style>
body {
	font-family: 'Segoe UI', Arial, sans-serif;
	background-color: #f4f7f6;
	padding: 20px;
}

.card {
	background: white;
	padding: 25px;
	border-radius: 12px;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
	max-width: 1000px;
	margin: auto;
}

.welcome-box {
	background: linear-gradient(135deg, #007bff, #0056b3);
	color: white;
	padding: 20px;
	border-radius: 8px;
	margin-bottom: 25px;
}

.welcome-box h2 {
	margin: 0;
	font-size: 24px;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 10px;
}

th, td {
	border-bottom: 1px solid #eee;
	padding: 15px;
	text-align: left;
}

th {
	background: #f8f9fa;
	color: #333;
	text-transform: uppercase;
	font-size: 13px;
}

tr:hover {
	background-color: #fafafa;
}

.btn-issue {
	background: #28a745;
	color: white;
	border: none;
	padding: 8px 16px;
	cursor: pointer;
	border-radius: 4px;
	transition: 0.3s;
}

.btn-issue:hover {
	background: #218838;
}

.status-available {
	color: #28a745;
	font-weight: bold;
}

.status-issued {
	color: #dc3545;
	font-weight: bold;
}

.pagination {
	margin-top: 20px;
	display: flex;
	justify-content: center;
	gap: 10px;
	align-items: center;
}

.page-link {
	padding: 8px 16px;
	background: #eee;
	text-decoration: none;
	color: #333;
	border-radius: 4px;
}

.page-link:hover {
	background: #007bff;
	color: white;
}
</style>
</head>
<body>

	<div class="card">
		<div class="welcome-box">
			<h2>
				Welcome,
				<%= (user != null) ? user.getName() : "User" %>!
			</h2>
			<p>
				Membership No: <strong><%= mNo %></strong>
			</p>
		</div>

		<h3>Available Books</h3>

		<table>
			<thead>
				<tr>
					<th>Title</th>
					<th>Author</th>
					<th>Status</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="book" items="${bookList}">
					<tr>
						<td><strong><c:out value="${book.name}" /></strong></td>
						<td><c:out value="${book.author}" /></td>
						<td><c:choose>
								<%-- If issueRecords is null or empty, the book is available --%>
								<c:when test="${empty book.issueRecords}">
									<span class="status-available">Available</span>
								</c:when>
								<c:otherwise>
									<span class="status-issued">Issued</span>
								</c:otherwise>
							</c:choose></td>
						<td><c:if test="${empty book.issueRecords}">
								<form action="issueBook" method="post">
									<input type="hidden" name="bookId" value="${book.id}">
									<button type="submit" class="btn-issue">Issue Book</button>
								</form>
							</c:if></td>
					</tr>
				</c:forEach>

				<c:if test="${empty bookList}">
					<tr>
						<td colspan="4"
							style="text-align: center; padding: 30px; color: #888;">No
							books found in the database.</td>
					</tr>
				</c:if>
			</tbody>
		</table>

		<div class="pagination">
			<c:if test="${currentPage > 1}">
				<a href="userDashboard.jsp?page=${currentPage - 1}"
					class="page-link">Previous</a>
			</c:if>
			<span>Page <strong>${currentPage}</strong></span> <a
				href="userDashboard.jsp?page=${currentPage + 1}" class="page-link">Next</a>
		</div>
	</div>
	<div class="card" style="margin-top: 30px; border-top: 4px solid #28a745;">
    <h3>My Issued Books & Requests</h3>
    <table>
        <thead>
            <tr style="background-color: #e9ecef;">
                <th>Book Title</th>
                <th>Issue Date</th>
                <th>Return Date</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="record" items="${myRecords}">
                <tr>
                    <td><strong>${record.book.name}</strong></td>
                    <td>
                        <c:out value="${record.startDate != null ? record.startDate : '---'}" />
                    </td>
                    <td>
                        <c:out value="${record.endDate != null ? record.endDate : '---'}" />
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${record.status == 'PENDING'}">
                                <span style="color: orange; font-weight: bold;">Waiting for Admin</span>
                            </c:when>
                            <c:when test="${record.status == 'ISSUED'}">
                                <span style="color: green; font-weight: bold;">Active</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: gray;">${record.status}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            
            <c:if test="${empty myRecords}">
                <tr>
                    <td colspan="4" style="text-align: center; color: #888; padding: 20px;">
                        You haven't requested or issued any books yet.
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

</body>
</html>
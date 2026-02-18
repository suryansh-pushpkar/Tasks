<%@page import="com.lib.entity.Library"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.lib.entity.Admin"%>
<%@ page import="com.lib.dao.IssueRecordDao"%>
<%@ page import="com.lib.dao.BookDao"%>

<%@ page import="com.lib.entity.IssueRecord"%>
<%@ page import="java.util.List"%>



<%
BookDao bDao = new BookDao();

long totalBooks = bDao.getTotalBookCount();
long availableBooks = bDao.getAvailableBookCount();
Admin currentAdmin = (Admin) session.getAttribute("currentAdmin");
if (currentAdmin == null) {
	response.sendRedirect("adminlogin.jsp");
	return;
}

IssueRecordDao irDao = new IssueRecordDao();

// Data for the two tables
List<IssueRecord> activeIssues = irDao.getAllPendingReturns(); // Status: ISSUED
List<IssueRecord> pendingRequests = irDao.getPendingRequests(); // Status: PENDING

String action = request.getParameter("action");
if ("logout".equals(action)) {
	session.invalidate();
	response.sendRedirect("adminlogin.jsp");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/partials/__bootstrap.jsp"%>
<title>Admin Dashboard | Library Manager</title>
<style>
body {
	background-color: #f8f9fa;
}

.sidebar {
	min-height: 100vh;
	background: #212529;
	color: white;
	padding-top: 20px;
}

.sidebar a {
	color: #adb5bd;
	text-decoration: none;
	padding: 10px 20px;
	display: block;
}

.sidebar a:hover {
	background: #343a40;
	color: white;
}

.sidebar a.active {
	background: #007bff;
	color: white;
}

.stat-card {
	border: none;
	border-radius: 10px;
	transition: transform 0.3s;
}

.stat-card:hover {
	transform: translateY(-5px);
}

.action-btn {
	border-radius: 50px;
	font-weight: 600;
}

.badge-request {
	background-color: #ffc107;
	color: #000;
}
</style>
</head>
<body>
	<% if (request.getParameter("msg") != null) { %>
	<div class="alert alert-success alert-dismissible fade show">
		<strong>Success!</strong>
		<%= request.getParameter("msg") %>
		<button type="button" class="close" data-dismiss="alert">&times;</button>
	</div>
	<% } %>
	<div class="container-fluid">
		<div class="row">
			<nav class="col-md-2 d-none d-md-block sidebar">
				<div class="text-center mb-4">
					<h5 class="text-white">Library Admin</h5>
					<small class="text-muted"><%=currentAdmin.getMembershipNo()%></small>
				</div>
				<a href="adminDashboard.jsp" class="active">Dashboard</a> <a
					href="viewBooks.jsp">Books Inventory</a> <a href="issueBook.jsp">Issue
					a Book</a>
				<hr class="bg-secondary">
				<a href="admindashboard.jsp?action=logout" class="text-danger">Logout</a>
			</nav>

			<main class="col-md-10 ml-sm-auto px-4 py-4">
				<div
					class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
					<h1 class="h2">
						Welcome,
						<%=currentAdmin.getName()%>!
					</h1>
				</div>

				<div class="row mb-4">
					<div class="col-md-3">
						<div class="card stat-card bg-primary text-white shadow">
							<div class="card-body">
								<h6>Total Books</h6>
								<h2><%=totalBooks%></h2>
							</div>
						</div>
					</div>

					<div class="col-md-3">
						<div class="card stat-card bg-success text-white shadow">
							<div class="card-body">
								<h6>Available Copies</h6>
								<h2><%=availableBooks%></h2>
							</div>
						</div>
					</div>

					<div class="col-md-3">
						<div class="card stat-card bg-warning text-dark shadow">
							<div class="card-body">
								<h6>Issued Books</h6>
								<h2><%=activeIssues.size()%></h2>
							</div>
						</div>
					</div>

					<div class="col-md-3">
						<div class="card stat-card bg-danger text-white shadow">
							<div class="card-body">
								<h6>New Requests</h6>
								<h2><%=pendingRequests.size()%></h2>
							</div>
						</div>
					</div>
				</div>

				<div class="row mt-4">
					<div class="col-md-12">
						<div class="card shadow-sm border-0">
							<div
								class="card-header bg-white d-flex justify-content-between align-items-center">
								<h5 class="mb-0 font-weight-bold text-warning">
									<i class="fas fa-clock mr-2"></i>Pending Issue Requests
								</h5>
								<span class="badge badge-pill badge-request"><%=pendingRequests.size()%>
									Requests</span>
							</div>
							<div class="card-body">
								<div class="table-responsive">
									<table class="table table-hover">
										<thead class="thead-light">
											<tr>
												<th>Member</th>
												<th>Book Title</th>
												<th>Request Date</th>
												<th>Required Till</th>
												<th>Actions</th>
											</tr>
										</thead>
										<tbody>
											<%
											if (!pendingRequests.isEmpty()) {
												for (IssueRecord req : pendingRequests) {
											%>
											<tr>
												<td><strong><%=req.getUser().getName()%></strong><br>
													<small class="text-muted"><%=req.getUser().getMembershipNo()%></small></td>
												<td><%=req.getBook().getName()%></td>
												<td><%=req.getStartDate()%></td>
												<td><%=req.getEndDate()%></td>
												<td><a href="approveIssue?id=<%=req.getId()%>"
													class="btn btn-sm btn-success px-3">Approve</a> <a
													href="rejectIssue?id=<%=req.getId()%>"
													class="btn btn-sm btn-outline-danger ml-1">Reject</a></td>
											</tr>
											<%
											}
											} else {
											%>
											<tr>
												<td colspan="5" class="text-center text-muted py-3">No
													pending requests found.</td>
											</tr>
											<%
											}
											%>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="row mt-4">
					<div class="col-md-12">
						<div class="card shadow-sm border-0">
							<div
								class="card-header bg-white d-flex justify-content-between align-items-center">
								<h5 class="mb-0 font-weight-bold text-primary">
									<i class="fas fa-book mr-2"></i>Current Active Loans
								</h5>
								<span class="badge badge-pill badge-primary"><%=activeIssues.size()%>
									Books Out</span>
							</div>
							<div class="card-body">
								<div class="table-responsive">
									<table class="table table-hover">
										<thead class="thead-light">
											<tr>
												<th>Book Title</th>
												<th>Issued To</th>
												<th>Issue Date</th>
												<th>Due Date</th>
												<th>Action</th>
											</tr>
										</thead>
										<tbody>
											<%
											if (!activeIssues.isEmpty()) {
												for (IssueRecord record : activeIssues) {
											%>
											<tr>
												<td><strong><%=record.getBook().getName()%></strong></td>
												<td><%=record.getUser().getName()%></td>
												<td><%=record.getStartDate()%></td>
												<td>
													<%
													boolean isOverdue = new java.util.Date().after(record.getEndDate());
													%> <span
													class="<%=isOverdue ? "text-danger font-weight-bold" : ""%>">
														<%=record.getEndDate()%> <%=isOverdue ? "<i class='fas fa-exclamation-triangle'></i>" : ""%>
												</span>
												</td>
												<td><a href="returnBook?id=<%=record.getId()%>"
													class="btn btn-sm btn-outline-success">Mark Returned</a></td>
											</tr>
											<%
											}
											} else {
											%>
											<tr>
												<td colspan="5" class="text-center text-muted py-3">No
													active loans found.</td>
											</tr>
											<%
											}
											%>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="row mt-5">
					<div class="col-md-6">
						<div class="card shadow-sm mb-4">
							<div class="card-header bg-white font-weight-bold">Books
								Management</div>
							<div class="card-body text-center">
								<p class="text-muted">Manage your library catalog here.</p>
								<a href="addBook.jsp"
									class="btn btn-outline-dark action-btn m-1">Add New Book</a> <a
									href="viewBooks.jsp"
									class="btn btn-outline-dark action-btn m-1">View/Edit
									Inventory</a>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="card shadow-sm mb-4">
							<div class="card-header bg-white font-weight-bold">Circulation</div>
							<div class="card-body text-center">
								<p class="text-muted">Manage circulation activities.</p>
								<a href="issueBook.jsp" class="btn btn-primary action-btn m-1">Manual
									Issue</a> <a href="renewBook.jsp"
									class="btn btn-info action-btn text-white m-1">Renew Issue
									Date</a>
							</div>
						</div>
					</div>
				</div>
			</main>
		</div>
	</div>
</body>
</html>
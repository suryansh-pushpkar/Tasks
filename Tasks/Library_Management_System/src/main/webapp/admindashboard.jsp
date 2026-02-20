<%@page import="com.lib.entity.Library"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.lib.entity.Admin, com.lib.entity.IssueRecord, com.lib.dao.IssueRecordDao, com.lib.dao.BookDao, java.util.List"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    if (session == null || session.getAttribute("currentAdmin") == null) {
        response.sendRedirect("adminlogin.jsp");
        return;
    }
%>
<%
Admin currentAdmin = (Admin) session.getAttribute("currentAdmin");
if (currentAdmin == null) {
	response.sendRedirect("adminlogin.jsp");
	return;
}

int libId = currentAdmin.getLibrary().getId();
String libName = currentAdmin.getLibrary().getName();

BookDao bDao = new BookDao();
IssueRecordDao irDao = new IssueRecordDao();

long totalBooks = bDao.getTotalBookCountByLibrary(libId);
long availableBooks = bDao.getAvailableBookCountByLibrary(libId);

List<IssueRecord> activeIssues = irDao.getActiveIssuesByLibrary(libId);
List<IssueRecord> pendingRequests = irDao.getPendingRequestsByLibrary(libId);
List<IssueRecord> renewalRequests = irDao.getRenewalRequestsByLibrary(libId);

// Logout Action
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
<title><%=libName%> | Admin Dashboard</title>
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
</style>
</head>
<body>
	<%
	if (request.getParameter("msg") != null) {
	%>
	<div class="alert alert-success alert-dismissible fade show m-3">
		<strong>Success!</strong>
		<%=request.getParameter("msg")%>
		<button type="button" class="close" data-dismiss="alert">&times;</button>
	</div>
	<%
	}
	%>

	<div class="container-fluid">
		<div class="row">
			<%-- Sidebar --%>
			<nav class="col-md-2 d-none d-md-block sidebar">
				<div class="text-center mb-4">
					<h5 class="text-white"><%=libName%></h5>
					<small class="text-muted">Admin: <%=currentAdmin.getName()%></small>
				</div>
				<a href="admindashboard.jsp" class="active"><i
					class="fas fa-tachometer-alt mr-2"></i>Dashboard</a> <a
					href="viewBooks"><i class="fas fa-book mr-2"></i>Inventory</a> <a
					href="#issueRequests"><i class="fas fa-clipboard-list mr-2"></i>Requests</a>
				<hr class="bg-secondary">
				<a href="admindashboard.jsp?action=logout" class="text-danger"><i
					class="fas fa-sign-out-alt mr-2"></i>Logout</a>
			</nav>

			<main class="col-md-10 ml-sm-auto px-4 py-4">
				<div
					class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
					<h1 class="h2">Dashboard Overview</h1>
				</div>

				<%-- Stat Cards --%>
				<div class="row mb-4">
					<div class="col-md-3">
						<div class="card stat-card bg-primary text-white shadow">
							<div class="card-body">
								<h6>Total Catalog</h6>
								<h2><%=totalBooks%></h2>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="card stat-card bg-success text-white shadow">
							<div class="card-body">
								<h6>Available Now</h6>
								<h2><%=availableBooks%></h2>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="card stat-card bg-warning text-dark shadow">
							<div class="card-body">
								<h6>Currently Loaned</h6>
								<h2><%=activeIssues.size()%></h2>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="card stat-card bg-danger text-white shadow">
							<div class="card-body">
								<h6>Pending Approval</h6>
								<h2><%=pendingRequests.size()%></h2>
							</div>
						</div>
					</div>
				</div>

				<%-- Pending Issue Requests --%>
				<div class="row mt-4" id="issueRequests">
					<div class="col-12">
						<div class="card shadow-sm border-0">
							<div
								class="card-header bg-white d-flex justify-content-between align-items-center">
								<h5 class="mb-0 font-weight-bold text-danger">New Issue
									Requests</h5>
								<span class="badge badge-pill badge-danger"><%=pendingRequests.size()%></span>
							</div>
							<div class="card-body">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>Member</th>
											<th>Book Title</th>
											<th>Request Date</th>
											<th>Action</th>
										</tr>
									</thead>
									<tbody>
										<%
										if (!pendingRequests.isEmpty()) {
											for (IssueRecord req : pendingRequests) {
										%>
										<tr>
											<td><strong><%=req.getUser().getName()%></strong><br>
											<small><%=req.getUser().getMembershipNo()%></small></td>
											<td><%=req.getBook().getName()%></td>
											<td><%=req.getStartDate()%></td>
											<td><a href="approveIssue?id=<%=req.getId()%>"
												class="btn btn-sm btn-success">Approve</a> <a
												href="rejectIssue?id=<%=req.getId()%>"
												class="btn btn-sm btn-outline-danger">Reject</a></td>
										</tr>
										<%
										}
										} else {
										%>
										<tr>
											<td colspan="4" class="text-center py-3 text-muted">No
												pending requests.</td>
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

				<%-- Renewal Requests --%>
				<div class="row mt-4">
					<div class="col-12">
						<div class="card shadow-sm border-0">
							<div
								class="card-header bg-info text-white d-flex justify-content-between align-items-center">
								<h5 class="mb-0 font-weight-bold">Renewal Requests</h5>
								<span class="badge badge-pill badge-light"><%=renewalRequests.size()%></span>
							</div>
							<div class="card-body">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>Member</th>
											<th>Book</th>
											<th>Current Due Date</th>
											<th>Action</th>
										</tr>
									</thead>
									<tbody>
										<%
										if (!renewalRequests.isEmpty()) {
											for (IssueRecord req : renewalRequests) {
										%>
										<tr>
											<td><%=req.getUser().getName()%></td>
											<td><%=req.getBook().getName()%></td>
											<td><span class="text-danger"><%=req.getEndDate()%></span></td>
											<td><a href="approveRenewal?id=<%=req.getId()%>"
												class="btn btn-sm btn-success">Approve</a> <a
												href="rejectRenewal?id=<%=req.getId()%>"
												class="btn btn-sm btn-outline-secondary">Decline</a></td>
										</tr>
										<%
										}
										} else {
										%>
										<tr>
											<td colspan="4" class="text-center py-3 text-muted">No
												renewal requests.</td>
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

				<%-- Active Loans --%>
				<div class="row mt-4">
					<div class="col-12">
						<div class="card shadow-sm border-0">
							<div
								class="card-header bg-white d-flex justify-content-between align-items-center border-bottom">
								<h5 class="mb-0 font-weight-bold text-primary">Current
									Active Loans</h5>
							</div>
							<div class="card-body">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>Book Title</th>
											<th>Issued To</th>
											<th>Due Date</th>
											<th>Action</th>
										</tr>
									</thead>
									<tbody>
										<%
										if (!activeIssues.isEmpty()) {
											for (IssueRecord record : activeIssues) {
												boolean isOverdue = new java.util.Date().after(record.getEndDate());
										%>
										<tr>
											<td><strong><%=record.getBook().getName()%></strong></td>
											<td><%=record.getUser().getName()%></td>
											<td><span
												class="<%=isOverdue ? "text-danger font-weight-bold" : ""%>">
													<%=record.getEndDate()%> <%=isOverdue ? "⚠️" : ""%></span></td>
											<td><a href="returnBook?id=<%=record.getId()%>"
												class="btn btn-sm btn-outline-success">Mark Return</a></td>
										</tr>
										<% } } else { %>
										<tr>
											<td colspan="4" class="text-center py-3 text-muted">No
												active loans.</td>
										</tr>
										<% } %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</main>
		</div>
	</div>
</body>
</html>
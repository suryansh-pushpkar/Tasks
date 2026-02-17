<%@page import="com.lib.entity.Library"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.lib.entity.Admin"%>
<%
Admin currentAdmin = (Admin) session.getAttribute("currentAdmin");
if (currentAdmin == null) {
	response.sendRedirect("adminlogin.jsp");
	return;
}
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
</style>
</head>
<body>

	<div class="container-fluid">
		<div class="row">
			<nav class="col-md-2 d-none d-md-block sidebar">
				<div class="text-center mb-4">
					<h5 class="text-white">Library Admin</h5>
					<small class="text-muted"><%=currentAdmin.getMembershipNo()%></small>
				</div>
				<a href="adminDashboard.jsp" class="active">Dashboard</a> <a
					href="manageBooks.jsp">Books Inventory</a> <a href="issueBook.jsp">Issue
					a Book</a> <a href="viewUsers.jsp">Manage Members</a>
				<hr class="bg-secondary">
				<a href="logout" class="text-danger">Logout</a>
			</nav>

			<main class="col-md-10 ml-sm-auto px-4 py-4">
				<div
					class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
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
								<h2>245</h2>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="card stat-card bg-success text-white shadow">
							<div class="card-body">
								<h6>Available Copies</h6>
								<h2>182</h2>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="card stat-card bg-warning text-dark shadow">
							<div class="card-body">
								<h6>Issued Books</h6>
								<h2>63</h2>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="card stat-card bg-danger text-white shadow">
							<div class="card-body">
								<h6>Pending Returns</h6>
								<h2>12</h2>
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
							<div class="card-header bg-white font-weight-bold">Circulation
								(Issuing & Renewals)</div>
							<div class="card-body text-center">
								<p class="text-muted">Handle user requests and dates.</p>
								<a href="issueBook.jsp" class="btn btn-primary action-btn m-1">Issue
									New Book</a> <a href="renewBook.jsp"
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
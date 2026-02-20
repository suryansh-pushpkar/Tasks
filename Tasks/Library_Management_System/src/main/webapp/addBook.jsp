<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.lib.entity.Library"%>
<%
Library currentLibrary = (Library) session.getAttribute("currentLibrary");
if (currentLibrary == null) {
	response.sendRedirect("admindashboard.jsp?error=create_library_first");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>

<%@ include file="/partials/__bootstrap.jsp"%>
<title>Add New Book | LibManager</title>
<style>
body {
	background-color: #f4f7f6;
}

.form-card {
	max-width: 600px;
	margin: 50px auto;
	border-radius: 15px;
	border: none;
}
</style>
</head>
<body>
	<div class="container">
		<div class="card form-card shadow-sm">
			<div class="card-header bg-dark text-white">
				<h4 class="mb-0">Add Book to Catalog</h4>
				<small>Adding to: <%=currentLibrary.getName()%></small>
			</div>
			<div class="card-body">
				<form action="addBook" method="POST">
					<div class="form-group">
						<label>Book Title</label> <input type="text" name="name"
							class="form-control" required>
					</div>
					<div class="form-group">
						<label>Author Name</label> <input type="text" name="author"
							class="form-control" required>
					</div>
					<div class="form-group">
						<label>ISBN Number</label> <input type="text" name="isbn"
							class="form-control" placeholder="e.g. 978-3-16-148410-0">
					</div>
					<button type="submit" class="btn btn-dark btn-block mt-4">Save
						Book to Inventory</button>
					<a href="adminDashboard.jsp"
						class="btn btn-link btn-block text-muted">Cancel and Back</a>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
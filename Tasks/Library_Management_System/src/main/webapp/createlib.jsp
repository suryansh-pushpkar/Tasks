<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.lib.entity.Admin"%>
<%
Admin currentAdmin = (Admin) session.getAttribute("currentAdmin");
if (currentAdmin == null) {
	response.sendRedirect("adminlogin.jsp");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/partials/__bootstrap.jsp"%>
<title>Setup Your Library</title>
<style>
body {
	background-color: #212529;
	min-height: 100vh;
	display: flex;
	align-items: center;
	color: #fff;
}

.setup-card {
	width: 100%;
	max-width: 450px;
	margin: auto;
	border-radius: 15px;
	background: #fff;
	color: #333;
	padding: 30px;
}
</style>
</head>
<body>

	<div class="container">
		<div class="card setup-card shadow-lg">
			<div class="text-center mb-4">
				<h3 class="font-weight-bold">Establish Library</h3>
				<p class="text-muted small">Step 2: Name your Library</p>
			</div>

			<form action="createLibrary" method="POST">
				<div class="form-group">
					<label class="font-weight-bold">Library Name</label> <input
						type="text" name="libraryName" class="form-control"
						placeholder="e.g. Central City Library" required>
				</div>

				<div class="form-group">
					<label class="font-weight-bold">Founder / Owner</label> <input
						type="text" class="form-control"
						value="<%=currentAdmin.getName()%>" readonly> <small
						class="text-muted">Linked to your Admin ID: <%=currentAdmin.getMembershipNo()%></small>
				</div>

				<button type="submit" class="btn btn-primary btn-block btn-lg mt-4">Initialize
					Library</button>
			</form>
		</div>
	</div>

</body>
</html>
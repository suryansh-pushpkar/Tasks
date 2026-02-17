<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/partials/__bootstrap.jsp"%>
<title>Library Manager: Admin Login</title>
<style>
body {
	background-color: #343a40; /* Darker background for admin */
	height: 100vh;
	display: flex;
	align-items: center;
}

.admin-card {
	width: 100%;
	max-width: 400px;
	padding: 15px;
	margin: auto;
	border-radius: 10px;
	box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
}
</style>
</head>
<body>

	<div class="container">
		<div class="card admin-card">
			<div class="card-body">
				<h3 class="text-center mb-2">Admin Panel</h3>
				<p class="text-center text-muted small mb-4">Authorized
					Personnel Only</p>

				<form action="adminLogin" method="POST">
					<div class="form-group">
						<label for="adminUser">Enter Membership Number</label> <input type="text"
							class="form-control" id="adminUser" name="membershipNo"
							placeholder="ADXXXNNNN" required>
					</div>

					<div class="form-group">
						<label for="adminPass">Password</label> <input type="password"
							class="form-control" id="adminPass" name="password"
							placeholder="••••••••" required>
					</div>

					<button type="submit" class="btn btn-dark btn-block">Log
						In as Admin</button>
				</form>

				<div class="text-center mt-4">
					<a href="index.jsp" class="small text-secondary">&larr; Back to
						Member Login</a>
				</div>
				<div class="text-center mt-4">
					<a href="adminregister.jsp" class="small text-secondary">Register as Admin </a>
				</div>
			</div>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
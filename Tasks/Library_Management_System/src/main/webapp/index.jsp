<html>
<head>
<%@ include file="/partials/__bootstrap.jsp"%>
<title>Library Manager: Login</title>
</head>
<style>
body {
	background-color: #f8f9fa;
	height: 100vh;
	display: flex;
	align-items: center;
}

.login-card {
	width: 100%;
	max-width: 400px;
	padding: 15px;
	margin: auto;
	border-radius: 10px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>
</head>
<body>

	<div class="container">
		<div class="card login-card">
		<a href="adminlogin.jsp">Admin?</a>
			<div class="card-body">
				<h3 class="text-center mb-4">Member Login</h3>

				<form action="login" method="POST">
					<div class="form-group">
						<label for="membershipNo">Membership Number</label> <input
							type="text" class="form-control" id="membershipNo"
							name="membershipNo" placeholder="Enter your ID" required>
					</div>

					<div class="form-group">
						<label for="password">Password</label> <input type="password"
							class="form-control" id="password" name="password"
							placeholder="password" required>
					</div>



					<button type="submit" class="btn btn-primary btn-block">Sign
						In</button>
				</form>

				<div class="text-center mt-3">
					<small><a href="register.jsp">New User? Register</a></small>
				</div>
			</div>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>

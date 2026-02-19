<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/partials/__bootstrap.jsp"%>
<meta charset="UTF-8">
<title>Library Manager: Admin Registration</title>
<style>
body {
	background-color: #212529;
	min-height: 100vh;
	display: flex;
	align-items: center;
	color: #fff;
}

.admin-register-card {
	width: 100%;
	max-width: 500px;
	padding: 20px;
	margin: auto;
	border-radius: 15px;
	background: #ffffff;
	color: #333;
	box-shadow: 0 15px 35px rgba(0, 0, 0, 0.4);
}

.admin-header {
	border-bottom: 2px solid #343a40;
	margin-bottom: 20px;
	padding-bottom: 10px;
}
</style>
</head>
<body>

	<div class="container">
		<div class="card admin-register-card">
			<div class="card-body">
				<div class="admin-header text-center">
					<h3 class="font-weight-bold text-dark">Admin Registration</h3>
				</div>

				<form id="adminRegForm" action="adminRegister" method="POST">
					<div class="form-group">
						<label for="name" class="font-weight-bold">Admin Full Name</label>
						<input type="text" class="form-control" id="name" name="name"
							placeholder="Enter full name" required>
					</div>

					<div class="form-group">
						<label for="mail" class="font-weight-bold">Official Email</label>
						<input type="email" class="form-control" id="mail" name="mail"
							placeholder="admin@library.com" required>
					</div>

					<div class="form-group">
						<label for="password" class="font-weight-bold">Admin
							Password</label> <input type="password" class="form-control"
							id="password" name="password"
							placeholder="Min 8 characters + Special Char" required>
					</div>

					<div class="form-group">
						<label for="confirmPassword" class="font-weight-bold">Confirm
							Password</label> <input type="password" class="form-control"
							id="confirmPassword" placeholder="Repeat password" required>
					</div>


					<div class="form-group">
						<label for="address" class="font-weight-bold">Office/Residential
							Address</label> <input type="text" class="form-control" id="address"
							name="address" placeholder="Enter your complete address" required>
					</div>


					<button type="submit" class="btn btn-dark btn-block btn-lg mt-4">Create
						Admin Account</button>
				</form>

				<div class="text-center mt-4">
					<p class="mb-0 small">
						Already have an admin account? <a href="adminlogin.jsp"
							class="text-primary font-weight-bold">Login here</a>
					</p>
					<a href="index.jsp" class="text-muted small">&larr; Back to
						Member Portal</a>
				</div>
			</div>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
		$(document)
				.ready(
						function() {
							$('#adminRegForm')
									.on('submit',function(e) {
										const name = $('#name').val();
										const nameRegex = /^[A-Za-z\s]+$/; 

										// Name Validation
										if (!nameRegex.test(name)) {
										    showError('#name', 'Name should only contain alphabets.');
										    isValid = false;
										}
												$('.form-control').removeClass(
														'is-invalid');
												$('.invalid-feedback').remove();

												const password = $('#password')
														.val();
												const confirmPassword = $('#confirmPassword').val();
												const specialCharRegex = /[!@#$%^&*(),.?":{}|<>]/;
												let isValid = true;

												// Password Logic
												if (password.length < 8
														|| !specialCharRegex.test(password)) {
													showError('#password','Password must be 8+ characters and contain a special character.');
													isValid = false;
												}

												if (password !== confirmPassword) {
													showError(
															'#confirmPassword','Passwords do not match.');
													isValid = false;
												}

												if (!isValid) {
													e.preventDefault();
												}
											});

							function showError(selector, message) {
								$(selector).addClass('is-invalid');
								$(selector).after(
										'<div class="invalid-feedback">'
												+ message + '</div>');
							}
						});
	</script>

</body>
</html>
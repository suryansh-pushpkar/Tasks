<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- Assuming bootstrap.jsp contains Bootstrap 4 CSS and JS --%>
<%@ include file="/partials/__bootstrap.jsp"%>
<meta charset="UTF-8">
<title>Library Manager: Register</title>
<style>
body {
	background-color: #f8f9fa;
	min-height: 100vh;
	display: flex;
	align-items: center;
}

.register-card {
	width: 100%;
	max-width: 450px;
	padding: 15px;
	margin: auto;
	border-radius: 12px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
	background: #fff;
}
/* Ensures the error message looks clean */
.invalid-feedback {
	display: block;
	font-size: 0.8rem;
}
</style>
</head>
<body>

	<div class="container">
		<div class="card register-card">
			<div class="card-body">
				<h3 class="text-center mb-4">Create Account</h3>

				<form id="regForm" action="register" method="POST">
					<div class="form-group">
						<label for="name">Full Name</label> <input type="text"
							class="form-control" id="name" name="name" placeholder="John Doe"
							required>
					</div>

					<div class="form-group">
						<label for="mail">Email Address</label> <input type="mail"
							class="form-control" id="mail" name="mail"
							placeholder="name@example.com" required>
					</div>

					<div class="form-group">
						<label for="password">Password</label> <input type="password"
							class="form-control" id="password" name="password"
							placeholder="Minimum 8 characters" required>
					</div>

					<div class="form-group">
						<label for="confirmPassword">Confirm Password</label> <input
							type="password" class="form-control" id="confirmPassword"
							placeholder="Repeat your password" required>
					</div>

					<button type="submit" class="btn btn-success btn-block mt-4">Register
						Now</button>
				</form>

				<hr>
				<div class="text-center">
					<p class="mb-0">
						Already a member? <a href="index.jsp">Sign In</a>
					</p>
				</div>
			</div>
		</div>
	</div>

	<script>
$(document).ready(function() {
    $('#regForm').on('submit', function(e) {
    	const name = $('#name').val();
		const nameRegex = /^[A-Za-z\s]+$/; 

		if (!nameRegex.test(name)) {
		    showError('#name', 'Name should only contain alphabets.');
		    isValid = false;
		}
        
        $('.form-control').removeClass('is-invalid');
        $('.invalid-feedback').remove();

        const password = $('#password').val();
        const confirmPassword = $('#confirmPassword').val();
        const specialCharRegex = /[!@#$%^&*(),.?":{}|<>]/;
        
        let isValid = true;

        if (password.length < 8) {
            showError('#password', 'Password must be at least 8 digits/characters long.');
            isValid = false;
        } 
        else if (!specialCharRegex.test(password)) {
            showError('#password', 'Must contain at least 1 special character.');
            isValid = false;
        }

        if (password !== confirmPassword) {
            showError('#confirmPassword', 'Passwords do not match.');
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault(); 
            return false;      
        }
        
    });

    function showError(selector, message) {
        $(selector).addClass('is-invalid');
        $(selector).after('<div class="invalid-feedback">' + message + '</div>');
    }
});
</script>

</body>
</html>
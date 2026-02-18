<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.lib.entity.Book, com.lib.dao.BookDao"%>
<%
    String bookIdStr = request.getParameter("bookId");
    Book book = null;
    if (bookIdStr != null) {
        int bookId = Integer.parseInt(bookIdStr);
        book = new BookDao().findById(bookId); 
    }

    if (book == null) {
        response.sendRedirect("userDashboard.jsp?error=Invalid Book");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
<title>Confirm Issue | <%= book.getName() %></title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css">
<style>
body {
	background-color: #f4f7f6;
	height: 100vh;
	display: flex;
	align-items: center;
}

.issue-card {
	max-width: 500px;
	margin: auto;
	border: none;
	border-radius: 15px;
}

.book-preview {
	background-color: #e9ecef;
	padding: 15px;
	border-radius: 10px;
	margin-bottom: 20px;
}
</style>
</head>
<body>

	<div class="container">
		<div class="card issue-card shadow-lg">
			<div class="card-body p-4">
				<h3 class="text-center mb-4 font-weight-bold text-primary">Confirm
					Issue</h3>

				<div class="book-preview">
					<h5 class="mb-1"><%= book.getName() %></h5>
					<p class="text-muted mb-0">
						By
						<%= book.getAuthor() %></p>
					<small class="text-info">ISBN: <%= (book.getIsbn() != null) ? book.getIsbn() : "N/A" %></small>
				</div>

				<form action="confirmIssueAction" method="POST" id="issueForm">
					<input type="hidden" name="bookId" value="<%= book.getId() %>">

					<div class="form-group">
						<label class="font-weight-bold">Expected Return Date</label> <input
							type="date" name="endDate" id="endDate"
							class="form-control form-control-lg" required> <small
							class="form-text text-muted"> Note: Maximum issue
							duration is <strong>2 months</strong>.
						</small>
					</div>

					<div id="dateError" class="alert alert-danger d-none"></div>

					<div class="row mt-4">
						<div class="col-6">
							<a href="userDashboard.jsp" class="btn btn-light btn-block">Cancel</a>
						</div>
						<div class="col-6">
							<button type="submit" class="btn btn-primary btn-block">Confirm
								& Issue</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>



	<script>
    document.getElementById('issueForm').onsubmit = function(e) {
        const dateInput = document.getElementById('endDate').value;
        const errorDiv = document.getElementById('dateError');
        
        if (!dateInput) return false;

        const selectedDate = new Date(dateInput);
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        // Calculate the difference in time
        const diffTime = selectedDate - today;
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

        if (selectedDate <= today) {
            errorDiv.textContent = "Return date must be in the future.";
            errorDiv.classList.remove('d-none');
            return false;
        }

        if (diffDays > 60) {
            errorDiv.textContent = "You cannot issue a book for more than 60 days (2 months).";
            errorDiv.classList.remove('d-none');
            return false;
        }

        return true;
    };

    // Set minimum date to tomorrow automatically
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    document.getElementById('endDate').setAttribute('min', tomorrow.toISOString().split('T')[0]);
</script>

</body>
</html>
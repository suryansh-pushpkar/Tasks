<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.lib.entity.Book, com.lib.dao.BookDao, com.lib.dao.IssueRecordDao, java.util.Date, java.text.SimpleDateFormat"%>
<%
    String bookIdStr = request.getParameter("bookId");
    Book book = null;
    Date nextAvailableDate = null;

    if (bookIdStr != null) {
        int bookId = Integer.parseInt(bookIdStr);
        book = new BookDao().findById(bookId); 
        
        // Fetch the expected availability date from the DAO
        IssueRecordDao irDao = new IssueRecordDao();
        nextAvailableDate = irDao.getExpectedAvailabilityDate(bookId);
    }

    if (book == null) {
        response.sendRedirect("userDashboard.jsp?error=Invalid Book");
        return;
    }
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String suggestionMsg = null;
    if (nextAvailableDate != null && nextAvailableDate.after(new Date())) {
        suggestionMsg = "This copy is currently issued. It is expected to be available after: <strong>" 
                        + sdf.format(nextAvailableDate) + "</strong>";
    }
%>

<!DOCTYPE html>
<html>
<head>
<title>Request Issue | <%= book.getName() %></title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<style>
body {
	background-color: #f4f7f6;
	min-height: 100vh;
	display: flex;
	align-items: center;
}

.issue-card {
	max-width: 550px;
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

.suggestion-box {
	border-left: 5px solid #17a2b8;
	background-color: #e1f5fe;
	padding: 10px;
	border-radius: 5px;
}
</style>
</head>
<body>

	<div class="container py-5">
		<div class="card issue-card shadow-lg">
			<div class="card-body p-4">
				<h3 class="text-center mb-4 font-weight-bold text-primary">Request
					Book Issue</h3>

				<div class="book-preview">
					<h5 class="mb-1 text-dark">
						<i class="fas fa-book mr-2"></i><%= book.getName() %></h5>
					<p class="text-muted mb-1">
						By
						<%= book.getAuthor() %></p>
					<small class="text-secondary d-block">ISBN: <%= (book.getIsbn() != null) ? book.getIsbn() : "N/A" %></small>
				</div>

				<% if (suggestionMsg != null) { %>
				<div class="suggestion-box mb-4 small text-info">
					<i class="fas fa-info-circle mr-1"></i>
					<%= suggestionMsg %>
				</div>
				<% } %>



				<form action="confirmIssueAction" method="POST" id="issueForm">
					<input type="hidden" name="bookId" value="<%= book.getId() %>">

					<div class="row">
						<div class="col-md-6">
							<div class="form-group">
								<label class="font-weight-bold">Start Date</label> <input
									type="date" name="startDate" id="startDate"
									class="form-control" required>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group">
								<label class="font-weight-bold">Return Date</label> <input
									type="date" name="endDate" id="endDate" class="form-control"
									required>
							</div>
						</div>
					</div>

					<div id="dateError" class="alert alert-danger d-none small"></div>

					<p class="text-muted small mt-2">
						<i class="fas fa-exclamation-circle mr-1"></i> Maximum issue
						duration is <strong>60 days</strong>.
					</p>

					<div class="row mt-4">
						<div class="col-6">
							<a href="userDashboard.jsp" class="btn btn-light btn-block">Cancel</a>
						</div>
						<div class="col-6">
							<button type="submit" class="btn btn-primary btn-block">Request
								Issue</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script>
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const errorDiv = document.getElementById('dateError');

    // Set default min for Start Date to today
    const today = new Date().toISOString().split('T')[0];
    startDateInput.setAttribute('min', today);

    // Update End Date min whenever Start Date changes
    startDateInput.addEventListener('change', function() {
        endDateInput.setAttribute('min', this.value);
    });

    document.getElementById('issueForm').onsubmit = function(e) {
        const startVal = startDateInput.value;
        const endVal = endDateInput.value;
        
        if (!startVal || !endVal) return false;

        const start = new Date(startVal);
        const end = new Date(endVal);
        
        const diffTime = end - start;
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

        errorDiv.classList.add('d-none');

        if (end <= start) {
            errorDiv.textContent = "Return date must be after the start date.";
            errorDiv.classList.remove('d-none');
            return false;
        }

        if (diffDays > 60) {
            errorDiv.textContent = "Duration exceeds the 60-day limit.";
            errorDiv.classList.remove('d-none');
            return false;
        }

        return true;
    };
</script>

</body>
</html>
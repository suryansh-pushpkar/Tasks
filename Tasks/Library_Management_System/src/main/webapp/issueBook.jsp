<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.lib.entity.Book, com.lib.dao.BookDao, com.lib.dao.IssueRecordDao, java.util.Date, java.text.SimpleDateFormat"%>

<%
String bookIdStr = request.getParameter("bookId");
Book book = null;
Date nextAvailableDate = null;
String minStartDateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

if (bookIdStr != null) {
	int bookId = Integer.parseInt(bookIdStr);
	book = new BookDao().findById(bookId);
	IssueRecordDao irDao = new IssueRecordDao();
	nextAvailableDate = irDao.getExpectedAvailabilityDate(bookId);

	// If out of stock and we have a date, that becomes the minimum allowed start date
	if (book != null && book.getQuantity() <= 0 && nextAvailableDate != null) {
		minStartDateStr = new SimpleDateFormat("yyyy-MM-dd").format(nextAvailableDate);
	}
}

if (book == null) {
	response.sendRedirect("userDashboard.jsp?error=Invalid Book");
	return;
}

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
boolean isOutOfStock = (book.getQuantity() <= 0);
%>

<!DOCTYPE html>
<html>
<head>
<title>Request Issue | <%=book.getName()%></title>
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

.availability-alert {
	border-left: 5px solid;
	padding: 12px;
	border-radius: 5px;
	margin-bottom: 20px;
}
</style>
</head>
<body>
	<div class="container py-5">
		<div class="card issue-card shadow-lg">
			<div class="card-body p-4">
				<h3 class="text-center mb-4 font-weight-bold text-primary">
					<%=isOutOfStock ? "Reserve Book" : "Request Issue"%>
				</h3>

				<div class="book-preview text-center">
					<h4 class="mb-1 font-weight-bold"><%=book.getName()%></h4>
					<p class="text-muted">
						By
						<%=book.getAuthor()%></p>
					<span class="badge badge-info">Edition: <%=book.getEdition()%></span>
				</div>

				<%
				if (isOutOfStock) {
				%>
				<div class="availability-alert alert-warning">
					<i class="fas fa-clock mr-2"></i> <strong>Currently Out of
						Stock.</strong><br> Estimated available after: <span class="text-dark"><%=(nextAvailableDate != null) ? sdf.format(nextAvailableDate) : "TBD"%></span>.
					You can still create a reservation request for this date or later.
				</div>
				<%
				} else {
				%>
				<div class="availability-alert alert-success">
					<i class="fas fa-check-circle mr-2"></i> <strong>Available
						Now!</strong><br> There are
					<%=book.getQuantity()%>
					copies ready for immediate issue.
				</div>
				<%
				}
				%>

				<form action="confirmIssueAction" method="POST" id="issueForm">
					<input type="hidden" name="bookId" value="<%=book.getId()%>">

					<div class="row">
						<div class="col-md-6">
							<div class="form-group">
								<label class="font-weight-bold">Start Date</label> <input
									type="date" name="startDate" id="startDate"
									class="form-control" min="<%=minStartDateStr%>"
									value="<%=minStartDateStr%>" required> <small
									class="text-muted">Earliest possible date</small>
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

					<div class="row mt-4">
						<div class="col-6">
							<a href="userDashboard.jsp" class="btn btn-light btn-block">Cancel</a>
						</div>
						<div class="col-6">
							<button type="submit" class="btn btn-primary btn-block">
								<%=isOutOfStock ? "Request Reservation" : "Request Issue"%>
							</button>
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
    
    const today = new Date().toISOString().split('T')[0];
    startDateInput.setAttribute('min', today);

    startDateInput.addEventListener('change', () => { 
        endDateInput.setAttribute('min', startDateInput.value); 
    });

    document.getElementById('issueForm').onsubmit = function(e) {
        const start = new Date(startDateInput.value);
        const end = new Date(endDateInput.value);
        
        const diffTime = end - start;
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

        errorDiv.classList.add('d-none');
        
        if (end <= start) {
            errorDiv.textContent = "Return date must be after start date.";
            errorDiv.classList.remove('d-none');
            return false;
        }
        
        if (diffDays > 60) {
            errorDiv.textContent = "Maximum duration for reservation is 60 days.";
            errorDiv.classList.remove('d-none');
            return false;
        }
        return true;
    };
</script>
</body>
</html>
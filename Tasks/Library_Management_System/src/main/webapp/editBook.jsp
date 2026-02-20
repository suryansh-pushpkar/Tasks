<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/partials/__bootstrap.jsp"%>
<title>Edit Book | Admin Dashboard</title>
<style>
body {
	background-color: #f8f9fa;
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.edit-container {
	max-width: 600px;
	margin: 50px auto;
}

.card {
	border: none;
	border-radius: 15px;
}

.card-header {
	background: #007bff;
	color: white;
	border-radius: 15px 15px 0 0 !important;
	padding: 20px;
}

.form-label {
	font-weight: 600;
	color: #495057;
}

.btn-update {
	background-color: #007bff;
	border: none;
	padding: 10px 25px;
	transition: 0.3s;
}

.btn-update:hover {
	background-color: #0056b3;
	transform: translateY(-2px);
}

.btn-cancel {
	color: #6c757d;
	text-decoration: none;
	margin-left: 15px;
}
</style>
</head>
<body>
	<div class="container edit-container">
		<div class="card shadow">
			<div class="card-header text-center">
				<h3 class="mb-0">Update Book Details</h3>
			</div>
			<div class="card-body p-4">
				<%-- Check if book object exists --%>
				<c:if test="${not empty book}">
					<form action="updateBook" method="post">

						<input type="hidden" name="bookId" value="${book.id}">

						<div class="form-group mb-3">
							<label class="form-label">Book Name</label> <input type="text"
								name="bookName" class="form-control" value="${book.name}"
								placeholder="Enter book title" required>
						</div>

						<div class="form-group mb-3">
							<label class="form-label">Author Name</label> <input type="text"
								name="author" class="form-control" value="${book.author}"
								placeholder="Enter author name" required>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="form-group mb-3">
									<label class="form-label">Edition</label> <input type="text"
										name="edition" class="form-control" value="${book.edition}"
										placeholder="e.g. 5th" required>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group mb-4">
									<label class="form-label">Quantity</label> <input type="number"
										name="quantity" class="form-control" value="${book.quantity}"
										min="1" required>
								</div>
							</div>
						</div>

						<hr>

						<div class="d-flex align-items-center justify-content-center mt-4">
							<button type="submit"
								class="btn btn-primary btn-update shadow-sm">
								<i class="fas fa-save mr-2"></i> Save Changes
							</button>
							<a href="viewBooks.jsp" class="btn-cancel"> <i
								class="fas fa-times mr-1"></i> Cancel
							</a>
						</div>
					</form>
				</c:if>

				<c:if test="${empty book}">
					<div class="alert alert-danger text-center">
						<i class="fas fa-exclamation-triangle mr-2"></i> Error: Book
						details not found. <br> <a href="inventory.jsp"
							class="alert-link">Go back to Inventory</a>
					</div>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>
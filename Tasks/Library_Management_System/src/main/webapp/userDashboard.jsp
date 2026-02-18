<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.lib.entity.User, com.lib.entity.Book, com.lib.entity.IssueRecord"%>
<%@ page
	import="com.lib.dao.UserDao, com.lib.dao.BookDao, com.lib.dao.IssueRecordDao"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>


<%
String mNo = (String) session.getAttribute("currentUser");
String mail = (String) session.getAttribute("currentEmail");

if (mNo == null || mail == null) {
	response.sendRedirect("index.jsp");
	return;
}

UserDao uDao = new UserDao();
User user = uDao.getUserByMnoAndEmail(mNo, mail);

//  Pagination 
int currentPage = 1;
int pageSize = 5;
if (request.getParameter("page") != null) {
	try {
		currentPage = Integer.parseInt(request.getParameter("page"));
	} catch (NumberFormatException e) {
		currentPage = 1;
	}
}

BookDao bDao = new BookDao();
List<Book> books = bDao.getBooks(currentPage, pageSize);

IssueRecordDao irDao = new IssueRecordDao();
List<IssueRecord> myRecords = irDao.getAllIssuesByUser(user.getId());

pageContext.setAttribute("bookList", books);
pageContext.setAttribute("currentPage", currentPage);
pageContext.setAttribute("myRecords", myRecords);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Library Dashboard | <%=(user != null) ? user.getName() : "User"%></title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<style>
body {
	background-color: #f4f7f6;
	font-family: 'Segoe UI', sans-serif;
}

.welcome-box {
	background: linear-gradient(135deg, #007bff, #0056b3);
	color: white;
	padding: 25px;
	border-radius: 12px;
	margin-bottom: 30px;
	box-shadow: 0 4px 15px rgba(0, 123, 255, 0.3);
}

.main-card {
	background: white;
	border-radius: 15px;
	border: none;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
	overflow: visible;
}

.table thead th {
	background-color: #f8f9fa;
	border-top: none;
	text-transform: uppercase;
	font-size: 0.85rem;
	letter-spacing: 1px;
}

/* Search Dropdown Styling */
#searchResults {
	position: absolute;
	top: 100%;
	left: 0;
	right: 0;
	z-index: 1050;
	max-height: 350px;
	overflow-y: auto;
	background: white;
	border-radius: 0 0 10px 10px;
	border: 1px solid #ddd;
	display: none;
}

.search-results-dropdown .list-group-item:hover {
	background-color: #f8f9fa;
	cursor: pointer;
}

.status-available {
	color: #28a745;
	font-weight: 600;
}

.status-issued {
	color: #dc3545;
	font-weight: 600;
}

.page-link {
	border-radius: 5px;
	margin: 0 3px;
	color: #007bff;
}

.page-link.active {
	background: #007bff;
	color: white;
	border-color: #007bff;
}
</style>
</head>
<body>

	<div class="container py-4">

		<div class="row justify-content-center mb-5">
			<div class="col-md-10">
				<div class="position-relative">
					<div class="input-group input-group-lg shadow-sm">
						<div class="input-group-prepend">
							<span class="input-group-text bg-white border-right-0"><i
								class="fas fa-search text-muted"></i></span>
						</div>
						<input type="text" id="bookSearch"
							class="form-control border-left-0 shadow-none"
							placeholder="Search by book name or author..."
							onkeyup="fetchBooks()" autocomplete="off">
					</div>
					<div id="searchResults"
						class="list-group shadow-lg search-results-dropdown"></div>
				</div>
			</div>
		</div>

		<div class="welcome-box">
			<div class="row align-items-center">
				<div class="col-md-8">
					<h2>
						Welcome back,
						<%=(user != null) ? user.getName() : "Member"%>!
					</h2>
					<p class="mb-0 text-white-50">
						Membership No: <strong><%=mNo%></strong>
					</p>
				</div>
				<div class="col-md-4 text-md-right">
					<form action="login" method="POST">
						<button>Logout</button>
					</form>
				</div>
			</div>
		</div>

		<div class="card main-card mb-5">
			<div class="card-body">
				<h4 class="mb-4 font-weight-bold">
					<i class="fas fa-book-open mr-2 text-primary"></i>Available Catalog
				</h4>
				<div class="table-responsive">
					<table class="table align-middle">
						<thead>
							<tr>
								<th>Title</th>
								<th>Author</th>
								<th>Status</th>
								<th class="text-right">Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="book" items="${bookList}">
								<tr>
									<td><strong><c:out value="${book.name}" /></strong></td>
									<td><c:out value="${book.author}" /></td>
									<td><c:choose>
											<c:when test="${empty book.issueRecords}">
												<span class="status-available"><i
													class="fas fa-check-circle mr-1"></i>Available</span>
											</c:when>
											<c:otherwise>
												<span class="status-issued"><i
													class="fas fa-times-circle mr-1"></i>Issued</span>
											</c:otherwise>
										</c:choose></td>
									<td class="text-right"><c:if
											test="${empty book.issueRecords}">
											<a href="issueBook.jsp?bookId=${book.id}"
												class="btn btn-success btn-sm px-3">Issue</a>
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

				<nav class="mt-4">
					<ul class="pagination justify-content-center">
						<c:if test="${currentPage > 1}">
							<li class="page-item"><a class="page-link"
								href="userDashboard.jsp?page=${currentPage - 1}">Previous</a></li>
						</c:if>
						<li class="page-item"><span class="page-link active">${currentPage}</span></li>
						<li class="page-item"><a class="page-link"
							href="userDashboard.jsp?page=${currentPage + 1}">Next</a></li>
					</ul>
				</nav>
			</div>
		</div>

		<div class="card main-card" style="border-top: 5px solid #28a745;">
			<div class="card-body">
				<h4 class="mb-4 font-weight-bold text-success">
					<i class="fas fa-history mr-2"></i>My Borrowing History
				</h4>
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>Book Title</th>
								<th>Issue Date</th>
								<th>Due Date</th>
								<th>Status</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty myRecords}">
									<c:forEach var="record" items="${myRecords}">
										<tr>
											<td><strong>${record.book.name}</strong></td>
											<td><c:out
													value="${record.startDate != null ? record.startDate : '---'}" />
											</td>
											<td><c:out
													value="${record.endDate != null ? record.endDate : '---'}" />
											</td>
											<td><c:choose>
													<c:when test="${record.status == 'ISSUED'}">
														<span class="badge badge-warning p-2">Active</span>
													</c:when>
													<c:when test="${record.status == 'RETURNED'}">
														<span class="badge badge-secondary p-2">Returned</span>
													</c:when>
													<c:otherwise>
														<span class="badge badge-info p-2">${record.status}</span>
													</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</c:when>

								<c:otherwise>
									<tr>
										<td colspan="4" class="text-center text-muted py-4">You
											have no active or past issues.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>



	<script>
		let timeout = null;

		function fetchBooks() {
			clearTimeout(timeout);
			let query = $("#bookSearch").val().trim();
			let resultsDiv = $("#searchResults");

			if (query.length < 2) {
				resultsDiv.empty().hide();
				return;
			}

			// Debounce to prevent server overload
			timeout = setTimeout(function() {
				$.ajax({
					url : "searchBooks",
					type : "GET",
					data : {
						query : query
					},
					success : function(data) {
						resultsDiv.html(data).show();
					},
					error : function() {
						console.error("Failed to fetch search results.");
					}
				});
			}, 300);
		}

		// Close results when clicking outside
		$(document).on("click", function(e) {
			if (!$(e.target).closest("#bookSearch, #searchResults").length) {
				$("#searchResults").hide();
			}
		});
	</script>

</body>
</html>
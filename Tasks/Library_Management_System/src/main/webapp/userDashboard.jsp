<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.lib.entity.User, com.lib.entity.Book, com.lib.entity.IssueRecord"%>
<%@ page
	import="com.lib.dao.UserDao, com.lib.dao.BookDao, com.lib.dao.IssueRecordDao"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    if (session == null || ((session.getAttribute("currentUser") == null) && (session.getAttribute("User") == null))	) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<%
HttpSession currentSession = request.getSession(false);
User user1 = (User) currentSession.getAttribute("User");
String mNo = user1.getMembershipNo();
String mail = user1.getMail();
if (mNo == null || mail == null) {
	response.sendRedirect("index.jsp");
	return;
}

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

// Fetch User
UserDao uDao = new UserDao();
User user = uDao.getUserByMnoAndEmail(mNo, mail);

IssueRecordDao irDao = new IssueRecordDao();

// Fetch WHOLE history 
List<IssueRecord> myRecords = irDao.getAllIssuesByUser(user.getId());

//  Fetch ONLY (Status = 'ISSUED')
List<IssueRecord> activeBooks = irDao.getActiveIssuesByUser(user.getId());

// Set attributes for JSTL
pageContext.setAttribute("myRecords", myRecords);
pageContext.setAttribute("activeBooks", activeBooks);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Library Dashboard | <%=(user != null) ? user.getName() : "Member"%></title>
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
	background: linear-gradient(135deg, #283048, #859398);
	color: white;
	padding: 25px;
	border-radius: 12px;
	margin-bottom: 30px;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.main-card {
	background: white;
	border-radius: 15px;
	border: none;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

#searchResults {
	position: absolute;
	top: 100%;
	left: 0;
	right: 0;
	z-index: 1050;
	max-height: 400px;
	overflow-y: auto;
	background: white;
	border-radius: 0 0 10px 10px;
	border: 1px solid #ddd;
	display: none;
}

.search-container {
	max-width: 800px;
	margin: 0 auto 50px auto;
}

.badge-pending {
	background-color: #17a2b8;
	color: white;
}

.badge-renew {
	background-color: #6f42c1;
	color: white;
}

.book-icon {
	font-size: 2rem;
	color: #007bff;
}
</style>
</head>
<body>

	<div class="container py-5">

		<%-- Welcome & Profile Section --%>
		<div
			class="welcome-box d-flex justify-content-between align-items-center">
			<div>
				<h2 class="mb-1">
					Hello,
					<%=(user != null) ? user.getName() : "Member"%>!
				</h2>
				<p class="mb-0 opacity-75">
					Member ID: <strong><%=mNo%></strong>
				</p>
			</div>
			<a href="logout" class="btn btn-outline-light btn-sm px-4">Logout</a>
		</div>


		<%
		if (request.getParameter("msg") != null) {
		%>
		<div class="alert alert-success alert-dismissible fade show"><%=request.getParameter("msg")%>
			<button type="button" class="close" data-dismiss="alert">&times;</button>
		</div>
		<%
		}
		%>
		<%
		if (request.getParameter("error") != null) {
		%>
		<div class="alert alert-danger alert-dismissible fade show"><%=request.getParameter("error")%>
			<button type="button" class="close" data-dismiss="alert">&times;</button>
		</div>
		<%
		}
		%>

		<%-- SEARCH SECTION --%>
		<div class="search-container text-center">
			<h4 class="mb-4 text-dark font-weight-bold">What would you like
				to read today?</h4>
			<div class="position-relative">
				<div class="input-group input-group-lg shadow-sm">
					<div class="input-group-prepend">
						<span class="input-group-text bg-white border-right-0"> <i
							class="fas fa-search text-muted"></i></span>
					</div>
					<input type="text" id="bookSearch"
						class="form-control border-left-0 shadow-none"
						placeholder="Search by book name or author..."
						onkeyup="fetchBooks()" autocomplete="off">
				</div>
				<div id="searchResults" class="list-group shadow-lg text-left"></div>
			</div>
		</div>

		<%-- CURRENTLY ISSUED BOOKS --%>
		<div class="row mb-5">
			<div class="col-12">
				<h4 class="mb-4 font-weight-bold text-dark">
					<i class="fas fa-book-reader mr-2 text-warning"></i>Books You
					Currently Have
				</h4>
				<div class="row">
					<c:choose>
						<c:when test="${not empty activeBooks}">
							<c:forEach var="active" items="${activeBooks}">
								<div class="col-md-4 mb-3">
									<div class="card h-100 shadow-sm border-0"
										style="border-left: 4px solid #ffc107 !important;">
										<div class="card-body d-flex align-items-center">
											<div class="mr-3">
												<i class="fas fa-book book-icon"></i>
											</div>
											<div>
												<h6 class="mb-1 font-weight-bold text-truncate"
													style="max-width: 150px;">${active.book.name}</h6>
												<small class="text-muted d-block">Due:
													${active.endDate}</small> <a href="requestRenew?id=${active.id}"
													class="btn btn-link btn-sm p-0 mt-1 text-primary">Quick
													Renew</a>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<div class="col-12">
								<div class="alert alert-light border text-muted">You don't
									have any books issued at the moment.</div>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>

		<%-- BORROWING HISTORY --%>

		<div class="card main-card border-top"
			style="border-top: 5px solid #007bff !important;">
			<div class="card-body">
				<h4 class="mb-4 font-weight-bold">
					<i class="fas fa-history mr-2 text-primary"></i>My Books & History
				</h4>
				<div class="table-responsive">
					<table class="table align-middle">
						<thead class="thead-light">
							<tr>
								<th>Book Title</th>
								<th>Issue Date</th>
								<th>Due Date</th>
								<th>Status</th>
								<th class="text-right">Action</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty myRecords}">
									<c:forEach var="record" items="${myRecords}">
										<tr>
											<td><strong><c:out value="${record.book.name}" /></strong></td>
											<td><c:out
													value="${record.startDate != null ? record.startDate : '---'}" /></td>
											<td><c:out
													value="${record.endDate != null ? record.endDate : '---'}" /></td>
											<td><c:choose>
													<c:when test="${record.status == 'ISSUED'}">
														<span class="badge badge-warning p-2"><i
															class="fas fa-book-reader mr-1"></i>Active</span>
													</c:when>
													<c:when test="${record.status == 'PENDING'}">
														<span class="badge badge-pending p-2"><i
															class="fas fa-hourglass-half mr-1"></i>Waiting Approval</span>
													</c:when>
													<c:when test="${record.status == 'RENEW_REQUESTED'}">
														<span class="badge badge-renew p-2"><i
															class="fas fa-sync mr-1"></i>Renewal Pending</span>
													</c:when>
													<c:when test="${record.status == 'RETURNED'}">
														<span class="badge badge-secondary p-2">Returned</span>
													</c:when>
													<c:otherwise>
														<span class="badge badge-info p-2">${record.status}</span>
													</c:otherwise>
												</c:choose></td>
											<td class="text-right"><c:if
													test="${record.status == 'ISSUED'}">
													<a href="requestRenew?id=${record.id}"
														class="btn btn-outline-primary btn-sm"
														onclick="return confirm('Request to extend this book by 14 days?')">
														Request Renewal </a>
												</c:if></td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="5" class="text-center text-muted py-5">You
											haven't issued any books yet.</td>
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
			if (query.length < 1) {
				resultsDiv.empty().hide();
				return;
			}
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
						console.error("Search failed.");
					}
				});
			}, 300);
		}

		$(document).on("click", function(e) {
			if (!$(e.target).closest("#bookSearch, #searchResults").length) {
				$("#searchResults").hide();
			}
		});
	</script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.lib.dao.BookDao, com.lib.entity.Admin, com.lib.entity.Library, java.util.List"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<%
    // 1. Session and Security Check
    Admin currentAdmin = (Admin) session.getAttribute("currentAdmin");
    if (currentAdmin == null) {
        response.sendRedirect("adminlogin.jsp");
        return;
    }

    Library lib = currentAdmin.getLibrary();
    int libId = lib.getId();
    String libName = lib.getName();

    // 2. Fetch all individual books with their current statuses
    BookDao bookDao = new BookDao();
    List<Object[]> allBooksWithStatus = bookDao.getAllBooksWithStatus(libId);

    // 3. Set to pageContext for JSTL access
    pageContext.setAttribute("allBooks", allBooksWithStatus);
%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/partials/__bootstrap.jsp"%>
<title><%= libName %> | Full Inventory</title>
<style>
body {
	background-color: #f4f7f6;
}

.status-badge {
	font-size: 0.85rem;
	padding: 5px 12px;
	border-radius: 50px;
	font-weight: 600;
}

.bg-available {
	background-color: #d4edda;
	color: #155724;
}

.bg-issued {
	background-color: #fff3cd;
	color: #856404;
}

.bg-pending {
	background-color: #d1ecf1;
	color: #0c5460;
}

.table-card {
	border-radius: 15px;
	border: none;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.book-title {
	color: #333;
	font-size: 1rem;
}
</style>
</head>
<body>
	<div class="container py-5">

		<%-- Status Alerts --%>
		<c:if test="${not empty param.msg}">
			<div class="alert alert-success alert-dismissible fade show">
				<i class="fas fa-check-circle mr-2"></i>${param.msg}
				<button type="button" class="close" data-dismiss="alert">&times;</button>
			</div>
		</c:if>

		<div class="d-flex justify-content-between align-items-center mb-4">
			<div>
				<h3 class="font-weight-bold mb-0 text-dark">Library Inventory</h3>
				<p class="text-muted">
					Detailed status for <strong><%= libName %></strong>
				</p>
			</div>
			<a href="addBook.jsp" class="btn btn-primary shadow-sm"> <i
				class="fas fa-plus mr-1"></i> Add New Book
			</a>
		</div>



		<div class="card table-card">
			<div class="table-responsive">
				<table class="table table-hover mb-0">
					<thead class="thead-light">
						<tr>
							<th class="py-3 pl-4">Title</th>
							<th class="py-3">Author</th>
							<th class="py-3">ISBN</th>
							<th class="py-3 text-center">Current Status</th>
							<th class="py-3 text-right pr-4">Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty allBooks}">
								<c:forEach var="book" items="${allBooks}">
									<tr>
										<td class="align-middle pl-4"><span
											class="font-weight-bold book-title">${book[0]}</span></td>
										<td class="align-middle text-secondary">${book[1]}</td>
										<td class="align-middle small text-muted">${book[2]}</td>
										<td class="align-middle text-center"><c:choose>
												<c:when test="${book[3] == 'ISSUED'}">
													<span class="status-badge bg-issued"> <i
														class="fas fa-user-clock mr-1"></i> On Loan
													</span>
												</c:when>
												<c:when test="${book[3] == 'PENDING'}">
													<span class="status-badge bg-pending"> <i
														class="fas fa-pause-circle mr-1"></i> Reserved
													</span>
												</c:when>
												<c:when test="${book[3] == 'RENEW_REQUESTED'}">
													<span class="status-badge bg-pending"> <i
														class="fas fa-sync mr-1"></i> Renewal Req
													</span>
												</c:when>
												<c:otherwise>
													<span class="status-badge bg-available"> <i
														class="fas fa-check mr-1"></i> Available
													</span>
												</c:otherwise>
											</c:choose></td>
										<td class="align-middle text-right pr-4">
											<div class="btn-group">
												<a href="editBook.jsp?name=${book[0]}&author=${book[1]}"
													class="btn btn-sm btn-outline-warning border-0"
													title="Edit"> <i class="fas fa-edit"></i>
												</a> <a href="deleteBook?name=${book[0]}&author=${book[1]}"
													class="btn btn-sm btn-outline-danger border-0"
													onclick="return confirm('Delete this record?')"
													title="Delete"> <i class="fas fa-trash"></i>
												</a>
											</div>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="5" class="text-center py-5 text-muted">No
										books found in your library.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>

		<div class="mt-4">
			<a href="admindashboard.jsp" class="btn btn-link text-primary pl-0">
				<i class="fas fa-arrow-left mr-1"></i> Return to Dashboard
			</a>
		</div>
	</div>
</body>
</html>
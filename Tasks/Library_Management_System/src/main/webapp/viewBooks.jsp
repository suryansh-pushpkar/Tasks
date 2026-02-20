<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.lib.dao.BookDao, com.lib.entity.Admin, com.lib.entity.Library, com.lib.entity.Book, java.util.List"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<%
Admin currentAdmin = (Admin) session.getAttribute("currentAdmin");
if (currentAdmin == null) {
	response.sendRedirect("adminlogin.jsp");
	return;
}

int libId = currentAdmin.getLibrary().getId();
String libName = currentAdmin.getLibrary().getName();

BookDao bookDao = new BookDao();
List<Book> inventory = bookDao.getLibraryInventory(libId);
pageContext.setAttribute("inventory", inventory);
%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/partials/__bootstrap.jsp"%>
<title><%=libName%> | Inventory Management</title>
<style>
body {
	background-color: #f4f7f6;
}

.table-card {
	border-radius: 15px;
	border: none;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.stock-badge {
	font-size: 0.85rem;
	padding: 5px 12px;
	border-radius: 50px;
}
</style>
</head>
<body>
	<div class="container py-5">
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
					Managing stock for <strong><%=libName%></strong>
				</p>
			</div>
			<a href="addBook.jsp" class="btn btn-primary shadow-sm"> <i
				class="fas fa-plus mr-1"></i> Add New Book Title
			</a>
		</div>

		<div class="card table-card">
			<div class="table-responsive">
				<table class="table table-hover mb-0">
					<thead class="thead-light">
						<tr>
							<th class="py-3 pl-4">Title</th>
							<th class="py-3">Author</th>
							<th class="py-3">Edition</th>
							<th class="py-3 text-center">Available Stock</th>
							<th class="py-3 text-right pr-4">Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty inventory}">
								<c:forEach var="book" items="${inventory}">
									<tr>
										<td class="align-middle pl-4 font-weight-bold">${book.name}</td>
										<td class="align-middle text-secondary">${book.author}</td>
										<td class="align-middle text-info">${book.edition}</td>
										<td class="align-middle text-center"><c:choose>
												<c:when test="${book.quantity > 0}">
													<span class="badge badge-success stock-badge">${book.quantity}
														Copies</span>
												</c:when>
												<c:otherwise>
													<span class="badge badge-danger stock-badge">Out of
														Stock</span>
												</c:otherwise>
											</c:choose></td>
										<td class="align-middle text-right pr-4"><a
											href="editBook.jsp?id=${book.id}"
											class="btn btn-sm btn-warning text-white mr-1"> <i
												class="fas fa-edit"></i> Edit
										</a> <a href="deleteBook?id=${book.id}"
											class="btn btn-sm btn-danger"
											onclick="return confirm('Delete this book and all its records?')">
												<i class="fas fa-trash"></i> Delete
										</a></td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="5" class="text-center py-5 text-muted">Inventory
										is empty.</td>
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
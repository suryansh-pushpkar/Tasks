<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.lib.dao.BookDao, com.lib.dao.AdminDao, com.lib.entity.Admin, com.lib.entity.Library, java.util.List"%>

<%
    Admin admin = (Admin) session.getAttribute("currentAdmin");
    if (admin == null) {
        response.sendRedirect("adminlogin.jsp");
        return;
    }

    AdminDao adminDao = new AdminDao();
    Library lib = adminDao.getLibrary(admin);
    BookDao bookDao = new BookDao();

    // Handle Delete Action
    String action = request.getParameter("action");
    if ("delete".equals(action)) {
        String nameToDelete = request.getParameter("name");
        String authorToDelete = request.getParameter("author");
        if (nameToDelete != null && authorToDelete != null && lib != null) {
            try {
                bookDao.deleteBooksByNameAndAuthor(nameToDelete, authorToDelete, lib.getId());
                response.sendRedirect("viewBooks.jsp?msg=Book records removed successfully");
                return;
            } catch (Exception e) {
                response.sendRedirect("viewBooks.jsp?error=Cannot delete books currently issued to members.");
                return;
            }
        }
    }

    List<Object[]> groupedBooks = null;
    String libName = "Your Library";
    if (lib != null) {
        libName = lib.getName();
        groupedBooks = bookDao.getBooksWithQuantities(lib.getId());
    }
%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/partials/__bootstrap.jsp"%>
<title><%=libName%> Inventory</title>
<style>
.available-badge {
	font-size: 0.9rem;
	padding: 6px 12px;
	border-radius: 50px;
}
</style>
</head>
<body class="bg-light">
	<div class="container mt-5">
		<%-- Status Messages --%>
		<% if (request.getParameter("msg") != null) { %>
		<div class="alert alert-success alert-dismissible fade show">
			<%=request.getParameter("msg")%>
			<button type="button" class="close" data-dismiss="alert">&times;</button>
		</div>
		<% } %>
		<% if (request.getParameter("error") != null) { %>
		<div class="alert alert-danger alert-dismissible fade show">
			<%=request.getParameter("error")%>
			<button type="button" class="close" data-dismiss="alert">&times;</button>
		</div>
		<% } %>

		<div class="d-flex justify-content-between align-items-center mb-4">
			<h3 class="font-weight-bold text-dark">
				<i class="fas fa-warehouse mr-2"></i>Available Inventory
			</h3>
			<a href="addBook.jsp" class="btn btn-primary shadow-sm">+ Add New
				Copy</a>
		</div>

		<div class="card shadow-sm border-0">
			<table class="table table-hover mb-0">
				<thead class="bg-dark text-white">
					<tr>
						<th class="py-3">Book Title</th>
						<th class="py-3">Author</th>
						<th class="py-3 text-center">Available Copies</th>
						<th class="py-3 text-right">Management</th>
					</tr>
				</thead>
				<tbody>
					<% if (groupedBooks != null && !groupedBooks.isEmpty()) {
                        for (Object[] row : groupedBooks) {
                            String encName = java.net.URLEncoder.encode(row[0].toString(), "UTF-8");
                            String encAuthor = java.net.URLEncoder.encode(row[1].toString(), "UTF-8");
                    %>
					<tr>
						<td class="align-middle font-weight-bold"><%=row[0]%></td>
						<td class="align-middle text-secondary"><%=row[1]%></td>
						<td class="align-middle text-center"><span
							class="badge badge-success available-badge"> <%=row[2]%>
								In Stock
						</span></td>
						<td class="align-middle text-right"><a
							href="editBook.jsp?name=<%=encName%>&author=<%=encAuthor%>"
							class="btn btn-sm btn-outline-warning">Edit</a> <a
							href="viewBooks.jsp?action=delete&name=<%=encName%>&author=<%=encAuthor%>"
							class="btn btn-sm btn-outline-danger ml-1"
							onclick="return confirm('Confirm deletion of all available copies of this title?')">Delete</a>
						</td>
					</tr>
					<% } } else { %>
					<tr>
						<td colspan="4" class="text-center py-5 text-muted">Your
							library is currently empty.</td>
					</tr>
					<% } %>
				</tbody>
			</table>
		</div>
		<div class="mt-4">
			<a href="admindashboard.jsp" class="text-primary"><i
				class="fas fa-arrow-left mr-1"></i> Return to Dashboard</a>
		</div>
	</div>
</body>
</html>
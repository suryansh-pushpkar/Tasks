<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.lib.dao.BookDao"%>
<%@ page import="com.lib.dao.AdminDao"%>
<%@ page import="com.lib.entity.Admin"%>
<%@ page import="com.lib.entity.Library"%>
<%@ page import="java.util.List"%>
<%@ page import="java.net.URLDecoder"%>

<%
    Admin admin = (Admin) session.getAttribute("currentAdmin");
    if (admin == null) {
        response.sendRedirect("adminlogin.jsp");
        return;
    }

    AdminDao adminDao = new AdminDao();
    Library lib = adminDao.getLibrary(admin);
    BookDao bookDao = new BookDao();

    String action = request.getParameter("action");
    if ("delete".equals(action)) {
        String nameToDelete = request.getParameter("name");
        String authorToDelete = request.getParameter("author");
        
        if (nameToDelete != null && authorToDelete != null && lib != null) {
            try {
                bookDao.deleteBooksByNameAndAuthor(nameToDelete, authorToDelete, lib.getId());
                response.sendRedirect("viewBooks.jsp?msg=Book deleted successfully");
                return;
            } catch (Exception e) {
                response.sendRedirect("viewBooks.jsp?error=Cannot delete. Book might be issued.");
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
<title><%= libName %> Inventory</title>
</head>
<body class="bg-light">
	<div class="container mt-5">

		<%-- Status Messages --%>
		<% if(request.getParameter("msg") != null) { %>
		<div class="alert alert-success"><%= request.getParameter("msg") %></div>
		<% } %>
		<% if(request.getParameter("error") != null) { %>
		<div class="alert alert-danger"><%= request.getParameter("error") %></div>
		<% } %>

		<div class="d-flex justify-content-between align-items-center mb-4">
			<h3><%= libName %>
				- Live Inventory
			</h3>
			<a href="addBook.jsp" class="btn btn-success">+ Add New Copy</a>
		</div>

		<table class="table table-bordered bg-white shadow-sm">
			<thead class="thead-dark">
				<tr>
					<th>Title</th>
					<th>Author</th>
					<th>ISBN</th>
					<th>Quantity</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<% if (groupedBooks != null && !groupedBooks.isEmpty()) { 
                    for (Object[] row : groupedBooks) { 
                        // Encode for URL safety
                        String encName = java.net.URLEncoder.encode(row[0].toString(), "UTF-8");
                        String encAuthor = java.net.URLEncoder.encode(row[1].toString(), "UTF-8");
                %>
				<tr>
					<td><%= row[0] %></td>
					<td><%= row[1] %></td>
					<td><%= row[3] %></td>
					<td><span class="badge badge-primary"><%= row[2] %>
							Copies</span></td>
					<td><a
						href="editBook.jsp?name=<%= encName %>&author=<%= encAuthor %>"
						class="btn btn-sm btn-warning">Edit</a> <%-- The Delete Link points back to this same page with action=delete --%>
						<a
						href="viewBooks.jsp?action=delete&name=<%= encName %>&author=<%= encAuthor %>"
						class="btn btn-sm btn-danger"
						onclick="return confirm('Delete ALL copies of this book?')">Delete</a>
					</td>
				</tr>
				<% } } else { %>
				<tr>
					<td colspan="5" class="text-center">No books found.</td>
				</tr>
				<% } %>
			</tbody>
		</table>
		<a href="adminDashboard.jsp" class="btn btn-link">&larr; Dashboard</a>
	</div>
</body>
</html>
<%@page import="com.lib.entity.Admin"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.lib.dao.BookDao"%>
<%@ page import="com.lib.entity.Book"%>
<%
    Admin currentAdmin = (Admin) session.getAttribute("currentAdmin");
    
    if (currentAdmin == null) {
        response.sendRedirect("adminlogin.jsp");
        return;
    }

    int libId = currentAdmin.getLibrary().getId();

    String name = request.getParameter("name");
    String author = request.getParameter("author");

    BookDao dao = new BookDao();
    Book book = dao.findBookByNameAuthorAndLibrary(name, author, libId);

    if (book == null) {
        response.sendRedirect("viewBooks.jsp?error=not_found");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/partials/__bootstrap.jsp"%>
<title>Edit Book: <%=name%></title>
<style>
body {
	background-color: #f8f9fa;
}

.edit-card {
	max-width: 500px;
	margin: 50px auto;
	border-radius: 15px;
}
</style>
</head>
<body>
	<div class="container">
		<div class="card edit-card shadow">
			<div class="card-header bg-warning text-dark font-weight-bold">
				Update Book Details</div>
			<div class="card-body">
				<form action="updateBookAction" method="POST">
					<input type="hidden" name="oldName" value="<%=book.getName()%>">
					<input type="hidden" name="oldAuthor"
						value="<%=book.getAuthor()%>">

					<div class="form-group">
						<label>Book Title</label> <input type="text" name="newName"
							class="form-control" value="<%=book.getName()%>" required>
					</div>
					<div class="form-group">
						<label>Author Name</label> <input type="text" name="newAuthor"
							class="form-control" value="<%=book.getAuthor()%>" required>
					</div>
					<div class="form-group">
						<label>ISBN</label> <input type="text" name="newIsbn"
							class="form-control"
							value="<%=(book.getIsbn() != null) ? book.getIsbn() : ""%>">
					</div>

					<button type="submit"
						class="btn btn-warning btn-block font-weight-bold">Apply
						Changes to All Copies</button>
					<a href="viewBooks.jsp" class="btn btn-link btn-block text-muted">Cancel</a>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Administration Page</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="model.Professional, model.DataBaseBridge" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap-grid.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/grid-box.css"/>
	<!-- JS -->
	<script src="/TEDProject/js/lib/jquery-3.3.1.min.js"></script>
	<script src="/TEDProject/js/lib/js-cookie.js"></script>
	<script src="/TEDProject/js/lib/bootstrap.min.js"></script>
</head>
<body>
	<% boolean exportPage = false;
	   if (request.getAttribute("exportXML") != null && request.getAttribute("exportXML").equals("form")) {
		   exportPage = true;
	   } %>
	<div class="main_container">
		<jsp:include page="AdminNavBar.jsp"></jsp:include>
		<div class="justify-content-between">
			<% if (!exportPage) { %>
				<a href="/TEDProject/admin/AdminServlet?exportXML=form" class="btn btn-primary float-right">Export Professionals' data to XML</a>
				<h2 class="my_h2 pt-1">Registered Professionals</h2>
			<% } else {	%>
				<h2 class="my_h2">Export Registered Professionals to XML</h2>
			<% } %> 
		</div>
		<div>
			<% DataBaseBridge db = new DataBaseBridge();
			   Professional[] professionals = db.getAllProfessionals(); 
			   if (exportPage) { %>
				<form method="POST" action="/TEDProject/admin/AdminServlet?exportXML=submitted">
					<input type="checkbox" onClick="selectAll(this)">Select All<br>
			<% } %>
					<ul class="grid_container"> 
					<% for (int i = 0 ; i < professionals.length ; i++ ){ %>
						<li class="grid_item">
							<% if (exportPage) { %>
								<input type="checkbox" name="profID" value="<%= professionals[i].getID() %>">
								<br>
							<% } %>	
							<img class="img-thumbnail" src="<%= professionals[i].getProfilePicURI() %>" alt="Profile picture"><br>
							<%= professionals[i].getFirstName() %> <%= professionals[i].getLastName() %><br>
						 	<% if (professionals[i].getEmploymentStatus() != null && !professionals[i].getEmploymentStatus().isEmpty()) { %> 
								<%= professionals[i].getEmploymentStatus() %> 
								<br> 
							<% } 
							   if (professionals[i].getEmploymentInstitution() != null && !professionals[i].getEmploymentInstitution().isEmpty()) { %> 
							   	<%= professionals[i].getEmploymentInstitution() %> 
								<br>
							<% } %>
							<!-- The following is a servlet URI which will forward the HTTP GET request to ProfPublicProfilePage.jsp with the correct ID  -->
							<a href="/TEDProject/ProfileLink?ProfID=<%= professionals[i].getID() %>" class="mt-2">View Details</a>
						</li>
					<% } %>
					</ul>
				<% if (exportPage) { %>
					<div class="buttonContainer text-center">
						<input type="submit" value="Download Selected" onclick="return checkIfAnyChecked();" class="btn btn-primary">
						<a href="/TEDProject/admin/AdminServlet" class="btn btn-secondary">Cancel</a>
					</div>
				</form>
			<% } 
			   db.close(); %>
		</div>
		<jsp:include page="/footer.html"></jsp:include>
	</div>
	<script src="/TEDProject/js/adminPage.js"></script>
</body>
</html>
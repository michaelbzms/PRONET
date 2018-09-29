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
		<% DataBaseBridge db = new DataBaseBridge();
		   Professional[] professionals = db.getAllProfessionals(); %>
		<div class="justify-content-between">
		<% if (!exportPage) { 
				if (professionals.length > 0) { %>
					<a href="/TEDProject/admin/AdminServlet?exportXML=form" class="btn btn-primary float-right">Export Professionals' data to XML</a>
			 <% } %>
				<h2 class="my_h2 pt-1">Registered Professionals</h2>
		<% } else {	%>
				<h2 class="my_h2">Export Registered Professionals to XML</h2>
		<% } %> 
		</div>
		<div>
			<% if (professionals.length == 0) { %>
				<p><i>There are no Registered Professionals.</i></p>
			<% } else { %>
			    <% if (exportPage) { %>
					<form method="POST" action="/TEDProject/admin/AdminServlet?exportXML=submitted">
						<input type="checkbox" onClick="selectAll(this)">Select All<br>
				<% } %>
						<div class="grid_container"> 
					 <% for (int i = 0 ; i < professionals.length ; i++ ) { 
							if (exportPage) { %>
								<input id="profID<%= professionals[i].getID() %>" type="checkbox" name="profID" value="<%= professionals[i].getID() %>" class="d-none">
								<div class="grid_item" onclick="document.getElementById('profID<%= professionals[i].getID() %>').click();">
						 <% } else { %>
								<a class="grid_item" href="/TEDProject/ProfileLink?ProfID=<%= professionals[i].getID() %>">
						 <% } %>
									<div class="text-dark">
										<img class="img-thumbnail" src="<%= professionals[i].getProfilePicURI() %>" alt="Profile picture"><br>
										<%= professionals[i].getFirstName() %> <%= professionals[i].getLastName() %><br>
									 	<% if (professionals[i].getEmploymentStatus() != null && !professionals[i].getEmploymentStatus().isEmpty()) { %> 
											<%= professionals[i].getEmploymentStatus() %> 
										<% } %>
										<br> 
										<% if (professionals[i].getEmploymentInstitution() != null && !professionals[i].getEmploymentInstitution().isEmpty()) { %> 
										   	<%= professionals[i].getEmploymentInstitution() %> 
										<% } %>
										<br>
									</div>
						 <% if (exportPage) { %>
								</div>
						 <% } else { %>
								</a>
						 <% } %>
					 <% } %>
						</div>
					<% if (exportPage) { %>
						<div class="buttonContainer text-center">
							<input type="submit" value="Download Selected" onclick="return checkIfAnyChecked();" class="btn btn-primary">
							<a href="/TEDProject/admin/AdminServlet" class="btn btn-secondary">Cancel</a>
						</div>
					</form>
					<script src="/TEDProject/js/adminExportPage.js"></script>
				 <% } 
			   }
			   db.close(); %>
		</div>
		<jsp:include page="/footer.html"></jsp:include>
	</div>
</body>
</html>
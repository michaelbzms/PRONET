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
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/grid-box.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/js-cookie.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
	<% boolean exportPage = false;
	   if (request.getAttribute("exportXML") != null && request.getAttribute("exportXML").equals("form")) {
		   exportPage = true;
	   } %>
	<div class="main_container">
		<nav class="navbar navbar-expand-xl bg-light justify-content-center">
			<div class="container-fluid">
			    <div class="navbar-header">
			    	<a class="navbar-brand" href="/TEDProject/admin/AdminServlet">PRONET</a>
			    </div>
				<ul class="navbar-nav"  role="navigation">
					<li class="nav-item">
						<form class="form-inline" action="/TEDProject/LogoutServlet" method="post">
							<input class="btn btn-primary" type="submit" value="Logout" >
						</form>
					</li>
				</ul>
			</div>
		</nav>
		<div class="justify-content-between">
			<% if (!exportPage) { %>
				<h2>Registered Professionals 
				<a href="/TEDProject/admin/AdminServlet?exportXML=form" class="btn btn-primary float-right pb-1">Export Professionals' data to XML</a></h2>
			<% } else {	%>
				<h2>Export Registered Professionals to XML</h2>
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
							<% } %>	
							<!-- profile picture here as an <img> element  -->
							<%= professionals[i].getFirstName() %> <%= professionals[i].getLastName() %><br>
							<% if ( professionals[i].getEmploymentStatus() != null ) { %>
								<%= professionals[i].getEmploymentStatus() %> <br>
							<% } %>
							<!-- The following is a servlet URI which will forward the HTTP GET request to ProfPublicProfilePage.jsp with the correct ID  -->
							<a href="/TEDProject/ProfileLink?ProfID=<%= professionals[i].getID() %>">View Details</a>
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
	<script src="/TEDProject/Javascript/adminPage.js"></script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Administration Page</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/grid-box.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
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
		<h2>Registered Professionals:</h2><br>
		<div>
			<% DataBaseBridge db = new DataBaseBridge();
			   Professional[] professionals = db.getAllProfessionals(); %>
			<ul class="grid_container"> 
			<% for (int i = 0 ; i < professionals.length ; i++ ){ %>
				<li class="grid_item">
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
			<% db.close(); %>
		</div>
	</div>
</body>
</html>
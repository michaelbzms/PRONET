<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Network</title>
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>
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
<%	DataBaseBridge db = new DataBaseBridge(); 
	Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
	if ( prof == null ) {  %>
		<h2>INTERNAL ERROR</h2>	
		<p>Could not retrieve your info from our data base. How did you login?</p>
<% 	} else { %>
		<div class="main_container">
			<nav class="navbar navbar-expand-xl bg-light justify-content-center">
				<div class="container-fluid">
				    <div class="navbar-header">
				      <a class="navbar-brand" href="/TEDProject/prof/NavigationServlet?page=HomePage">PRONET</a>
				    </div>
					<ul class="navbar-nav"  role="navigation">
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=HomePage">Home Page</a></li>
						<li class="nav-item active"><a id="active_page" class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Network">Network</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=WorkAds">Work Ads</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Messages">Messages</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Notifications">Notifications</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/ProfileLink">Personal Information</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Settings">Settings</a></li>
						<li class="nav-item">
							<form class="form-inline" action="/TEDProject/LogoutServlet" method="post">
								<input class="btn btn-primary" type="submit" value="Logout" >
							</form>
						</li>
					</ul>
				</div>
			</nav>
			<div class="search_bar">
				<h2>Search for professionals</h2>
				<form id="AJAXform" action="/TEDProject/AJAXServlet?action=searchProfessional" method="post" class="ajax">
					<label>Find: </label>
					<input type="text" name="searchString" id="searchString">
					<input type="submit" value="search">
				</form>
				<div id="searchProfessional" class="ajax_target_div">
				</div>
			</div>
			<div class="connections_bar">
				<h2>Connections</h2>
				<div class="grid_container_container">
					<ul id="connections_grid" class="grid_container">
					<% List<Professional> Connections = db.getConnectedProfessionalsFor(prof.getID());
					   if ( Connections != null ) { 
						   for (Professional p : Connections) { %>
								<li class="grid_item">
									<img class="img-thumbnail" src="<%= p.getProfilePicURI() %>" alt="Profile picture"><br>
									<b><%= p.getFirstName() %> <%= p.getLastName() %></b><br>
									<% if (p.getEmploymentStatus() != null) { %> 
										<%= p.getEmploymentStatus() %> 
									<% } else { %> 
										N/A  
									<% } %>
									<br> 
									<% if (p.getEmploymentInstitution() != null) { %> <%= p.getEmploymentInstitution() %> 
									<% } else { %> 
										N/A
									<% } %>
									<br>
									<a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">View details</a>					
								</li>
						<%	} %>
					<% } %>
					</ul>
				</div>
			</div>
		</div>
<%	} 
	db.close(); %>
	<!-- JavaScript scripts declaration: -->
	<script src="/TEDProject/Javascript/AJAX.js"></script>    
</body>
</html>
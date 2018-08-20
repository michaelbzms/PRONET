<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/network.css"/>
	<title>PRONET - Network</title>
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>
	<% 	DataBaseBridge db = new DataBaseBridge(); 
		Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
		if ( prof == null ) {  %>
			<h2>INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { %>
		<div class="main_container">
			<nav class="navbar">
				<ul>
					<li><a href="/TEDProject/prof/NavigationServlet?page=HomePage">Home Page</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Network">Network</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=WorkAds">Work Ads</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Messages">Messages</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Notifications">Notifications</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=PersonalInformation">Personal Information</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Settings">Settings</a></li>
					<li><form action="/TEDProject/LogoutServlet" method="post">
							<input type="submit" value="Logout" >
						</form>
					</li>
				</ul>
			</nav>
			<div class="connection_requests_bar">
				<h2>Connection Requests</h2>
				<% List<Professional> RequestedBy = db.getConnectionRequestsFor(prof.getID()); 
				   if ( RequestedBy != null ){ %>
					   	<ul>
				     <%	for ( Professional asker : RequestedBy ) { %>
					   		<li>
					   			<a href="/TEDProject/ProfileLink?ID=<%= asker.getID() %>"><%= asker.getFirstName() %> <%= asker.getLastName() %></a>
					   			<form action="/TEDProject/prof/AcceptRequest" method="POST" style="float:right">    <!-- use AJAX for this form! -->
					   				<input type="submit" value="accept">
					   				<input type="submit" value="decline">
					   			</form>
					   			<br>
					   		</li>
					   		<!-- AJAX JS code goes here? -->
				     <% } %>
				   		</ul>
				<% } else { %>
				   		<p>You have no connection requests pending.</p>
				<% } %>
			</div>
			<div class="search_bar">
				<h2>Search for professionals</h2>
				<form id="AJAXform" action="/TEDProject/AJAXServlet?action=searchProfessional" method="post" class="ajax">  <!-- use AJAX for this form! -->
					<label>Find: </label>
					<input type="text" name="searchString" id="searchString">
					<input type="submit" value="search">
				</form>
				<div class="ajax_target_div">
				</div>
			</div>
			<div class="connections_bar">
				<h2>Connections</h2>
				<div class="grid_container">
					<% List<Professional> Connections = db.getConnectedProfessionalsFor(prof.getID());
					   if ( Connections != null ) {
							for (Professional p : Connections) { %>
								<div class="grid_item">
									<img src="<%= p.getProfile_pic_file_path() %>" alt="Profile picture"><br>
									<%= p.getFirstName() %> <%= p.getLastName() %><br>
									<%= p.getEmploymentStatus() %><br>
									<%= p.getEmploymentInstitution() %><br>	
									<a href="/TEDProject/ProfileLink?ID=<%= p.getID() %>">View details</a>					
								</div>
					<% 		} 
					   } else { %>
					   		<p>You are not connected with any other professional.</p>
					<% } %>
				</div>
			</div>
		</div>
	<% } 
	   db.close(); %>
	<script src="/TEDProject/Javascript/AJAX.js"></script>    
</body>
</html>
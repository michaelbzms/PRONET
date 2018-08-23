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
	<% 	DataBaseBridge db = new DataBaseBridge(); 
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
			<div class="connection_requests_bar">
				<h2>Connection Requests</h2>
				<% List<Professional> RequestedBy = db.getConnectionRequestsFor(prof.getID()); 
				   if ( RequestedBy != null ){ %>
					   	<ul class="list-group">
				     <%	for ( Professional asker : RequestedBy ) { %>
					   		<li id="request<%= asker.getID() %>">
					   			<a href="/TEDProject/ProfileLink?ProfID=<%= asker.getID() %>"><%= asker.getFirstName() %> <%= asker.getLastName() %></a>
					   			<div style="float:right">
					   				<button class="btn btn-outline-primary" id="accept<%= asker.getID() %>" value="accept">accept</button>
					   				<button class="btn btn-outline-secondary" id="decline<%= asker.getID() %>" value="decline">decline</button>
					   				<script> <!-- JS script for accepting/rejecting friend requests -->
					   					$("#accept<%= asker.getID() %>").on("click", function(){
					   						// send AJAX post information to server
					   						$.ajax({
					   							url: "/TEDProject/AJAXServlet?action=connectionRequest",
					   							type: "post",
					   							data: { AskerID: <%= asker.getID() %>, ReceiverID: <%= prof.getID() %>, decision:"accept" },
					   							success: function(response){
					   								console.log(response);
					   								$("#request<%= asker.getID() %>").fadeOut();
					   							 	// append new connection to list of Connections below
					   								$("#connections_grid").append(
				   										  "<li class=\"grid_item\">"
					   								  	+     "<img src=\"<%= asker.getProfile_pic_file_path() %>\" alt=\"Profile picture\"><br>"
														+     "<b><%= asker.getFirstName() %> <%= asker.getLastName() %></b><br>"
														+     "<%= asker.getEmploymentStatus() %><br>"
														+     "<%= asker.getEmploymentInstitution() %><br>"	
														+     "<a href=\"/TEDProject/ProfileLink?ProfID=\"<%= Integer.toString(asker.getID()) %>\">View details</a>"
					   								    + "</li>"		
					   								);
					   							}
					   						});
					   					});
					   					
					   					$("#decline<%= asker.getID() %>").on("click", function(){
					   						// send AJAX post information to server
					   						$.ajax({
					   							url: "/TEDProject/AJAXServlet?action=connectionRequest",
					   							type: "post",
					   							data: { AskerID: <%= asker.getID() %>, ReceiverID: <%= prof.getID() %>, decision:"decline" },
					   							success: function(response){
					   								console.log(response);
					   								$("#request<%= asker.getID() %>").fadeOut();
					   							}
					   						});
					   					});
					   				</script>
					   			</div>
					   			<br>
					   		</li>
				     <% } %>
				   		</ul>
				<% } else { %>
				   		<p>Ooops! It appears that we cannot load your connection requests from our database.<br>
				   		Please contact our administrators.</p>
				<% } %>
			</div>
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
					<ul id="connections_grid" class="grid_container">
					<% List<Professional> Connections = db.getConnectedProfessionalsFor(prof.getID());
					   if ( Connections != null ) { 
						   for (Professional p : Connections) { %>
								<li class="grid_item">
									<img src="<%= p.getProfile_pic_file_path() %>" alt="Profile picture"><br>
									<b><%= p.getFirstName() %> <%= p.getLastName() %></b><br>
									<%= p.getEmploymentStatus() %><br>
									<%= p.getEmploymentInstitution() %><br>	
									<a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">View details</a>					
								</li>
						<%	} %>
					<% } %>
					</ul>
			</div>
		</div>
	<% } 
	   db.close(); %>
	<!-- JavaScript scripts declaration: -->
	<script src="/TEDProject/Javascript/AJAX.js"></script>    
</body>
</html>
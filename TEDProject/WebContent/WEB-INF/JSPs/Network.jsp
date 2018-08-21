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
	<!-- Online version: <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script> -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
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
					<li><a href="/TEDProject/ProfileLink">Personal Information</a></li>
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
					   		<li id="request">
					   			<a href="/TEDProject/ProfileLink?ProfID=<%= asker.getID() %>"><%= asker.getFirstName() %> <%= asker.getLastName() %></a>
					   			<div style="float:right">    <!-- use AJAX for this form! -->
					   				<button id="accept" value="accept">accept</button>
					   				<button id="decline" value="decline">decline</button>
					   				<script> <!-- JS script for accepting/rejecting friend requests -->
					   					$("#accept").on("click", function(){
					   						// send AJAX post information to server
					   						$.ajax({
					   							url: "/TEDProject/AJAXServlet?action=connectionRequest",
					   							type: "post",
					   							data: { AskerID: <%= asker.getID() %>, ReceiverID: <%= prof.getID() %>, decision:"accept" },
					   							success: function(response){
					   								console.log(response);
					   								$("#request").fadeOut();
					   							}
					   						});
					   					});
					   					
					   					$("#decline").on("click", function(){
					   						// send AJAX post information to server
					   						$.ajax({
					   							url: "/TEDProject/AJAXServlet?action=connectionRequest",
					   							type: "post",
					   							data: { AskerID: <%= asker.getID() %>, ReceiverID: <%= prof.getID() %>, decision:"decline" },
					   							success: function(response){
					   								console.log(response);
					   								$("#request").fadeOut();
					   							}
					   						});
					   					});
					   				</script>
					   			</div>
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
				<div id="searchProfessional" class="ajax_target_div">
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
									<a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">View details</a>					
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
	<!-- JavaScript scripts declaration: -->
	<script src="/TEDProject/Javascript/AJAX.js"></script>    
</body>
</html>
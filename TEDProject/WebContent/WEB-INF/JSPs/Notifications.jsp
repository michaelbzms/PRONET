<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Notifications</title>
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
			<jsp:include page="ProfNavBar.jsp"> 
				<jsp:param name="activePage" value="Notifications"/> 
			</jsp:include>
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
					   				<script> 
					   					<!-- JS script for accepting/rejecting friend requests -->
					   					
					   					$("#accept<%= asker.getID() %>").on("click", function(){
					   						// send AJAX post information to server
					   						$.ajax({
					   							url: "/TEDProject/AJAXServlet?action=connectionRequest",
					   							type: "post",
					   							data: { AskerID: <%= asker.getID() %>, ReceiverID: <%= prof.getID() %>, decision:"accept" },
					   							success: function(response){
					   								console.log(response);
					   								$("#request<%= asker.getID() %>").fadeOut();
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
			<jsp:include page="/footer.html"></jsp:include>
		</div>
<%	} 
 	db.close(); %>
</body>
</html>
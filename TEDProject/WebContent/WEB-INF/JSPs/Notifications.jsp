<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Notifications</title>
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality, model.Notification, model.MyUtil" %>
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
				   if ( RequestedBy != null ){ 
				   		if (! RequestedBy.isEmpty() ) {	%>
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
					    	<p>You don't have any Connection Requests.</p>
					 <% } %>
				   		<br>
				   		<h2>Notifications</h2>
			   			<% List<Notification> notifications = db.getNotificationsFor(prof.getID());
		   			    if ( notifications != null ) {
		   				 	if (! notifications.isEmpty() ) {
			   				   	int i = 0; %>
						   	    <ul class="list-group">
			   			  	 <% for ( Notification n : notifications ) { 
				   			   		Professional notifier = db.getBasicProfessionalInfo(n.getNotifiedByProfID());
				   			   		if ( notifier != null ) { %>
					   					<li id="notification<%= i %>" class="notification">
					   						<% if ( n.getType().equals("interest") ) {            // interest %>
						   							<p style="float: left">
						   								<a href="/TEDProject/ProfileLink?ProfID=<%= n.getNotifiedByProfID() %>"><%= notifier.getFirstName() %> <%= notifier.getLastName() %></a>
						   								has shown interest in one of your <a href="/TEDProject/prof/NavigationServlet?page=Article&ArticleID=<%= n.getPostID() %>">articles</a>!
						   							</p>
						   							<button id="cancel<%= i %>"  class="btn btn-outline-primary" style="float: right">✓</button>
						   							<span style="float: right; color: #007bff; margin-right: 10px"><%= MyUtil.getTimeAgo(n.getTimeHappened()) %></span>
					   						<% } else if ( n.getType().equals("comment") ) {      // comment  %>
						   							<p style="float: left">
						   								<a href="/TEDProject/ProfileLink?ProfID=<%= n.getNotifiedByProfID() %>"><%= notifier.getFirstName() %> <%= notifier.getLastName() %></a>
						   								has commented on one of your <a href="/TEDProject/prof/NavigationServlet?page=Article&ArticleID=<%= n.getPostID() %>">articles</a>!
						   							</p>
						   							<button id="cancel<%= i %>" class="btn btn-outline-primary" style="float: right">✓</button>
						   							<span style="float: right; color: #007bff; margin-right: 10px"><%= MyUtil.getTimeAgo(n.getTimeHappened()) %></span>
						   							<p style="float: left; clear: left; color: #0e1956; overflow: hidden">"<%= n.getText() %>"</p>
					   						<% } else if ( n.getType().equals("application") ) {  // application  %>
						   							<p style="float: left">
						   								<a href="/TEDProject/ProfileLink?ProfID=<%= n.getNotifiedByProfID() %>"><%= notifier.getFirstName() %> <%= notifier.getLastName() %></a>
						   								has made an application on one of your <a href="/TEDProject/WorkAdLink?AdID=<%= n.getPostID() %>">work ads</a>!
						   							</p>
						   							<button id="cancel<%= i %>" class="btn btn-outline-primary" style="float: right">✓</button>
						   							<span style="float: right; color: #007bff; margin-right: 10px"><%= MyUtil.getTimeAgo(n.getTimeHappened()) %></span>
						   							<p style="float: left; clear: left; color: #0e1956; overflow: hidden">"<%= n.getText() %>"</p>
					   						<% } else { %>
					   								<p>Error: Unknown notification type</p>
					   						<% } %>
					   						<script>
					   							// when marked as "seen" this should also be reflected in the database
					   							$("#cancel<%= i %>").on("click", function(){
					   								
													$.ajax({
							    						url: "/TEDProject/AJAXServlet?action=markAsSeen",
							    						type: "post",
							    						data: {  
							    							type: "<%= n.getType() %>",
							    							commentORapplicationID: <%= n.getNotificationID() %>,     // null or idComment or idApplication
							    							interestBy: <%= n.getNotifiedByProfID() %>,               // idInterestShownBy
							    							articleID: <%= n.getPostID() %>                           // idArticle
							    						},
							    						success: function(response){
							    							if ( response === "success" ) {
							    								$("#notification<%= i %>").fadeOut();
							    							} else {
							    								window.alert(response);
							    							}
							    						}
													});
					   								
					   							});
					   						</script>
					   					</li>   
				   					<% } else { %>
				   						<li class="notification">
				   							<p>Error: We could not recover the Professional who notified you from our database.</p>
				   						</li> 
			   						<% }
				   			   		i++;
			   			 		} %>
			   			  	 	</ul>
			   			 <% } else { %>
						    	<p>You don't have any Notifications.</p>
						 <% }
		   			    } %>
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
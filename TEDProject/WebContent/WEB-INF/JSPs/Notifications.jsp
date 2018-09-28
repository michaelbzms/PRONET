<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Notifications</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality, model.Notification, model.MyUtil" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap-grid.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/grid-box.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/notifications.css"/>
	<!-- JS -->
	<script src="/TEDProject/js/lib/jquery-3.3.1.min.js"></script>
	<script src="/TEDProject/js/lib/bootstrap.min.js"></script>
</head>
<body>
	<div class="main_container">
	<% 	DataBaseBridge db = new DataBaseBridge(); 
		Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
		if ( prof == null ) {  %>
			<h2 class="my_h2">INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { %>
			<jsp:include page="ProfNavBar.jsp"> 
				<jsp:param name="activePage" value="Notifications"/> 
			</jsp:include>
			<div id="connection_requests_bar" class="connection_requests_bar">
				<h2 class="my_h2">Connection Requests</h2>
				<% List<Professional> RequestedBy = db.getConnectionRequestsFor(prof.getID()); 
				   if ( RequestedBy != null ){ 
				   		if (! RequestedBy.isEmpty() ) {	%>
						   	<ul class="list-group notifications_list">
					     <%	for ( Professional asker : RequestedBy ) { %>
						   		<li id="request<%= asker.getID() %>" class="request">
						   			<a href="/TEDProject/ProfileLink?ProfID=<%= asker.getID() %>"><%= asker.getFirstName() %> <%= asker.getLastName() %></a>
						   			<div class="float-right">
						   				<button class="btn btn-outline-primary" id="accept<%= asker.getID() %>" value="accept">accept</button>
						   				<button class="btn btn-outline-secondary" id="decline<%= asker.getID() %>" value="decline">decline</button>
						   				<script> 
						   					$("#accept<%= asker.getID() %>").on("click", function(){
						   						answer_request(true, <%= asker.getID() %>, <%= prof.getID() %>);     // accept request
						   					});
						   					
						   					$("#decline<%= asker.getID() %>").on("click", function(){
						   						answer_request(false, <%= asker.getID() %>, <%= prof.getID() %>);    // decline request
						   					});
						   				</script>
						   			</div>
						   			<br>
						   		</li>
					    <%  } %>
					   		</ul>
			 			<%  } else { %>
				    		<p><i>You don't have any Connection Requests.</i></p>
			 			<%  } %>
				<%	} else { %>
			   			<p>Ooops! It appears that we cannot load your connection requests from our database.<br>
			   			Please contact the administrators.</p>
				<%	} %>
			</div>
			<br>
		 	<div id="notifications_bar" class="connection_requests_bar">
		 		<button id="markAll" class="btn btn-primary float-right">Mark all as seen</button>
	   			<h2 class="my_h2 pt-1">Notifications</h2>
	  				<%	List<Notification> notifications = db.getNotificationsFor(prof.getID());
		    		if ( notifications != null ) {
		   				if (! notifications.isEmpty() ) {
			   				int i = 0; %>
							<ul class="list-group notifications_list">
		   			  	 <% for ( Notification n : notifications ) { 
			   			   		Professional notifier = db.getBasicProfessionalInfo(n.getNotifiedByProfID());
			   			   		if ( notifier != null ) { %>
				   					<li id="notification<%= i %>" class="notification">
				   						<% if ( n.getType().equals("interest") ) {            // interest %>
					   							<p class="float-left">
					   								<a href="/TEDProject/ProfileLink?ProfID=<%= n.getNotifiedByProfID() %>"><%= notifier.getFirstName() %> <%= notifier.getLastName() %></a>
					   								has shown interest in one of your <a href="/TEDProject/prof/NavigationServlet?page=Article&ArticleID=<%= n.getPostID() %>">articles</a>!
					   							</p>
					   							<button id="cancel<%= i %>" class="btn btn-outline-primary cancel float-right">✓</button>
					   							<span class="notification_time"><%= MyUtil.getTimeAgo(n.getTimeHappened()) %></span>
				   						<% } else if ( n.getType().equals("comment") ) {      // comment  %>
					   							<p class="float-left">
					   								<a href="/TEDProject/ProfileLink?ProfID=<%= n.getNotifiedByProfID() %>"><%= notifier.getFirstName() %> <%= notifier.getLastName() %></a>
					   								has commented on one of your <a href="/TEDProject/prof/NavigationServlet?page=Article&ArticleID=<%= n.getPostID() %>">articles</a>!
					   							</p>
					   							<button id="cancel<%= i %>" class="btn btn-outline-primary cancel float-right">✓</button>
					   							<span class="notification_time"><%= MyUtil.getTimeAgo(n.getTimeHappened()) %></span>
					   							<p class="notification_text">"<%= n.getText() %>"</p>
				   						<% } else if ( n.getType().equals("application") ) {  // application  %>
					   							<p class="float-left">
					   								<a href="/TEDProject/ProfileLink?ProfID=<%= n.getNotifiedByProfID() %>"><%= notifier.getFirstName() %> <%= notifier.getLastName() %></a>
					   								has made an application on one of your <a href="/TEDProject/WorkAdLink?AdID=<%= n.getPostID() %>">work ads</a>!
					   							</p>
					   							<button id="cancel<%= i %>" class="btn btn-outline-primary cancel float-right">✓</button>
					   							<span class="notification_time"><%= MyUtil.getTimeAgo(n.getTimeHappened()) %></span>
					   							<p class="notification_text">"<%= n.getText() %>"</p>
				   						<% } else { %>
				   								<p>Error: Unknown notification type</p>
				   						<% } %>
				   						<script>
				   							// when marked as "seen" this should also be reflected in the database
				   							$("#cancel<%= i %>").on("click", function(){
				   								mark_as_seen(<%= i %>, "<%= n.getType() %>", <%= n.getNotificationID() %>, <%= n.getNotifiedByProfID() %>, <%= n.getPostID() %>)
				   							});
				   						</script>
				   					</li>   
			   				<%  } else { %>
			   						<li class="notification">
			   							<p>Error: We could not recover the Professional who notified you from our database.</p>
			   						</li> 
		   					<%  }
			   			   		i++;
		   			 		} %>
		   			  	 	</ul>
		   			 <% } else { %>
					    	<p><i>You don't have any Notifications.</i></p>
					 <% }
				} else { %>
				   		<p>Ooops! It appears that we cannot load your notifications from our database.<br>
				   		Please contact our administrators.</p>
			<%	} %>
			</div>
			<br>
			<script src="/TEDProject/js/notifications.js"></script>
	<%	} 
	 	db.close(); %>
	 	<jsp:include page="/footer.html"></jsp:include>
 	</div>	
</body>
</html>
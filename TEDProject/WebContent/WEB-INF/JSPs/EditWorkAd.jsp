<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="model.Professional, model.DataBaseBridge, model.SiteFunctionality, model.WorkAd" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/simplemde.min.css">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style2.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
	<script src="/TEDProject/Javascript/simplemde.min.js"></script>
</head>
<body>
	<div class="main_container">
	<% 	DataBaseBridge db = new DataBaseBridge(); 
		Professional Prof = SiteFunctionality.acquireProfFromSession(db, request); 
		WorkAd ad = null;		// the ad we're editing; if null then a new ad is being created
		if (request.getParameter("AdID") != null) {
			ad = db.getWorkAd(Integer.parseInt(request.getParameter("AdID")));
		} 
		if (Prof == null) {  %>
			<h2 class="my_h2">INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else if (ad == null && request.getParameter("AdID") != null) {  %>
			<h2 class="my_h2">INVALID REQUEST</h2>	
			<p>The requested Work Ad doesn't exist.</p>
	<% 	} else if (ad != null && ad.getPublishedByID() != Prof.getID()) {  %>
			<h2 class="my_h2">PERMISSION ERROR</h2>	
			<p>You don't have permission to edit this Work Ad.</p>
	<% 	} else {	%>
			<nav class="navbar navbar-expand-xl bg-light justify-content-center">
				<div class="container-fluid">
				    <div class="navbar-header">
				      <a class="navbar-brand" href="/TEDProject/prof/NavigationServlet?page=HomePage">PRONET</a>
				    </div>
					<ul class="navbar-nav"  role="navigation">
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=HomePage">Home Page</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Network">Network</a></li>
						<li class="nav-item active"><a id="active_page" class="nav-link" href="/TEDProject/prof/NavigationServlet?page=WorkAds">Work Ads</a></li>
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
			<form method=POST action="/TEDProject/prof/WorkAdManagementServlet?action=<%= (ad == null) ? "create" : ("edit&AdID=" + ad.getID()) %>">
				<% if (ad == null) {	%>
					<h2 class="my_h2">Title</h2>
					<input type="text" class="form-control" name="title" required>
				<% } else {		%>
					<h1 class="my_h1"><%= ad.getTitle() %></h1>
				<% } %>
				<br>
				<h2 class="my_h2">Description</h2>
			    <textarea id="description" name="description"><%= (ad != null) ? ad.getDescription() : "" %></textarea>
			    <div class="buttonContainer text-center">
					<input type="submit" value=<%= (ad == null) ? "Post Work Ad" : "Save" %> class="btn btn-primary">
					<a href="/TEDProject/prof/NavigationServlet?page=WorkAds" class="btn btn-secondary">Cancel</a>
				</div>
			</form>
			<script>
				var description = new SimpleMDE({ element: document.getElementById("description"), showIcons: ["code", "table"] });
			</script>
		<% } 
			db.close(); %>
		<jsp:include page="/footer.html"></jsp:include>
	</div>
</body>
</html>
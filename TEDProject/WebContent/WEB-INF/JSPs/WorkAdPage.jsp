<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<%@ page import="model.Professional, model.DataBaseBridge, model.WorkAd" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style2.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
	<div class="main_container">
		<% int adID = Integer.parseInt(request.getAttribute("AdID").toString());
		   DataBaseBridge db = new DataBaseBridge();
		   WorkAd ad = db.getWorkAd(adID);
		   if (ad == null) { %>
		   		<h2 class="my_h2">INVALID WORK AD REQUEST</h2>
				<p>Requested work ad does not exist.</p>
		<% } else { 
			HttpSession currentSession = request.getSession(false);
			int profID;
			if (request.getSession(false) != null) {
				profID = (int) currentSession.getAttribute("ProfID");
			} else {
				profID = -1;
			}
			boolean isAdmin = ( currentSession != null && ((boolean) currentSession.getAttribute("isAdmin")) );
			// Navbar only for professionals
			if (profID > -1 && !isAdmin) { %>
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
		<% } %>
			<h1 class="my_h1"><%= ad.getTitle() %></h1>
			<h5 class="text-center">Published by <a href="/TEDProject/ProfileLink?ProfID=<%= ad.getPublishedByID() %>"><%= db.getProfessionalFullName(ad.getPublishedByID()) %></a> on <%= ad.printDate(false) %></h5>
			<div class="ProfileOptions">
			<% if (profID > -1) { 			// TODO: Admin should be able to delete ads?
				   if (profID == ad.getPublishedByID()) { 		// ad was published by current prof   %>
				   	<a href="#" class="btn btn-danger">Delete Work Ad</a>
				<% } else { 		// current prof may apply 	%>
					<a href="#" class="btn btn-primary">Apply</a>
				<% } 
			   } %>
			</div>
			<br>
		  	<p id="adDescription"><%= ad.getDescription() %></p>
		  	<br>
			<!-- Back button only for admin -->
			<% if (isAdmin) { %>
					<a href="/TEDProject/admin/AdminServlet">Return to admin page</a>
			<% } %>
		<% } 
		   db.close(); %>
	</div>
	<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
	<script>
		var adDescription = document.getElementById("adDescription");
		if (adDescription) adDescription.innerHTML = SimpleMDE.prototype.markdown(`<%= ad.getDescription().replace("\\", "\\\\").replace("`", "\\`") %>`);
	</script>
</body>
</html>
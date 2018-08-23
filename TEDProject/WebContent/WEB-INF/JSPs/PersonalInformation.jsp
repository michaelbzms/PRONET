<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
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
		<% int profID = Integer.parseInt(request.getAttribute("ProfID").toString());
		   DataBaseBridge db = new DataBaseBridge();
		   Professional Prof = db.getProfessional(profID);
		   if (Prof == null) { %>
		   		<h2 class="my_h2">INVALID PROFILE REQUEST</h2>
				<p>Requested profile does not exist.</p>
		<% } else { 
			HttpSession currentSession = request.getSession(false);
			int sessionProfID;
			if (request.getSession(false) != null) {
				sessionProfID = (int) currentSession.getAttribute("ProfID");
			} else {
				sessionProfID = -1;
			}
			boolean isSelf = (sessionProfID == profID); 
			boolean isAdmin = ( currentSession != null && ((boolean) currentSession.getAttribute("isAdmin")) );
			// Navbar only for professionals
			if (currentSession != null && !isAdmin) { %>
				<nav class="navbar navbar-expand-xl bg-light justify-content-center">
					<div class="container-fluid">
					    <div class="navbar-header">
					      <a class="navbar-brand" href="/TEDProject/prof/NavigationServlet?page=HomePage">PRONET</a>
					    </div>
						<ul class="navbar-nav"  role="navigation">
							<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=HomePage">Home Page</a></li>
							<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Network">Network</a></li>
							<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=WorkAds">Work Ads</a></li>
							<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Messages">Messages</a></li>
							<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Notifications">Notifications</a></li>
							<li class="nav-item active"><a id="active_page" class="nav-link" href="/TEDProject/ProfileLink">Personal Information</a></li>
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
			<h1 class="my_h1"><%= Prof.getFirstName() %>  <%= Prof.getLastName() %></h1>
			<!-- insert image here with  style = "float: right" -->
			<p style = "font-weight: bold; text-align: center">
			<% if ( Prof.getEmploymentStatus() != null ) { %>
				<%= Prof.getEmploymentStatus() %> <br> 
			<% } %>
			<% if ( Prof.getEmploymentInstitution() != null ) { %>
				<%= Prof.getEmploymentInstitution() %> <br> 
			<% } %>
			<% if ( Prof.getDescription() != null ) { %>
				<%= Prof.getDescription() %> <br> 
			<% } %>
			</p>	
			<% if (currentSession != null && !isAdmin) { 
				if (isSelf) { %>
					<a href="/TEDProject/prof/NavigationServlet?page=EditProfile" class="btn btn-primary">Edit Profile</a>
				<% } else if (db.areProfessionalsConnected(profID, sessionProfID)) { 	// An already connected prof %>
					<a href="/TEDProject/prof/ConnectionServlet?action=remove&ProfID=<%= profID %>" class="btn btn-danger">Remove Connection</a>
				<% } else if (db.pendingConnectionRequest(profID, sessionProfID)) { 	// A not connected prof with pending connection request from them %>
					<a href="/TEDProject/prof/ConnectionServlet?action=accept&ProfID=<%= profID %>" class="btn btn-success">Accept Connection Request</a>
					<a href="/TEDProject/prof/ConnectionServlet?action=reject&ProfID=<%= profID %>" class="btn btn-danger">Reject Connection Request</a>
				<% } else if (db.pendingConnectionRequest(sessionProfID, profID)) { 	// A not connected prof with pending connection request from logged in prof %>
					<a href="/TEDProject/prof/ConnectionServlet?action=cancel&ProfID=<%= profID %>" class="btn btn-outline-danger">Cancel Connection Request</a>
				<% } else {		// Any other not connected prof %>		
					<a href="/TEDProject/prof/ConnectionServlet?action=connect&ProfID=<%= profID %>" class="btn btn-success">Connect</a>
				<% }
			} %>
			<p style="text-align: center">
			   <u>Contact Info:</u><br> 
			   Email: <%= Prof.getEmail() %> <br>
			   Phone Number: <%= Prof.getPhone() %> <br> 
			</p>
			<br>
			<% if ( Prof.getProfessionalExperience() != null && !Prof.getProfessionalExperience().isEmpty() && 
						(Prof.getProfExpVisibility() || isSelf || isAdmin || (currentSession != null && db.areProfessionalsConnected(profID, sessionProfID))) ) { %>
					<h2 class="my_h2">Professional Experience</h2>
					<p id="profExp"><%= Prof.getProfessionalExperience() %></p>
					<br>
			<% } %>
			<% if ( Prof.getEducationBackground() != null && !Prof.getEducationBackground().isEmpty() && 
						(Prof.getEdBackgroundVisibility() || isSelf || isAdmin || (currentSession != null && db.areProfessionalsConnected(profID, sessionProfID)))) { %>
					<h2 class="my_h2">Education Background</h2>
					<p id="edBackground"><%= Prof.getEducationBackground() %></p>
					<br>
			<% } %>
			<% if ( Prof.getSkills() != null && !Prof.getSkills().isEmpty() && 
						(Prof.getSkillsVisibility() || isSelf || isAdmin || (currentSession != null && db.areProfessionalsConnected(profID, sessionProfID)))) { %>
					<h2 class="my_h2">Skills</h2>
					<p id="skills"><%= Prof.getSkills() %></p>
					<br>
			<% } %>
			<!-- Back button only for admin -->
			<% if (isAdmin) { %>
					<a href="/TEDProject/admin/AdminServlet">Return to admin page</a>
			<% } %>
		<% } 
		   db.close(); %>
	</div>
	<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
	<script>
		var profExp = document.getElementById("profExp");
		if (profExp) profExp.innerHTML = SimpleMDE.prototype.markdown(`<%= Prof.getProfessionalExperience().replace("\\", "\\\\").replace("`", "\\`") %>`);
		var edBackground = document.getElementById("edBackground");
		if (edBackground) edBackground.innerHTML = SimpleMDE.prototype.markdown(`<%= Prof.getEducationBackground().replace("\\", "\\\\").replace("`", "\\`") %>`);
		var skills = document.getElementById("skills");
		if (skills) skills.innerHTML = SimpleMDE.prototype.markdown(`<%= Prof.getSkills().replace("\\", "\\\\").replace("`", "\\`") %>`);
	</script>
</body>
</html>
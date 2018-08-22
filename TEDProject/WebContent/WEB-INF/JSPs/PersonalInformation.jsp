<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style2.css"/>
	<title>PRONET - Personal Information</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
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
			<% if (isSelf) { %>
				<a href="/TEDProject/prof/NavigationServlet?page=EditProfile" class="changeButton">Edit Profile</a>
			<% } %>
			<p>
			   <span style="text-decoration: underline">Contact Info:</span><br> 
			   Email: <%= Prof.getEmail() %> <br>
			   Phone Number: <%= Prof.getPhone() %> <br> 
			   <br>
			</p>
			<% if ( Prof.getProfessionalExperience() != null && (Prof.getProfExpVisibility() || isSelf || isAdmin || (currentSession != null && db.areProfessionalsConnected(profID, sessionProfID))) ) { %>
					<h2 class="my_h2">Professional Experience</h2>
					<p id="profExp"><%= Prof.getProfessionalExperience() %></p>
					<br>
			<% } %>
			<% if ( Prof.getEducationBackground() != null && (Prof.getEdBackgroundVisibility() || isSelf || isAdmin || (currentSession != null && db.areProfessionalsConnected(profID, sessionProfID)))) { %>
					<h2 class="my_h2">Education Background</h2>
					<p id="edBackground"><%= Prof.getEducationBackground() %></p>
					<br>
			<% } %>
			<% if ( Prof.getSkills() != null && (Prof.getSkillsVisibility() || isSelf || isAdmin || (currentSession != null && db.areProfessionalsConnected(profID, sessionProfID)))) { %>
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
		if (profExp) {
			profExp.innerHTML = SimpleMDE.prototype.markdown(`<%= Prof.getProfessionalExperience()%>`);
		}
		var edBackground = document.getElementById("edBackground");
		if (edBackground) {
			edBackground.innerHTML = SimpleMDE.prototype.markdown(`<%= Prof.getEducationBackground()%>`);
		}
		var skills = document.getElementById("skills");
		if (skills) {
			skills.innerHTML = SimpleMDE.prototype.markdown(`<%= Prof.getSkills()%>`);
		}
	</script>
</body>
</html>
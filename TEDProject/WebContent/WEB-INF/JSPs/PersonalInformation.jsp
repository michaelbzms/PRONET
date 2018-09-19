<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="model.Professional, model.DataBaseBridge, model.MyUtil" %>
	<!-- CSS -->
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
		<% int profID = Integer.parseInt(request.getAttribute("ProfID").toString());
		   DataBaseBridge db = new DataBaseBridge();
		   Professional Prof = db.getProfessional(profID);
		   if (Prof == null) { %>
		   		<h2 class="my_h2">INVALID PROFILE REQUEST</h2>
				<p>Requested profile does not exist.</p>
		<% } else { 
			HttpSession currentSession = request.getSession(false);
			int sessionProfID;
			if (request.getSession(false) != null && currentSession.getAttribute("ProfID") != null) {
				sessionProfID = (int) currentSession.getAttribute("ProfID");
			} else {
				sessionProfID = -1;
			}
			boolean isSelf = (sessionProfID == profID); 
			boolean isAdmin = ( currentSession != null && currentSession.getAttribute("isAdmin") != null && ((boolean) currentSession.getAttribute("isAdmin")) );
			// Navbar only for professionals
			if (currentSession != null && !isAdmin) { %>
				<jsp:include page="ProfNavBar.jsp"> 
					<jsp:param name="activePage" value="PersonalInformation"/> 
				</jsp:include>
		<% } %>
			<h1 class="my_h1"><%= Prof.getFirstName() %>  <%= Prof.getLastName() %></h1>
			<p class="text-center font-weight-bold">
				<img class="img-thumbnail profile_thumbnail m-2" src="<%= Prof.getProfilePicURI() %>" alt="Profile picture"><br>
				<% if ( Prof.getEmploymentStatus() != null ) { %>
					<%= Prof.getEmploymentStatus() %><br> 
				<% } %>
				<% if ( Prof.getEmploymentInstitution() != null ) { %>
					<%= Prof.getEmploymentInstitution() %><br> 
				<% } %>
			</p>
			<% if ( Prof.getDescription() != null ) { %>
				<p class="text-center"><%= Prof.getDescription() %><br></p>
			<% } %>
			<div class="ProfileOptions">
			<% if (currentSession != null) { 
				   if (!isAdmin) {
					   if (isSelf) { %>
							<a href="/TEDProject/prof/NavigationServlet?page=EditProfile" class="btn btn-primary">Edit Profile</a>
					<% } else { %>
							<a href="/TEDProject/prof/NavigationServlet?page=Messages&chatWith=<%= profID %>" class="btn btn-outline-primary mb-2 ml-1">Message</a>
							<br>
						<%  if (db.areProfessionalsConnected(profID, sessionProfID)) { 	// An already connected prof %>
								<small class="text-info">Connected since <%= MyUtil.printDate(db.getConnectionDate(profID, sessionProfID), false) %></small><br>
								<a href="/TEDProject/prof/ConnectionServlet?action=remove&ProfID=<%= profID %>" class="btn btn-danger">Remove Connection</a>
						<%  } else if (db.pendingConnectionRequest(profID, sessionProfID)) { 	// A not connected prof with pending connection request from them %>
								<small class="text-info">Request sent <%= MyUtil.getTimeAgo(db.getConnectionRequestDate(profID, sessionProfID)) %></small><br>
								<a href="/TEDProject/prof/ConnectionServlet?action=accept&ProfID=<%= profID %>" class="btn btn-success">Accept Connection Request</a>
								<a href="/TEDProject/prof/ConnectionServlet?action=reject&ProfID=<%= profID %>" class="btn btn-danger">Reject Connection Request</a>
						<%  } else if (db.pendingConnectionRequest(sessionProfID, profID)) { 	// A not connected prof with pending connection request from logged in prof %>
								<small class="text-info">Request sent <%= MyUtil.getTimeAgo(db.getConnectionRequestDate(sessionProfID, profID)) %></small><br>
								<a href="/TEDProject/prof/ConnectionServlet?action=cancel&ProfID=<%= profID %>" class="btn btn-outline-danger">Cancel Connection Request</a>
						<%  } else {		// Any other not connected prof %>		
								<a href="/TEDProject/prof/ConnectionServlet?action=connect&ProfID=<%= profID %>" class="btn btn-primary">Connect</a>
						<%  } %>
					<% }
				   } else {	%>
				   	   <a href="/TEDProject/admin/AdminServlet?exportXML=downloadProf&ProfID=<%= profID %>" class="btn btn-primary">Export to XML</a>
				<% }
			   } %>
			</div>
		  	<h2 class="my_h2">Contact Info:</h2> 
		  	<label>Email: </label><%= Prof.getEmail() %><br>
		  	<label>Phone Number: </label><%= Prof.getPhone() %><br> 
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
		   <jsp:include page="/footer.html"></jsp:include>
	</div>
	<script>
		<% if ( Prof.getProfessionalExperience() != null ) { %>
			var profExp = document.getElementById("profExp");
			if (profExp) profExp.innerHTML = SimpleMDE.prototype.markdown(`<%= Prof.getProfessionalExperience().replace("\\", "\\\\").replace("`", "\\`") %>`);
		<% } %>
		<% if ( Prof.getEducationBackground() != null ) { %>
			var edBackground = document.getElementById("edBackground");
			if (edBackground) edBackground.innerHTML = SimpleMDE.prototype.markdown(`<%= Prof.getEducationBackground().replace("\\", "\\\\").replace("`", "\\`") %>`);
		<% } %>
		<% if ( Prof.getSkills() != null ) { %>
			var skills = document.getElementById("skills");
			if (skills) skills.innerHTML = SimpleMDE.prototype.markdown(`<%= Prof.getSkills().replace("\\", "\\\\").replace("`", "\\`") %>`);
		<% } %>
	</script>
</body>
</html>
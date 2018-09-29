<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="model.Professional, model.DataBaseBridge, model.MyUtil, java.util.List" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap-grid.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/article.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/grid-box.css"/>
	<!-- JS -->
	<script src="/TEDProject/js/lib/jquery-3.3.1.min.js"></script>
	<script src="/TEDProject/js/lib/bootstrap.min.js"></script>
	<script src="/TEDProject/js/lib/simplemde.min.js"></script>
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
			// Appropriate navbar:
			if (isAdmin) { %>
				<jsp:include page="AdminNavBar.jsp"></jsp:include>
		 <% } else {
				if (sessionProfID > -1) { %>
					<jsp:include page="ProfNavBar.jsp">
						<jsp:param name="activePage" value="PersonalInformation"/> 
					</jsp:include>
			 <% } else { %>
			 		<jsp:include page="VisitorNavBar.jsp"></jsp:include>
			 <% } 
		    } %>
		  	<div class="row">
				<div class="col-4">
					<div class="info_tab">
						<h1 class="my_h1"><%= Prof.getFirstName() %> <%= Prof.getLastName() %></h1>
						<p class="text-center font-weight-bold">
							<img id="profile_image" class="img-thumbnail profile_thumbnail m-2" src="<%= Prof.getProfilePicURI() %>" onclick="$('#modal_profile_img').modal('show');" alt="Profile picture" data-toggle="tooltip" data-placement="top" title="Click to enlarge!"><br>
							<% if ( Prof.getEmploymentStatus() != null && !Prof.getEmploymentStatus().isEmpty() ) { %>
								<%= Prof.getEmploymentStatus() %><br> 
							<% } %>
							<% if ( Prof.getEmploymentInstitution() != null && !Prof.getEmploymentInstitution().isEmpty() ) { %>
								<%= Prof.getEmploymentInstitution() %><br> 
							<% } %>
						</p>
						<div id="modal_profile_img" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true" style="padding-left: 16px !important;">
							<div class="modal-dialog image_modal_dialog" role="document">
						    	<div class="modal-content image_modal_content">
							        <div class="modal-header">
								       <h5 class="modal-title"><%= Prof.getFirstName() %> <%= Prof.getLastName() %></h5>
								       <button type="button" class="close" data-dismiss="modal" aria-label="Close">
								       		<span aria-hidden="true">&times;</span>
								       </button>
								    </div>
								    <div class="modal-body text-center">
								        <img class="modal_img" src="<%= Prof.getProfilePicURI() %>">
								    </div>
						    	</div>
						    </div>
						</div>
					</div>
					<div class="ProfileOptions">
					<% if (currentSession != null) { 
						   if (!isAdmin) {
							   if (isSelf) { %>
									<a href="/TEDProject/prof/NavigationServlet?page=EditProfile" class="btn btn-primary">Edit Profile</a>
							<% } else { %>
									<a href="/TEDProject/prof/NavigationServlet?page=Messages&chatWith=<%= profID %>" class="btn btn-outline-primary mb-2 ml-1">Message</a>
									<br>
								<%  if (db.areProfessionalsConnected(profID, sessionProfID)) { 	// An already connected prof %>
										<small class="text-secondary"><i>Connected since <%= MyUtil.printDate(db.getConnectionDate(profID, sessionProfID), false) %></i></small><br>
										<a href="/TEDProject/prof/ConnectionServlet?action=remove&ProfID=<%= profID %>" class="btn btn-danger">Remove Connection</a>
								<%  } else if (db.pendingConnectionRequest(profID, sessionProfID)) { 	// A not connected prof with pending connection request from them %>
										<small class="text-secondary"><i>Request sent <%= MyUtil.getTimeAgo(db.getConnectionRequestDate(profID, sessionProfID)) %></i></small><br>
										<a href="/TEDProject/prof/ConnectionServlet?action=accept&ProfID=<%= profID %>" class="btn btn-success mt-1">Accept Connection Request</a>
										<br>
										<a href="/TEDProject/prof/ConnectionServlet?action=reject&ProfID=<%= profID %>" class="btn btn-danger mt-2 mr-1">Reject Connection Request</a>
								<%  } else if (db.pendingConnectionRequest(sessionProfID, profID)) { 	// A not connected prof with pending connection request from logged in prof %>
										<small class="text-secondary"><i>Request sent <%= MyUtil.getTimeAgo(db.getConnectionRequestDate(sessionProfID, profID)) %></i></small><br>
										<a href="/TEDProject/prof/ConnectionServlet?action=cancel&ProfID=<%= profID %>" class="btn btn-outline-danger">Cancel Connection Request</a>
								<%  } else {		// Any other not connected prof %>		
										<a href="/TEDProject/prof/ConnectionServlet?action=connect&ProfID=<%= profID %>" class="btn btn-primary mt-1">Connect</a>
								<%  } %>
							<% }
						   } else {	%>
						   	   <a href="/TEDProject/admin/AdminServlet?exportXML=downloadProf&ProfID=<%= profID %>" class="btn btn-primary">Export to XML</a>
						<% }
					   } %>
					</div>
				</div>
				<div class="col-8">
				 	<!-- Alerts -->
				    <div id="editSuccessAlert" class="alert alert-success alert-dismissible" role="alert" style="display:none;">
						Your profile was updated successfully.
						<button type="button" class="close" data-dismiss="alert" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<script src="/TEDProject/js/alerts/profileAlerts.js"></script>
					<div class="info_tab">
						<% if ( Prof.getDescription() != null && !Prof.getDescription().isEmpty()) { %>
								<h2 class="my_h2">Description</h2>
								<p><%= Prof.getDescription() %></p>
								<br>
						<% } %>
						<% if ( Prof.getProfessionalExperience() != null && !Prof.getProfessionalExperience().isEmpty() && (Prof.getProfExpVisibility() || isSelf || isAdmin || (currentSession != null && db.areProfessionalsConnected(profID, sessionProfID))) ) { %>
							<h2 class="my_h2">Professional Experience</h2>
							<p id="profExp"><%= Prof.getProfessionalExperience() %></p>
							<br>
						<% } %>
						<% if ( Prof.getEducationBackground() != null && !Prof.getEducationBackground().isEmpty() && (Prof.getEdBackgroundVisibility() || isSelf || isAdmin || (currentSession != null && db.areProfessionalsConnected(profID, sessionProfID)))) { %>
							<h2 class="my_h2">Education Background</h2>
							<p id="edBackground"><%= Prof.getEducationBackground() %></p>
							<br>
						<% } %>
						<% if ( Prof.getSkills() != null && !Prof.getSkills().isEmpty() && (Prof.getSkillsVisibility() || isSelf || isAdmin || (currentSession != null && db.areProfessionalsConnected(profID, sessionProfID)))) { %>
							<h2 class="my_h2">Skills</h2>
							<p id="skills"><%= Prof.getSkills() %></p>
							<br>
						<% } %>
						<h2 class="my_h2">Contact Information</h2>
						<label>Email: </label><%= Prof.getEmail() %><br>
					  	<label>Phone Number: </label><%= Prof.getPhone() %><br>
					</div>
				</div>
			</div>
		<% 	if ( isAdmin || (currentSession != null && db.areProfessionalsConnected(profID, sessionProfID) && !isSelf) ) { %>
				<div class="connections_bar">
					<h2 class="my_h2">Connections</h2>
					<div class="grid_container_container">					
						<% List<Professional> Connections = db.getConnectedProfessionalsFor(profID);
						   if ( Connections != null && Connections.size() > 0) { %>
								<div id="connections_grid" class="grid_container">
								<%	for (Professional p : Connections) { %>
										<a class="grid_item" href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">
											<div class="text-dark">
												<img class="img-thumbnail" src="<%= p.getProfilePicURI() %>" alt="Profile picture"><br>
												<b><%= p.getFirstName() %> <%= p.getLastName() %></b><br>
												<% if (p.getEmploymentStatus() != null && !p.getEmploymentStatus().isEmpty()) { %> 
													<%= p.getEmploymentStatus() %> 
												<% } %>
												<br>
												<% if (p.getEmploymentInstitution() != null && !p.getEmploymentInstitution().isEmpty()) { %> 
													<%= p.getEmploymentInstitution() %>
												<% } %>
												<br>
											</div>
										</a>
								<%	} %>
								</div>
						<% } else if ( Connections != null ) { %>
								<p><i>This Professional does not have any connections.</i></p>
						<% } %>
					</div>
				</div>
		<%	 } %>
			<!-- Back button only for admin -->
			<% if (isAdmin) { %>
					<div class="text-center">
						<a href="/TEDProject/admin/AdminServlet" class="btn btn-secondary">Return to admin page</a>
					</div>
			<% } %>
			<script>
				<% if ( Prof != null ) { %>
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
				<% } %>
			</script>
	<% 	} 
		db.close(); %>
		<jsp:include page="/footer.html"></jsp:include>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Work Ad Page</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.WorkAd, model.Application, model.MyUtil" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/simplemde.min.css">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap-grid.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/workads.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/applications.css"/>
	<!-- JS -->
	<script src="/TEDProject/js/lib/jquery-3.3.1.min.js"></script>
	<script src="/TEDProject/js/lib/bootstrap.min.js"></script>
	<script src="/TEDProject/js/lib/simplemde.min.js"></script>
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
			if (request.getSession(false) != null && currentSession.getAttribute("ProfID") != null) {
				profID = (int) currentSession.getAttribute("ProfID");
			} else {
				profID = -1;
			}
			boolean isAdmin = ( currentSession != null && currentSession.getAttribute("isAdmin") != null && ((boolean) currentSession.getAttribute("isAdmin")) );
			// Navbar for professionals or visitors
			if (!isAdmin) { 
				if (profID > -1) { %>
					<jsp:include page="ProfNavBar.jsp">
						<jsp:param name="activePage" value="null"/> 
					</jsp:include>
			 <% } else { %>
			 		<jsp:include page="VisitorNavBar.jsp"></jsp:include>
			 <% } 
		 	} %>
		 	<!-- Alerts -->
		    <div id="workAdCreationSuccessAlert" class="alert alert-success alert-dismissible" role="alert" style="display:none;">
				Your Work Ad was created successfully.
				<button type="button" class="close" data-dismiss="alert" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
		    <div id="workAdEditSuccessAlert" class="alert alert-success alert-dismissible" role="alert" style="display:none;">
				Your Work Ad was updated successfully.
				<button type="button" class="close" data-dismiss="alert" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<script src="/TEDProject/js/alerts/workAdPageAlerts.js"></script>
		 	<!-- Work Ad -->
			<h1 class="my_h1"><%= ad.getTitle() %></h1>
			<h5 class="text-center">Published by <a href="/TEDProject/ProfileLink?ProfID=<%= ad.getPublishedByID() %>"><%= db.getProfessionalFullName(ad.getPublishedByID()) %></a> on <%= MyUtil.printDate(ad.getPostedDate(), true) %></h5>
			<div class="ProfileOptions">
			<% if (profID > -1) { 
				   if (profID == ad.getPublishedByID()) { 		// ad was published by current prof
				   		if (!isAdmin) { %>
							<a href="/TEDProject/prof/NavigationServlet?page=EditWorkAd&AdID=<%= ad.getID() %>" class="btn btn-primary">Edit</a>
						<% } %>
				   		<a href="/TEDProject/prof/WorkAdManagementServlet?action=delete&AdID=<%= ad.getID() %>" class="btn btn-danger" 
				   			onclick="return confirm('Deleting this Work Ad will also delete all the applications that have been made on it. This cannot be undone. Are you sure you want to delete it?')">
				   			Delete</a>
				<% }
			   } %>
			</div>
			<br>
			<div class="workAdDescription">
		  		<p id="adDescription"><%= ad.getDescription() %></p>
		  	</div>
		  	<br>
			<% if (isAdmin) { %>
				<a href="/TEDProject/admin/AdminServlet">Return to admin page</a>
			<% } else if (profID == ad.getPublishedByID()) {	// ad belongs to current prof; show its applications	%>
				<div>
					<h2 class="my_h2">Applications made to this Work Ad</h2>
				 	<div class="list-group">
					<%  List<Application> applications = db.getApplications(ad.getID(), true);
						if (applications != null && !applications.isEmpty()) { 
						   int count = 1;
						   for (Application apl : applications) { %>
								<div class="list-group-item list-group-item-action flex-column align-items-start apl_accordion">
									<div class="d-flex w-100 apl_arrow">
										<div class="d-flex w-100 justify-content-between">
											<object><a href="/TEDProject/ProfileLink?ProfID=<%= apl.getProfID() %>"><%= db.getProfessionalFullName(apl.getProfID()) %></a></object>
								  			<small><%= MyUtil.printDate(apl.getApplyDate(), true) %></small>
								  		</div>
							  		</div>
								</div>
								<div class="apl_panel">
									<br>
									<p id="aplNote<%= count %>"><%= apl.getNote() %></p>
								</div>
								<span id="aplFocusPoint<%= count %>"></span>
								<script>
									var aplNote = document.getElementById("aplNote" + <%= count %>);
									if (aplNote) aplNote.innerHTML = SimpleMDE.prototype.markdown(`<%= apl.getNote().replace("\\", "\\\\").replace("`", "\\`") %>`);
							  	</script>
							<% count++;		
						  	} %>
					<%  } else {  %>
							<p><i>No applications have been made to this Work Ad.</i></p>
					<%  } %>
				 	</div>
				</div>
			<% } else if (profID > -1) {	
				if (! db.pendingWorkAdApplication(profID, ad.getID())) {		// current prof can apply		%>
					<div class="buttonContainer">
						<button id="focusButton" class="btn btn-primary" type="button" onclick="$('#focusPoint').scrollTo(1000)" data-toggle="collapse" data-target="#collapseEditor" aria-expanded="false" aria-controls="collapseEditor">Open Application Form</button>
					</div>
					<div class="collapse" id="collapseEditor">		
						<form method=POST action="/TEDProject/prof/WorkAdManagementServlet?action=apply&AdID=<%= ad.getID() %>">
					   		<textarea id="applyNote" name="applyNote"></textarea>
						   	<div class="buttonContainer text-right mt-0">
								<input type="submit" value="Submit" class="btn btn-primary">
								<button class="btn btn-secondary" type="button" data-toggle="collapse" data-target="#collapseEditor" aria-expanded="false" aria-controls="collapseEditor">Cancel</button>
							</div>
					   	</form>
					</div>
					<span id="focusPoint"></span>
				<% } else { 	// current prof has already applied		%>
					<div class="buttonContainer">	
						<small class="text-secondary"><i>You have already applied for this Work Ad</i></small><br>
						<a href="/TEDProject/prof/WorkAdManagementServlet?action=cancel&AdID=<%= ad.getID() %>" class="btn btn-outline-danger" 
							onclick="return confirm('Are you sure you want to cancel your application to &quot;<%= ad.getTitle() %>&quot;?')">
							Cancel Application</a>
					</div>
				<% }
			  } %>
			<script>
				var adDescription = document.getElementById("adDescription");
				if (adDescription) adDescription.innerHTML = SimpleMDE.prototype.markdown(`<%= ad.getDescription().replace("\\", "\\\\").replace("`", "\\`") %>`);
				var applyNote = document.getElementById("applyNote");
				if (applyNote) var applyNoteSMDE = new SimpleMDE({ element: applyNote, showIcons: ["code", "table"] });
			</script>
			<script src="/TEDProject/js/apl_accordion.js"></script>    
			<script src="/TEDProject/js/util.js"></script>
	<% } 
	   db.close(); %>
	   <jsp:include page="/footer.html"></jsp:include>
	</div>
</body>
</html>
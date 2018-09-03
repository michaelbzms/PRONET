<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.WorkAd, model.Application, model.MyUtil" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style2.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/applications.css"/>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">	
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
	<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
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
			// Navbar only for professionals
			if (profID > -1 && !isAdmin) { %>
				<jsp:include page="ProfNavBar.jsp"> 
					<jsp:param name="activePage" value="WorkAds"/> 
				</jsp:include>
		<% } %>
			<h1 class="my_h1"><%= ad.getTitle() %></h1>
			<h5 class="text-center">Published by <a href="/TEDProject/ProfileLink?ProfID=<%= ad.getPublishedByID() %>"><%= db.getProfessionalFullName(ad.getPublishedByID()) %></a> on <%= MyUtil.printDate(ad.getPostedDate(), true) %></h5>
			<div class="ProfileOptions">
			<% if (profID > -1) { 			// TODO: Admin should be able to delete ads?
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
		  	<p id="adDescription"><%= ad.getDescription() %></p>
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
							<p>No applications have been made to this Work Ad.</p>
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
						   	<div class="buttonContainer text-right">
								<input type="submit" value="Submit" class="btn btn-primary">
								<button class="btn btn-secondary" type="button" data-toggle="collapse" data-target="#collapseEditor" aria-expanded="false" aria-controls="collapseEditor">Cancel</button>
							</div>
					   	</form>
					</div>
					<span id="focusPoint"></span>
				<% } else { 	// current prof has already applied		%>
					<div class="buttonContainer">	
						<small class="text-secondary">You have already applied for this Work Ad</small><br>
						<a href="/TEDProject/prof/WorkAdManagementServlet?action=cancel&AdID=<%= ad.getID() %>" class="btn btn-outline-danger" 
							onclick="return confirm('Are you sure you want to cancel your application to &quot;<%= ad.getTitle() %>&quot;?')">
							Cancel Application</a>
					</div>
				<% }
			   } 
		   } 
		   db.close(); %>
		   <jsp:include page="/footer.html"></jsp:include>
	</div>
	<% if (ad != null) { %>
		<script>
			var adDescription = document.getElementById("adDescription");
			if (adDescription) adDescription.innerHTML = SimpleMDE.prototype.markdown(`<%= ad.getDescription().replace("\\", "\\\\").replace("`", "\\`") %>`);
			var applyNoteSMDE = new SimpleMDE({ element: document.getElementById("applyNote"), showIcons: ["code", "table"] });
		</script>
	<% } %>
	<script src="/TEDProject/Javascript/apl_accordion.js"></script>    
	<script>
		scrollingElement = (document.scrollingElement || document.body);
		function scrollSmoothToBottom() {
		   $(scrollingElement).animate({
		      scrollTop: document.body.scrollHeight
		   }, 1000);
		}
		$.fn.scrollTo = function(speed) {
		    $('html, body').animate({
		        scrollTop: parseInt($(this).offset().top)
		    }, speed);
		};
	</script>
</body>
</html>
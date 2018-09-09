<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - WorkAds</title>
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality, model.WorkAd, model.Application, model.MyUtil" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style2.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/applications.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/workads.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
	<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
</head>
<body>
	<% 	DataBaseBridge db = new DataBaseBridge();
		Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
		if ( !db.checkIfConnected() ) { %>
			<h2 class="my_h2">DATABASE ERROR</h2>	
			<p>It appears that our database is down. Please contact the site's administrators.</p>
	<%	} else if ( prof == null ) {  %>
			<h2 class="my_h2">INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { %>
			<div class="main_container">
				<jsp:include page="ProfNavBar.jsp"> 
					<jsp:param name="activePage" value="WorkAds"/> 
				</jsp:include>
				<div>
					<h2 class="my_h2">Work Ads from Connected Professionals</h2>
					<div class="list-group ad_list_container">
					<% List<WorkAd> connectedWorkAds = db.getWorkAds(prof.getID(), 1);
						if (connectedWorkAds != null && !connectedWorkAds.isEmpty()) { 
						   for (WorkAd ad : connectedWorkAds) { %>
								<a href="/TEDProject/WorkAdLink?AdID=<%= ad.getID() %>" class="list-group-item list-group-item-action flex-column align-items-start">
							  		<div class="d-flex w-100 justify-content-between">
							  			<h5 class="mb-1"><%= ad.getTitle() %></h5>
							  			<small><%= MyUtil.getTimeAgo(ad.getPostedDate()) %></small>
							  		</div>
							  		<small>Published by <object><a href="/TEDProject/ProfileLink?ProfID=<%= ad.getPublishedByID() %>"><%= db.getProfessionalFullName(ad.getPublishedByID()) %></a></object></small>
							  	</a>
						<%	} %>
					<%  } else {  %>
							<p>There are no Work Ads from Professionals you're connected with.</p>
					<%  } %>
				 	</div>
				</div>
				<br>
				<div>
					<h2 class="my_h2">Work Ads from Others</h2>
					<div class="list-group ad_list_container">
					<% List<WorkAd> otherWorkAds = db.getWorkAds(prof.getID(), 2);
						if (otherWorkAds != null && !otherWorkAds.isEmpty()) { 
						   for (WorkAd ad : otherWorkAds) { %>
								<a href="/TEDProject/WorkAdLink?AdID=<%= ad.getID() %>" class="list-group-item list-group-item-action flex-column align-items-start">
							  		<div class="d-flex w-100 justify-content-between">
							  			<h5 class="mb-1"><%= ad.getTitle() %></h5>
							  			<small><%= MyUtil.getTimeAgo(ad.getPostedDate()) %></small>
							  		</div>
							  		<small>Published by <object><a href="/TEDProject/ProfileLink?ProfID=<%= ad.getPublishedByID() %>"><%= db.getProfessionalFullName(ad.getPublishedByID()) %></a></object></small>
							  	</a>
						<%	} %>
					<%  } else {  %>
							<p>No Professionals have applied to this Work Ad yet.</p>
					<%  } %>
				 	</div>
				</div>
				<br>
				<div>
					<div class="justify-content-between">
						<a href="/TEDProject/prof/NavigationServlet?page=EditWorkAd" class="btn btn-primary float-right">Create New</a>
						<h2 class="my_h2" style="padding-top: 6px">My Work Ads</h2>
					</div>
					<div class="list-group ad_list_container">
					<% List<WorkAd> myWorkAds = db.getWorkAds(prof.getID(), 0);
						if (myWorkAds != null && !myWorkAds.isEmpty()) { 
						   for (WorkAd ad : myWorkAds) { %>
								<a href="/TEDProject/WorkAdLink?AdID=<%= ad.getID() %>" class="list-group-item list-group-item-action flex-column align-items-start">
							  		<div class="d-flex w-100 justify-content-between">
							  			<h5 class="mb-1"><%= ad.getTitle() %></h5>
							  			<small><%= MyUtil.getTimeAgo(ad.getPostedDate()) %></small>
							  		</div>
							  	</a>
						<%	} %>			
					<%  } else {  %>
							<p>You haven't posted any Work Ads.</p>
					<%  } %>
				 	</div>
				</div>
				<br>
				<div>
					<h2 class="my_h2">My Applications</h2>
					<div class="list-group">
					<%  List<Application> applications = db.getApplications(prof.getID(), false);
						if (applications != null && !applications.isEmpty()) { 
						   int count = 1;
						   WorkAd appliedAd;
						   for (Application apl : applications) { 
							   appliedAd = db.getWorkAd(apl.getAdID());	%>
								<div class="list-group-item list-group-item-action flex-column align-items-start apl_accordion">
									<div class="d-flex w-100 apl_arrow">
										<div class="d-flex w-100 justify-content-between">
											<div>
												<object><a href="/TEDProject/WorkAdLink?AdID=<%= appliedAd.getID() %>"><%= appliedAd.getTitle() %></a></object><br>
								  				<small>Published by <object><a href="/TEDProject/ProfileLink?ProfID=<%= appliedAd.getPublishedByID() %>"><%= db.getProfessionalFullName(appliedAd.getPublishedByID()) %></a></object></small>			
								  			</div>
								  			<div>
								  				<a href="/TEDProject/prof/WorkAdManagementServlet?action=cancel&AdID=<%= appliedAd.getID() %>" class="btn btn-outline-danger" 
										   			onclick="return confirm('Are you sure you want to cancel your application to &quot;<%= appliedAd.getTitle() %>&quot;?')">
													Cancel Application</a>
								  				<small class="timeAgo_block"><%= MyUtil.getTimeAgo(apl.getApplyDate()) %></small>
								  			</div>
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
									if (aplNote) aplNote.innerHTML = SimpleMDE.prototype.markdown(`<%= apl.getNote().replace("\\", "\\\\").replace("`", "\\`") %>`);   //TODO: are you sure replace === replaceALL?
							  	</script>
							<% count++;		
						  	} %>
					<%  } else {  %>
							<p>You haven't applied to any Work Ads.</p>
					<%  } %>
				 	</div>
				</div>
				<jsp:include page="/footer.html"></jsp:include>
			</div>
	<% 	}
		db.close(); %>
	<script src="/TEDProject/Javascript/apl_accordion.js"></script>   
</body>
</html>
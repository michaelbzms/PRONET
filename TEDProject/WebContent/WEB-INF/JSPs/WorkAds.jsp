<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - WorkAds</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality, model.WorkAd, model.Application, model.MyUtil, model.KNNWorkAds, model.SkillRelevanceEvaluator" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/applications.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/workads.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
	<script src="/TEDProject/Javascript/simplemde.min.js"></script>
</head>
<body>
	<div class="main_container">
	<% 	DataBaseBridge db = new DataBaseBridge();
		Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
		if ( !db.checkIfConnected() ) { %>
			<h2 class="my_h2">DATABASE ERROR</h2>	
			<p>It appears that our database is down. Please contact the site's administrators.</p>
	<%	} else if ( prof == null ) {  %>
			<h2 class="my_h2">INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else {
			int MAXIMUM_ADS_SHOWN = 2000;        // CONFIG: This limits ads fetched from database to the 'MAXIMUM_ADS_SHOWN' most recent ads (assuming that the user would never scroll more than that number of ads)
			SkillRelevanceEvaluator skill_eval = null; %>
			<jsp:include page="ProfNavBar.jsp"> 
				<jsp:param name="activePage" value="WorkAds"/> 
			</jsp:include>
			<div>
				<h2 class="my_h2">Work Ads from Connected Professionals</h2>
				<div class="list-group ad_list_container">
				<% 	{   // Load workAdIds for connected professionals, order them based on prof's skills and previously applied ads with KNN and then present them
						int[] workAdsIDs = db.getWorkAdsIDs(prof.getID(), 0, MAXIMUM_ADS_SHOWN);
						if (workAdsIDs != null && workAdsIDs.length > 0) {
							if (workAdsIDs.length > 1) {
								if ( skill_eval == null && prof.getSkills() != null  ) { skill_eval = new SkillRelevanceEvaluator(prof.getSkills()); }
								boolean res = SiteFunctionality.reorderWorkAds(db, prof.getID(), workAdsIDs, skill_eval);
								if (!res) { System.err.println("Error: reordering work ads failed"); }
							}
						   	for (int i = 0 ; i < workAdsIDs.length ; i++) {
						   		WorkAd ad = db.getWorkAd(workAdsIDs[i]); %>
								<a href="/TEDProject/WorkAdLink?AdID=<%= ad.getID() %>" class="list-group-item list-group-item-action flex-column align-items-start">
							  		<div class="d-flex w-100 justify-content-between">
							  			<h5 class="mb-1"><%= ad.getTitle() %></h5>
							  			<small data-toggle="tooltip" data-placement="top" title="<%= MyUtil.printDate(ad.getPostedDate(), true) %>"><%= MyUtil.getTimeAgo(ad.getPostedDate()) %></small>
							  		</div>
							  		<small>Published by <object><a href="/TEDProject/ProfileLink?ProfID=<%= ad.getPublishedByID() %>"><%= db.getProfessionalFullName(ad.getPublishedByID()) %></a></object></small>
							  	</a>
						<%	} %>
					<%  } else {  %>
							<p><i>There are no Work Ads from Professionals you're connected with.</i></p>
					<%  } 
					} %>
			 	</div>
			</div>
			<br>
			<div>
				<h2 class="my_h2">Work Ads from Others</h2>
				<div class="list-group ad_list_container">
				<% 	{	// Load workAdIds for NOT connected professionals, order them based on prof's skills and previously applied ads with KNN and then present them
						int[] workAdsIDs = db.getWorkAdsIDs(prof.getID(), 1, MAXIMUM_ADS_SHOWN);
						if (workAdsIDs != null && workAdsIDs.length > 0) {
							if (workAdsIDs.length > 1) {
								if ( skill_eval == null && prof.getSkills() != null ) { skill_eval = new SkillRelevanceEvaluator(prof.getSkills()); }
								boolean res = SiteFunctionality.reorderWorkAds(db, prof.getID(), workAdsIDs, skill_eval);
								if (!res) { System.err.println("Error: reordering work ads failed"); }
							}
							for (int i = 0 ; i < workAdsIDs.length ; i++) {
						   		WorkAd ad = db.getWorkAd(workAdsIDs[i]); %>
								<a href="/TEDProject/WorkAdLink?AdID=<%= ad.getID() %>" class="list-group-item list-group-item-action flex-column align-items-start">
							  		<div class="d-flex w-100 justify-content-between">
							  			<h5 class="mb-1"><%= ad.getTitle() %></h5>
							  			<small data-toggle="tooltip" data-placement="top" title="<%= MyUtil.printDate(ad.getPostedDate(), true) %>"><%= MyUtil.getTimeAgo(ad.getPostedDate()) %></small>
							  		</div>
							  		<small>Published by <object><a href="/TEDProject/ProfileLink?ProfID=<%= ad.getPublishedByID() %>"><%= db.getProfessionalFullName(ad.getPublishedByID()) %></a></object></small>
							  	</a>
						<%	} %>
					<%  } else {  %>
							<p><i>There are no Work Ads from Professionals you're not connected with.</i></p>
					<%  }
					} %>
			 	</div>
			</div>
			<br>
			<div>
				<div class="justify-content-between">
					<a href="/TEDProject/prof/NavigationServlet?page=EditWorkAd" class="btn btn-primary float-right">Create New</a>
					<h2 class="my_h2 pt-1">My Work Ads</h2>
				</div>
				<div class="list-group ad_list_container">
				<% List<WorkAd> myWorkAds = db.getWorkAdsFromProf(prof.getID());
					if (myWorkAds != null && !myWorkAds.isEmpty()) { 
					   for (WorkAd ad : myWorkAds) { %>
							<a href="/TEDProject/WorkAdLink?AdID=<%= ad.getID() %>" class="list-group-item list-group-item-action flex-column align-items-start">
						  		<div class="d-flex w-100 justify-content-between">
						  			<h5 class="mb-1"><%= ad.getTitle() %></h5>
						  			<small data-toggle="tooltip" data-placement="top" title="<%= MyUtil.printDate(ad.getPostedDate(), true) %>"><%= MyUtil.getTimeAgo(ad.getPostedDate()) %></small>
						  		</div>
						  	</a>
					<%	} %>			
				<%  } else {  %>
						<p><i>You haven't posted any Work Ads.</i></p>
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
							  				<small class="timeAgo_block" data-toggle="tooltip" data-placement="top" title="<%= MyUtil.printDate(apl.getApplyDate(), true) %>"><%= MyUtil.getTimeAgo(apl.getApplyDate()) %></small>
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
								if (aplNote) aplNote.innerHTML = SimpleMDE.prototype.markdown(`<%= apl.getNote().replace("\\", "\\\\").replace("`", "\\`") %>`);
						  	</script>
						<% count++;		
					  	} %>
				<%  } else {  %>
						<p><i>You haven't applied to any Work Ads.</i></p>
				<%  } %>
			 	</div>
			</div>
			<script src="/TEDProject/Javascript/apl_accordion.js"></script>
	<% 	}
		db.close(); %>
		<jsp:include page="/footer.html"></jsp:include>
	</div> 
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - WorkAds</title>
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality, model.WorkAd, model.MyUtil" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/workads.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
	<% 	DataBaseBridge db = new DataBaseBridge();
		Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
		if ( !db.checkIfConnected() ) { %>
			<h2>DATABASE ERROR</h2>	
			<p>It appears that our database is down. Please contact the site's administrators.</p>
	<%	} else if ( prof == null ) {  %>
			<h2>INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { %>
			<div class="main_container">
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
			
				<div>
					<h2>Work Ads from Connected Professionals</h2>
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
					<h2>Work Ads from Others</h2>
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
							<p>There are no Work Ads from other Professionals.</p>
					<%  } %>
				 	</div>
				</div>
				<br>
				<div>
					<div class="justify-content-between">
						<h2>My Work Ads <a href="/TEDProject/prof/NavigationServlet?page=EditWorkAd" class="btn btn-primary float-right" style="border-bottom: 10px">Create New</a></h2>
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
			</div>
	<% 	}
		db.close(); %>
</body>
</html>
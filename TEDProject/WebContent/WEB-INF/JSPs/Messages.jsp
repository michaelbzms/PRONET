<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<title>PRONET - Messages</title>
	<%@ page import="model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>
</head>
<body>
	<% 	DataBaseBridge db = new DataBaseBridge(); 
		Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
		if ( prof == null ) {  %>
			<h2>INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { %>
		<div class="main_container">
			<nav class="navbar">
				<ul>
					<li><a href="/TEDProject/prof/NavigationServlet?page=HomePage">Home Page</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Network">Network</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=WorkAds">Work Ads</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Messages">Messages</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Notifications">Notifications</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=PersonalInformation">Personal Information</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Settings">Settings</a></li>
					<li><form action="/TEDProject/LogoutServlet" method="post">
						<input type="submit" value="Logout" >
					</form>
					</li>
				</ul>
			</nav>
			<h2>Here be messages for <%= prof.getFirstName() %>  <%= prof.getLastName() %>!</h2>
		
		</div>
	<% } %>
	<% db.close(); %>
</body>
</html>
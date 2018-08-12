<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<title>PRONET - Messages</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
</head>
<body>
	<!-- Not safe - need sessions -->
	<% int ID = Integer.parseInt(request.getAttribute("ProfID").toString()); %>
	<div class="main_container">
		<nav class="navbar">
			<ul>
				<!-- NavigationServlet should forward request to the correct JSP along with ProfID Attribute -->
				<li><a href="/TEDProject/NavigationServlet?page=HomePage&ID=<%= ID %>">Home Page</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Network&ID=<%= ID %>">Network</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=WorkAds&ID=<%= ID %>">Work Ads</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Messages&ID=<%= ID %>">Messages</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Notifications&ID=<%= ID %>">Notifications</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=PersonalInformation&ID=<%= ID %>">Personal Information</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Settings&ID=<%= ID %>">Settings</a></li>
			</ul>
		</nav>
	
	
	</div>
</body>
</html>
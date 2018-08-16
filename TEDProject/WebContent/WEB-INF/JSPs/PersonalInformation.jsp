<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<title>PRONET - Personal Information</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
</head>
<body>
	<div class="main_container">
		<nav class="navbar">
			<ul>
				<!-- NavigationServlet should forward request to the correct JSP along with ProfID Attribute -->
				<li><a href="/TEDProject/NavigationServlet?page=HomePage">Home Page</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Network">Network</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=WorkAds">Work Ads</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Messages">Messages</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Notifications">Notifications</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=PersonalInformation">Personal Information</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Settings">Settings</a></li>
			</ul>
		</nav>
	
	
	</div>
</body>
</html>
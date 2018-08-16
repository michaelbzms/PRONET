<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<title>PRONET - Network</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
</head>
<body>
	<div class="main_container">
		<nav class="navbar">
			<ul>
				<li><a href="/TEDProject/NavigationServlet?page=HomePage">Home Page</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Network">Network</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=WorkAds">Work Ads</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Messages">Messages</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Notifications">Notifications</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=PersonalInformation">Personal Information</a></li>
				<li><a href="/TEDProject/NavigationServlet?page=Settings">Settings</a></li>
				<li><form action="LogoutServlet" method="post">
						<input type="submit" value="Logout" >
					</form>
				</li>
			</ul>
		</nav>
	
	
	</div>
</body>
</html>
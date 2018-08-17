<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<title>PRONET - Settings</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
</head>
<body>
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
		<div class="buttonContainer">
			<a href="/TEDProject/prof/NavigationServlet?page=ChangeSettings&attr=email" class="changeButton">Change Email</a>
		</div>
		<div class="buttonContainer">
			<a href="/TEDProject/prof/NavigationServlet?page=ChangeSettings&attr=password" class="changeButton">Change Password</a>
		</div>
	</div>
</body>
</html>
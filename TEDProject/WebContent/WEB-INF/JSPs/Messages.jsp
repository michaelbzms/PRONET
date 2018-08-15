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
	<%  int LoggedProfID = -1;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("ProfID")) LoggedProfID = Integer.parseInt(cookie.getValue());
			}
		}
		DataBaseBridge db = new DataBaseBridge();
		Professional loggedProf;
		if (LoggedProfID < 0) {
			System.out.println("Failed to retrieve ProfID. Maybe cookies are disabled?");
			// TODO: error page
			return;
		} else {
			loggedProf = db.getProfessional(LoggedProfID);
		}
	%>
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
			</ul>
		</nav>
		<h2>Here be messages for <%= loggedProf.firstName %>  <%= loggedProf.lastName %>!</h2>
	
	</div>
</body>
</html>
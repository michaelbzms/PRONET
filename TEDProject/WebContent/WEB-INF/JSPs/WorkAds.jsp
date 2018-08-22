<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - WorkAds</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
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
	
	
	</div>
</body>
</html>
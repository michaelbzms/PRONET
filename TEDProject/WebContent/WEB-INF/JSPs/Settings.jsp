<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Settings</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="model.Professional, model.DataBaseBridge" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
	<div class="main_container">
		<jsp:include page="ProfNavBar.jsp"> 
			<jsp:param name="activePage" value="Settings"/> 
		</jsp:include>
		<div class="buttonContainer changeButton">
			<a href="/TEDProject/prof/NavigationServlet?page=ChangeSettings&attr=email" class="btn btn-primary btn-lg">Change Email</a>
		</div>
		<div class="buttonContainer changeButton">
			<a href="/TEDProject/prof/NavigationServlet?page=ChangeSettings&attr=password" class="btn btn-primary btn-lg">Change Password</a>
		</div>
		<jsp:include page="/footer.html"></jsp:include>
	</div>
</body>
</html>
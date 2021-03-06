<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Settings</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap-grid.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<!-- JS -->
	<script src="/TEDProject/js/lib/jquery-3.3.1.min.js"></script>
	<script src="/TEDProject/js/lib/bootstrap.min.js"></script>
</head>
<body>
	<div class="main_container">
		<jsp:include page="ProfNavBar.jsp"> 
			<jsp:param name="activePage" value="Settings"/> 
		</jsp:include>
		<!-- Alerts -->
	    <div id="emailChangeSuccessAlert" class="alert alert-success alert-dismissible" role="alert" style="display:none;">
			Your email was changed successfully.
			<button type="button" class="close" data-dismiss="alert" aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
		</div>
	    <div id="passwordChangeSuccessAlert" class="alert alert-success alert-dismissible" role="alert" style="display:none;">
			Your password was changed successfully.
			<button type="button" class="close" data-dismiss="alert" aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
		</div>
		<script src="/TEDProject/js/alerts/settingsAlerts.js"></script>
		<!-- Buttons to actually change seetings -->
		<div class="buttonContainer changeButton">
			<a href="/TEDProject/prof/NavigationServlet?page=ChangeSettings&attr=email" class="btn btn-primary btn-lg">Change Email</a>
		</div>
		<div class="buttonContainer changeButton">
			<a href="/TEDProject/prof/NavigationServlet?page=ChangeSettings&attr=password" class="btn btn-primary btn-lg">Change Password</a>
		</div>
		<div class="buttonContainer changeButton">
			<a href="/TEDProject/prof/NavigationServlet?page=ChangeSettings&attr=deleteAccount" class="btn btn-danger btn-lg">Delete Account</a>
		</div>
		<jsp:include page="/footer.html"></jsp:include>
	</div>
</body>
</html>
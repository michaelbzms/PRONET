<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Professional Networking and more</title>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
	<div class="main_container" style="align: center">
	<% if ( request.getAttribute("attr").equals("email") ) { %>
		<h2>Change Email</h2>
		<div class="form">
			<form method="POST" action="/TEDProject/ChangeServlet?attr=email">
				<label>Password: </label><input type="password" name="password"><br>
				<label>New Email: </label><input type="email" name="newEmail"><br>
				<input style="display: inline-block; margin: 0px 0px 0px 160px" type="submit" value="Submit"><br>
			</form>
		</div>
	<% } else if ( request.getAttribute("attr").equals("password") ) { %>
		<h2>Change Password</h2>
		<div class="form">
			<form method="POST" action="/TEDProject/ChangeServlet?attr=password">
				<label>Current Password: </label><input type="password" name="currentPassword"><br>
				<label>New Password: </label><input type="password" name="newPassword"><br>
				<label>Confirm New Password: </label><input type="password" name="reNewPassword"><br>
				<input style="display: inline-block; margin: 0px 0px 0px 160px" type="submit" value="Submit"><br>
			</form>
		</div>
	<% } else { %>		<!-- This should never happen -->
		<h2>UNKNOWN ERROR</h2>
		<p>Well, this is embarassing... An unknown error has occured! :(</p>
	<% } %>
		<br>
		<a style="display: inline-block" href="/TEDProject/prof/NavigationServlet?page=Settings">Go back to Settings</a>
	</div>
</body>
</html>
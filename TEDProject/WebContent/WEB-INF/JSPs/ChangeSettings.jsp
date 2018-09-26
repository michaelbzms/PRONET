<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Professional Networking and more</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
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
	<% if ( request.getAttribute("attr").equals("email") ) { %>
			<h2 class="my_h2 text-center">Change Email</h2>
			<br>
			<div class="form">
				<form method="POST" action="/TEDProject/prof/ChangeServlet?attr=email">
					<label>Password: </label><input class="form_field" type="password" name="password" required><br>
					<label>New Email: </label><input class="form_field" type="email" name="newEmail" required><br>
					<input class="btn btn-light d-block mt-2 mb-1 ml-auto mr-auto" type="submit" value="Submit">
				</form>
			</div>
	<% } else if ( request.getAttribute("attr").equals("password") ) { %>
			<h2 class="my_h2 text-center">Change Password</h2>
			<br>
			<div class="form pwd_form">
				<form method="POST" action="/TEDProject/prof/ChangeServlet?attr=password" onsubmit="return checkIdenticalPasswords()">
					<label class="form_field">Current Password: </label><input class="form_field" type="password" name="currentPassword" required><br>
					<label class="form_field">New Password: </label><input id="newPassword" class="form_field" type="password" name="newPassword" required><br>
					<label class="form_field">Confirm New Password: </label><input id="reNewPassword" class="form_field" type="password" name="reNewPassword" required><br>
					<input class="btn btn-light d-block mt-2 mb-1 ml-auto mr-auto" type="submit" value="Submit">
				</form>
			</div>
	<% } else if ( request.getAttribute("attr").equals("deleteAccount") ) { %>
			<h2 class="my_h2 text-center">Delete Account</h2>
			<br>
			<div class="form pwd_form">		<!-- TODO: opinions? -->
				<form method="POST" action="/TEDProject/prof/ChangeServlet?attr=deleteAccount" onsubmit="return confirm('This action is irreversible. Are you sure you want to delete your account?')">
					<label class="form_field">Type your Password: </label><input class="form_field" type="password" name="password" required><br>
					<input class="btn btn-light d-block mt-3 mb-2 ml-auto mr-auto" type="submit" value="Delete Account">
				</form>
			</div>
	<% } else { %>		<!-- This should never happen -->
			<h2 class="my_h2">UNKNOWN ERROR</h2>
			<br>
			<p>Well, this is embarrassing... An unknown error has occurred! :(</p>
	<% } %>
		<br>
		<p class="text-center" ><a class="btn btn-secondary" href="/TEDProject/prof/NavigationServlet?page=Settings">Go back to Settings</a></p>
		<jsp:include page="/footer.html"></jsp:include>
	</div>
	<script>
		function checkIdenticalPasswords() {
			if ($("#newPassword").val() != $("#reNewPassword").val()) {
		        alert("Passwords do not match.");
		        return false;
		    }
			return true;
		}
	</script>
</body>
</html>
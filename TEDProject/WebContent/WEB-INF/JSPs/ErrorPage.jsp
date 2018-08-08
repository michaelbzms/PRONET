<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<title>PRONET - Professional Networking and more</title>
</head>
<body>
	<div class="main_container" style="text-align: center">
	<% if ( request.getAttribute("errorType").equals("invalidLogin") ) { %>
		<h2>LOGIN FAILED</h2>
		<p>Oops! The log in information that you entered appears to be incorrect.</p>
	<% } else if ( request.getAttribute("errorType").equals("notMatchingPasswords") ) { %>
		<h2>REGISTRATION FAILED</h2>
		<p>It seems that the password you re-entered does not match the first one.</p>
	<% } else if ( request.getAttribute("errorType").equals("emailTaken") ) { %>
		<h2>REGISTRATION FAILED</h2>
		<p>It seems that the email you entered is already registered to an account.</p>
	<% } else { %>
		<h2>UNKNOWN ERROR</h2>
		<p>Well, this is embarassing.. An unknown error has occured! :(</p>
	<% } %>
		<br>
		<a style="display: inline-block" href="/TEDProject">back</a>
	</div>
</body>
</html>
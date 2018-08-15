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
	<% if ( request.getAttribute("errorType").equals("invalidLoginEmail") ) { %>
		<h2>LOGIN FAILED</h2>
		<p>Oops! It appears that the email you entered does not belong to a registered account.</p>
	<% } else if ( request.getAttribute("errorType").equals("invalidLoginPassword") ) { %>	
		<h2>LOGIN FAILED</h2>
		<p>Oops! It  appears that the password that you entered is incorrect.</p>
	<% } else if ( request.getAttribute("errorType").equals("notMatchingPasswords") ) { %>
		<h2>REGISTRATION FAILED</h2>
		<p>It seems that the password you re-entered does not match the first one.</p>
	<% } else if ( request.getAttribute("errorType").equals("emailTaken") ) { %>
		<h2>REGISTRATION FAILED</h2>
		<p>It seems that the email you entered is already registered to an account.</p>
	<% } else if ( request.getAttribute("errorType").equals("invalidPageRequest") ) { %>
		<h2>INVALID PAGE REQUEST</h2>
		<p>The page you requested does not exist.</p>
	<% } else if ( request.getAttribute("errorType").equals("nullSession") ) { %>
		<h2>SESSION ENDED</h2>
		<p>Your session has ended.</p>
	<% } else { %>
		<h2>UNKNOWN ERROR</h2>
		<p>Well, this is embarassing.. An unknown error has occured! :(</p>
	<% } %>
		<br>
		<a style="display: inline-block" href="/TEDProject">Go back to Homepage</a>
	</div>
</body>
</html>
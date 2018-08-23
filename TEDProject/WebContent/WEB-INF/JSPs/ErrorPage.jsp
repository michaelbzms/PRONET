<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<title>PRONET - Professional Networking and more</title>
</head>
<body>
	<div class="main_container" style="text-align: center">
	<% if ( request != null &&  request.getAttribute("errorType") != null ) { %>
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
		<% } else if ( request.getAttribute("errorType").equals("illegalTextInput") ) { %>
			<h2>INVALID TEXT INPUT</h2>
			<p>It appears that you either gave too big an input for us or you entered one or more illegal input characters at one or more fields of a form. Make sure to avoid super long inputs as well as special characters and try again.</p>
		<% } else if ( request.getAttribute("errorType").equals("nullSession") ) { %>
			<h2>SESSION ENDED</h2>
			<p>Your session has ended. Please login again.</p>
		<% } else if ( request.getAttribute("errorType").equals("unauthorizedAccess") ) { %>
			<h2>Unauthorized Access</h2>
			<p>You are not authorized to access this page.</p>
		<% } else if ( request.getAttribute("errorType").equals("invalidCurrentPassword") ) { %>
			<h2>Settings Change Failed</h2>
			<p>The password you entered does not match your current password.</p>
		<% } else if ( request.getAttribute("errorType").equals("newEmailTaken") ) { %>
			<h2>Email Change Failed</h2>
			<p>It seems that the email you entered is already registered to an account.</p>
		<% } else if ( request.getAttribute("errorType").equals("dbError") ) { %>
			<h2>Database Error</h2>
			<p>There was an error connecting to the database.<br>
			If this error persists please contact the administrators.</p>
		<% } else if ( request.getAttribute("errorType").equals("emptyFormFields") ) { %>
			<h2>Form Submission Failed</h2>
			<p>One or more required fields were empty.</p>
		<% } else if ( request.getAttribute("errorType").equals("notMatchingPasswordsChange") ) { %>
			<h2>Password Change Failed</h2>
			<p>The password you re-entered does not match the first one.</p>
		<% } else if ( request.getAttribute("errorType").equals("unchangedPassword") ) { %>
			<h2>Password Change Failed</h2>
			<p>The new password you entered is identical to your current one.</p>
		<% } else { %>
			<h2>UNKNOWN ERROR: <%= request.getAttribute("errorType") %></h2>
			<p>Well, this is embarassing... An unknown error has occured! :(</p>
		<% } %>
	<% } else { %>
			<h2>NO ERRORS REPORTED BY US</h2>
			<p>How did you get here?!</p>
	<% } %>
		<br>
	<%  HttpSession currentSession = request.getSession(false);
		if ( currentSession == null || (request.getAttribute("errorType") != null && 
				 (  request.getAttribute("errorType").equals("invalidLoginEmail")    || request.getAttribute("errorType").equals("invalidLoginPassword")
				 || request.getAttribute("errorType").equals("notMatchingPasswords") || request.getAttribute("errorType").equals("emailTaken")
		   ) ) ) { %>
			<a style="display: inline-block" href="/TEDProject">Go back to welcome page</a>
	<% } else  if ( currentSession != null && ((boolean) currentSession.getAttribute("isAdmin")) ){ %>
			<a style="display: inline-block" href="/TEDProject/admin/AdminServlet">Go back to admin page</a>	
	<% } else { %>
			<a style="display: inline-block" href="<%= currentSession.getAttribute("lastVisited") %>">Back</a>
	<% } %>	
	</div>
</body>
</html>
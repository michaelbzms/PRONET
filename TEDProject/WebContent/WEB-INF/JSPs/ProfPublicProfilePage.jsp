<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<title>PRONET</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
</head>
<body>
	<div class="main_container">
		<% int ID = Integer.parseInt(request.getAttribute("ProfID").toString()); %>
		<% DataBaseBridge db = new DataBaseBridge(); %>
		<% Professional Prof = db.getProfessional(ID); %>
		<h1><%= Prof.firstName %>  <%= Prof.lastName %></h1>
		<!-- insert image here with  style = "float: right" -->
		<p>
		<% if ( Prof.employmentStatus != null ) { %>
			<%= Prof.employmentStatus %> <br> 
		<% } %>
		<% if ( Prof.employmentInstitution != null ) { %>
			<%= Prof.employmentInstitution %> <br> 
		<% } %>
		</p>
		<p>
		   <u>Contact Info:</u><br> 
		   email: <%= Prof.email %> <br>
		   phone number: <%= Prof.phone %> <br> 
		</p>
		<% if ( Prof.profExpVisibility && Prof.professionalExperience != null ) { %>
				<h2>Professional Experience</h2>
				<p><%= Prof.professionalExperience %></p>
		<% } %>
		<% if ( Prof.edBackgroundVisibility && Prof.educationBackground != null ) { %>
				<h2>Education Background</h2>
				<p><%= Prof.educationBackground %></p>
		<% } %>
		<% if ( Prof.skillsVisibility && Prof.skills != null ) { %>
				<h2>Skills</h2>
				<p><%= Prof.skills %></p>
		<% } %>
		<% db.close(); %>
	</div>
</body>
</html>
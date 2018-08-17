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
		<% db.close(); %>
		<h1><%= Prof.getFirstName() %>  <%= Prof.getLastName() %></h1>
		<!-- insert image here with  style = "float: right" -->
		<p style = "font-weight: bold">
		<% if ( Prof.getEmploymentStatus() != null ) { %>
			<%= Prof.getEmploymentStatus() %> <br> 
		<% } %>
		<% if ( Prof.getEmploymentInstitution() != null ) { %>
			<%= Prof.getEmploymentInstitution() %> <br> 
		<% } %>
		</p>
		<p>
		   <span style="text-decoration: underline">Contact Info:</span><br> 
		   email: <%= Prof.getEmail() %> <br>
		   phone number: <%= Prof.getPhone() %> <br> 
		</p>
		<% if ( Prof.isProfExpVisibility() && Prof.getProfessionalExperience() != null ) { %>
				<h2>Professional Experience</h2>
				<p><%= Prof.getProfessionalExperience() %></p>
		<% } %>
		<% if ( Prof.isEdBackgroundVisibility() && Prof.getEducationBackground() != null ) { %>
				<h2>Education Background</h2>
				<p><%= Prof.getEducationBackground() %></p>
		<% } %>
		<% if ( Prof.isSkillsVisibility() && Prof.getSkills() != null ) { %>
				<h2>Skills</h2>
				<p><%= Prof.getSkills() %></p>
		<% } %>
	</div>
</body>
</html>
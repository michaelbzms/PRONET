<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style2.css"/>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">
	<title>PRONET - Personal Information</title>
	<%@ page import="model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>
</head>
<body>
	<div class="main_container">
	<% 	DataBaseBridge db = new DataBaseBridge(); 
		Professional Prof = SiteFunctionality.acquireProfFromSession(db, request);
		db.close();
		if ( Prof == null ) {  %>
			<h2>INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { %>
			<nav class="navbar">
				<ul>
					<li><a href="/TEDProject/prof/NavigationServlet?page=HomePage">Home Page</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Network">Network</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=WorkAds">Work Ads</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Messages">Messages</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Notifications">Notifications</a></li>
					<li><a href="/TEDProject/ProfileLink">Personal Information</a></li>
					<li><a href="/TEDProject/prof/NavigationServlet?page=Settings">Settings</a></li>
					<li><form action="/TEDProject/LogoutServlet" method="post">
							<input type="submit" value="Logout" >
						</form>
					</li>
				</ul>
			</nav>
			<h1 class="my_h1"><%= Prof.getFirstName() %>  <%= Prof.getLastName() %></h1>
			<!-- insert image here with  style = "float: right" -->
			<form method=POST action="/TEDProject/ChangeServlet?attr=profile">		
				<h3>Employment Status: <input type="text" name="employmentStatus" value="<%= Prof.getEmploymentStatus() != null ? Prof.getEmploymentStatus() : "" %>"></h3>
				<h3>Employment Institution: <input type="text" name="employmentInstitution" value="<%= Prof.getEmploymentInstitution() != null ? Prof.getEmploymentInstitution() : ""%>"></h3>
				<h3>Description: <textarea name="description"><%= Prof.getDescription() != null ? Prof.getDescription() : ""%></textarea></h3>
				<p>
				   <span style="text-decoration: underline">Contact Info:</span><br> 
				   Email: <%= Prof.getEmail() %> <br>
				   Phone Number: <input type="text" name="phoneNumber" value="<%= Prof.getPhone() %>"> <br> 
				   <br>
				</p>
				<h2 class="my_h2">Professional Experience</h2>
				<input type="checkbox" name="profExpVisibility" <% if (Prof.getProfExpVisibility()) { %> checked <% } %>> Visible to non-connected professionals <br>
			    <textarea id="profExp" name="profExp"><%= Prof.getProfessionalExperience() != null ? Prof.getProfessionalExperience().replace("\\`", "`") : "" %></textarea><br>
				<h2 class="my_h2">Education Background</h2>
				<input type="checkbox" name="edBackgroundVisibility" <% if (Prof.getEdBackgroundVisibility()) { %> checked <% } %>> Visible to non-connected professionals <br>
			    <textarea id="edBackground" name="edBackground"><%= Prof.getEducationBackground() != null ? Prof.getEducationBackground().replace("\\`", "`") : "" %></textarea><br>
				<h2 class="my_h2">Skills</h2>
				<input type="checkbox" name="skillsVisibility" <% if (Prof.getSkillsVisibility()) { %> checked <% } %>> Visible to non-connected professionals <br>
			    <textarea id="skills" name="skills"><%= Prof.getSkills() != null ? Prof.getSkills().replace("\\`", "`") : "" %></textarea><br>
			    <div class="buttonContainer">
					<input type="submit" value="Save" class="changeButton">
					<a href="/TEDProject/ProfileLink" class="changeButton">Cancel</a>
				</div>
			</form>
		<% } %>
	</div>
	<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
	<script>
		var profExmSMDE = new SimpleMDE({ element: document.getElementById("profExp"), showIcons: ["code", "table"] });
		var edBackgroundSMDE = new SimpleMDE({ element: document.getElementById("edBackground"), showIcons: ["code", "table"] });
		var skillsSMDE = new SimpleMDE({ element: document.getElementById("skills"), showIcons: ["code", "table"] });
	</script>
</body>
</html>
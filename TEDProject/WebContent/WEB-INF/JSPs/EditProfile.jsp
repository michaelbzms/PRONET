<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<%@ page import="model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style2.css"/>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
	<style>
	
	.mylabel{
		display: inline-block;
		font-weight: 600;
		font-size: 110%;
		width: 220px;
		text-align: left;
		margin-bottom: 3px;
	}
	
	</style>
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
			<nav class="navbar navbar-expand-xl bg-light justify-content-center">
				<div class="container-fluid">
				    <div class="navbar-header">
				      <a class="navbar-brand" href="/TEDProject/prof/NavigationServlet?page=HomePage">PRONET</a>
				    </div>
					<ul class="navbar-nav"  role="navigation">
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=HomePage">Home Page</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Network">Network</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=WorkAds">Work Ads</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Messages">Messages</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Notifications">Notifications</a></li>
						<li class="nav-item active"><a id="active_page" class="nav-link" href="/TEDProject/ProfileLink">Personal Information</a></li>
						<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Settings">Settings</a></li>
						<li class="nav-item">
							<form class="form-inline" action="/TEDProject/LogoutServlet" method="post">
								<input class="btn btn-primary" type="submit" value="Logout" >
							</form>
						</li>
					</ul>
				</div>
			</nav>
			<h1 class="my_h1"><%= Prof.getFirstName() %>  <%= Prof.getLastName() %></h1>
			<!-- insert image here with  style = "float: right" -->
			<form method=POST action="/TEDProject/ChangeServlet?attr=profile">		
				<div id="edit_input" style="margin-left: 32%">
					<div class="mylabel">Employment Status:</div><input type="text" name="employmentStatus" value="<%= Prof.getEmploymentStatus() != null ? Prof.getEmploymentStatus() : "" %>"><br>
					<div class="mylabel">Employment Institution: </div><input type="text" name="employmentInstitution" value="<%= Prof.getEmploymentInstitution() != null ? Prof.getEmploymentInstitution() : ""%>"><br>
					<div class="mylabel">Description: </div><textarea name="description"><%= Prof.getDescription() != null ? Prof.getDescription() : ""%></textarea><br>
					<div class="mylabel">Email: </div><%= Prof.getEmail() %> <br>
					<div class="mylabel">Phone Number: </div><input type="text" name="phoneNumber" value="<%= Prof.getPhone() %>"><br> 
					<br>
				</div>
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
					<input type="submit" value="Save" class="btn btn-primary">
					<a href="/TEDProject/ProfileLink" class="btn btn-secondary">Cancel</a>
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
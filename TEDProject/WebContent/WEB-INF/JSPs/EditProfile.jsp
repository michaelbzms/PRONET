<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<%@ page import="model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/simplemde.min.css">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style2.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
	<script src="/TEDProject/Javascript/simplemde.min.js"></script>
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
			<jsp:include page="ProfNavBar.jsp"> 
				<jsp:param name="activePage" value="PersonalInformation"/> 
			</jsp:include>
			<h1 class="my_h1"><%= Prof.getFirstName() %>  <%= Prof.getLastName() %></h1>
			<br>
			<form method=POST action="/TEDProject/ChangeServlet?attr=profile" enctype="multipart/form-data">
				<!-- Change basic information  -->
				<div class="row mt-2">
					<div class="col-4 text-center m-auto">
						<img class="img-thumbnail profile_thumbnail" src="<%= Prof.getProfilePicURI() %>" alt="Profile picture">
					</div>
					<div id="edit_input" class="col-8">
						<div class="mylabel">Employment Status:</div><input type="text" name="employmentStatus" value="<%= Prof.getEmploymentStatus() != null ? Prof.getEmploymentStatus() : "" %>"><br>
						<div class="mylabel">Employment Institution: </div><input type="text" name="employmentInstitution" value="<%= Prof.getEmploymentInstitution() != null ? Prof.getEmploymentInstitution() : ""%>"><br>
						<div class="mylabel">Description: </div><textarea name="description"><%= Prof.getDescription() != null ? Prof.getDescription() : ""%></textarea><br>
						<div class="mylabel">Email: </div><%= Prof.getEmail() %> <br>
						<div class="mylabel">Phone Number: </div><input type="text" name="phoneNumber" value="<%= Prof.getPhone() %>"><br> 
						<!-- Change image -->
						<div class="mylabel">New Profile Picture: </div><input type="file" name="profile_picture" accept="image/*"><br>
						<br>
					</div>
				</div>
				<br>
				
				<!-- Markdown editor for text fields -->
				<h2 class="my_h2">Professional Experience</h2>
				<input type="checkbox" name="profExpVisibility" <% if (Prof.getProfExpVisibility()) { %> checked <% } %>> Visible to non-connected professionals <br>
			    <textarea id="profExp" name="profExp"><%= Prof.getProfessionalExperience() != null ? Prof.getProfessionalExperience() : "" %></textarea><br>
				<h2 class="my_h2">Education Background</h2>
				<input type="checkbox" name="edBackgroundVisibility" <% if (Prof.getEdBackgroundVisibility()) { %> checked <% } %>> Visible to non-connected professionals <br>
			    <textarea id="edBackground" name="edBackground"><%= Prof.getEducationBackground() != null ? Prof.getEducationBackground() : "" %></textarea><br>
				<h2 class="my_h2">Skills</h2>
				<input type="checkbox" name="skillsVisibility" <% if (Prof.getSkillsVisibility()) { %> checked <% } %>> Visible to non-connected professionals <br>
			    <textarea id="skills" name="skills"><%= Prof.getSkills() != null ? Prof.getSkills() : "" %></textarea><br>
			    <!-- Save/Cancel changes -->
			    <div class="buttonContainer text-center">
					<input type="submit" value="Save" class="btn btn-primary">
					<a href="/TEDProject/ProfileLink" class="btn btn-secondary">Cancel</a>
				</div>
			</form>
			<script>
				var profExpSMDE = new SimpleMDE({ element: document.getElementById("profExp"), showIcons: ["code", "table"] });
				var edBackgroundSMDE = new SimpleMDE({ element: document.getElementById("edBackground"), showIcons: ["code", "table"] });
				var skillsSMDE = new SimpleMDE({ element: document.getElementById("skills"), showIcons: ["code", "table"] });
			</script>
		<% } %>
		<jsp:include page="/footer.html"></jsp:include>
	</div>
</body>
</html>
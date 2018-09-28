<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Edit Personal Information</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/simplemde.min.css">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap-grid.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<!-- JS -->
	<script src="/TEDProject/js/lib/jquery-3.3.1.min.js"></script>
	<script src="/TEDProject/js/lib/bootstrap.min.js"></script>
	<script src="/TEDProject/js/lib/simplemde.min.js"></script>
</head>
<body>
	<div class="main_container">
	<% 	DataBaseBridge db = new DataBaseBridge(); 
		Professional Prof = SiteFunctionality.acquireProfFromSession(db, request);
		db.close();
		if ( Prof == null ) {  %>
			<h2 class="my_h2">INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { %>
			<jsp:include page="ProfNavBar.jsp"> 
				<jsp:param name="activePage" value="PersonalInformation"/> 
			</jsp:include>
			<h1 class="my_h1"><%= Prof.getFirstName() %>  <%= Prof.getLastName() %></h1>
			<br>
			<form method=POST action="/TEDProject/prof/ChangeServlet?attr=profile" enctype="multipart/form-data">
				<!-- Change basic information  -->
				<div class="row mt-2">
					<div class="col-4 text-center m-auto">
						<img id="profile_img" class="img-thumbnail profile_thumbnail" src="<%= Prof.getProfilePicURI() %>" onclick="$('#modal_profile_img').modal('show');" alt="Profile picture" data-toggle="tooltip" data-placement="top" title="Click to enlarge!">
					</div>
					<div id="edit_input" class="col-8">
						<label class="mylabel">Employment Status:</label>
						<input class="form-control" type="text" name="employmentStatus" value="<%= Prof.getEmploymentStatus() != null ? Prof.getEmploymentStatus() : "" %>"><br>
						<label class="mylabel">Employment Institution:</label>
						<input class="form-control" type="text" name="employmentInstitution" value="<%= Prof.getEmploymentInstitution() != null ? Prof.getEmploymentInstitution() : ""%>"><br>
						<label class="mylabel mb-2">Description:</label>
						<textarea class="form-control" name="description"><%= Prof.getDescription() != null ? Prof.getDescription() : ""%></textarea><br>
						<label class="mylabel mb-2 mr-2">Email:</label><%= Prof.getEmail() %> <br>
						<label class="mylabel">Phone Number:</label>
						<input class="form-control" type="text" name="phoneNumber" value="<%= Prof.getPhone() %>"><br> 
						<label class="mylabel">New Profile Picture:</label>
						<div class="custom-file">
						    <input id="article_file_input" class="custom-file-input" type="file" name="profile_picture" accept="image/*">
						    <label class="custom-file-label" for="inputGroupFile01"><i>Choose file</i></label>
						</div>
						<small class="image-recommendation-text text-secondary">We recommend uploading a square picture, at least 200x200 px in size</small>
						<br>
					</div>
				</div>
				<br>
				<!-- Markdown editor for text fields -->
				<h2 class="my_h2">Professional Experience</h2>
				<p>&ensp;<input type="checkbox" name="profExpVisibility" <% if (Prof.getProfExpVisibility()) { %> checked <% } %>> Visible to non-connected professionals</p>
			    <textarea id="profExp" name="profExp"><%= Prof.getProfessionalExperience() != null ? Prof.getProfessionalExperience() : "" %></textarea><br>
				<h2 class="my_h2">Education Background</h2>
				<p>&ensp;<input type="checkbox" name="edBackgroundVisibility" <% if (Prof.getEdBackgroundVisibility()) { %> checked <% } %>> Visible to non-connected professionals</p>
			    <textarea id="edBackground" name="edBackground"><%= Prof.getEducationBackground() != null ? Prof.getEducationBackground() : "" %></textarea><br>
				<h2 class="my_h2">Skills</h2>
				<p>&ensp;<input type="checkbox" name="skillsVisibility" <% if (Prof.getSkillsVisibility()) { %> checked <% } %>> Visible to non-connected professionals</p>
			    <textarea id="skills" name="skills"><%= Prof.getSkills() != null ? Prof.getSkills() : "" %></textarea><br>
			    <!-- Save/Cancel changes -->
			    <div class="buttonContainer">
					<input type="submit" value="Save" class="btn btn-primary">
					<a href="/TEDProject/ProfileLink" class="btn btn-secondary">Cancel</a>
				</div>
			</form>
			<div class="modal fade" id="modal_profile_img" tabindex="-1" role="dialog" aria-hidden="true" style="padding-left: 16px !important;">
				<div class="modal-dialog image_modal_dialog" role="document">
			    	<div class="modal-content image_modal_content">
				        <div class="modal-header">
					       <h5 class="modal-title"><%= Prof.getFirstName() %> <%= Prof.getLastName() %></h5>
					       <button type="button" class="close" data-dismiss="modal" aria-label="Close">
					       		<span aria-hidden="true">&times;</span>
					       </button>
					    </div>
					    <div class="modal-body text-center">
					        <img id="modal_img" class="modal_img" src="<%= Prof.getProfilePicURI() %>">
					    </div>
			    	</div>
			    </div>
			</div>
			<script>
				var profExpSMDE = new SimpleMDE({ element: document.getElementById("profExp"), showIcons: ["code", "table"] });
				var edBackgroundSMDE = new SimpleMDE({ element: document.getElementById("edBackground"), showIcons: ["code", "table"] });
				var skillsSMDE = new SimpleMDE({ element: document.getElementById("skills"), showIcons: ["code", "table"] });
			</script>
			<script id="fileInputUpdateLabelScript" src="/TEDProject/js/fileInputUpdateLabelScript.js" data-emptyText="<i>No file chosen</i>"></script>
		<% } %>
		<jsp:include page="/footer.html"></jsp:include>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Home Page</title>
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/grid-box.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/article.css"/>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">	
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
	<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
</head>
<body>
	<% 	DataBaseBridge db = new DataBaseBridge();
		Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
		if ( !db.checkIfConnected() ) { %>
			<h2>DATABASE ERROR</h2>	
			<p>It appears that our database is down. Please contact the site's administrators.</p>
	<%	} else if ( prof == null ) {  %>
			<h2>INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { %>
			<div class="main_container">
				<jsp:include page="ProfNavBar.jsp"> 
					<jsp:param name="activePage" value="HomePage"/> 
				</jsp:include>
				<div class="row">
					<div class="col-3">
						<div class="side_tab">
							<h4 class="neon_header"><%= prof.getFirstName() %>  <%= prof.getLastName() %></h4>
							<img class="img-thumbnail" style="height: 120px; width: 120px; margin: 5px 0px 5px 0px" src="<%= prof.getProfilePicURI() %>" alt="Profile picture"><br>
							<p>
								<% if ( prof.getEmploymentStatus() != null ) { %>
									<%= prof.getEmploymentStatus() %><br> 
								<% } %>
								<% if ( prof.getEmploymentInstitution() != null ) { %>
									<%= prof.getEmploymentInstitution() %><br> 
								<% } %>
								<% if ( prof.getDescription() != null ) { %>
									<%= prof.getDescription() %><br> 
								<% } %>
							</p>
							<a href="/TEDProject/ProfileLink">View details</a>
						</div>
						<div class="side_tab" style="max-height: 42vh; overflow-y: auto">
							<h4 class="neon_header">My Connections</h4>
							<ul class="grid_container_mini">
							<% List<Professional> Connections = db.getConnectedProfessionalsFor(prof.getID());
							   if ( Connections != null ) { 
								   for (Professional p : Connections) { %>
										<li class="grid_item_mini">
											<img style="height: 72px; width: 72px" class="img-thumbnail" src="<%= p.getProfilePicURI() %>" alt="Profile picture"><br>
											<a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>"><%= p.getFirstName() %><br><%= p.getLastName() %></a>		
										</li>
								<%	} %>
							<% } %>
							</ul>
						</div>
					</div>
					<div class="col-9">
						<div class="article_input">
							<form id="article_input_form" method="post" enctype="multipart/form-data">
						   		<textarea id="article_input_editor" name="text" autofocus></textarea>
						   		<input id="article_file_input" type="file" name="file_input" accept="/*" multiple
						   		       style="display: inline-block; cursor: pointer; margin-bottom: 15px">
						   		<div class="buttonContainer text-right">
						   			<input type="submit" value="Submit" class="btn btn-primary">
							   	</div>
							   	<script>
							   		$("#article_input_form").on("submit", function(e){
							   			e.preventDefault();
							   			
							   			// get form's data ---> PROBLEM: doesnt work for text!!!
							   			var formData = new FormData($(this)[0]);							   			
							   			for (var pair of formData.entries()) {
							   			    console.log(pair[0]+ ', ' + pair[1]); 
							   			}
							   			
							   			// send them via AJAX to AJAXServlet		   			
							   			$.ajax({
				    						url: "/TEDProject/AJAXServlet?action=addArticle",
				    						enctype: 'multipart/form-data',
				    						type: "post",
				    						data: formData,
				    						success: function(response){
				    							console.log("Made a post successfully");
				    							// reset form's fields
				    							$("#article_input_editor").val("");
				    							$("#article_file_input").val("");
				    						},
				    						cache: false,
				    				        contentType: false,
				    				        processData: false
				    					});
							   			
							   		});
							   	</script>
						   	</form>
						</div>
						<div>
							<!-- JSP include articles order by time uploaded + infinite scroll -->
							<!-- For testing purposes: -->
							<jsp:include page="Article.jsp"> 
								<jsp:param name="ArticleID" value="1" /> 
							</jsp:include>
							<jsp:include page="Article.jsp"> 
								<jsp:param name="ArticleID" value="2" /> 
							</jsp:include>
							<jsp:include page="Article.jsp"> 
								<jsp:param name="ArticleID" value="1" /> 
							</jsp:include>
							<jsp:include page="Article.jsp"> 
								<jsp:param name="ArticleID" value="2" /> 
							</jsp:include>
						</div>
					</div>	
				</div>
				<jsp:include page="/footer.html"></jsp:include>
			</div>
			<script>
				var post = new SimpleMDE({ element: document.getElementById("article_input_editor"), showIcons: ["code", "table"] });
			</script>
			<script>
				var comm_buttons = document.getElementsByClassName("comment_button");
				var i;
				for (i = 0; i < comm_buttons.length; i++) {
					comm_buttons[i].addEventListener("click", function() {
						comm_form = this.nextElementSibling.firstElementChild;
					    if (comm_form.style.height !== "0px") {
					    	comm_form.style.height = 0;
					    } else {
					    	comm_form.style.height = "auto";
					    }
				  	});
				}
			</script>
	<%	}
		db.close(); %>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Home Page</title>
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality, model.Article" %>
	<!-- CSS -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">	
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/grid-box.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/article.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
	<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
	<style>
		#article_input_editor{
			width: 100%;
			height: 10vh;
			resize: none;
			margin: 5px 0px 10px 0px;
			box-shadow: 0px 0px 3px 0px #6397ff;
		}
	</style>
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
						<div class="side_tab" style="max-height: 800px; overflow-y: auto">
							<h4 class="neon_header">My Connections</h4>
							<div class="grid_container_mini justify-content-left">
							<% List<Professional> Connections = db.getConnectedProfessionalsFor(prof.getID());
							   if ( Connections != null ) { 
								   for (Professional p : Connections) { %>
									    <a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">
											<div class="grid_item_mini text-dark">
												<img style="height: 72px; width: 72px" class="img-thumbnail" src="<%= p.getProfilePicURI() %>" alt="Profile picture"><br>
												<%= p.getFirstName() %><br><%= p.getLastName() %>		
											</div>
										</a>
								<%	} %>
							<% } %>
							</div>
						</div>
					</div>
					<div class="col-9">
						<div class="article_input">
							<form id="article_input_form" method="post" enctype="multipart/form-data">
						   		<textarea id="article_input_editor" name="text" autofocus></textarea><br>
							   	<input id="article_file_input" type="file" name="file_input" accept="/*" multiple
							   		   style="display: inline-block; cursor: pointer; float: left">
						   		<div class="buttonContainer text-right">
						   			<input type="submit" value="Post" class="btn btn-primary">
							   	</div>
							   	<script>
							   		$("#article_input_form").on("submit", function(e){
							   			e.preventDefault();
							   			
							   			// get form's data
							   			var formData = new FormData($(this)[0]);							   			
							   			// DEBUG: Print formData to console
							   			//for (var pair of formData.entries()) {
							   			//    console.log(pair[0]+ ', ' + pair[1]); 
							   			//}
							   			
							   			// send them via AJAX to AJAXServlet		   			
							   			$.ajax({
				    						url: "/TEDProject/AJAXServlet?action=addArticle",
				    						enctype: 'multipart/form-data',
				    						type: "post",
				    						data: formData,
				    						success: function(response){
				    							var articleID = parseInt(response);
				    							if ( !isNaN(articleID) ){        // if response is a number then it is the ID of the article we just posted successfully
				    								console.log("Made a post successfully");
				    								// reset form's fields
				    								$("#article_input_editor").val("");
				    								$("#article_file_input").val("");
				    								// get article just posted with ajax and prepend it to our wall
				    								$.ajax({
							    						url: "/TEDProject/AJAXServlet?action=loadArticle",
							    						type: "post",
							    						async: false,           // make these calls synchronous!
							    						data: { ArticleID : articleID },
							    						success: function(response){
							    							$("#wall").prepend(response);
							    						}
													});
				    							} else {
				    								window.alert(response);
				    							}
				    						},
				    						cache: false,
				    				        contentType: false,
				    				        processData: false       // (!) important
				    					});
							   			
							   		});
							   	</script>
						   	</form>
						</div>
						<div id="wall">
							<!-- JSP include articles order by time uploaded + infinite scroll -->
							<%	int InitialCount = 3;       // CONFIG number of articles loaded immediatelly when loading the page (more can be loaded through AJAX)
								int[] articleIDs = db.getWallArticlesIDsFor(prof.getID()); 
								if (articleIDs != null) {
									for (int i = 0 ; i < InitialCount && i < articleIDs.length ; i++) {  %>
										<jsp:include page="Article.jsp"> 
											<jsp:param name="ArticleID" value="<%= articleIDs[i] %>" /> 
										</jsp:include>							
							<% 		} 
								} %>
						</div>
						<script>
							// Client-side variables:
							var nextArticleIDindex = <% if (articleIDs!= null && articleIDs.length > InitialCount ) { %> <%= InitialCount %> <% } else { %> -1 <% } %>;
							var ArticleIDs = [];
							<% for (int i = 0 ; i < articleIDs.length ; i++ ) { %>   // "transfer" Java article IDs table to client side (aka in javascript)
								ArticleIDs[<%= i %>] = <%= articleIDs[i] %>;
							<% } %>
							var padding = 1;        // should be >= 1 to avoid decimal errors. Could be more if we want to load new articles earlier than the exact bottom-scroll but not too high
							
							// if scrolled to bottom and we can show more articles then do so with AJAX
							$(window).scroll(function(){
								// DEBUG: console.log("scrollTop + height = " + ( $("#wall").scrollTop() +  $("#wall").height()) + ", scrollHeight = " + $("#wall")[0].scrollHeight );
								
								if ( nextArticleIDindex !== -1 && nextArticleIDindex < <%= articleIDs.length %> && ( $(window).scrollTop() + $(window).height() + padding >= document.documentElement.scrollHeight ) ){   // plus one to avoid decimal errors
									// DEBUG: console.log("nextArticleIDindex = " + nextArticleIDindex + ", ArticleIDs[nextArticleIDindex] = " + ArticleIDs[nextArticleIDindex]);
									
									$.ajax({
			    						url: "/TEDProject/AJAXServlet?action=loadArticle",
			    						type: "post",
			    						data: { ArticleID : ArticleIDs[nextArticleIDindex] },
			    						success: function(response){
			    							$("#wall").append(response);
			    						}
									});
									
									nextArticleIDindex++;    // this update MUST be done synchronoysly (and NOT on AJAX callback)
								}
							});
							
							// if we load less articles than enough to cause overflow due to small "InitialCount" value then load more as a client until overflow happens
							$("#wall").ready(function(){
								while ( nextArticleIDindex !== -1 && nextArticleIDindex < <%= articleIDs.length %> && $("body").height() <= $(window).height() ){   // while no overflow has happened to window after loading "InitialCount" articles then load more right now (immediatelly after loading the page)
									$.ajax({
			    						url: "/TEDProject/AJAXServlet?action=loadArticle",
			    						type: "post",
			    						async: false,           // make these calls synchronous!
			    						data: { ArticleID : ArticleIDs[nextArticleIDindex] },
			    						success: function(response){
			    							$("#wall").append(response);
			    						}
									});
									nextArticleIDindex++;
								}
								$(window).scrollTop(0);     // scroll back to top
							});
							
						</script>
					</div>	
				</div>
				<jsp:include page="/footer.html"></jsp:include>
			</div>
			<script>
				// Markdown inteferes with AJAX form! 
				// var post = new SimpleMDE({ element: document.getElementById("article_input_editor"), showIcons: ["code", "table"] });
			</script>
			<script id="deleteArticleScript" src="/TEDProject/Javascript/deleteArticleScript.js" data-profID="<%= prof.getID() %>"></script>
			<script id="toggleInterestScript" src="/TEDProject/Javascript/toggleInterest.js" data-profID="<%= prof.getID() %>"></script>
			<script src="/TEDProject/Javascript/openCommentForm.js"></script>
			<script id="submitCommentScript" src="/TEDProject/Javascript/submitComment.js" data-profID="<%= prof.getID() %>" data-profProfilePicURI="<%= prof.getProfilePicURI() %>"
					data-profFullName="<%= prof.getFirstName() %> <%= prof.getLastName() %>"></script>
		   	<script src="/TEDProject/Javascript/commentSendOnEnter.js"></script>
	<%	}
		db.close(); %>
</body>
</html>
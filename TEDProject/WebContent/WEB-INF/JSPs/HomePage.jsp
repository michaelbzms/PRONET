<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Home Page</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality, model.Article, model.KNNArticles" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/simplemde.min.css">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/grid-box.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/article.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
	<script src="/TEDProject/Javascript/simplemde.min.js"></script>
</head>
<body>
	<% 	DataBaseBridge db = new DataBaseBridge();
		Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
		if ( !db.checkIfConnected() ) { %>
			<h2 class="my_h2">DATABASE ERROR</h2>	
			<p>It appears that our database is down. Please contact the site's administrators.</p>
	<%	} else if ( prof == null ) {  %>
			<h2 class="my_h2">INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { %>
			<div class="main_container">
				<jsp:include page="ProfNavBar.jsp"> 
					<jsp:param name="activePage" value="HomePage"/> 
				</jsp:include>
				<% /* KNNArticles: */
					int MAXIMUM_ARTICLES_SHOWN = 1000;    // CONFIG: This limits articles fetched from database to the 'MAXIMUM_ADS_SHOWN' most recent relevant articles (assuming that the user would never scroll more than that number of articles)
					int[] articleIDs = db.getWallArticlesIDsFor(prof.getID(), MAXIMUM_ARTICLES_SHOWN);
					int[] topProfIDs = null;
					if (articleIDs != null) {
						int K = 6;                                  // CONFIG KNN's K parameter (bigger K means slower 'reorderArticleIDs')
						boolean giveRecentArticlesBonus = true;     // CONFIG
						boolean giveNotLikedArticlesBonus = true;   // CONFIG
						boolean givePopularArticlesBonus = true;    // CONFIG
						// Use KNN to reorder ArticleIDs
						KNNArticles KNN = new KNNArticles(K);
						int result = KNN.fit(db, articleIDs, prof.getID());
						if ( result == 0 ){    // success
							KNN.reorderArticleIDs(db, prof.getID(), giveRecentArticlesBonus, giveNotLikedArticlesBonus, givePopularArticlesBonus);
						} else if ( result < 0 ) { System.err.println("KNNArticles fit failed"); }
						// else if result == 1 then we did not need to run KNN (|connected profs| <= 1)
						// get top KNNs ids to show in "My Connections" tab
						int maxNumber = 6;         // maximum number of the top KNNs that should be shown in "My Connections" side tab -> 6 since at most we can fit a 2x3 grid at xl resolutuon (not responsive we know)
						if ( result == 0 ){        // only if reordering happends -> we have found the KNNs
							topProfIDs = KNN.getTopKProfessionals(maxNumber);
						}
					}
				%>
				<div class="row">
					<div class="col-3">
						<div class="sticky-top sticky_side_container">
							<div class="side_tab">
								<h4 class="neon_header"><%= prof.getFirstName() %>  <%= prof.getLastName() %></h4>
								<img class="img-thumbnail side_tab_thumbnail" src="<%= prof.getProfilePicURI() %>" alt="Profile picture"><br>
								<p>
									<% if ( prof.getEmploymentStatus() != null && !prof.getEmploymentStatus().equals("") ) { %>
										<%= prof.getEmploymentStatus() %><br> 
									<% } %>
									<% if ( prof.getEmploymentInstitution() != null && !prof.getEmploymentInstitution().equals("") ) { %>
										<%= prof.getEmploymentInstitution() %><br> 
									<% } %>
								</p>
								<% if ( prof.getDescription() != null && !prof.getDescription().equals("") ) { %>
									<p class="side_description"><%= prof.getDescription() %></p> 
								<% } %>
								<a href="/TEDProject/ProfileLink">View details</a>
							</div>
							<div class="side_tab">
								<h4 class="neon_header">My Connections</h4>
								<div class="grid_container_mini justify-content-left">
								<% if ( topProfIDs != null ) { 
									   for (int i = 0 ; i < topProfIDs.length ; i++) { 
									   		Professional p = db.getBasicProfessionalInfo(topProfIDs[i]); 
									   		if ( p != null ) { %>
											    <a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">
													<div class="grid_item_mini text-dark">
														<img class="img-thumbnail side_tab_connection_thumbnail" src="<%= p.getProfilePicURI() %>" alt="Profile picture"><br>
														<%= p.getFirstName() %><br><%= p.getLastName() %>		
													</div>
												</a>
									<%		} 
									   	}
										if ( topProfIDs.length == 0 ) { %>
											<p><i>You have not got any connections.</i></p>
									<%  } 
									} else { // either 0 connections or an error from KNN %>
										<p><i>You have not got any connections.</i></p>
								<% 	} %>
								</div>
							<% 	if ( topProfIDs != null && topProfIDs.length > 0 ) { %>
									<a href="/TEDProject/prof/NavigationServlet?page=Network#connections">View all connections</a>
							<% 	} %>
							</div>							
							<div class="buttonContainer mt-0">
					   			<button type="button" class="btn btn-primary" onclick="scrollToTop();">Back to top</button>
						   	</div>
						</div>
					</div>
					<div class="col-9">
						<div id="articleInputContainer" class="article_input">
							<form id="article_input_form" method="post" enctype="multipart/form-data">
						   		<textarea id="article_input_editor" name="text"></textarea>
						   		<div class="d-flex justify-content-between mt-1">
								   	<div class="custom-file d-inline-block">
									    <input id="article_file_input" class="custom-file-input" type="file" name="file_input" accept="/*" multiple="">
									    <label class="custom-file-label" for="inputGroupFile01"><i>Choose file(s) to upload</i></label>
									</div>
							   		<div class="d-inline-block text-right mr-1">
							   			<input type="submit" value="Post" class="btn btn-primary pl-3 pr-3">
								   	</div>
							   	</div>
						   	</form>
							<script>
								var articleEditor = new SimpleMDE({ element: document.getElementById("article_input_editor"), showIcons: ["code", "table"] });
								articleEditor.value("");
							</script>
						</div>
						<div id="wall">
							<%	int InitialCount = 5;                 // CONFIG number of articles loaded immediatelly when loading the page (more can be loaded through AJAX)
								if (articleIDs != null) {
									for (int i = 0 ; i < InitialCount && i < articleIDs.length ; i++) {  %>
										<jsp:include page="Article.jsp"> 
											<jsp:param name="ArticleID" value="<%= articleIDs[i] %>" /> 
										</jsp:include>							
							<% 		} 
								} %>
						</div>
						<script> <!-- Infinite scrolling script -->
							// Client-side variables:
							var nextArticleIDindex = <% if (articleIDs!= null && articleIDs.length > InitialCount ) { %> <%= InitialCount %> <% } else { %> -1 <% } %>;
							var ArticleIDs = [];
							<% for (int i = 0 ; i < articleIDs.length ; i++ ) { %>   // "transfer" Java article IDs table to client side (aka in javascript)
								ArticleIDs[<%= i %>] = <%= articleIDs[i] %>;
							<% } %>
							var padding = 1;        // should be >= 1 to avoid decimal errors. Could be more if we want to load new articles earlier than the exact bottom-scroll but not too high
							
							// if scrolled to bottom and we can show more articles then do so with AJAX
							$(window).scroll(function(){
								if ( nextArticleIDindex !== -1 && nextArticleIDindex < <%= articleIDs.length %> && ( $(window).scrollTop() + $(window).height() + padding >= document.documentElement.scrollHeight ) ){   // plus one to avoid decimal errors
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
							
							// (rare) if we load less articles than enough to cause overflow due to small "InitialCount" value then load more as a client until overflow happens
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
   			<script src="/TEDProject/Javascript/util.js"></script>
   			<script src="/TEDProject/Javascript/responsiveInputHeight.js"></script>
			<script id="fileInputUpdateLabelScript" src="/TEDProject/Javascript/fileInputUpdateLabelScript.js" data-emptyText="<i>No files chosen</i>"></script>
   			<script src="/TEDProject/Javascript/submitArticle.js"></script>
			<script id="deleteArticleScript" src="/TEDProject/Javascript/deleteArticleScript.js" data-profID="<%= prof.getID() %>" data-redirect="false"></script>
			<script id="toggleInterestScript" src="/TEDProject/Javascript/toggleInterest.js" data-profID="<%= prof.getID() %>"></script>
			<script src="/TEDProject/Javascript/openCommentForm.js"></script>
			<script id="submitCommentScript" src="/TEDProject/Javascript/submitComment.js" data-profID="<%= prof.getID() %>" data-profProfilePicURI="<%= prof.getProfilePicURI() %>"
					data-profFullName="<%= prof.getFirstName() %> <%= prof.getLastName() %>"></script>
		   	<script src="/TEDProject/Javascript/commentSendOnEnter.js"></script>
   			<script id="deleteCommentScript" src="/TEDProject/Javascript/deleteCommentScript.js" data-profID="<%= prof.getID() %>"></script>
   			<script src="/TEDProject/Javascript/imageModalScript.js"></script>
	<%	}
		db.close(); %>
</body>
</html>
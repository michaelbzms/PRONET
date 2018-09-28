<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Home Page</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="model.Professional, model.DataBaseBridge, model.MyUtil" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/simplemde.min.css">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap-grid.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/article.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/articlepage.css"/>
	<!-- JS -->
	<script src="/TEDProject/js/lib/jquery-3.3.1.min.js"></script>
	<script src="/TEDProject/js/lib/bootstrap.min.js"></script>
	<script src="/TEDProject/js/lib/simplemde.min.js"></script>
</head>
<body>
	<div class="main_container">
	<%  HttpSession currentSession = request.getSession(false);
	    int profID;
	    if (request.getSession(false) != null && currentSession.getAttribute("ProfID") != null) {
	   		profID = (int) currentSession.getAttribute("ProfID");
	    } else { %>
		 	<h2 class="my_h2">SESSION ENDED</h2>
		 	<p>Your session has ended. Please login again.</p>
		 <% return;
	    }
	    DataBaseBridge db = new DataBaseBridge();
		Professional prof = db.getBasicProfessionalInfo(profID);
		if ( !db.checkIfConnected() ) { %>
			<h2 class="my_h2">DATABASE ERROR</h2>	
			<p>It appears that our database is down. Please contact the site's administrators.</p>
	<%	} else if ( prof == null ) {  %>
			<h2 class="my_h2">INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { 
		   Professional authorProf = db.getBasicProfessionalInfo(profID);
		   boolean isAdmin = ( currentSession.getAttribute("isAdmin") != null && ((boolean) currentSession.getAttribute("isAdmin")) );
		   // Navbar only for professionals
		   if (currentSession != null && !isAdmin) { %>
				<jsp:include page="ProfNavBar.jsp"> 
					<jsp:param name="activePage" value="null"/> 
				</jsp:include>
		 <% } 
		 
		   int articleID = -1;
		   if (request.getAttribute("ArticleID") != null) {
			   articleID = Integer.parseInt(request.getAttribute("ArticleID").toString());
		   } else { %>
			   <h2 class="my_h2">INVALID ARTICLE</h2>
			   <p>The requested Article does not exist.</p>
			 <% return;
		   } %>		   
		    <!-- Alerts -->
		    <div id="editSuccessAlert" class="alert alert-success alert-dismissible" role="alert" style="display:none;">
				Your article was updated successfully.
				<button type="button" class="close" data-dismiss="alert" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<script src="/TEDProject/js/alerts/articleAlerts.js"></script>
			<!-- Article -->
			<jsp:include page="Article.jsp"> 
				<jsp:param name="ArticleID" value="<%= articleID %>" /> 
			</jsp:include>
			<jsp:include page="/footer.html"></jsp:include>
			<!-- js scripts -->
   			<script src="/TEDProject/js/util.js"></script>
			<script id="deleteArticleScript" src="/TEDProject/js/deleteArticleScript.js" data-profID="<%= prof.getID() %>" data-redirect="true"></script>
			<script id="toggleInterestScript" src="/TEDProject/js/toggleInterest.js" data-profID="<%= prof.getID() %>"></script>
			<script src="/TEDProject/js/openCommentForm.js"></script>
			<script id="submitCommentScript" src="/TEDProject/js/submitComment.js" data-profID="<%= prof.getID() %>" data-profProfilePicURI="<%= prof.getProfilePicURI() %>"
					data-profFullName="<%= prof.getFirstName() %> <%= prof.getLastName() %>"></script>
		   	<script src="/TEDProject/js/commentSendOnEnter.js"></script>
   			<script id="deleteCommentScript" src="/TEDProject/js/deleteCommentScript.js" data-profID="<%= prof.getID() %>"></script>
   			<script src="/TEDProject/js/imageModalScript.js"></script>
	<%  }
	    db.close(); %>
	</div>
</body>
</html>
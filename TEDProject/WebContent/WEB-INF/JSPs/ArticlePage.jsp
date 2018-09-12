<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<%@ page import="model.Professional, model.DataBaseBridge, model.MyUtil" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style2.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/article.css"/>
	<style>
		.past_comments_container {		/* override max-height declared in article.css */
		    max-height: 2000px;
		}
	</style>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
	<div class="main_container">
	<%  HttpSession currentSession = request.getSession(false);
	    int profID;
	    if (request.getSession(false) != null && currentSession.getAttribute("ProfID") != null) {
	   		profID = (int) currentSession.getAttribute("ProfID");
	    } else {	%>
		 	<h2 class="my_h2">SESSION ENDED</h2>
		 	<p>Your session has ended. Please login again.</p>
		 <% return;
	    }
	    DataBaseBridge db = new DataBaseBridge();
		Professional prof = db.getBasicProfessionalInfo(profID);
		if ( !db.checkIfConnected() ) { %>
			<h2>DATABASE ERROR</h2>	
			<p>It appears that our database is down. Please contact the site's administrators.</p>
	<%	} else if ( prof == null ) {  %>
			<h2>INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { 
		   Professional authorProf = db.getBasicProfessionalInfo(profID);
		   int articleID = Integer.parseInt(request.getAttribute("ArticleID").toString());		// TODO: checks

		   boolean isAdmin = ( currentSession.getAttribute("isAdmin") != null && ((boolean) currentSession.getAttribute("isAdmin")) );
		   // Navbar only for professionals
		   if (currentSession != null && !isAdmin) { %>
				<jsp:include page="ProfNavBar.jsp"> 
					<jsp:param name="activePage" value="PersonalInformation"/> 
				</jsp:include>
		 <% } %>
		   
			<jsp:include page="Article.jsp"> 
				<jsp:param name="ArticleID" value="<%= articleID %>" /> 
			</jsp:include>
			<jsp:include page="/footer.html"></jsp:include>
			<script id="deleteArticleScript" src="/TEDProject/Javascript/deleteArticleScript.js" data-profID="<%= prof.getID() %>"></script>
			<script id="toggleInterestScript" src="/TEDProject/Javascript/toggleInterest.js" data-profID="<%= prof.getID() %>"></script>
			<script src="/TEDProject/Javascript/openCommentForm.js"></script>
			<script id="submitCommentScript" src="/TEDProject/Javascript/submitComment.js" data-profID="<%= prof.getID() %>" data-profProfilePicURI="<%= prof.getProfilePicURI() %>"
					data-profFullName="<%= prof.getFirstName() %> <%= prof.getLastName() %>"></script>
		   	<script src="/TEDProject/Javascript/commentSendOnEnter.js"></script>
	<%  }
	    db.close(); %>
	</div>
</body>
</html>
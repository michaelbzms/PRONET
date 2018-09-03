<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<%@ page import="model.Professional, model.DataBaseBridge, model.MyUtil" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style2.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/article.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
	<div class="main_container">
		<% HttpSession currentSession = request.getSession(false);
		   int profID;
		   if (request.getSession(false) != null && currentSession.getAttribute("ProfID") != null) {
		   		profID = (int) currentSession.getAttribute("ProfID");
		   } else {	%>
				<h2 class="my_h2">SESSION ENDED</h2>
				<p>Your session has ended. Please login again.</p>
			<% 	return;
		   }
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
	</div>
</body>
</html>
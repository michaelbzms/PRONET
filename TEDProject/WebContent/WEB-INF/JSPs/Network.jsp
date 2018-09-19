<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Network</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/grid-box.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
<%	DataBaseBridge db = new DataBaseBridge(); 
	Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
	if ( prof == null ) {  %>
		<h2>INTERNAL ERROR</h2>	
		<p>Could not retrieve your info from our data base. How did you login?</p>
<% 	} else { %>
		<div class="main_container">
			<jsp:include page="ProfNavBar.jsp"> 
				<jsp:param name="activePage" value="Network"/> 
			</jsp:include>
			<div class="search_bar">
				<h2>Search for professionals</h2>
				<form id="search_form">
					<label>Find: </label>
					<input type="text" name="searchString" id="searchString">
					<input type="submit" value="search">
					<script>
						$("#search_form").on("submit", function(e){
							e.preventDefault();
							$.ajax({
								url: "/TEDProject/AJAXServlet?action=searchProfessional",
								type: "post",
								data: { searchString : $("#searchString").val() },
								success: function(response){
									$("#search_results").html(response);      // print results to search_results div
								}
							});
						});
					</script>
				</form>
				<div id="search_results">
				</div>
			</div>
			<br>
			<div class="connections_bar">
				<h2>Connections</h2>
				<div class="grid_container_container">
					<ul id="connections_grid" class="grid_container">
					<% List<Professional> Connections = db.getConnectedProfessionalsFor(prof.getID());
					   if ( Connections != null ) { 
						   for (Professional p : Connections) { %>
								<li class="grid_item">
									<img class="img-thumbnail" src="<%= p.getProfilePicURI() %>" alt="Profile picture"><br>
									<b><%= p.getFirstName() %> <%= p.getLastName() %></b><br>
									<% if (p.getEmploymentStatus() != null) { %> 
										<%= p.getEmploymentStatus() %> 
									<% } else { %> 
										N/A  
									<% } %>
									<br> 
									<% if (p.getEmploymentInstitution() != null) { %> <%= p.getEmploymentInstitution() %> 
									<% } else { %> 
										N/A
									<% } %>
									<br>
									<a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">View details</a>					
								</li>
						<%	} %>
					<% } %>
					</ul>
				</div>
			</div>
			<jsp:include page="/footer.html"></jsp:include>
		</div>
<%	} 
	db.close(); %>  
</body>
</html>
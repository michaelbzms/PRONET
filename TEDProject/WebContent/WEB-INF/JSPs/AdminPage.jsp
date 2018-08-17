<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<title>PRONET - Administration Page</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
	<style>
		
		#list_of_professionals ul{
			list-style-type: none;
			display: inline-grid;
		}
		
		#list_of_professionals ul li{
			text-align: center;
			display: inline-block;
			margin: 5px;
		    background-color: #e2ecff;
			border-style: solid;
		    border-color: black;
		   	border-width: 2px;
		    padding: 5px;
		}
		
	</style>
</head>
<body>
	<div class="main_container">
		<h2>Registered Professionals:</h2><br>
		<div id="list_of_professionals">
			<% DataBaseBridge db = new DataBaseBridge(); %>
			<% Professional[] professionals = db.getAllProfessionals(); %>
			<ul> 
			<% for (int i = 0 ; i < professionals.length ; i++ ){ %>
				<li>
					<!-- profile picture here as an <img> element  -->
					<%= professionals[i].getFirstName() %> <%= professionals[i].getLastName() %><br>
					<% if ( professionals[i].getEmploymentStatus() != null ) { %>
						<%= professionals[i].getEmploymentStatus() %> <br>
					<% } %>
					<!-- The following is a servlet URI which will forward the HTTP GET request to ProfPublicProfilePage.jsp with the correct ID  -->
					<a href="/TEDProject/ProfileLink?ID=<%= professionals[i].getID() %>">View Details</a>
				</li>
			<% } %>
			</ul>
			<% db.close(); %>
		</div>
	</div>
</body>
</html>
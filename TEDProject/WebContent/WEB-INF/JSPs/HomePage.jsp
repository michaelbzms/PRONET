<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Home Page</title>
	<%@ page import="model.Professional, model.DataBaseBridge" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
</head>
<body>
	<div class="main_container">
		<jsp:include page="ProfNavBar.jsp"> 
			<jsp:param name="activePage" value="HomePage"/> 
		</jsp:include>
		<p>HERE<br> BE<br> STARTING<br> PAGE<br></p>
		<jsp:include page="/footer.html"></jsp:include>
	</div>
</body>
</html>
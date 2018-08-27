<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Messages</title>
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/messanger.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
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
				<nav class="navbar navbar-expand-xl bg-light justify-content-center">
					<div class="container-fluid">
					    <div class="navbar-header">
					      <a class="navbar-brand" href="/TEDProject/prof/NavigationServlet?page=HomePage">PRONET</a>
					    </div>
						<ul class="navbar-nav"  role="navigation">
							<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=HomePage">Home Page</a></li>
							<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Network">Network</a></li>
							<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=WorkAds">Work Ads</a></li>
							<li class="nav-item active"><a id="active_page"  class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Messages">Messages</a></li>
							<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Notifications">Notifications</a></li>
							<li class="nav-item"><a class="nav-link" href="/TEDProject/ProfileLink">Personal Information</a></li>
							<li class="nav-item"><a class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Settings">Settings</a></li>
							<li class="nav-item">
								<form class="form-inline" action="/TEDProject/LogoutServlet" method="post">
									<input class="btn btn-primary" type="submit" value="Logout" >
								</form>
							</li>
						</ul>
					</div>
				</nav>
				<h2>Here be messages for <%= prof.getFirstName() %>  <%= prof.getLastName() %>!</h2>
			<%  String chatWith = request.getParameter("chatWith"); 
			    Professional chatWithProf = null;
				if (chatWith != null) {
					chatWithProf = db.getProfessional(Integer.parseInt(chatWith));  
				} %>			
				<div id="messanger">
					<div class="conversations_list">		
						<% List<Professional> messagedProfs = db.getProfsMessagingWith(prof.getID());
						   if ( messagedProfs == null ) { %> <p>DATABASE DOWN!</p> <% }   // should not happen
						   else {  %>
						   		<ul>
						<%	   	if ( chatWith != null && chatWithProf == null ){ %>
							   		<li id="404prof" class=conv_li style="background-color: #b2cdff">Unknown professional</li>
							   		<script>
					    				$("#404prof").on("click", function(){
					    					// Make the corresponding conversation active and the rest hidden
					    					$(".conversation").hide();
					    					$(".conv_li").css("background-color", "#fbfcff");
					    					$("#404CHATWITH").show();
					    					$("#404prof").css("background-color", "#b2cdff");
					    				});
					    			</script>
						<%	   	} else if ( chatWith != null && chatWithProf.getID() != prof.getID() && !messagedProfs.contains(chatWithProf)) {
						   			messagedProfs.add(0, chatWithProf);   // add at start
							   	} %>	
					    <% 		for (Professional p : messagedProfs) { %>
									<li id="conv<%= p.getID() %>" class="conv_li" <% if ( chatWith != null && chatWithProf!= null && chatWithProf.getID() == p.getID() ) { %> style="background-color: #b2cdff" <% } %> > 
										<%= p.getFirstName() %> <%= p.getLastName() %> 
									</li>
									<script>
					    				$("#conv<%= p.getID() %>").on("click", function(){
					    					// Make the corresponding conversation active and the rest hidden
					    					$(".conversation").hide();
					    					$(".conv_li").css("background-color", "#fbfcff");
					    					$("#conversation<%= p.getID() %>").show();
					    					$("#conv<%= p.getID() %>").css("background-color", "#b2cdff");
					    				});
					    			</script>
					    <% 		} %>
					    		</ul>
					    <%  } %>
					</div>
					<div class="conversation_box">
						<div class="conversation_container">
						<%  if ( chatWith != null && chatWithProf == null ){ %>
								<div id="404CHATWITH" class="conversation">
									<p>The professional you are attempting to message does not exist.</p>
								</div>
						<%  } else if ( chatWith == null || chatWithProf.getID() == prof.getID() ) { %>
								<div id="NOCONVSELECTED" class="conversation">
									<p>No conversation is selected.</p>
								</div>
						<%  } %>
							<!-- Prof's conversation divs but only one active at a time, the rest are hidden -->
						<% if ( messagedProfs == null ) { %> <p>DATABASE DOWN!</p> <% }   // should not happen
						   else { %>
						<% 		for (Professional p : messagedProfs) { %>
									<div id="conversation<%= p.getID() %>" class="conversation" <% if ( chatWithProf == null || chatWithProf.getID() != p.getID() ) { %> style="display: none" <% } %> >
										<p>This is your conversation with <%= p.getFirstName() %> <%= p.getLastName() %></p>
									</div>
						<% 		} %>
						<% } %>
						</div>
						<div class="conversation_input">
							<form class="ajax" method="post">
								<textarea id="msg_input" name="msg"></textarea>
								<input id="msg_submit" type="submit" value="send">
							</form>
						</div>
					</div>
				</div>
			</div>
	<% } %>
	<% db.close(); %>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Messages</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality, model.MyUtil" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/lib/bootstrap-grid.min.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/messanges.css"/>
	<!-- JS -->
	<script src="/TEDProject/js/lib/jquery-3.3.1.min.js"></script>
	<script src="/TEDProject/js/lib/bootstrap.min.js"></script>
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
					<jsp:param name="activePage" value="Messages"/> 
				</jsp:include>
			<%  String chatWith = request.getParameter("chatWith"); 
			    Professional chatWithProf = null;
				if (chatWith != null) {
					chatWithProf = db.getBasicProfessionalInfo(Integer.parseInt(chatWith));  
				} %>			
				<div id="messanger">
					<div class="conversations_list">		
						<% List<Professional> messagedProfs = db.getProfsMessagingWith(prof.getID());
						   if ( messagedProfs == null ) { %> <p>DATABASE DOWN!</p> <% }   // should not happen
						   else {  %>
						   		<ul>
						<%	   	if ( chatWith != null && chatWithProf == null ){ %>
							   		<li id="404prof" class="conv_li" style="background-color: #b2cdff">Unknown professional</li>
						<%	   	} else if ( chatWith != null && chatWithProf.getID() != prof.getID() && !messagedProfs.contains(chatWithProf)) {
						   			messagedProfs.add(0, chatWithProf);   // add at start
							   	} %>	
					    <% 		for (Professional p : messagedProfs) { %>
									<li id="conv<%= p.getID() %>" class="conv_li" <% if ( chatWith != null && chatWithProf!= null && chatWithProf.getID() == p.getID() ) { %> style="background-color: #b2cdff" <% } %> > 
										<a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">
											<img class="img-thumbnail float-left conv_prof_img" src="<%= p.getProfilePicURI() %>" alt="Profile picture">
										</a>
										<%= p.getFirstName() %> <%= p.getLastName() %> 
										<script>
						    				$("#conv<%= p.getID() %>").on("click", function(){
						    					select_conversation(<%= p.getID() %>);
						    				});
						    			</script>
									</li>
					    <% 		} %>
					    		</ul>
					    <%  } %>
					</div>
					<div class="conversation_box">
						<div class="conversation_container">
						<%  if ( chatWith != null && chatWithProf == null ){ %>
								<div id="404CHATWITH" class="conversation">
									<p><i>The professional you are attempting to message does not exist.</i></p>
								</div>
						<%  } else if ( chatWith == null || chatWithProf.getID() == prof.getID() ) { %>
								<div id="NOCONVSELECTED" class="conversation">
									<p><i>No conversation is selected.</i></p>
								</div>
						<%  } %>
							<!-- Prof's conversation divs but ONLY ONE active at a time, the rest are hidden -->
						<% if ( messagedProfs == null ) { %> <p>DATABASE DOWN!</p> <% }   // should not happen
						   else { %>
						<% 		for (Professional p : messagedProfs) { %>
									<div id="conversation<%= p.getID() %>" 
									  <% if (chatWith != null && chatWithProf != null && chatWithProf.getID() == p.getID()) { %>  class="conversation active_conv" <% } else { %> class="conversation" <% } %> 
									  <% if ( chatWithProf == null || chatWithProf.getID() != p.getID() ) { %> style="display: none" <% } %> >
											<% if (chatWith != null && chatWithProf != null && chatWithProf.getID() == p.getID() ) {  // if page is loaded with a conversation already selected via URL query parameters %>
													<jsp:include page="Conversation.jsp"> 
														<jsp:param name="homeprof" value="<%= prof.getID() %>" /> 
														<jsp:param name="awayprof" value="<%= p.getID()%>"/> 
													</jsp:include>
													<script>
														$(".active_conv").ready(function(){
															updateScroll();
														});
													</script>
											<% } %>
									</div>										
						<% 		} %>
						<% } %>
						</div>
						<div class="conversation_input">
							<form id="send_text">
								<textarea id="msg_input" name="msg"></textarea>
								<input id="msg_submit" type="submit" value="send" class="btn btn-outline-primary">
							</form>
						</div>
					</div>
				</div>
				<jsp:include page="/footer.html"></jsp:include>
			</div>
	<% } %>
	<% db.close(); %>
	 <!-- JS scripts for this page -->
	<script src="/TEDProject/js/messages.js" id="messages_script" data-profID="<%= prof.getID() %>"></script>
	<script src="/TEDProject/js/util.js"></script>
</body>
</html>
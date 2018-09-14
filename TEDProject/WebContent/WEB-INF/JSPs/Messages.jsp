<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Messages</title>
	<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality, model.MyUtil" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/messanges.css"/>
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
				<jsp:include page="ProfNavBar.jsp"> 
					<jsp:param name="activePage" value="Messages"/> 
				</jsp:include>
				<h2>Here be messages for <%= prof.getFirstName() %>  <%= prof.getLastName() %>!</h2><br>
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
							   		<script>
					    				$("#404prof").on("click", function(){
					    					// Make the corresponding conversation active and the rest hidden
					    					$(".conversation").hide();
					    					$(".conversation").removeClass("active_conv");
					    					$(".conv_li").css("background-color", "#fbfcff");
					    					$("#404CHATWITH").show();
					    					$("#404CHATWITH").addClass("active_conv");
					    					$("#404prof").css("background-color", "#b2cdff");
					    				});
					    			</script>
						<%	   	} else if ( chatWith != null && chatWithProf.getID() != prof.getID() && !messagedProfs.contains(chatWithProf)) {
						   			messagedProfs.add(0, chatWithProf);   // add at start
							   	} %>	
					    <% 		for (Professional p : messagedProfs) { %>
									<li id="conv<%= p.getID() %>" class="conv_li" <% if ( chatWith != null && chatWithProf!= null && chatWithProf.getID() == p.getID() ) { %> style="background-color: #b2cdff" <% } %> > 
										<img class="img-thumbnail float-left conv_prof_img" src="<%= p.getProfilePicURI() %>" alt="Profile picture"><%= p.getFirstName() %> <%= p.getLastName() %> 
									</li>
									<script>
					    				$("#conv<%= p.getID() %>").on("click", function(){
					    					// Make the corresponding conversation active and the rest hidden
					    					$(".conversation").hide();
					    					$(".conversation").removeClass("active_conv");
					    					$(".conv_li").css("background-color", "#fbfcff");
					    					$("#conversation<%= p.getID() %>").show();
					    					$("#conversation<%= p.getID() %>").addClass("active_conv");
					    					$("#conv<%= p.getID() %>").css("background-color", "#b2cdff");
					    					// load the conversation from server using AJAX
					    					$.ajax({
					    						url: "/TEDProject/AJAXServlet?action=loadConvo",
					    						type: "post",
					    						data: { homeprof: <%= prof.getID() %>, awayprof: <%= p.getID() %> },
					    						success: function(response){
					    							$("#conversation<%= p.getID() %>").html(response);
					    							updateScroll();
					    						}
					    					});
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
									<div id="conversation<%= p.getID() %>" 
									  <% if (chatWith != null && chatWithProf != null && chatWithProf.getID() == p.getID()) { %>  class="conversation active_conv" <% } else { %> class="conversation" <% } %> 
									  <% if ( chatWithProf == null || chatWithProf.getID() != p.getID() ) { %> style="display: none" <% } %> >
										<% if (chatWith != null && chatWithProf != null && chatWithProf.getID() == p.getID() ) { %>
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
								<input id="msg_submit" type="submit" value="send">
								<script>									
									$("#send_text").on("submit", function(){
										// if active conversation is with an unknown professional then dont sybmit anything
										if ( $(".active_conv").attr("id") === "404CHATWITH" ){
											 $("#msg_input").val("");  // reset input value
											return false;              // dont send anything
										}
										
										// Parse the id of active_conv to get the conversation's other professional id
										var other_prof_id = $('.active_conv').attr('id').substring(12);   // TODO: make this more versatile?
										
										// update database with AJAX
										$.ajax({
					    						url: "/TEDProject/AJAXServlet?action=addMessage",
					    						type: "post",
					    						data: { text: $("#msg_input").val(),
					    								sentBy: <%= prof.getID() %>,
					    								sentTo: other_prof_id,
										              },
					    						success: function(response){
					    							if ( response === "success" ){
						    							console.log("Conversation updated successfully");
														var datetime = "<%= MyUtil.printDate(null, true) %>";
														// append text to the (must be only one) active conversation
														$(".active_conv").append("<span class=\"home_timestamp\">" + datetime + "</span><p class=\"home_message\">" + $("#msg_input").val().replaceAll("\n","\n<br>\n") + "</p><br>");
														// reset input value
														$("#msg_input").val("");
														updateScroll();
					    							} else { // else toast-notify user
					    								window.alert(response);
					    							}
					    						}
					    				});
										
										return false;    // override default form action
									});
								</script>
							</form>
						</div>
					</div>
				</div>
				<jsp:include page="/footer.html"></jsp:include>
			</div>
	<% } %>
	<% db.close(); %>
	 <!-- JS functions for this page -->
	<script>
		// use this to scroll a conversation to the bottom
		function updateScroll(){
			var scroll_box = $('.active_conv');
		    var height = scroll_box[0].scrollHeight;
		    scroll_box.scrollTop(height);
		}
		
		function twoDigits(d) {
		    if (0 <= d && d < 10) return "0" + d.toString();
		    if (-10 < d && d < 0) return "-0" + (-1*d).toString();
		    return d.toString();
		}
		
		// submit text with 'enter' but not on shift+enter
		$("#send_text").keypress(function (e) {
		    if(e.which == 13 && !e.shiftKey) {        
		        $(this).closest("form").submit();
		        e.preventDefault();
		        return false;
		    }
		});	
		
		//update active_conv (ONLY the active conversation) in real time every 2 secs:
		window.setInterval(function(){
			// find latest away message on the (ONE) active conversation
			var latest_timestamp = $(".active_conv .away_timestamp").last().text();
			// Parse the id of active_conv to get the conversation's other professional id
			var other_prof_id = $('.active_conv').attr('id').substring(12);   // TODO: make this more versatile?
			// use ajax to update it with new messages - if they exist
			$.ajax({
				url: "/TEDProject/AJAXServlet?action=checkForNewMessages",
				type: "post",
				data: { latestGot: latest_timestamp,
						homeprof: <%= prof.getID() %>,
						awayprof: other_prof_id,
		              },
				success: function(response){            // on success we append any new (away) messages to the conversation
					$(".active_conv").append(response);
					// alternatively use: $(".conversation" + other_prof_id)
					updateScroll();
				}
			});
		}, 2000);
	</script>
	<script src="/TEDProject/Javascript/util.js"></script>
</body>
</html>
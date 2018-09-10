<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.DataBaseBridge, model.Article, model.Professional, model.Comment, model.MyUtil, model.SiteFunctionality" %>

<%! private DataBaseBridge db = new DataBaseBridge();   // TODO: This is inefficient	%>

<!-- stand-alone page or not based on boolean attribute -->

<%	String articleIDstr = request.getParameter("ArticleID");
	if ( articleIDstr == null ) {  %>
		<p>Error: Invalid Article ID.</p>
	<%	return;
	}
	int articleID = -1;
	try {
		articleID = Integer.parseInt(articleIDstr);
	} catch ( NumberFormatException e ) {  %>
		<p>Error: Invalid Article ID.</p>
	<%  return;
	}	
	Article article = db.getArticle(articleID);
	if (article == null) { %>
		<p>Error: Article not found.</p>
	<%	return;
	}	
	Professional authorProf = db.getProfessional(article.getAuthorID()); 
	if (authorProf == null) { %>
		<p>Error: Article author not found.</p>
	<%	return;
	}
	Professional sessionProf = SiteFunctionality.acquireProfFromSession(db, request); 
	if (sessionProf == null) { %>
		<p>Error: Invalid session.</p>
	<%	return;
	}	
	boolean sessionProfInterest = db.getInterest(articleID, sessionProf.getID()); %>
	<div class="article">
		<div class="d-flex flex-row vertical_center">
			<div>
				<a href="/TEDProject/ProfileLink?ProfID=<%= authorProf.getID() %>">
					<img class="img-thumbnail float-left article_prof_img" src="<%= authorProf.getProfilePicURI() %>" alt="Profile picture">
				</a>
			</div>
			<div>
				<a href="/TEDProject/ProfileLink?ProfID=<%= authorProf.getID() %>"><%= authorProf.getFirstName() %> <%= authorProf.getLastName() %></a><br>
				<a href="/TEDProject/prof/NavigationServlet?page=Article&ArticleID=<%= articleID %>"><small class="text-secondary"><%= MyUtil.getTimeAgo(article.getPostedDate()) %></small></a>
			</div>
		</div> 
		<div class="content_container">
			<%= article.getContent() %>
		</div>
		<form id="like_button<%= articleID %>" class="d-inline"><button type="submit" class="btn btn-sm <% if (sessionProfInterest) { %> btn-primary <% } else { %> btn-outline-primary <% } %> ml-1">I'm interested</button></form>
		<script>
	   		$("#like_button<%= articleID %>").on("submit", function(e){			
	   			e.preventDefault();
	   			$.ajax({
					url: "/TEDProject/AJAXServlet?action=toggleInterest",
					type: "post",
					data: { ArticleID: <%= articleID %>,
						 	ProfID: <%= sessionProf.getID() %> },
					success: function(response){
						if ( response === "success" ){
							console.log("Showed interest successfully");
						} else {
							window.alert(response);
						}
					},
				});
				if (this.firstChild.classList.contains("btn-outline-primary")) {
					this.firstChild.classList.add("btn-primary");
					this.firstChild.classList.remove("btn-outline-primary");
		   			$("#interestsCount<%= articleID %>").html(parseInt($("#interestsCount<%= articleID %>").html(), 10) + 1);
		   			$("#interestsCount<%= articleID %>_2").html(parseInt($("#interestsCount<%= articleID %>_2").html(), 10) + 1);
					document.getElementById('selfInterest<%= articleID %>').removeAttribute('style');
		   			if (document.getElementById('noInterestP<%= articleID %>')) {
						document.getElementById('noInterestP<%= articleID %>').setAttribute('style', 'display: none');
		   			}
				} else {
					this.firstChild.classList.add("btn-outline-primary");
					this.firstChild.classList.remove("btn-primary");
		   			$("#interestsCount<%= articleID %>").html(parseInt($("#interestsCount<%= articleID %>").html(), 10) - 1);
		   			$("#interestsCount<%= articleID %>_2").html(parseInt($("#interestsCount<%= articleID %>_2").html(), 10) - 1);
					document.getElementById('selfInterest<%= articleID %>').setAttribute('style', 'display: none !important');
		   			if (document.getElementById('noInterestP<%= articleID %>')) {
						document.getElementById('noInterestP<%= articleID %>').removeAttribute('style');
		   			}
				}
	   		});
	   	</script>
		<button id="commentButton<%= articleID %>" type="button" class="btn btn-sm btn-outline-primary comment_button ml-1">Comment</button>
		<div class="ml-2">
			<% List<Professional> interestedProfs = db.getInterestedProfessionals(articleID);
			   List<Comment> comments = db.getComments(articleID, true); %>
			<small><span class="interests_count" data-toggle="modal" data-target="#interestedProfs<%= articleID %>"><span id="interestsCount<%= articleID %>"><%= interestedProfs.size() %></span> Interests</span>
			&nbsp;·&nbsp;
			<span id="commentsCount<%= articleID %>"><%= comments.size() %></span> Comments</small>
		</div>
		<div class="comments_container">
			<div id="comment_form<%= articleID %>" class="comment_form" style="height: 0">	<!-- This style is needed here -->
				<form id="comment_input_form<%= articleID %>">
					<textarea id="comment_input_textarea<%= articleID %>" name="comment_text" class="comment_form_text" required></textarea>
					<div class="text-right">
						<input type="submit" value="Submit Comment" class="btn btn-primary">
					</div>
				</form>
				<script>
					$("#commentButton<%= articleID %>").on("click", function(){
					    commentForm = document.getElementById("comment_form<%= articleID %>");
					    if (commentForm.style.height !== "0px") {
					    	$(commentForm).animate({
				   	            height: '0px'
				   	        });
					    } else {
					    	$(commentForm).animate({ 
					    		height : commentForm.scrollHeight+'px' 
					    	});
					    	setTimeout(function() { 
					    		commentForm.style.height = "auto"; 
					    	}, 500);
					    }
					});
				</script>
				<script>
			   		$("#comment_input_form<%= articleID %>").on("submit", function(e){			
			   			e.preventDefault();
			   			$.ajax({
	   						url: "/TEDProject/AJAXServlet?action=addComment",
	   						type: "post",
	   						data: { commentText: $("#comment_input_textarea<%= articleID %>").val(), 
	   							  	ArticleID: <%= articleID %>,
	   							 	AuthorID: <%= sessionProf.getID() %> },
	   						success: function(response){
	   							if ( response === "success" ){
	   								console.log("Commented successfully");
	   								// reset form's fields
	   								$("#comment_input_textarea<%= articleID %>").val("");
	   							} else {
	   								window.alert(response);
	   							}
	   						},
	   					});
			   			$(this.parentElement).animate({
			   	            height: '0px'
			   	        });
			   			$("#past_comments_container<%= articleID %>").prepend($(`
			   				<div class="comment">
								<div class="d-flex flex-row vertical_center">
									<div>
										<a href="/TEDProject/ProfileLink?ProfID=<%= sessionProf.getID() %>">
											<img class="img-thumbnail float-left comment_prof_img" src="<%= sessionProf.getProfilePicURI() %>" alt="Profile picture">
										</a>
									</div>
									<div>
										<a href="/TEDProject/ProfileLink?ProfID=<%= sessionProf.getID() %>"><%= sessionProf.getFirstName() %> <%= sessionProf.getLastName() %></a> 
										&nbsp;<small class="text-secondary">just now</small>
									</div>
								</div> 
								<div class="content_container">
									`+ $("#comment_input_textarea<%= articleID %>").val() +`
								</div>
							</div>
						`).fadeIn('slow'));
			   			$("#commentsCount<%= articleID %>").html(parseInt($("#commentsCount<%= articleID %>").html(), 10) + 1);
			   		});
			   	</script>
			</div>
			<div id="past_comments_container<%= articleID %>" class="past_comments_container">
			 <% if (comments != null) {	   	
				   for (Comment c : comments) { 
				   		Professional commentAuthorProf = db.getProfessional(c.getAuthorID()); %>
						<div class="comment">
							<div class="d-flex flex-row vertical_center">
								<div>
									<a href="/TEDProject/ProfileLink?ProfID=<%= commentAuthorProf.getID() %>">
										<img class="img-thumbnail float-left comment_prof_img" src="<%= commentAuthorProf.getProfilePicURI() %>" alt="Profile picture">
									</a>
								</div>
								<div>
									<a href="/TEDProject/ProfileLink?ProfID=<%= commentAuthorProf.getID() %>"><%= commentAuthorProf.getFirstName() %> <%= commentAuthorProf.getLastName() %></a> 
									&nbsp;<small class="text-secondary"><%= MyUtil.getTimeAgo(c.getDateWritten()) %></small>
								</div>
							</div> 
							<div class="content_container">
								<%= c.getText() %>
							</div>
						</div>
				<% } 
			   } %>
			</div>
		</div>
		<div class="modal fade" id="interestedProfs<%= articleID %>" tabindex="-1" role="dialog" aria-hidden="true">
			<div class="modal-dialog modal-dialog-centered" role="document">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<h5 class="modal-title" id="exampleModalLongTitle">Interested Professionals (<span id="interestsCount<%= articleID %>_2"><%= interestedProfs.size() %></span>)</h5>
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          			<span aria-hidden="true">&times;</span>
		        		</button>
		      		</div>
		      		<div class="modal-body interested_profs_container">
	        			<div id="selfInterest<%= articleID %>" class="d-flex flex-row vertical_center mb-1" <% if (!sessionProfInterest) { %>style="display: none !important;"<% } %>>
							<div>
								<a href="/TEDProject/ProfileLink?ProfID=<%= sessionProf.getID() %>">
									<img class="img-thumbnail float-left comment_prof_img" src="<%= sessionProf.getProfilePicURI() %>" alt="Profile picture">
								</a>
							</div>
							<div>
								<a href="/TEDProject/ProfileLink?ProfID=<%= sessionProf.getID() %>"><%= sessionProf.getFirstName() %> <%= sessionProf.getLastName() %></a>
							</div>
						</div> 
		        	<% if (interestedProfs.size() > 0) { %>
						<% for (Professional p : interestedProfs) { 
						   		if ( p.getID() != sessionProf.getID() ) { %>
								<div class="d-flex flex-row vertical_center mb-1">
									<div>
										<a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">
											<img class="img-thumbnail float-left comment_prof_img" src="<%= p.getProfilePicURI() %>" alt="Profile picture">
										</a>
									</div>
									<div>
										<a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>"><%= p.getFirstName() %> <%= p.getLastName() %></a>
									</div>
								</div> 
							 <% }
						   } 
					   }
		        	   if (interestedProfs.size() == 0 || (interestedProfs.size() == 1 && sessionProfInterest)) { %>
						 <p id="noInterestP<%= articleID %>" <% if (sessionProfInterest) { %>style="display: none;"<% } %>><i>No professionals have shown interest in this post.</i></p>
					<% } %>
		      		</div>
			    </div>
			</div>
		</div>
	</div>

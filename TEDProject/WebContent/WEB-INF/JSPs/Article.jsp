<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.nio.file.Files, java.nio.file.Paths, model.DataBaseBridge, model.Article, model.Professional, model.Comment, model.MyUtil, model.SiteFunctionality" %>

<%! private DataBaseBridge db = new DataBaseBridge(); %>

<%! @Override
	public void finalize(){                               // kind of like a destructor
		db.close();
	} %>


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
	Professional authorProf = db.getBasicProfessionalInfo(article.getAuthorID()); 
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
	<div id="article<%= articleID %>" class="article">
		<div class="d-flex flex-row vertical_center">
			<div>
				<a href="/TEDProject/ProfileLink?ProfID=<%= authorProf.getID() %>">
					<img class="img-thumbnail float-left article_prof_img" src="<%= authorProf.getProfilePicURI() %>" alt="Profile picture">
				</a>
			</div>
			<div>
				<a href="/TEDProject/ProfileLink?ProfID=<%= authorProf.getID() %>"><%= authorProf.getFirstName() %> <%= authorProf.getLastName() %></a><br>
				<a href="/TEDProject/prof/NavigationServlet?page=Article&ArticleID=<%= articleID %>">
				<small class="text-secondary" data-toggle="tooltip" data-placement="top" title="<%= MyUtil.printDate(article.getPostedDate(), true) %>"><%= MyUtil.getTimeAgo(article.getPostedDate()) %></small></a>
			</div>
			<% if (article.getAuthorID() == sessionProf.getID()) { %>
				<div class="ml-auto mr-1">
					<a href="/TEDProject/prof/NavigationServlet?page=EditArticle&ArticleID=<%= articleID %>" class="btn btn-sm btn-outline-primary">Edit</a>
					<button id="deleteArticle<%= articleID %>" type="button" class="btn btn-sm btn-outline-secondary ml-1">✕</button>
		  		</div>
			<% } %>
		</div> 
		<div class="content_container">
			<% 	if ( article.getContent() != null && !article.getContent().isEmpty() ) { %>
					<p id="articleContent<%= articleID %>"><%= article.getContent() %></p>
					<script>
						document.getElementById("articleContent<%= articleID %>").innerHTML = SimpleMDE.prototype.markdown(`<%= article.getContent().replace("\\", "\\\\").replace("`", "\\`") %>`);
				  	</script>
			<% 	} %>
			<% 	if ( article.getContainsFiles() ) { %>
					<div class="articleFilesDiv">
				   	<%	List<String> fileURIs = article.getFileURIs(); 
				   		for ( String URI : fileURIs ) { 
				   			switch( MyUtil.getFileType(URI) ){
				   				case 1:    // image  %>
				   					<img id="<%= (MyUtil.getFileName(URI)).replace(".", "_") %>" class="article_img" src="<%= URI %>"  data-toggle="tooltip" data-placement="top" title="Click to enlarge!">
				   					<div class="modal fade" id="modal_<%= (MyUtil.getFileName(URI)).replace(".", "_") %>" tabindex="-1" role="dialog" aria-hidden="true" style="padding-left: 16px !important;">
										<div class="modal-dialog image_modal_dialog" role="document">
									    	<div class="modal-content image_modal_content">
										        <div class="modal-header">
											       <h5 class="modal-title">Posted by <a href="/TEDProject/ProfileLink?ProfID=<%= authorProf.getID() %>"><%= authorProf.getFirstName() %> <%= authorProf.getLastName() %></a>
											       &nbsp;<small class="text-secondary" data-toggle="tooltip" data-placement="top" title="<%= MyUtil.printDate(article.getPostedDate(), true) %>"><%= MyUtil.getTimeAgo(article.getPostedDate()) %></small></h5>
											       <button type="button" class="close" data-dismiss="modal" aria-label="Close">
											       		<span aria-hidden="true">&times;</span>
											       </button>
											    </div>
											    <div class="modal-body text-center">
											        <img class="modal_img" src="<%= URI %>">
											    </div>
									    	</div>
									    </div>
									</div>
				   		<%			break;
				   				case 2:    // video  %>
				   					<video class="article_vid" controls>
				   						<source src="<%= URI %>" type="<%= Files.probeContentType(Paths.get((MyUtil.getFileName(URI)))) %>">
				   					</video>
				   		<%			break;
				   				case 3:    // audio  %>
				   					<audio class="article_aud" controls>
				   						<source src="<%= URI %>" type="<%= Files.probeContentType(Paths.get((MyUtil.getFileName(URI)))) %>">
				   					</audio>
				   		<%			break;
				   				default:   // unsupported  %>
				   					<img src="/TEDProject/images/errorImage.png">
				   		<%			break;
				   			}
				   		}   %>
			   		</div>
			 <%	} %>
		</div>
		<form id="like_button<%= articleID %>" class="d-inline"><button type="submit" class="btn btn-sm <% if (sessionProfInterest) { %> btn-primary <% } else { %> btn-outline-primary <% } %> ml-1">I'm interested</button></form>
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
						<input type="submit" value="Submit Comment" class="btn btn-sm btn-primary">
					</div>
				</form>
			</div>
			<div id="past_comments_container<%= articleID %>" class="past_comments_container">
			 <% if (comments != null) {	   	
				   for (Comment c : comments) { 
				   		Professional commentAuthorProf = db.getBasicProfessionalInfo(c.getAuthorID()); %>
						<div id="comment<%= c.getID() %>" class="comment">
							<div class="d-flex flex-row vertical_center">
								<div>
									<a href="/TEDProject/ProfileLink?ProfID=<%= commentAuthorProf.getID() %>">
										<img class="img-thumbnail float-left comment_prof_img" src="<%= commentAuthorProf.getProfilePicURI() %>" alt="Profile picture">
									</a>
								</div>
								<div>
									<a href="/TEDProject/ProfileLink?ProfID=<%= commentAuthorProf.getID() %>"><%= commentAuthorProf.getFirstName() %> <%= commentAuthorProf.getLastName() %></a> 
									&nbsp;<small class="text-secondary" data-toggle="tooltip" data-placement="top" title="<%= MyUtil.printDate(c.getDateWritten(), true) %>"><%= MyUtil.getTimeAgo(c.getDateWritten()) %></small>
								</div>
								<% if (commentAuthorProf.getID() == sessionProf.getID()) { %>
									<div class="ml-auto">
										<button id="deleteComment<%= articleID %>_<%= c.getID() %>" class="btn btn-sm btn-outline-secondary mt-1">✕</button>
									</div>

								<% } %>
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
		        		<h5 class="modal-title">Interested Professionals (<span id="interestsCount<%= articleID %>_2"><%= interestedProfs.size() %></span>)</h5>
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

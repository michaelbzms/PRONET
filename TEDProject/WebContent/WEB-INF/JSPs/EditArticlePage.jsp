<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>PRONET - Personal Information</title>
	<link rel="icon" type="image/x-icon" href="/TEDProject/images/favicon.ico">
	<%@ page import="model.Professional, java.nio.file.Files, java.nio.file.Paths, model.DataBaseBridge, model.MyUtil, model.Article, java.util.List" %>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/simplemde.min.css">
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/bootstrap-grid.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/article.css"/>
	<link rel="stylesheet" type="text/css" href="/TEDProject/css/articlepage.css"/>
	<!-- JS -->
	<script src="/TEDProject/Javascript/jquery-3.3.1.js"></script>
	<script src="/TEDProject/Javascript/bootstrap.min.js"></script>
	<script src="/TEDProject/Javascript/simplemde.min.js"></script>
</head>
<body>
	<div class="main_container">
	<%  HttpSession currentSession = request.getSession(false);
	    int profID;
	    if (request.getSession(false) != null && currentSession.getAttribute("ProfID") != null) {
	   		profID = (int) currentSession.getAttribute("ProfID");
	    } else { %>
		 	<h2 class="my_h2">SESSION ENDED</h2>
		 	<p>Your session has ended. Please login again.</p>
		 <% return;
	    }
	    DataBaseBridge db = new DataBaseBridge();
		Professional prof = db.getBasicProfessionalInfo(profID);
		if ( !db.checkIfConnected() ) { %>
			<h2 class="my_h2">DATABASE ERROR</h2>	
			<p>It appears that our database is down. Please contact the site's administrators.</p>
	<%	} else if ( prof == null ) {  %>
			<h2 class="my_h2">INTERNAL ERROR</h2>	
			<p>Could not retrieve your info from our data base. How did you login?</p>
	<% 	} else { 
		   Professional authorProf = db.getBasicProfessionalInfo(profID);
		   boolean isAdmin = ( currentSession.getAttribute("isAdmin") != null && ((boolean) currentSession.getAttribute("isAdmin")) ); %>
		   
		   <jsp:include page="ProfNavBar.jsp"> 
		   		<jsp:param name="activePage" value="null"/> 
		   </jsp:include>
		   
		<% int articleID = -1;
		   if (request.getAttribute("ArticleID") != null) {
			   articleID = Integer.parseInt(request.getAttribute("ArticleID").toString());
		   } else { %>
			   <h2 class="my_h2">INVALID ARTICLE</h2>
			   <p>The requested Article does not exist.</p>
			 <% return;
		   }
		   Article article = db.getArticle(articleID);
			if (article == null) { %>
				<p>Error: Article not found.</p>
			<%	return;
			}
		   if (article.getAuthorID() != profID) { %>
			   <h2 class="my_h2">Permission Denied</h2>
			   <p>You don't have permission to edit this article.</p>
			 <% return;	   
		   } %>
		   
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
					<div class="ml-auto">
						<button id="deleteArticle<%= articleID %>" type="button" class="btn btn-sm btn-danger mr-2">Delete Article</button>
			  		</div>
				</div> 
				<div class="content_container">
					<form method=POST action="/TEDProject/prof/EditArticleServlet?ArticleID=<%= articleID %>">	
					    <textarea id="articleContent" name="articleContent"><%= article.getContent() %></textarea>
						<% 	if ( article.getContainsFiles() ) { %>
								<div class="articleFilesDiv">
							   	<%	List<String> fileURIs = article.getFileURIs(); 
							   		for ( String URI : fileURIs ) { 
							   			switch( MyUtil.getFileType(URI) ){
							   				case 1:    // image  %>
							   					<img id="<%= (MyUtil.getFileName(URI)).replace(".", "_") %>" class="article_img" src="<%= URI %>"  data-toggle="tooltip" data-placement="top" title="Click to enlarge!">
							   					<div class="modal fade" id="modal_<%= (MyUtil.getFileName(URI)).replace(".", "_") %>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true" style="padding-left: 16px !important;">
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
					    <div class="buttonContainer">
							<input type="submit" value="Save" class="btn btn-primary">
							<a href="/TEDProject/prof/NavigationServlet?page=HomePage" class="btn btn-secondary">Cancel</a>
						</div>
					</form>
				</div>
			</div>
		
			<script>
				var content = new SimpleMDE({ element: document.getElementById("articleContent"), showIcons: ["code", "table"] });
			</script>
   			<script src="/TEDProject/Javascript/util.js"></script>
			<script id="deleteArticleScript" src="/TEDProject/Javascript/deleteArticleScript.js" data-profID="<%= prof.getID() %>" data-redirect="true"></script>
   			<script src="/TEDProject/Javascript/imageModalScript.js"></script>
	<%  }
	    db.close(); %>
	</div>
</body>
</html>

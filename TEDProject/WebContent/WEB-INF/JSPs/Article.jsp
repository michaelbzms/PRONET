<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.DataBaseBridge, model.Article, model.Professional, model.MyUtil" %>

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
<%   	return;
	}	
	Article article = db.getArticle(articleID);
	if (article == null) { %>
		<p>Error: Article not found.</p>
		return;
<%	}	
	Professional authorProf = db.getProfessional(article.getAuthorID()); 
	if (authorProf == null) { %>
		<p>Error: Article author not found.</p>
		return;
<%	}	%>
	<div class="article">
		<div class="article_header">
			<a href="/TEDProject/ProfileLink?ProfID=<%= authorProf.getID() %>">
				<img class="img-thumbnail float-left article_prof_img" src="<%= authorProf.getProfilePicURI() %>" alt="Profile picture">
			</a>
			<a href="/TEDProject/ProfileLink?ProfID=<%= authorProf.getID() %>"><%= authorProf.getFirstName() %> <%= authorProf.getLastName() %></a><br>
			<small class="text-secondary"><%= MyUtil.getTimeAgo(article.getPostedDate()) %></small>
		</div> 
		<div class="content_container">
			<%= article.getContent() %>
		</div>
		<div class="ml-1 mb-2">
			<button type="button" class="btn btn-outline-primary">Like</button>
			<button type="button" class="btn btn-outline-primary">Comment</button>
		</div>
		<div class="comment_container">
			Comments go here
		</div>
	</div>

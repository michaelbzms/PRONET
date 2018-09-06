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
	<%	return;
	}	
	Professional authorProf = db.getProfessional(article.getAuthorID()); 
	if (authorProf == null) { %>
		<p>Error: Article author not found.</p>
	<%	return;
	}	%>
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
		<button type="button" class="btn btn-outline-primary ml-1 mb-2">Like</button>
		<button type="button" class="btn btn-outline-primary comment_button ml-1 mb-2">Comment</button>
		<div class="comment_container">
			<div class="comment_form" style="height: 0">
				<form method=POST action="#">
					<textarea name="comment_text" class="comment_form_text"></textarea>
					<div class="text-right">
						<input type="submit" value="Submit Comment" class="btn btn-primary">
					</div>
				</form>
			</div>

			<p>Comments go here</p>
		</div>
	</div>

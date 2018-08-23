<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>

<!-- Start of AJAX html response -->
<% String searchString = request.getParameter("searchString"); %>

<h4>Search results for "<%= searchString %>" are:</h4>

<%  DataBaseBridge db = new DataBaseBridge();
	Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
	List<Professional> matches = db.getSearchResultsFor(searchString);
	if ( matches == null ){ %>
		<p>It seems like our database is down. Please contact the site's administrators.</p>
<%	} else if ( matches.isEmpty() ) { %>
		<p>No results found.</p>
<%	} else { %>
	<ul class="grid_container">
	<%	for ( Professional p : matches ) { 
			if ( prof == null || p.getID() != prof.getID() ) {  // Should not be able to search for yourself %>
				<li class="grid_item">
					<img class="img-thumbnail" src="<%= p.getProfile_pic_file_path() %>" alt="Profile picture"><br>
					<b><%= p.getFirstName() %> <%= p.getLastName() %></b><br>
					<% if (p.getEmploymentStatus() != null) { %> 
						<%= p.getEmploymentStatus() %> 
					<% } else { %> 
						N/A  
					<% } %>
					<br> 
					<% if (p.getEmploymentInstitution() != null) { %> <%= p.getEmploymentInstitution() %> 
					<% } else { %> 
						N/A
					<% } %>
					<br>
					<a href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">View details</a>
				</li>
	<%		} 
		} %>
	</ul>
<%	} %>	
<% db.close(); %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Professional, model.DataBaseBridge, model.SiteFunctionality" %>

<% String searchString = request.getParameter("searchString");   // guaranteed to exist from AJAXServlet %>

<h4>Search results for "<%= searchString %>" are:</h4>

<%  DataBaseBridge db = new DataBaseBridge();
	Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
	List<Professional> matches = db.getSearchResultsFor(searchString);
	if ( matches == null ){ %>
		<p>It seems like our database is down. Please contact the site's administrators.</p>
<%	} else if ( matches.isEmpty() ) { %>
		<p>No results found.</p>
<%	} else { %>
		<div class="grid_container_container">
			<div class="grid_container">
			<%	for ( Professional p : matches ) { 
					if ( prof == null || p.getID() != prof.getID() ) {  // Should not be able to search for yourself %>
						<a class="grid_item" href="/TEDProject/ProfileLink?ProfID=<%= p.getID() %>">
							<div class="text-dark">
								<img class="img-thumbnail" src="<%= p.getProfilePicURI() %>" alt="Profile picture"><br>
								<b><%= p.getFirstName() %> <%= p.getLastName() %></b><br>
								<% if (p.getEmploymentStatus() != null && !p.getEmploymentStatus().isEmpty()) { %> 
									<%= p.getEmploymentStatus() %> 
								<% } %>
								<br>
								<% if (p.getEmploymentInstitution() != null && !p.getEmploymentInstitution().isEmpty()) { %> 
									<%= p.getEmploymentInstitution() %>
								<% } %>
								<br>
							</div>
						</a>
			<%		}
				} %>
			</div>
		</div>
<%	} %>	
<% db.close(); %>

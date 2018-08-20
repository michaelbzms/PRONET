<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List, model.Professional, model.DataBaseBridge" %>

<!-- Start of AJAX html response -->
<% String searchString = request.getParameter("searchString"); %>

<h4>Search results for "<%= searchString %>" are:</h4>

<%  DataBaseBridge db = new DataBaseBridge();
	List<Professional> matches = db.getSearchResultsFor(searchString);
	if ( matches == null ){ %>
		<p>Something went wrong on our side! We apologize for the inconvenience</p>
<%	} else if ( matches.isEmpty() ) { %>
		<p>No results found.</p>
<%	} else { %>
	<div class="grid_container">
	<%	for ( Professional p : matches ) { %>
		<div class="grid_item">
			<img src="<%= p.getProfile_pic_file_path() %>" alt="Profile picture"><br>
			<%= p.getFirstName() %> <%= p.getLastName() %><br>
			<%= p.getEmploymentStatus() %><br>
			<%= p.getEmploymentInstitution() %><br>	
			<a href="/TEDProject/ProfileLink?ID=<%= p.getID() %>">View details</a>
		</div>
	<%	} %>
	</div>
<%	} %>	
<% db.close(); %>

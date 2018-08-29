<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.DataBaseBridge, model.Message" %>
<!-- This JSP is loaded very often (ex every 2 secs) -->

<%	String latestGot = request.getParameter("latestGot");
	String homeprofIDstr = request.getParameter("homeprof");
	String awayprofIDstr = request.getParameter("awayprof");
	if ( latestGot == null || homeprofIDstr == null || awayprofIDstr == null ) {   // should not happen (checked by AJAXServlet)
		return;
	} else {
		int homeprofID, awayprofID;
		try{
			homeprofID = Integer.parseInt(homeprofIDstr);
			awayprofID = Integer.parseInt(awayprofIDstr);
		} catch ( NumberFormatException e ){                 // also should not happen (checked by AJAXServlet) %>
			<p>Error: the ID of one of the professionals appears to not be a number.</p>
	<%   	return;
		}
		DataBaseBridge db = new DataBaseBridge();            // TODO making a new connection every 2 secs is very expensive: CHANGE CONNECTIONS OF DATABASEBRIDGE TO STATIC
		List<Message> messages = db.getNewAwayMessagesAfter(latestGot, homeprofID, awayprofID);
		if ( messages == null ) { %>
			<p>Error: DataBase down</p>
	<%	} else {
			for ( Message msg : messages ) { %>
				<span class="away_timestamp">
					<%= msg.getTimeSent() /* (!) Important: dont change timestamp format presented */ %>
				</span>
				<p class="away_message"> 
					<% 	if (msg.getText() != null) { %>
							<%= msg.getText() %><br>
					<% 	} 
					   	if (msg.getContainsFiles()) { %>
					   		<!-- TODO get files -->
					   		<br>**insert file(s) here**<br>
					<%	} %>
				</p>
	<% 		}
		}
		db.close(); 
	} %>



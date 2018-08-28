<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.DataBaseBridge, model.Message" %>

<% 	String homeprofIDstr = request.getParameter("homeprof");
	String awayprofIDstr = request.getParameter("awayprof");
	if ( homeprofIDstr == null || awayprofIDstr == null ) {   // should not happen (checked by AJAXServlet)
		return;
	} else {
		int homeprofID, awayprofID;
		try{
			homeprofID = Integer.parseInt(homeprofIDstr);
			awayprofID = Integer.parseInt(awayprofIDstr);
		} catch ( NumberFormatException e ){ %>
			<p>Error: the ID of one of the professionals appears to not be a number.</p>
	<%   	return;
		}
		DataBaseBridge db = new DataBaseBridge();
		List<Message> messages = db.getMessagesForConvo(homeprofID, awayprofID);
		if ( messages == null ) { %>
			<p>Error: DataBase down</p>
	<%	} else {
			for ( Message msg : messages ) { %>
				<span <% if ( msg.getSentByProfID() == homeprofID )  { %> class="home_timestamp" 
				   	  <% } else if (  msg.getSentByProfID() == awayprofID ) { %> class="away_timestamp" <% } %>>
					<%= msg.getTimeSent() %>
				</span>
				<p <% if ( msg.getSentByProfID() == homeprofID )  { %> class="home_message" 
				   <% } else if (  msg.getSentByProfID() == awayprofID ) { %> class="away_message" <% } %>> 
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

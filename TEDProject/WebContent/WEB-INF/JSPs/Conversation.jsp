<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.DataBaseBridge, model.Message, model.MyUtil" %>


<% 	String homeprofIDstr = request.getParameter("homeprof");
	String awayprofIDstr = request.getParameter("awayprof");
	if ( homeprofIDstr == null || awayprofIDstr == null ) {   // should not happen (checked by AJAXServlet)
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
		DataBaseBridge db = new DataBaseBridge();
		List<Message> messages = db.getMessagesForConvo(homeprofID, awayprofID);
		if ( messages == null ) { %>
			<p>Error: DataBase down</p>
	<%	} else {
			for ( Message msg : messages ) { %>
				<!-- (!) The following span must NOT be changed: do NOT add any kind of white space -->
				<span <% if ( msg.getSentByProfID() == homeprofID )  { %> class="home_timestamp" 
				   	  <% } else if (  msg.getSentByProfID() == awayprofID ) { %> class="away_timestamp" <% } %>><%= MyUtil.printDate(msg.getTimeSent(), true) %></span>
				<p <% if ( msg.getSentByProfID() == homeprofID )  { %> class="home_message" 
				   <% } else if (  msg.getSentByProfID() == awayprofID ) { %> class="away_message" <% } %>> 
					<% 	if (msg.getText() != null) { %>
							<%= msg.getText() %><br>
					<% 	} %>
				</p>
	<% 		}
		}
		db.close(); 
	} %>

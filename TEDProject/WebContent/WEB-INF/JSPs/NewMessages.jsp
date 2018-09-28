<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.time.LocalDateTime, model.DataBaseBridge, model.Message, model.MyUtil" %>
<%  // Warning: This JSP is loaded very often (ex every 2 secs)  %>
<%! private DataBaseBridge newMessagesConnection = new DataBaseBridge();    // this connection will close by finalize when this jsp servlet gets destroyed
 	private boolean failedBefore = false;

	@Override
	public void finalize(){               // kind of like a destructor
		newMessagesConnection.close();
	} 
%>

<%  if ( !newMessagesConnection.checkIfConnected() ) {      // if not connected to database try ONCE to make a new connection 
		if (failedBefore) return;
		newMessagesConnection = new DataBaseBridge();
		if ( !newMessagesConnection.checkIfConnected() ){   // if that fails as well then abort (database is down)
			failedBefore = true;
			System.err.println("Warning: Database is down!");
			return;
		}
	} %>
<%	String latestGotStr = request.getParameter("latestGot");
	String homeprofIDstr = request.getParameter("homeprof");
	String awayprofIDstr = request.getParameter("awayprof");
	if ( latestGotStr == null || homeprofIDstr == null || awayprofIDstr == null ) {   // should not happen (checked by AJAXServlet)
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
		LocalDateTime latestGot;
		if ( latestGotStr.isEmpty() ){
			latestGot = null;
		} else { 
			latestGot = MyUtil.getLocalDateTimeFromString(latestGotStr, true);
		}
		List<Message> messages = newMessagesConnection.getNewAwayMessagesAfter(latestGot, homeprofID, awayprofID);
		if ( messages == null ) { // should not happen %>
			DATABASE_DOWN
	<%	} else if ( messages.isEmpty() ) { // NO_NEW_MESSAGES is scanned for on client's side and if it is found then nothing is appened to message feed %>
			NO_NEW_MESSAGES
	<%	} else {
			for ( Message msg : messages ) { %>
				<!-- (!) The following span must NOT be changed: do NOT add any kind of white space -->
				<span class="away_timestamp"><%= MyUtil.printDate(msg.getTimeSent(), true) %></span>
				<p class="away_message"> 
					<% 	if (msg.getText() != null) { %>
							<%= msg.getText() %><br>
					<% 	} %>
				</p>
	<% 		}
		}
	} %>

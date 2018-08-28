package control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SiteFunctionality;


@WebServlet("/AJAXServlet")
public class AJAXServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public AJAXServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Got an AJAX GET request!!!");
	    response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
	    response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
	    PrintWriter out = response.getWriter();	    
	    out.write("AJAX SAYS HI!");       // Write response body.
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// System.out.println("Got an AJAX POST request!!!");
	    response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
	    response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
	    PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		if (action == null) {
			out.write("Error: null action!");
		} else {
			RequestDispatcher RequetsDispatcherObj;
			switch(action) {
				case "searchProfessional":
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/AJAXSearchResults.jsp");
					RequetsDispatcherObj.forward(request, response);		
					break;
				case "connectionRequest":
					String decision = request.getParameter("decision");
					String askerIDstr = request.getParameter("AskerID");
					String receiverIDstr = request.getParameter("ReceiverID");
					if ( decision == null || askerIDstr == null || receiverIDstr == null ) {
						out.write("AJAX connection request answer reached server with invalid parameters");
					} else {
						int AskerID = Integer.parseInt(askerIDstr);
						int ReceiverID = Integer.parseInt(receiverIDstr);
						SiteFunctionality.updateConnectionRequest(AskerID, ReceiverID, (decision.equals("accept")) ? true : false);
					}
					break;
				case "loadConvo":
					String homeprofIDstr = request.getParameter("homeprof");
					String awayprofIDstr = request.getParameter("awayprof");
					if ( homeprofIDstr == null || awayprofIDstr == null ) {
						out.write("AJAX load conversation request reached server with invalid parameters");
					} else {
						RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/Conversation.jsp");
						RequetsDispatcherObj.forward(request, response);
					}
					break;
				case "addMessage":
					String text = request.getParameter("text");
					String sentByProfStr = request.getParameter("sentBy");
					String sentToProfStr = request.getParameter("sentTo");
					String datetime = request.getParameter("timestamp");
					String containsFilesStr = request.getParameter("containsFiles");
					if ( text == null || sentByProfStr == null || sentToProfStr == null || datetime == null || containsFilesStr == null) {
						out.write("AJAX add message request reached server with invalid parameters");
					} else {
						boolean containsFiles = containsFilesStr.equals("true");
						int sentById, sentToId;
						try {
							sentById = Integer.parseInt(sentByProfStr);
							sentToId = Integer.parseInt(sentToProfStr);
						} catch ( NumberFormatException e ) {
							out.write("AJAX add message request reached server with invalid FORMAT parameters");
							return;
						}
						SiteFunctionality.addMessage(text, sentById, sentToId, datetime, containsFiles);
					}
					break;
				default:
					out.write("Error: Invalid AJAX action!");
					break;
			}
		}
	}

}

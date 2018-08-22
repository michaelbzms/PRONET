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
			switch(action) {
				case "searchProfessional":
					//out.write("<p style=\"font-weight: bold\">Searched for professional: " + request.getParameter("searchString") + ", successfully</p>");
					RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/AJAXSearchResults.jsp");
					RequetsDispatcherObj.forward(request, response);		
					break;
				case "connectionRequest":
					String decision = request.getParameter("decision");
					String askerIDstr = request.getParameter("AskerID");
					String receiverIDstr = request.getParameter("ReceiverID");
					if ( decision == null || askerIDstr == null || receiverIDstr == null ) {
						out.write("AJAX connection request answer reached server but invalid parameters");
					} else {
						out.write("server got AJAX connection request answer successfully");
						int AskerID = Integer.parseInt(askerIDstr);
						int ReceiverID = Integer.parseInt(receiverIDstr);
						SiteFunctionality.updateConnectionRequest(AskerID, ReceiverID, (decision.equals("accept")) ? true : false);
					}
					break;
				default:
					out.write("Error: Invalid AJAX action!");
					break;
			}
		}
	}

}
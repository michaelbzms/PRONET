package control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
		System.out.println("Got an AJAX POST request!!!");
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
				default:
					out.write("Error: Invalid AJAX action!");
					break;
			}
		}
	}

}

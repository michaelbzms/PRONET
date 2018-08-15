package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LogoutServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("errorType", "invalidPageRequest");
		RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
		RequetsDispatcherObj.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
    	if (session != null){
        	System.out.println("User with ID " + session.getAttribute("ProfID") + " successfully logged out.");
    		session.invalidate();
    	}
    	// TODO: Maybe also print a "successful logout" message?
    	response.sendRedirect("/TEDProject/index.html");
    }
}

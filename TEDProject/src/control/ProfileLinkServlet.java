package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/ProfileLink")
public class ProfileLinkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public ProfileLinkServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher RequetsDispatcherObj;
		int profID;
		String profIDstr = request.getParameter("ProfID");
		HttpSession currentSession = request.getSession(false);
		if (profIDstr != null && !profIDstr.isEmpty()) {		// if requested with a ProfID, use that
			profID = Integer.parseInt(profIDstr);
		} else if (currentSession != null) {					// else use the ProfID of the currently logged in professional
			profID = (int) currentSession.getAttribute("ProfID");
			// update lastVisited
			String referer = request.getHeader("Referer");
			currentSession.setAttribute("lastVisited", referer);
		} else {
			request.setAttribute("errorType", "invalidPageRequest");
			RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
			return;
		}
		RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/PersonalInformation.jsp");
		request.setAttribute("ProfID", profID);
		RequetsDispatcherObj.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SiteFunctionality;


@WebServlet("/prof/ConnectionServlet")
public class ConnectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ConnectionServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher RequetsDispatcherObj;
		String action = request.getParameter("action");
		String profIDstr = request.getParameter("ProfID");
		if (action == null || profIDstr == null || request.getSession(false) == null) {	
			request.setAttribute("errorType", "invalidPageRequest");
			RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
			return;
		}
		int sessionProfID = (int) request.getSession(false).getAttribute("ProfID");
		int profID = Integer.parseInt(profIDstr);
		int result;
		switch(action) {
			case "connect":
				result = SiteFunctionality.sendConnectionRequest(sessionProfID, profID);
				break;
			case "remove":
				result = SiteFunctionality.removeConnection(sessionProfID, profID);
				break;
			case "accept":
				result = SiteFunctionality.updateConnectionRequest(profID, sessionProfID, true);
				break;
			case "reject":
				result = SiteFunctionality.updateConnectionRequest(profID, sessionProfID, false);
				break;
			case "cancel":
				result = SiteFunctionality.updateConnectionRequest(sessionProfID, profID, false);
				break;
			default:	
				request.setAttribute("errorType", "invalidPageRequest");
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
				return;
		}
		switch (result) {
			case 0:     // success
				System.out.println(action + " success!");
				response.sendRedirect("/TEDProject/ProfileLink?ProfID=" + profID);
				break;
			case -1:      // database error
				request.setAttribute("errorType", "dbError");
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			default:      // should not happen
				request.setAttribute("errorType", "invalid return code");
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

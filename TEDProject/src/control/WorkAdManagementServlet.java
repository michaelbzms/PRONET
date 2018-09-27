package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SiteFunctionality;

@WebServlet("/prof/WorkAdManagementServlet")
public class WorkAdManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WorkAdManagementServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher RequetsDispatcherObj;
		String action = request.getParameter("action");
		String workdAdIDstr = request.getParameter("AdID");
		if (action == null || (workdAdIDstr == null && !action.equals("create")) || request.getSession(false) == null || request.getSession(false).getAttribute("ProfID") == null) {	
			request.setAttribute("errorType", "invalidPageRequest");
			RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
			return;
		}
		int profID = (int) request.getSession(false).getAttribute("ProfID");
		int adID = -1;
		if (workdAdIDstr != null) {
			adID = Integer.parseInt(workdAdIDstr);
		}
		int result;
		String title, description;
		switch(action) {
			case "create":
				title = request.getParameter("title");
				description = request.getParameter("description");
				if ( title.isEmpty() || description.isEmpty() ) {
					result = -2;
				} else if ( !SiteFunctionality.checkInputText(title, true, 127) ) {
					result = -3;
				} else {
					result = SiteFunctionality.createWorkAd(profID, title, description);
					if (result >= 0) {
						response.sendRedirect("/TEDProject/WorkAdLink?AdID=" + result + "&alert=workAdCreationSuccess");
						return;
					}
				}
				break;
			case "edit":
				description = request.getParameter("description");
				if ( description.isEmpty() ) {
					result = -2;
				} else {
					result = SiteFunctionality.updateWorkAd(adID, profID, description);
					if (result == 0) {
						response.sendRedirect("/TEDProject/WorkAdLink?AdID=" + adID + "&alert=workAdEditSuccess");
						return;
					}
				}
				break;
			case "delete":
				result = SiteFunctionality.removeWorkAd(adID, profID);
				if (result == 0) {
					response.sendRedirect("/TEDProject/prof/NavigationServlet?page=WorkAds&alert=workAdDeletionSuccess");
					return;
				}
				break;
			case "apply":
				description = request.getParameter("applyNote");
				if ( description.isEmpty() ) {
					result = -2;
				} else {
					result = SiteFunctionality.applyToWorkAd(adID, profID, description);
					if (result == 0) {
						response.sendRedirect("/TEDProject/prof/NavigationServlet?page=WorkAds&alert=applicationSubmissionSuccess");
						return;
					}
				}
				break;
			case "cancel":
				result = SiteFunctionality.removeApplication(profID, adID);
				if (result == 0) {
					response.sendRedirect("/TEDProject/prof/NavigationServlet?page=WorkAds&alert=applicationCancelationSuccess");
					return;
				}
				break;
			default:	
				request.setAttribute("errorType", "invalidPageRequest");
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
				return;
		}
		switch (result) {
			case 0:     // uncaught success
				response.sendRedirect("/TEDProject/prof/NavigationServlet?page=WorkAds");
				break;
			case -1:
			case -503:      // database error
				request.setAttribute("errorType", "dbError");
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			case -2:
				request.setAttribute("errorType", "emptyFormFields");
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			case -3:
				request.setAttribute("errorType", "illegalTextInput");
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			case -4:
				request.setAttribute("errorType", "noPermission");
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

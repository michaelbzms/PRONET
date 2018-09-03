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
 * Servlet implementation class NavigationServlet
 */
@WebServlet("/prof/NavigationServlet")
public class NavigationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    public NavigationServlet() {
        super();
    }

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher RequetsDispatcherObj;
		// Save last visited page (aka referer)
		String referer = request.getHeader("Referer");
		HttpSession currentSession = request.getSession(false);
		if ( currentSession == null ) {
			request.setAttribute("errorType", "nullSession");
			RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
			return;
		} else {
			currentSession.setAttribute("lastVisited", referer);
		}
		// Figure out requested page
		String page = request.getParameter("page");
		if (page == null) {
			request.setAttribute("errorType", "invalidPageRequest");
			RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
			return;
		}
		String attr;
		switch(page) {
			case "HomePage":
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/HomePage.jsp");
				RequetsDispatcherObj.forward(request, response);
				return;
			case "Network":
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/Network.jsp");
				RequetsDispatcherObj.forward(request, response);
				return;
			case "WorkAds":
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/WorkAds.jsp");
				RequetsDispatcherObj.forward(request, response);
				return;
			case "Messages":
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/Messages.jsp");
				RequetsDispatcherObj.forward(request, response);
				return;
			case "Notifications":
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/Notifications.jsp");
				RequetsDispatcherObj.forward(request, response);
				return;
			case "Settings":
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/Settings.jsp");
				RequetsDispatcherObj.forward(request, response);
				return;
			case "ChangeSettings":
				attr = request.getParameter("attr");
				if (attr != null && (attr.equals("email") || attr.equals("password")) ) {
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ChangeSettings.jsp");
					request.setAttribute("attr", attr);
					RequetsDispatcherObj.forward(request, response);
					return;
				} 
				break;
			case "EditProfile":
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/EditProfile.jsp");
				RequetsDispatcherObj.forward(request, response);
				return;
			case "EditWorkAd":
				attr = request.getParameter("AdID");
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/EditWorkAd.jsp");
				if (attr != null) {
					request.setAttribute("AdID", attr);
				}
				RequetsDispatcherObj.forward(request, response);
				return;
			case "Article":
				attr = request.getParameter("ArticleID");
				if (attr != null) {
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ArticlePage.jsp");
					request.setAttribute("ArticleID", attr);
					RequetsDispatcherObj.forward(request, response);
					return;
				}
				break;
		}
		request.setAttribute("errorType", "invalidPageRequest");
		RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
		RequetsDispatcherObj.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NavigationServlet
 */
@WebServlet("/NavigationServlet")
public class NavigationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NavigationServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		RequestDispatcher RequetsDispatcherObj;
		switch(page) {
			case "HomePage":
				RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/HomePage.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			case "Network":
				RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/Network.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			case "WorkAds":
				RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/WorkAds.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			case "Messages":
				RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/Messages.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			case "Notifications":
				RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/Notifications.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			case "PersonalInformation":
				RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/PersonalInformation.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			case "Settings":
				RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/Settings.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			default:
				request.setAttribute("errorType", "invalidPageRequest");
				RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

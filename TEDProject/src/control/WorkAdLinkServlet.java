package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/WorkAdLink")
public class WorkAdLinkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public WorkAdLinkServlet() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher RequetsDispatcherObj;
		int adID;
		String adIDstr = request.getParameter("AdID");
		if (adIDstr != null && !adIDstr.isEmpty()) {
			adID = Integer.parseInt(adIDstr);
		} else {
			request.setAttribute("errorType", "invalidPageRequest");
			RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
			return;
		}
		RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/WorkAdPage.jsp");
		request.setAttribute("AdID", adID);
		RequetsDispatcherObj.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

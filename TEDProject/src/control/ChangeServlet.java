package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ChangeServlet
 */
@WebServlet("/ChangeServlet")
public class ChangeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ChangeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String attr = request.getParameter("attr");
		RequestDispatcher RequetsDispatcherObj;
		if (attr == null) {
			request.setAttribute("errorType", "invalidPageRequest");
			RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
		}
		switch(attr) {
			case "email":
				// Here goes code
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/HomePage.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			case "password":
				// Here also goes code
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/HomePage.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
			default:
				request.setAttribute("errorType", "invalidPageRequest");
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
				break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

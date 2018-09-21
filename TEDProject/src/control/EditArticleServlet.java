package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SiteFunctionality;

@WebServlet("/prof/EditArticleServlet")
public class EditArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EditArticleServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher RequetsDispatcherObj;
		String articleIDstr = request.getParameter("ArticleID");
		if (articleIDstr == null || request.getSession(false) == null || request.getSession(false).getAttribute("ProfID") == null) {	
			request.setAttribute("errorType", "invalidPageRequest");
			RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
			return;
		}
		int profID = (int) request.getSession(false).getAttribute("ProfID");
		int articleID = Integer.parseInt(articleIDstr);
		int result;
		String articleContent = request.getParameter("articleContent");
		if ( articleContent.isEmpty() ) {		// TODO: do we want to prevent empty posts?
			result = -2;
		} else {
			result = SiteFunctionality.updateArticle(articleID, profID, articleContent);
		}			
		switch (result) {
			case 0:     // success
				response.sendRedirect("/TEDProject/prof/NavigationServlet?page=Article&ArticleID=" + articleID);
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

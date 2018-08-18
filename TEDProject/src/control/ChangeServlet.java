package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SiteFunctionality;

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
				String password, new_email;
				password = request.getParameter("password");
				new_email = request.getParameter("new_email");
				if ( password.isEmpty() || new_email.isEmpty() ) {
					System.out.println("Form submitted but has unfilled fields. Ignored.");
					// Notify user
					// TEMP: for now just reload the same page
					response.sendRedirect("/TEDProject/");  // this clears all input form data though (!) -  use AJAX instead?
				} else if ( false ) {	//!SiteFunctionality.checkInputText(new_email, false, true, 0) ) {
					System.out.println("|"+new_email+"|");
					System.out.println("Form submitted but one or more fields have illegal input characters.");
					request.setAttribute("errorType", "illegalTextInput");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else {
					int profID = (int) request.getSession(false).getAttribute("ProfID");
					int result = SiteFunctionality.ChangeEmail(profID, password, new_email);
					switch (result) {
						case 0:     // success
							System.out.println("Email changed successfully");
							// "login the user" or toast-notify him and prompt him to log in from the welcome page
							// ...	
							// TEMP: for now just reload the same page
							response.sendRedirect("/TEDProject/");  // this clears all input form data though (!) -  use AJAX instead?
							break;
						case -1:     // invalid current password
							request.setAttribute("errorType", "invalidCurrentPassword");
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
							RequetsDispatcherObj.forward(request, response);
							break;
						case -2:      // email already taken
							request.setAttribute("errorType", "newEmailTaken");
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
							RequetsDispatcherObj.forward(request, response);
							break;
						case -3:      // database error
							request.setAttribute("errorType", "dbError");
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
							RequetsDispatcherObj.forward(request, response);
							break;
						default:      // should not happen
							request.setAttribute("errorType", "invalid return code at registration");
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
							RequetsDispatcherObj.forward(request, response);
							break;
					}
				}
				break;
			case "password":
				// Here also goes code
				RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/Settings.jsp");
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

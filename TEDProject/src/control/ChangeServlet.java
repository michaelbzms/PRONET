package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SiteFunctionality;


@WebServlet("/ChangeServlet")
public class ChangeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ChangeServlet() {
        super();
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
				String password, newEmail;
				password = request.getParameter("password");
				newEmail = request.getParameter("newEmail");
				if ( password.isEmpty() || newEmail.isEmpty() ) {
					request.setAttribute("errorType", "emptyFormFields");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else if ( !SiteFunctionality.checkInputText(newEmail, true, true, 0) ) {
					System.out.println("|"+newEmail+"|");
					System.out.println("Form submitted but one or more fields have illegal input characters.");
					request.setAttribute("errorType", "illegalTextInput");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else {
					int profID = (int) request.getSession(false).getAttribute("ProfID");
					int result = SiteFunctionality.ChangeEmail(profID, password, newEmail);
					switch (result) {
						case 0:     // success
							System.out.println("Email changed successfully");
							// notify user for sucess and reload settings page	
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/Settings.jsp");
							RequetsDispatcherObj.forward(request, response);
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
				String currentPassword, newPassword, reNewPassword;
				currentPassword = request.getParameter("currentPassword");
				newPassword = request.getParameter("newPassword");
				reNewPassword = request.getParameter("reNewPassword");
				if ( currentPassword.isEmpty() || newPassword.isEmpty() || reNewPassword.isEmpty() ) {
					request.setAttribute("errorType", "emptyFormFields");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else if ( currentPassword.equals(newPassword) ) {		// current password same as new
					request.setAttribute("errorType", "unchangedPassword");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else if ( !newPassword.equals(reNewPassword) ) {			// passwords not matching
					request.setAttribute("errorType", "notMatchingPasswordsChange");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else if ( !SiteFunctionality.checkInputText(newPassword, true, true, 128) ) {
					System.out.println("Form submitted but one or more fields have illegal input characters.");
					request.setAttribute("errorType", "illegalTextInput");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else {
					int profID = (int) request.getSession(false).getAttribute("ProfID");
					int result = SiteFunctionality.ChangePassword(profID, currentPassword, newPassword);
					switch (result) {
						case 0:     // success
							System.out.println("Password changed successfully");
							// "login the user" or toast-notify him and prompt him to log in from the welcome page
							// ...	
							// TEMP: for now just reload the same page
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/Settings.jsp");
							RequetsDispatcherObj.forward(request, response);
							break;
						case -1:     // invalid current password
							request.setAttribute("errorType", "invalidCurrentPassword");
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
							RequetsDispatcherObj.forward(request, response);
							break;
						case -2:      // database error
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

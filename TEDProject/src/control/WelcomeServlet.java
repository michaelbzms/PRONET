package control;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.SiteFunctionality;


/**
 * Servlet implementation class WelcomeServlet
 */
@WebServlet("/WelcomeServlet")
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private int timeout_interval = 5*60;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WelcomeServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("errorType", "invalidPageRequest");
		RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
		RequetsDispatcherObj.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Invalidate old session, if there is one
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
		// check input of form got via HTTP POST
		if ( request.getParameter("register").equals("true") ) {         // Registration
			// fetch everything from the form
			String email, password, re_password, firstName, lastName, phone, profilePicFilePath;
			password = request.getParameter("password");
			re_password = request.getParameter("repeat_password");
			email = request.getParameter("email");
			firstName = request.getParameter("firstname");
			lastName = request.getParameter("lastname");
			phone = request.getParameter("phone_number");
			profilePicFilePath = "C:/defaultpicpath/defaultpic.jpeg";    // example
			// check if any field was empty:
			if ( email.isEmpty() || password.isEmpty() || re_password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || profilePicFilePath.isEmpty() ) {
				System.out.println("Form submitted but has unfilled fields. Ignored.");
				// Notify user
				// TEMP: for now just reload the same page
				response.sendRedirect("/TEDProject/");  // this clears all input form data though (!) -  use AJAX instead?
			}
			else if ( !SiteFunctionality.checkInputText(email, true, true, 0) || !SiteFunctionality.checkInputText(password, true, true, 128) || !SiteFunctionality.checkInputText(re_password, true, true, 128) 
				   || !SiteFunctionality.checkInputText(firstName, false, true, 255) || !SiteFunctionality.checkInputText(lastName, false, true, 255) || !SiteFunctionality.checkInputNumber(phone, 32) ) {
				System.out.println("Form submitted but one or more fields have illegal input characters.");
				request.setAttribute("errorType", "illegalTextInput");
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			}
			else {
				// call register function from the model
				int result = SiteFunctionality.Register(email, password, re_password, firstName, lastName, phone, profilePicFilePath);
				// handle the result appropriately
				RequestDispatcher RequetsDispatcherObj;
				switch (result) {
					case 0:     // success
						System.out.println("Register successful for email: " + email);
						// "login the user" or toast-notify him and prompt him to log in from the welcome page
						// ...	
						// TEMP: for now just reload the same page
						response.sendRedirect("/TEDProject/");  // this clears all input form data though (!) -  use AJAX instead?
						break;
					case -1:     // password mismatch
						request.setAttribute("errorType", "notMatchingPasswords");
						RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
						RequetsDispatcherObj.forward(request, response);
						break;
					case -2:      // email already taken
						request.setAttribute("errorType", "emailTaken");
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
		} 
		else {      // Log In
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			// First try to login as an admin
			int result = SiteFunctionality.LogIn(email, password);
			if ( result == -1 ) {                // successful Administrator LogIn
				System.out.println("Admin login successful for email: " + email);
	            // generate a new admin session
	            HttpSession newSession = request.getSession(true);
	            newSession.setAttribute("ProfID", -1);
	            newSession.setAttribute("isAdmin", true);
	            // set session to expire
	            newSession.setMaxInactiveInterval(timeout_interval);
				// forward HTTP POST to logged in page for administrators
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/AdminPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			} else if ( result == -2 ) {         // incorrect password for Administrator
				request.setAttribute("errorType", "invalidLoginPassword");
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			} else if ( result == -3 ) {         // unregistered email
				request.setAttribute("errorType", "invalidLoginEmail");
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			} else if ( result == -4 ) {         // incorrect password for Professional
				request.setAttribute("errorType", "invalidLoginPassword");
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			} else if ( result >= 0 ) {          // successful Professional login
				System.out.println("Login successful for email: " + email);
	            // generate a new prof session
	            HttpSession newSession = request.getSession(true);
	            newSession.setAttribute("ProfID", result);
	            newSession.setAttribute("isAdmin", false);
	            // set session to expire
	            newSession.setMaxInactiveInterval(timeout_interval);
	            // redirect to the correct uri
	            response.sendRedirect("/TEDProject/prof/NavigationServlet?page=HomePage");
	            // OLD WAY: forward professional to the respective jsp
				//RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/HomePage.jsp");
				//RequetsDispatcherObj.forward(request, response);
			
			} else {     // SHOULD NOT HAPPEN
				request.setAttribute("errorType", "???");
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			}
			
		}
	}

}

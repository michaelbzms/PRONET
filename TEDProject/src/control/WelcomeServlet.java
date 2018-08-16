package control;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Professional;
import model.Administrator;


/**
 * Servlet implementation class WelcomeServlet
 */
@WebServlet("/WelcomeServlet")
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WelcomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("errorType", "invalidPageRequest");
		RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
		RequetsDispatcherObj.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			else {
				// call register function from the model
				int result = Professional.register(email, password, re_password, firstName, lastName, phone, profilePicFilePath);
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
					case 1:     // password mismatch
						request.setAttribute("errorType", "notMatchingPasswords");
						RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
						RequetsDispatcherObj.forward(request, response);
						break;
					case 2:      // email already taken
						request.setAttribute("errorType", "emailTaken");
						RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
						RequetsDispatcherObj.forward(request, response);
						break;
				}
			}
		} 
		else {      // Log In
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			// First try to login as an admin
			int result = Administrator.login(email, password);
			if ( result == 0 ) {
				System.out.println("Login successful for email: " + email);
				// forward HTTP POST to logged in page for administrators
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/AdminPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			} 
			else if ( result == 2 ) {                   // if email existed on Administrators table then it cannot exist on Professional's table
				request.setAttribute("errorType", "invalidLoginPassword");
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			}
			else {  // If that fails because the email doesn't exist on Administrator's table then try to login as a professional
				int resultID = Professional.login(email, password);
				if ( resultID >= 0 ) {   // successful login
					System.out.println("Login successful for email: " + email);
					// redirect to logged in page for professional
					// forward HTTP POST to logged in page for administrators
					request.setAttribute("ProfID", Integer.toString(resultID));
					// Invalidate old session, if there is one
		            HttpSession oldSession = request.getSession(false);
		            if (oldSession != null) {
		                oldSession.invalidate();
		            }
		            // generate a new session
		            HttpSession newSession = request.getSession(true);
		            newSession.setAttribute("ProfID", resultID);
		            // set session to expire in 5'
		            newSession.setMaxInactiveInterval(5*60);
//		            Cookie profIDCookie = new Cookie("ProfID", Integer.toString(resultID));
//		            response.addCookie(profIDCookie);
					RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/HomePage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else if ( resultID == -1 ) {                    // unsuccessful login
					request.setAttribute("errorType", "invalidLoginEmail");
					RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else if ( resultID == -2 ) {
					request.setAttribute("errorType", "invalidLoginPassword");
					RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else {     // SHOULD NOT HAPPEN
					request.setAttribute("errorType", "???");
					RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				}
			}
		}
	}

}

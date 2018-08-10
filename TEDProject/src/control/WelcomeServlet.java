package control;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		// if got an HTTP GET request for this servlet's URI then redirect to index.html (home page)
		response.sendRedirect("");
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
				// handle the result appropriatelly
				RequestDispatcher RequetsDispatcherObj;
				switch (result) {
					case 0:     // success
						System.out.println("Register successful for email: " + email);
						// "login the user" or toast-notify him and promt him to log in from the welcome page
						// ...	
						// TEMP: for now just reload the same page
						response.sendRedirect("/TEDProject/");  // this clears all input form data though (!) -  use AJAX instead?
						break;
					case 1:     // password mismatch
						request.setAttribute("errorType", "notMatchingPasswords");
						RequetsDispatcherObj =request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
						RequetsDispatcherObj.forward(request, response);
						break;
					case 2:      // email already taken
						request.setAttribute("errorType", "emailTaken");
						RequetsDispatcherObj =request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
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
				// redirect to logged in page for administrator
				// TEMP: for now just reload the same page
				response.sendRedirect("/TEDProject/");  // this clears all input form data though (!) -  use AJAX instead?
			} 
			else if ( result == 2 ) {                   // if email existed on Administrators table then it cannot exist on Professional's table
				request.setAttribute("errorType", "invalidLoginPassword");
				RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			}
			else {  // If that fails because the email doesn't exist on Administrator's table then try to login as a professional
				result = Professional.login(email, password);
				if ( result == 0 ) {   // successful login
					System.out.println("Login successful for email: " + email);
					// redirect to logged in page for professional
					// TEMP: for now just reload the same page
					response.sendRedirect("/TEDProject/");  // this clears all input form data though (!) -  use AJAX instead?
				} else if ( result == 1 ) {                    // unsuccessful login
					request.setAttribute("errorType", "invalidLoginEmail");
					RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else if ( result == 2 ) {
					request.setAttribute("errorType", "invalidLoginPassword");
					RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else {     // SHOULD NOT HAPPEN
					request.setAttribute("errorType", "???");
					RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				}
			}
		}
	}

}

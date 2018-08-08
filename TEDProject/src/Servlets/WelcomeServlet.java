package Servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		if ( request.getParameter("register").equals("true") ) {   // Registration
			String email, password, re_password, firstName, lastName, phone;
			// fetch passwords and check if passwords match 
			password = request.getParameter("password");
			re_password = request.getParameter("repeat_password");
			if ( !password.equals(re_password) ) {
				request.setAttribute("errorType", "notMatchingPasswords");
				RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			} else {
				// fetch email and check if email is already taken
				email = request.getParameter("email");
				// check with the database
				if ( /* email is already taken */ false ) {
					request.setAttribute("errorType", "emailTaken");
					RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} 
				else {       // registration successful
					// fetch the other inputs
					firstName = request.getParameter("firstname");
					lastName = request.getParameter("lastname");
					phone = request.getParameter("phone_number");
					
					// insert a corresponding Professional record in the database
					// ...
					
					// "login the user" or toast-notify him and promt him to log in from the welcome page
					// ...
					
					// TEMP: for now just reload the same page
					response.sendRedirect("/TEDProject/");  // this clears all input form data though (!)
				}
			}
		} else {    // Log In
			String email, password;
			email = request.getParameter("email");
			password = request.getParameter("password");
			// check the data base for: 1. if that email exists, 2. if the password is correct
			//...
			if ( /*Exists*/ false ) {   // successful login

				// redirect to logged in page for user

			} else {                    // unsuccessful login
				request.setAttribute("errorType", "invalidLogin");
				RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			}
		}
	}

}

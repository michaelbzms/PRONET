package control;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.professional;

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
			// fetch everything from the form
			String email, password, re_password, firstName, lastName, phone, profilePicFilePath;
			password = request.getParameter("password");
			re_password = request.getParameter("repeat_password");
			email = request.getParameter("email");
			firstName = request.getParameter("firstname");
			lastName = request.getParameter("lastname");
			phone = request.getParameter("phone_number");
			profilePicFilePath = "C:/defaultpicpath/defaultpic.jpeg";    // example
			// call register function from the model
			int result = professional.register(email, password, re_password, firstName, lastName, phone, profilePicFilePath);
			// handle the result appropriatelly
			RequestDispatcher RequetsDispatcherObj;
			switch (result) {
				case 0:     // success
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
		else {      // Log In
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			boolean success = professional.login(email, password);
			if ( success ) {   // successful login

				// redirect to logged in page for user

			} else {                    // unsuccessful login
				request.setAttribute("errorType", "invalidLogin");
				RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			}
		}
	}

}

package control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import model.PropertiesManager;
import model.SiteFunctionality;


@WebServlet("/WelcomeServlet")
@MultipartConfig(fileSizeThreshold = 1024*1024, maxFileSize = 25*1024*1024)    // this location is only a temporary save location if we ran out of memory!
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UploadSaveDirectory = PropertiesManager.getProperty("saveDir");
	private File ProfileUploads = new File(UploadSaveDirectory + "/profile");
	
	
	private final int timeout_interval = Integer.parseInt(PropertiesManager.getProperty("sessionTimeoutMin")) * 60;
	
    
    public WelcomeServlet() {
        super();
        if (!ProfileUploads.exists()){
        	ProfileUploads.mkdirs();
        }
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("errorType", "invalidPageRequest");
		RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
		RequetsDispatcherObj.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Invalidate old session, if there is one
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
		// check input of form got via HTTP POST
		if ( request.getParameter("register") != null && request.getParameter("register").equals("true") ) {         // Registration
			// fetch everything from the form
			String email, password, re_password, firstName, lastName, phone, profilePicFileURL;
			password = request.getParameter("password");
			re_password = request.getParameter("repeat_password");
			email = request.getParameter("email");
			firstName = request.getParameter("firstname");
			lastName = request.getParameter("lastname");
			phone = request.getParameter("phone_number");
			// get image Part if it exists
			Part filePart = request.getPart("profile_picture"); // Retrieves <input type="file" name="profile_picture">
			if (filePart == null) {
				request.setAttribute("errorType", "TroubleFetchingUploadedImage");
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
				return;
			}
			profilePicFileURL = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();     // get the path of the user's input file (MSIE fix)
			// check if any field was empty:
			if ( email.isEmpty() || password.isEmpty() || re_password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() ) {   // (!) Profile picture is optional
				System.err.println("Registration form submitted but has unfilled fields. Ignored.");
				response.sendRedirect("/TEDProject/");
			}
			else if ( !SiteFunctionality.checkInputText(email, true, 255) || !SiteFunctionality.checkInputText(password, true, 32) || !SiteFunctionality.checkInputText(re_password, true, 32) 
				   || !SiteFunctionality.checkInputText(firstName, true, 45) || !SiteFunctionality.checkInputText(lastName, true, 45) || !SiteFunctionality.checkInputNumber(phone, 32, false) ) {
				System.err.println("Registration form submitted but one or more fields have illegal input characters (or too long).");
				request.setAttribute("errorType", "illegalTextInput");
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			}
			else {
				// Figure out under which file name his uploaded profile picture SHOULD be saved - if it exists, else use the default -
				boolean should_save_image = false;
				String unique_name = "", extension = "", diskFilePath = "";
				String projectURL = PropertiesManager.getProperty("protocol") + "://" + PropertiesManager.getProperty("hostname") + ':' + PropertiesManager.getProperty("port") + "/TEDProject";
				if (profilePicFileURL.isEmpty()) {      // use default
					profilePicFileURL = projectURL + "/images/defaultProfilePic.png";
				} else {                                 // save image (part) to server's images under a unique name
					int i = profilePicFileURL.lastIndexOf('.');
					if (i > 0) { extension = profilePicFileURL.substring(i+1); }
					int min = 0, max = 999999; 
					File f;
					do {	
						unique_name = "img" + Integer.toString(ThreadLocalRandom.current().nextInt(min, max + 1)) ;
						f = new File(UploadSaveDirectory + "/profile/" + unique_name + "." + extension);
					} while (f.exists() && !f.isDirectory());          // assure it's unique
					diskFilePath = UploadSaveDirectory + "/profile/" + unique_name + "." + extension;
					profilePicFileURL = projectURL + "/FileServlet?file=" + unique_name + "." + extension + "&type=profile";
					should_save_image = true;
				}
				// call register function from the model
				int result = SiteFunctionality.Register(email, password, re_password, firstName, lastName, phone, profilePicFileURL);
				// handle the result appropriately
				RequestDispatcher RequetsDispatcherObj;
				switch (result) {
					case 0:     // success
						if (should_save_image) {
							File newfile = new File(ProfileUploads, unique_name + "." + extension);
							try (InputStream input = filePart.getInputStream()) {
							    Files.copy(input, newfile.toPath());
							    System.out.println("File " + diskFilePath + " saved to disk at registration!");
							} catch ( IOException e ) {
								System.err.println("Could not save file " + diskFilePath + " to disk during registration!");
							}		
						}
						System.out.println("Register successful for email: " + email);
						// forward user to index, informing him of registration's success
						response.sendRedirect("/TEDProject/index.html?alert=registrationSuccess");
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
					case -3:      // database problem
						request.setAttribute("errorType", "dbError");
						RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
						RequetsDispatcherObj.forward(request, response);		
					default:      // should not happen
						request.setAttribute("errorType", "invalid return code at registration");
						RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
						RequetsDispatcherObj.forward(request, response);
						break;
				}
			}
		} 
		else if ( request.getParameter("register") != null ) {      // Log In
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String tmzOffset = request.getParameter("tmzOffset");
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
	            // set timezone offset property
	            if (SiteFunctionality.checkInputNumber(tmzOffset, 5, true)) {
	            	PropertiesManager.setProperty("timezoneOffset", tmzOffset);
	            }
	            response.sendRedirect("/TEDProject/prof/NavigationServlet?page=HomePage");
			} else {     // SHOULD NOT HAPPEN
				request.setAttribute("errorType", "???");   // unknown error
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
				RequetsDispatcherObj.forward(request, response);
			}
		}
		else {    // register attribute not sent
			request.setAttribute("errorType", "invalidPageRequest");
			RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
		}
	}

}

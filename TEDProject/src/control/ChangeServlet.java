package control;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.DataBaseBridge;
import model.FileManager;
import model.Professional;
import model.PropertiesManager;
import model.SiteFunctionality;


@WebServlet("/prof/ChangeServlet")
@MultipartConfig(fileSizeThreshold = 1024*1024, maxFileSize = 25*1024*1024)
public class ChangeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UploadSaveDirectory = FileServlet.SaveDirectory;
	private File Uploads = new File(UploadSaveDirectory);
       
    public ChangeServlet() {
        super();
        if (!Uploads.exists()){
        	Uploads.mkdirs();
        }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String attr = request.getParameter("attr");
		RequestDispatcher RequetsDispatcherObj;
		if (attr == null) {
			request.setAttribute("errorType", "invalidPageRequest");
			RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
			return;
		}
		switch(attr) {
			case "email":
				String password, newEmail;
				password = request.getParameter("password");
				newEmail = request.getParameter("newEmail");
				if ( password == null || password.isEmpty() || newEmail == null || newEmail.isEmpty() ) {
					request.setAttribute("errorType", "emptyFormFields");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else if ( !SiteFunctionality.checkInputText(newEmail, true, 0) ) {
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
							response.sendRedirect("/TEDProject/prof/NavigationServlet?page=Settings&alert=emailChangeSuccess");
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
				if ( currentPassword == null || currentPassword.isEmpty() || newPassword == null || newPassword.isEmpty() || reNewPassword == null || reNewPassword.isEmpty() ) {
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
				} else if ( !SiteFunctionality.checkInputText(newPassword, true, 32) ) {
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
							response.sendRedirect("/TEDProject/prof/NavigationServlet?page=Settings&alert=passwordChangeSuccess");
							break;
						case -1:     // invalid current password
							request.setAttribute("errorType", "invalidCurrentPassword");
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
							RequetsDispatcherObj.forward(request, response);
							break;
						case -503:      // database error
						case -2:
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
			case "profile":				
				// Instead of carrying 10+ variables as function arguments, we use a temporary Professional instead
				Professional tempProf = new Professional();
				tempProf.setEmploymentStatus(request.getParameter("employmentStatus"));
				tempProf.setEmploymentInstitution(request.getParameter("employmentInstitution"));
				tempProf.setDescription(request.getParameter("description"));
				tempProf.setPhone(request.getParameter("phoneNumber"));
				tempProf.setProfExpVisibility(request.getParameter("profExpVisibility") != null);
				tempProf.setProfessionalExperience(request.getParameter("profExp"));
				tempProf.setEdBackgroundVisibility(request.getParameter("edBackgroundVisibility") != null);
				tempProf.setEducationBackground(request.getParameter("edBackground"));
				tempProf.setSkillsVisibility(request.getParameter("skillsVisibility") != null);
				tempProf.setSkills(request.getParameter("skills"));
				// get image Part if it exists
				Part filePart = request.getPart("profile_picture"); // Retrieves <input type="file" name="profile_picture">
				if (filePart == null) {
					request.setAttribute("errorType", "TroubleFetchingUploadedImage");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
					return;
				}
				String profilePicFilePath = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();   // get the path of the user's input file (MSIE fix)
				// Check input
				if (  !SiteFunctionality.checkInputText(tempProf.getEmploymentStatus(), true, 255)
				   || !SiteFunctionality.checkInputText(tempProf.getEmploymentInstitution(), true, 255)
				   || !SiteFunctionality.checkInputText(tempProf.getDescription(), false, 1024) 
				   || !SiteFunctionality.checkInputNumber(tempProf.getPhone(), 32, false) 
				   || !SiteFunctionality.checkInputText(tempProf.getProfessionalExperience(), false, 0) 
				   || !SiteFunctionality.checkInputText(tempProf.getEducationBackground(), false, 0) 
				   || !SiteFunctionality.checkInputText(tempProf.getSkills(), false, 0) ) {
					System.out.println("Form to change profile submitted but one or more fields have illegal (or too big) input characters.");
					request.setAttribute("errorType", "illegalTextInput");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else {
					int profID = (int) request.getSession(false).getAttribute("ProfID");
					// update Profile Picture if changed
					if ( !profilePicFilePath.isEmpty() ) {                       // if professional submitted a new profile picture 
						boolean success = FileManager.editProfilePicture(tempProf, filePart, UploadSaveDirectory, profilePicFilePath, profID);
						if (!success) {        // should not happen
							request.setAttribute("errorType", "EditingProfilePictureError");    // this page doesn't exist and does not need to exist -> unknown error is ok
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
							RequetsDispatcherObj.forward(request, response);
							return;
						}
					} else {      // else leave the profilePicFilePath as is (== reset it to the same value)
						DataBaseBridge db = new DataBaseBridge();
						Professional prof = db.getBasicProfessionalInfo(profID);
						db.close();
						tempProf.setProfilePicURI(prof.getProfilePicURI());
					}
					int result = SiteFunctionality.EditProfile(profID, tempProf);
					switch (result) {
						case 0:     // success
							System.out.println("Profile updated successfully");
							response.sendRedirect("/TEDProject/ProfileLink?alert=editSuccess");
							break;
						case -503:      // database error
						case -1:
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
			case "deleteAccount":
				String pass;
				pass = request.getParameter("password");
				if ( pass == null || pass.isEmpty() ) {
					request.setAttribute("errorType", "emptyFormFields");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
				} else {
					int profID = (int) request.getSession(false).getAttribute("ProfID");
					int result = SiteFunctionality.DeleteAccount(profID, pass);
					switch (result) {
						case 0:     // success
							System.out.println("Account with ID " + profID + " deleted successfully");
							// forward user to index, informing him of registration's success
							response.sendRedirect("/TEDProject/index.html?alert=accountDeletionSuccess");
							break;
						case -1:     // invalid current password
							request.setAttribute("errorType", "invalidCurrentPassword");
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
							RequetsDispatcherObj.forward(request, response);
							break;
						case -503:      // database error
						case -2:
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
		doGet(request, response);
	}

}

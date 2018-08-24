package control;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import model.SiteFunctionality;


@WebServlet("/ChangeServlet")
@MultipartConfig(location = "D:/eclipse-workspace/TEDProject/WebContent/images", fileSizeThreshold = 1024*1024, maxFileSize = 25*1024*1024)      // this location is only a temporary save location in case we ran out of memory
public class ChangeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UploadSaveDirectory = FileServlet.SaveDirectory;   // CONFIG: hardcoded here and in the annotation above
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
				if ( password.isEmpty() || newEmail.isEmpty() ) {
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
				} else if ( !SiteFunctionality.checkInputText(newPassword, true, 128) ) {
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
			case "profile":				
				// Instead of carrying 10+ variables as function arguments, we use a temporary Professional instead
				Professional tempProf = new Professional();
				tempProf.setEmploymentStatus(request.getParameter("employmentStatus"));
				tempProf.setEmploymentInstitution(request.getParameter("employmentInstitution"));
				tempProf.setDescription(request.getParameter("description"));
				tempProf.setPhone(request.getParameter("phoneNumber"));
				tempProf.setProfExpVisibility(request.getParameter("profExpVisibility") != null);
				tempProf.setProfessionalExperience(request.getParameter("profExp").replace("`", "\\`"));
				tempProf.setEdBackgroundVisibility(request.getParameter("edBackgroundVisibility") != null);
				tempProf.setEducationBackground(request.getParameter("edBackground").replace("`", "\\`"));
				tempProf.setSkillsVisibility(request.getParameter("skillsVisibility") != null);
				tempProf.setSkills(request.getParameter("skills").replace("`", "\\`"));
				// get image Part if it exists
				Part filePart = request.getPart("profile_picture"); // Retrieves <input type="file" name="profile_picture">
				if (filePart == null) {
					request.setAttribute("errorType", "TroubleFetchingUploadedImage");
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
					RequetsDispatcherObj.forward(request, response);
					return;
				}
				String profilePicFilePath = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();   // get the path of the user's input file (MSIE fix)
				// Chenk input
				if (  !SiteFunctionality.checkInputText(tempProf.getEmploymentStatus(), true, 255)
				   || !SiteFunctionality.checkInputText(tempProf.getEmploymentInstitution(), true, 255)
				   || !SiteFunctionality.checkInputText(tempProf.getDescription(), false, 4096) 
				   || !SiteFunctionality.checkInputNumber(tempProf.getPhone(), 32) 
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
							request.setAttribute("errorType", "EditingProfilePictureError");    // should not happen. TODO: make such a page?
							RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
							RequetsDispatcherObj.forward(request, response);
							return;
						}
					} else {      // else leave the profilePicFilePath as is (== reset it to the same value)
						DataBaseBridge db = new DataBaseBridge();
						Professional prof = db.getProfessional(profID);
						db.close();
						tempProf.setProfile_pic_file_path(prof.getProfile_pic_file_path());
					}
					int result = SiteFunctionality.EditProfile(profID, tempProf);
					switch (result) {
						case 0:     // success
							System.out.println("Profile updated successfully");
							// notify user for success and reload settings page	
							response.sendRedirect("/TEDProject/ProfileLink");
							break;
						case -1:      // database error
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

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
import model.Professional;
import model.SiteFunctionality;


@WebServlet("/ChangeServlet")
@MultipartConfig(location = "D:/eclipse-workspace/TEDProject/WebContent/images", fileSizeThreshold = 1024*1024, maxFileSize = 25*1024*1024)
public class ChangeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String MultipartConfigLocation = "D:/eclipse-workspace/TEDProject/WebContent/images";   // CONFIG: hardcoded here and in the annotation above

       
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
				String profilePicFilePath = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix
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
					////////////////////// UNDER TESTING ///////////////////////
					// update Profile Picture
					boolean profilePicOk = true;
					if ( !profilePicFilePath.isEmpty() ) {         // if professional submitted a new profile picture 
						DataBaseBridge db = new DataBaseBridge();
						Professional prof = db.getProfessional(profID);
						db.close();
						if ( prof.getProfile_pic_file_path() == null || prof.getProfile_pic_file_path().equals("http://localhost:8080/TEDProject/images/defaultProfilePic.png") ) {
							// if previous picture was the default picture then must choose a new unique name and save the picture under it
							String unique_name = "", extension = "";
							int i = profilePicFilePath.lastIndexOf('.');
							if (i > 0) { extension = profilePicFilePath.substring(i+1); }
							int min = 0, max = 999999; 
							File f;
							do {	
								unique_name = "img" + Integer.toString(ThreadLocalRandom.current().nextInt(min, max + 1)) ;
								f = new File(MultipartConfigLocation + "/" + unique_name + "." + extension);
							} while (f.exists() && !f.isDirectory());          // assure it's unique
							// update tempProf to reflect changes. Later he will be flushed to the database
							tempProf.setProfile_pic_file_path("http://localhost:8080/TEDProject/images/" + unique_name + "." + extension);
							try {
				    			filePart.write(unique_name + "." + extension);
				    		} catch (IOException x) {
				    			System.err.println("Could not save new profile picture!");
				    		}
						} else {
							// else if a picture already existed first delete the old profile picture file
							Path filepath = FileSystems.getDefault().getPath(MultipartConfigLocation, prof.getProfile_pic_name());
				    		try {
				    		    Files.delete(filepath);
				    		} catch (NoSuchFileException x) {
				    		    System.err.format("Tried to delete %s but no such file or directory%n", filepath);
				    		} catch (IOException x) {    // File permission problems are caught here.
				    		    System.err.println("Do not have permission to delete previous profile picture!");
				    		}
				    		// and then save the new one under the same name, so tempProf.profile_picture_file_path shall remain the same as prof's
				    		tempProf.setProfile_pic_file_path(prof.getProfile_pic_file_path());
				    		try {
				    			filePart.write(prof.getProfile_pic_name());
				    		} catch (IOException x) {
				    			System.err.println("Could not save new profile picture (with old name)!");
				    		}
						}
					}
					if (!profilePicOk) {   // should not happen
						request.setAttribute("errorType", "ChangeProfilePicError");   //TODO: make such error page?
						RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
						RequetsDispatcherObj.forward(request, response);
					}
					///////////////////////////////////////////////
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

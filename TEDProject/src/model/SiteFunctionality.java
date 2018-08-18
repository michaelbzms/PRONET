package model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class SiteFunctionality {
	
	public static int LogIn(String email, String password) {              // as Administrator or Professional
		DataBaseBridge dbg = new DataBaseBridge();                        // create a connection to the database
		// First try to login as an administrator
		Administrator admin = dbg.recoverAdministratorRecord(email);
		if ( admin != null && !admin.getPassword().equals(password)) {    // email exists as administrator but password mismatch!
			dbg.close();               // close connection to the database
			return -2;
		}
		else if ( admin == null ) {    // email does not exists as administrator
			// Then try to login as a professional
			Professional prof = dbg.recoverProfessionalRecord(email);
			dbg.close();               // close connection to the database
			if (prof == null) {        // email does not exist in the database (on neither Admins nor Professionals)
				return -3;
			}
			else if (!prof.getPassword().equals(password)) {             // email exists as Professional but password mismatch!
				return -4;
			}  // else
			return prof.getID();
		} else {                       // successful administrator LogIn
			dbg.close();               // close connection to the database
			return -1;
		}
	}
	
	public static int Register(String email, String password, String re_password, String firstName, String lastName, String phone, String profilePicFilePath) {     // only registers Professionals
		if ( !password.equals(re_password) ) {
			return -1;       // code for mismatching passwords
		} else {	
			DataBaseBridge dbg = new DataBaseBridge();     // create a connection to the database
			Professional prof = dbg.recoverProfessionalRecord(email);   // check email with the database
			if ( prof != null ) {                          // email is already taken
				dbg.close();                               // close connection to the database
				return -2;   // code for email taken
			} 
			else {           // registration successful					
				// insert a corresponding Professional record in the database
				dbg.registerNewProfessional(email, password, firstName, lastName, phone, profilePicFilePath);	
			}
			dbg.close();     // close connection to the database
		}
		return 0;
	}
	
	public static boolean checkInputText(String input, boolean allowExtras, boolean oneLiner, int sizeRestriction) {    // check for special characters, etc that make a text input unnacceptable
		if (sizeRestriction > 0 && input.length() > sizeRestriction) {      // input must not be longer than this
			return false;
		}
		for (int i = 0, n = input.length(); i < n; i++) {
		    char c = input.charAt(i);                                        // Each character MUST be:
		    if ( !(  (c > 'a' && c < 'z') || (c > 'A' && c < 'Z')            // a latin letter
		    	  || (c > '0' && c < '9')                                    // or a number
		    	  || c == ' ' || c == '\t'                                   // or white space
		    	  || (!oneLiner && (c == '\r' || c == '\n'))                 // or, if we allow it, new line
		    	  || (allowExtras && (c == '@' || c == '.' || c == '!'       // or, if we allow it, extra symbols such as '.'. '@', '!', etc
		    	                   || c == '?' || c == ':' || c == ';'
		    	                   || c == '~' || c == '/' || c == '\\') )                 
		        ) ) {
		    	return false;                                                // if a character does not abide by the above then return false
		    }
		}
		return true;
	}
	
	public static boolean checkInputNumber(String input, int sizeRestriction) {
		if (sizeRestriction > 0 && input.length() > sizeRestriction) {      // input must not be longer than this
			return false;
		}
		for (int i = 0, n = input.length(); i < n; i++) {
		    char c = input.charAt(i);                                        // Each character MUST be:
		    if ( !(c > '0' && c < '9') ) {
		    	return false;                                                // if a character does not abide by the above then return false
		    }
		}
		return true;
	}
	
	public static Professional acquireProfFromSession(DataBaseBridge db, HttpServletRequest request) {
		int LoggedProfID = -1;
		Professional loggedProf;
		HttpSession currentSession = request.getSession(false);
    	if (currentSession != null) {
    		LoggedProfID = (int) currentSession.getAttribute("ProfID");
    		if (LoggedProfID < 0) {
    			System.out.println("Failed to retrieve ProfID.");
    			return null;
    		}
    		loggedProf = db.getProfessional(LoggedProfID);
    		if (loggedProf == null) {
    			System.out.println("Failed to retrieve Professional.");
    			return null;
    		} 		
    	} else {
    		// It should never get here due to filter
    		return null;
    	}
    	return loggedProf;
	}
	
	public static int ChangeEmail(int profID, String password, String new_email) {
		DataBaseBridge dbg = new DataBaseBridge();     // create a connection to the database
		String profPassword = dbg.getProfessionalPassword(profID);
		if ( !password.equals(profPassword) ) {			// invalid current password
			dbg.close();                               
			return -1;   
		}
		if ( dbg.recoverProfessionalRecord(new_email) != null ) {       // new_email is already taken
			dbg.close();                               
			return -2;   
		} 
		// Change email:
		if ( dbg.updateProfessionalEmail(profID, new_email) ) {		// Successful update
			dbg.close(); 
			return 0;	
		} else {				// database error
			dbg.close();
			return -3;
		}
	}
	
}

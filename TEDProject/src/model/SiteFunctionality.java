package model;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class SiteFunctionality {
	
	public static int LogIn(String email, String password) {              // as Administrator or Professional
		DataBaseBridge dbg = new DataBaseBridge();                        // create a connection to the database
		if ( !dbg.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
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
			if ( !dbg.checkIfConnected() ) {
				System.out.println("> Database error: database down");
				return -503;
			}
			Professional prof = dbg.recoverProfessionalRecord(email);   // check email with the database
			if ( prof != null ) {                          // email is already taken
				dbg.close();                               // close connection to the database
				return -2;   // code for email taken
			} 
			else {           // registration successful					
				// insert a corresponding Professional record in the database
				boolean success = dbg.registerNewProfessional(email, password, firstName, lastName, phone, profilePicFilePath);	
				if (!success) { dbg.close(); return -3; }
			}
			dbg.close();     // close connection to the database
		}
		return 0;
	}
	
	public static boolean checkInputText(String input, boolean oneLiner, int sizeRestriction) {    // check for special characters, etc that make a text input unnacceptable
		if (sizeRestriction > 0 && input.length() > sizeRestriction) {       // input must not be longer than this
			return false;
		}
		for (int i = 0, n = input.length(); i < n; i++) {
		    char c = input.charAt(i);                                       // Each character MUST be:
		    if ( c == '<' 		 	                                        // Do not allow tags on text input (HTML Injection?)
		         || (oneLiner && (c == '\r' || c == '\n'))                  // do not allow new line on text input unless oneLiner is false                 
		        ) {
		    	System.out.println("Found illegal: " + input);
		    	return false;
		    }
		}
		return true;
	}
	
	public static boolean checkInputNumber(String input, int sizeRestriction) {
		if (sizeRestriction > 0 && input.length() > sizeRestriction) {       // input must not be longer than this
			return false;
		}
		for (int i = 0, n = input.length(); i < n; i++) {
		    char c = input.charAt(i);                                        // Each character MUST be:
		    if ( !(c >= '0' && c <= '9') ) {
		    	return false;                                                // if a character does not abide by the above then return false
		    }
		}
		return true;
	}
		
	public static Professional acquireProfFromSession(DataBaseBridge db, HttpServletRequest request) {
		if ( !db.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return null;
		}
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

	public static int ChangeEmail(int profID, String currentPassword, String newEmail) {
		DataBaseBridge dbg = new DataBaseBridge();     // create a connection to the database
		if ( !dbg.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		String profPassword = dbg.getProfessionalPassword(profID);
		if ( !currentPassword.equals(profPassword) ) {			// invalid current password
			dbg.close();                               
			return -1;   
		}
		if ( dbg.recoverProfessionalRecord(newEmail) != null ) {       // new_email is already taken
			dbg.close();                               
			return -2;   
		} 
		// Change email:
		if ( dbg.updateProfessionalEmail(profID, newEmail) ) {		// Successful update
			dbg.close(); 
			return 0;	
		} else {				// database error
			dbg.close(); 
			return -3;
		}
	}
	
	public static int ChangePassword(int profID, String currentPassword, String newPassword) {
		DataBaseBridge dbg = new DataBaseBridge();              // create a connection to the database
		if ( !dbg.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		String profPassword = dbg.getProfessionalPassword(profID);
		if ( !currentPassword.equals(profPassword) ) {			// invalid current password
			dbg.close();                               
			return -1;   
		}
		// Change password:
		if ( dbg.updateProfessionalPassword(profID, newPassword) ) {		// Successful update
			dbg.close(); 
			return 0;	
		} else {				// database error
			dbg.close();
			return -2;
		}
	}
	
	public static int updateConnectionRequest(int AskerID, int ReceiverID, boolean decision) {
		DataBaseBridge db = new DataBaseBridge();              // create a connection to the database
		if ( !db.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		boolean success;
		if (decision) {        // if receiver accepted the asker's request to connect
			success = db.addConnectedProfessionals(AskerID, ReceiverID);
			if (!success) { System.out.println("> Database error: Server could not connect professionals!"); }   // should not happen
		}
		// remove connection request as a request in the data base
		success = db.removeConnectionRequest(AskerID, ReceiverID);
		if (!success) { System.out.println("> Database error: Server could not remove connection request!"); }   // should not happen	
		db.close();
		return 0;
	}
	
	public static int EditProfile(int profID, Professional tempProf) {
		DataBaseBridge dbg = new DataBaseBridge();     // create a connection to the database
		if ( !dbg.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		if ( dbg.updateProfessionalProfile(profID, tempProf) ) {		// Successful update
			dbg.close(); 
			return 0;	
		} else {				// database error
			dbg.close(); 
			return -1;
		}
	}
	
	public static int sendConnectionRequest(int askerID, int receiverID) {
		DataBaseBridge dbg = new DataBaseBridge();              // create a connection to the database
		if ( !dbg.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		if ( dbg.createConnectionRequest(askerID, receiverID) ) {		// Successful update
			dbg.close(); 
			return 0;	
		} else {				// database error
			dbg.close(); 
			return -1;
		}
	}
	
	public static int removeConnection(int profID1, int profID2) {
		DataBaseBridge dbg = new DataBaseBridge();              // create a connection to the database
		if ( !dbg.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		if ( dbg.deleteConnection(profID1, profID2) ) {		// Successful update
			dbg.close(); 
			return 0;	
		} else {				// database error
			dbg.close(); 
			return -1;
		}
	}
	
	public static int createWorkAd(int profID, String title, String description) {
		DataBaseBridge db = new DataBaseBridge();              // create a connection to the database
		if ( !db.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		if ( db.createWorkAd(profID, title, description) ) {		// Successful update
			db.close(); 
			return 0;	
		} else {				// database error
			db.close(); 
			return -1;
		}
	}
	
	public static int updateWorkAd(int adID, int profID, String description) {
		DataBaseBridge db = new DataBaseBridge();              // create a connection to the database
		if ( !db.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		// Check if profID is the owner of the ad
		if ( db.getWorkAdPublishedByID(adID) != profID ) {
			return -4;
		}
		if ( db.updateWorkAd(adID, description) ) {		// Successful update
			db.close(); 
			return 0;	
		} else {				// database error
			db.close(); 
			return -1;
		}
	}
	
	public static int removeWorkAd(int adID, int profID) {
		DataBaseBridge dbg = new DataBaseBridge();              // create a connection to the database
		if ( !dbg.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		// Check if profID is the owner of the ad
		if ( dbg.getWorkAdPublishedByID(adID) != profID ) {
			return -4;
		}
		if ( dbg.deleteWorkAd(adID) ) {		// Successful update
			dbg.close(); 
			return 0;	
		} else {				// database error
			dbg.close(); 
			return -1;
		}
	}
	
	public static int applyToWorkAd(int adID, int profID, String applyNote) {
		DataBaseBridge dbg = new DataBaseBridge();              // create a connection to the database
		if ( !dbg.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		// The owner of the ad cannot apply to it
		if ( dbg.getWorkAdPublishedByID(adID) == profID ) {
			return -4;
		}
		if ( dbg.createApplication(adID, profID, applyNote) ) {		// Successful update
			dbg.close(); 
			return 0;	
		} else {				// database error
			dbg.close(); 
			return -1;
		}
	}

	public static int addMessage(String text, int sentById, int sentToId, boolean containsFiles) {
		DataBaseBridge db = new DataBaseBridge();
		if ( !db.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		int result = db.existsConversationBetween(sentById, sentToId);
		switch (result) {
			case 0:   // conversation does not exist
				db.createConversationInOrder(sentById, sentToId);
				db.addMessageToConversationInOrder(sentById, sentToId, sentById, text, containsFiles);
				break;
			case 1:   // conversation exists with order (sentById, sentToId)
				db.addMessageToConversationInOrder(sentById, sentToId, sentById, text, containsFiles);
				db.updateLastSentToConversation(sentById, sentToId);
				break;
			case 2:   // conversation exists with order (sentToId, sentById)
				db.addMessageToConversationInOrder(sentToId, sentById, sentById, text, containsFiles);
				db.updateLastSentToConversation(sentToId, sentById);
				break;
			default:  // should not happen
				return -1;
		}
		db.close();
		return 0;
	}
	
	public static int removeApplication(int profID, int adID) {
		DataBaseBridge dbg = new DataBaseBridge();              // create a connection to the database
		if ( !dbg.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return -503;
		}
		if ( dbg.deleteApplication(profID, adID) ) {		// Successful update
			dbg.close(); 
			return 0;	
		} else {				// database error
			dbg.close(); 
			return -1;
		}
	}
	
	public static XMLProfessional createXMLprof(int profID) {
		DataBaseBridge db = new DataBaseBridge();
		if ( !db.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return null;
		}
		XMLProfessional xmlProf = new XMLProfessional();
		xmlProf.setProf(db.getProfessional(profID));
		xmlProf.setProfArticles(db.getProfArticles(profID));
		xmlProf.setProfWorkAds(db.getWorkAds(profID, 0));
		// + more
		return xmlProf;
	}
	
	public static XMLProfessionalList createXMLprofList(Integer profIDs[]) {
		DataBaseBridge db = new DataBaseBridge();
		if ( !db.checkIfConnected() ) {
			System.out.println("> Database error: database down");
			return null;
		}
		List<XMLProfessional> profList = new ArrayList<XMLProfessional>();
		XMLProfessional xmlProf;
		for (int profID : profIDs) {
			xmlProf = new XMLProfessional();
			xmlProf.setProf(db.getProfessional(profID));
			xmlProf.setProfArticles(db.getProfArticles(profID));
			xmlProf.setProfWorkAds(db.getWorkAds(profID, 0));
			// + more
			profList.add(xmlProf);
		}
		XMLProfessionalList xmlProfList = new XMLProfessionalList();
		xmlProfList.setProfList(profList);
		return xmlProfList;
	}
	
}

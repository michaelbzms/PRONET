package model;

import javax.servlet.RequestDispatcher;

import model.DataBaseBridge;

public class professional {
	
	/* Characteristics */
	public String ID;   // its database ID
	public String email, password, firstName, lastName, phone, profile_pic_file_path;
	
	/* Static functions */
	public static boolean login(String email, String password) {
		// check the data base for: 1. if that email exists, 2. if the password is correct
		DataBaseBridge dbg = new DataBaseBridge();     // create a connection to the database
		professional prof = dbg.recoverProfessionalRecord(email);
		dbg.close();           // close connection to the database
		if (prof == null) {    // email does not exist in the database
			return false;
		}
		else if (!prof.password.equals(password)) {    // password mismatch
			return false;
		}  // else
		return true;
	}
	
	public static int register(String email, String password, String re_password, String firstName, String lastName, String phone, String profilePicFilePath) {
		if ( !password.equals(re_password) ) {
			return 1;       // code for mismatching passwords
		} else {	
			DataBaseBridge dbg = new DataBaseBridge();     // create a connection to the database
			professional prof = dbg.recoverProfessionalRecord(email);   // check email with the database
			if ( prof != null ) {                          // email is already taken
				dbg.close();                               // close connection to the database
				return 2;   // code for email taken
			} 
			else {          // registration successful					
				// insert a corresponding Professional record in the database
				dbg.registerNewProfessional(email, password, firstName, lastName, phone, profilePicFilePath);	
			}
			dbg.close();    // close connection to the database
		}
		return 0;           // code for successful registration
	}
	
	/* Behaviour */
	public professional() {
		
	}
	
	
}

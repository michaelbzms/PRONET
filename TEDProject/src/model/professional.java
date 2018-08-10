package model;

import model.DataBaseBridge;

public class Professional {
	
	/* Static functions */
	public static int login(String email, String password) {
		// check the data base for: 1. if that email exists, 2. if the password is correct
		DataBaseBridge dbg = new DataBaseBridge();     // create a connection to the database
		Professional prof = dbg.recoverProfessionalRecord(email);
		dbg.close();           // close connection to the database
		if (prof == null) {    // email does not exist in the database
			return 1;
		}
		else if (!prof.password.equals(password)) {    // password mismatch
			return 2;
		}  // else
		return 0;
	}
	
	public static int register(String email, String password, String re_password, String firstName, String lastName, String phone, String profilePicFilePath) {
		if ( !password.equals(re_password) ) {
			return 1;       // code for mismatching passwords
		} else {	
			DataBaseBridge dbg = new DataBaseBridge();     // create a connection to the database
			Professional prof = dbg.recoverProfessionalRecord(email);   // check email with the database
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
	
	//////////////////////////////////////////////////////////////////////////////
	
	/* Characteristics */
	public String ID;   // its database ID
	public String email, password, firstName, lastName, phone, profile_pic_file_path;
	
	/* Methods */
	public Professional() {
		
	}
	
	
}

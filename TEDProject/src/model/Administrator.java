package model;

public class Administrator {
	
	/* Static Functions */
	public static int login(String email, String password) {
		// check the data base for: 1. if that email exists, 2. if the password is correct
		DataBaseBridge dbg = new DataBaseBridge();     // create a connection to the database
		Administrator admin = dbg.recoverAdministratorRecord(email);
		dbg.close();           // close connection to the database
		if (admin == null) {    // email does not exist in the database
			return 1;
		}
		else if (!admin.password.equals(password)) {    // password mismatch
			return 2;
		}  // else
		return 0;
	}
	
	//////////////////////////////////////////////////////////////////////////////

	/* Characteristics */
	public String ID;   // its database ID
	public String email, password, firstName, lastName;
	
	/* Methods */
	public Administrator() {
		
	}
	
}

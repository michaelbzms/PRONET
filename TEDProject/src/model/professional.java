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
			return -1;
		}
		else if (!prof.password.equals(password)) {    // password mismatch
			return -2;
		}  // else
		return prof.ID;
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
	private int ID;   // its database ID
	private String email, password, firstName, lastName, phone, profile_pic_file_path;
	private String employmentStatus = null, employmentInstitution = null; 
	private String professionalExperience = null, educationBackground = null, skills = null;
	private boolean profExpVisibility = true, edBackgroundVisibility = true, skillsVisibility = true;
	
	/* Methods */
	public Professional() {
		
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProfile_pic_file_path() {
		return profile_pic_file_path;
	}

	public void setProfile_pic_file_path(String profile_pic_file_path) {
		this.profile_pic_file_path = profile_pic_file_path;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public String getEmploymentInstitution() {
		return employmentInstitution;
	}

	public void setEmploymentInstitution(String employmentInstitution) {
		this.employmentInstitution = employmentInstitution;
	}

	public String getProfessionalExperience() {
		return professionalExperience;
	}

	public void setProfessionalExperience(String professionalExperience) {
		this.professionalExperience = professionalExperience;
	}

	public String getEducationBackground() {
		return educationBackground;
	}

	public void setEducationBackground(String educationBackground) {
		this.educationBackground = educationBackground;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public boolean isProfExpVisibility() {
		return profExpVisibility;
	}

	public void setProfExpVisibility(boolean profExpVisibility) {
		this.profExpVisibility = profExpVisibility;
	}

	public boolean isEdBackgroundVisibility() {
		return edBackgroundVisibility;
	}

	public void setEdBackgroundVisibility(boolean edBackgroundVisibility) {
		this.edBackgroundVisibility = edBackgroundVisibility;
	}

	public boolean isSkillsVisibility() {
		return skillsVisibility;
	}

	public void setSkillsVisibility(boolean skillsVisibility) {
		this.skillsVisibility = skillsVisibility;
	}
	
	
}

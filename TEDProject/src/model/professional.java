package model;


public class Professional {
	
	/* Characteristics */
	private int ID;   // its database ID
	private String email, password, firstName, lastName, phone, profile_pic_file_path;
	private String employmentStatus = null, employmentInstitution = null; 
	private String professionalExperience = null, educationBackground = null, skills = null;
	private boolean profExpVisibility = true, edBackgroundVisibility = true, skillsVisibility = true;
	
	/* Setters & Getters*/
	public int getID() { return ID; }
	public void setID(int iD) { ID = iD; }
	
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	public String getProfile_pic_file_path() { return profile_pic_file_path; }
	public void setProfile_pic_file_path(String profile_pic_file_path) { this.profile_pic_file_path = profile_pic_file_path; }

	public String getEmploymentStatus() { return employmentStatus; }
	public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

	public String getEmploymentInstitution() { return employmentInstitution; }
	public void setEmploymentInstitution(String employmentInstitution) { this.employmentInstitution = employmentInstitution; }

	public String getProfessionalExperience() { return professionalExperience; }
	public void setProfessionalExperience(String professionalExperience) { this.professionalExperience = professionalExperience; }

	public String getEducationBackground() { return educationBackground; }
	public void setEducationBackground(String educationBackground) { this.educationBackground = educationBackground; }

	public String getSkills() { return skills; }
	public void setSkills(String skills) { this.skills = skills; }

	public boolean isProfExpVisibility() { return profExpVisibility; }
	public void setProfExpVisibility(boolean profExpVisibility) { this.profExpVisibility = profExpVisibility; }

	public boolean isEdBackgroundVisibility() { return edBackgroundVisibility; }
	public void setEdBackgroundVisibility(boolean edBackgroundVisibility) { this.edBackgroundVisibility = edBackgroundVisibility; }

	public boolean isSkillsVisibility() { return skillsVisibility; }
	public void setSkillsVisibility(boolean skillsVisibility) { this.skillsVisibility = skillsVisibility; }

	
	/* Methods */
	public Professional() {
		
	}

	
	
	
}

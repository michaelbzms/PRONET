package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "profBio")
@XmlType(propOrder = {"firstName", "lastName", "email", "password", "phone", "description", "employmentStatus", "employmentInstitution", "professionalExperience", "educationBackground", "skills"})
public class Professional {
	
	/* Characteristics */
	private int ID = -1;          // its database ID
	private String email = null, password = null, firstName = null, lastName = null, phone = null, profilePicURI = null;
	private String employmentStatus = null, employmentInstitution = null, description = null; 
	private String professionalExperience = null, educationBackground = null, skills = null;
	private boolean profExpVisibility = true, edBackgroundVisibility = true, skillsVisibility = true;
	
	/* Setters & Getters*/
	@XmlAttribute
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
	
	@XmlTransient
	public String getProfilePicURI() { return profilePicURI; }
	public void setProfilePicURI(String profilePicURI) { this.profilePicURI = profilePicURI; }

	public String getEmploymentStatus() { return employmentStatus; }
	public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

	public String getEmploymentInstitution() { return employmentInstitution; }
	public void setEmploymentInstitution(String employmentInstitution) { this.employmentInstitution = employmentInstitution; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public String getProfessionalExperience() { return professionalExperience; }
	public void setProfessionalExperience(String professionalExperience) { this.professionalExperience = professionalExperience; }

	public String getEducationBackground() { return educationBackground; }
	public void setEducationBackground(String educationBackground) { this.educationBackground = educationBackground; }

	public String getSkills() { return skills; }
	public void setSkills(String skills) { this.skills = skills; }

	@XmlTransient
	public boolean getProfExpVisibility() { return profExpVisibility; }
	public void setProfExpVisibility(boolean profExpVisibility) { this.profExpVisibility = profExpVisibility; }

	@XmlTransient
	public boolean getEdBackgroundVisibility() { return edBackgroundVisibility; }
	public void setEdBackgroundVisibility(boolean edBackgroundVisibility) { this.edBackgroundVisibility = edBackgroundVisibility; }

	@XmlTransient
	public boolean getSkillsVisibility() { return skillsVisibility; }
	public void setSkillsVisibility(boolean skillsVisibility) { this.skillsVisibility = skillsVisibility; }

	
	/* Methods */
	public Professional() {
		
	}
	
	// Override '==' operator
	@Override public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
        if (!Professional.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Professional other = (Professional) obj;
		return (this.getID() == other.getID() && this.getID() != -1 && other.getID() != -1);
	}
	
	public String getProfile_pic_name(){            // not the most elegant function but it works as long as there is no other parameter ending in "file" (!)
		String profilePicUrlPath = getProfilePicURI(), name = "NOTFOUND";
		int i = profilePicUrlPath.lastIndexOf('?');
		if (i > 0) {
			i++;
			while( i + 4 < profilePicUrlPath.length() ) {   // repeatedly read the first five characters
				char c1 = profilePicUrlPath.charAt(i), 
					 c2 = profilePicUrlPath.charAt(i+1), 
					 c3 = profilePicUrlPath.charAt(i+2),
					 c4 = profilePicUrlPath.charAt(i+3),
					 c5 = profilePicUrlPath.charAt(i+4);
				if ( c1 == 'f' && c2 == 'i' && c3 == 'l' && c4 == 'e' && c5 == '=' ) {   // until reached "file=" or end of String
					i += 5;
					int start = i;
					while ( i < profilePicUrlPath.length() && profilePicUrlPath.charAt(i) != '&' && (profilePicUrlPath.charAt(i) != '/' || i < profilePicUrlPath.length() - 1)  ) i++;
					int end = i;
					name = profilePicUrlPath.substring(start, end);
					break;
				}
				i++;
			}
		}
		return name;
	}
}

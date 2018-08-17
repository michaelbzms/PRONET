package model;


public class Administrator {

	/* Characteristics */
	private int ID;   // its database ID
	private String email, password, firstName, lastName;
	
	/* Setters & Getters */
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

	/* Methods */
	public Administrator() {
		
	}
	
}

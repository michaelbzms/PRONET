package model;

// import JDBC packets
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 


public class DataBaseBridge {
	
	/* These fields are better hardcoded only here than all over the place on the caller's side */
	final private String database_url = "jdbc:mysql://localhost:3306/ted?serverTimezone=UTC";   // not using SSL yet
	final private String user = "root";
	final private String password = "root";
	private Connection connection;
	private boolean connected;
	
	public DataBaseBridge() {
		connected = true;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(database_url, user, password);
		} catch (ClassNotFoundException e) {
			connected = false;
			System.err.println("Forcing the JDBC Driver to register itself failed!");
			e.printStackTrace();
		} catch (SQLException e) {
			connected = false;
			System.err.println("Connection to the database failed!");
			e.printStackTrace();
		} 
	}
	
	public void close(){
		if (!connected) return;
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* DataBaseBridge services: (using prepared statements in order to be safe from SQL Injection) */
	public boolean registerNewProfessional(String email, String password, String firstName, String lastName, String phone, String profilePicFilePath) {
		if (!connected) return false;
		String registerInsert = "INSERT INTO Professionals (idProfessional, email, password, firstName, lastName, phoneNumber, profilePictureFilePath, employmentStatus, employmentInstitution,professionalExperience, educationBackground, skills, professionalExperienceVisibility, educationBackgroundVisibility, skillsVisibility) "
				              + " VALUES (default, ?, ?, ?, ?, ?, ?, NULL, NULL, NULL, NULL, NULL, default, default, default); ";
		try {
			PreparedStatement statement = connection.prepareStatement(registerInsert);
			statement.setString(1, email);
			statement.setString(2, password);
			statement.setString(3, firstName);
			statement.setString(4, lastName);
			statement.setString(5, phone);
			statement.setString(6, profilePicFilePath);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Professional recoverProfessionalRecord(String email) {
		if (!connected) return null;
		Professional record = null;
		String Query = "SELECT idProfessional, email, password, firstName, lastName, phoneNumber, profilePictureFilePath FROM Professionals WHERE email = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setString(1, email);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {            // move cursor to first record, if false is returned then we got an empty set
				return null;
			} else if ( resultSet.next() ) {    // move the cursor to the next record and if it returns true then more than one record exist with the same email --> SHOULD NOT HAPPEN
				System.err.println("Warning: Found more than one records of Professionals with the same email!");
				return null;
			} else {                            // else if it returned false then - correctly - only one such record exists
				resultSet.previous();           // move cursor back to that one record
				record = new Professional();
				record.ID = resultSet.getInt("idProfessional");
				record.email = resultSet.getString("email");         // should be equal to argument 'email'
				record.password = resultSet.getString("password");
				record.firstName = resultSet.getString("firstName");
				record.lastName = resultSet.getString("lastName");
				record.phone = resultSet.getString("phoneNumber");
				record.profile_pic_file_path = resultSet.getString("profilePictureFilePath");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return record;
	}
	
	public Administrator recoverAdministratorRecord(String email) {
		if (!connected) return null;
		Administrator record = null;
		String Query = "SELECT * FROM Administrators WHERE email = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setString(1, email);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {            // move cursor to first record, if false is returned then we got an empty set
				return null;
			} else if ( resultSet.next() ) {    // move the cursor to the next record and if it returns true then more than one record exist with the same email --> SHOULD NOT HAPPEN
				System.err.println("Warning: Found more than one records of Professionals with the same email!");
				return null;
			} else {                            // else if it returned false then - correctly - only one such record exists
				resultSet.previous();           // move cursor back to that one record
				record = new Administrator();
				record.ID = resultSet.getInt("idAdministrator");
				record.email = resultSet.getString("email");         // should be equal to argument 'email'
				record.password = resultSet.getString("password");
				record.firstName = resultSet.getString("firstName");
				record.lastName = resultSet.getString("lastName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return record;
	}
	
	public Professional[] getAllProfessionals(){
		if (!connected) return null;
		Professional[] P = null;
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Professionals;");
			if (resultSet.next()) {  // if not empty set is returned (should not happen with SELECT count(*) )
				int count = resultSet.getInt(1);
				System.out.println("Number of Professionals registered is: " + count);
				resultSet = statement.executeQuery("SELECT * FROM Professionals;");
				P = new Professional[count];
				int i = 0;
				while (i < count && resultSet.next()) {                // load all professionals onto memory
					P[i] = new Professional();
					P[i].ID = resultSet.getInt("idProfessional");
					P[i].email = resultSet.getString("email");         // should be equal to argument 'email'
					P[i].password = resultSet.getString("password");
					P[i].firstName = resultSet.getString("firstName");
					P[i].lastName = resultSet.getString("lastName");
					P[i].phone = resultSet.getString("phoneNumber");
					P[i].profile_pic_file_path = resultSet.getString("profilePictureFilePath");
					P[i].employmentStatus = resultSet.getString("employmentStatus");
					P[i].employmentInstitution = resultSet.getString("employmentInstitution");
					P[i].professionalExperience = resultSet.getString("professionalExperience");
					P[i].educationBackground = resultSet.getString("educationBackground");
					P[i].skills = resultSet.getString("skills");
					P[i].profExpVisibility = resultSet.getBoolean("professionalExperienceVisibility");
					P[i].edBackgroundVisibility = resultSet.getBoolean("educationBackgroundVisibility");
					P[i].skillsVisibility = resultSet.getBoolean("skillsVisibility");
					i++;
				}
				if ( resultSet.next() ) 
					System.err.println("Warning: Count of professionals is wrong?");
			} else 
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} 
		return P;
	}
	
	public Professional getProfessional(int ID) {
		if (!connected) return null;
		Professional prof = null;
		String Query = "SELECT * FROM Professionals WHERE idProfessional = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {            // move cursor to first record, if false is returned then we got an empty set
				return null;
			} else {
				prof = new Professional();
				prof.ID = resultSet.getInt("idProfessional");
				prof.email = resultSet.getString("email");         // should be equal to argument 'email'
				prof.password = resultSet.getString("password");
				prof.firstName = resultSet.getString("firstName");
				prof.lastName = resultSet.getString("lastName");
				prof.phone = resultSet.getString("phoneNumber");
				prof.profile_pic_file_path = resultSet.getString("profilePictureFilePath");
				prof.employmentStatus = resultSet.getString("employmentStatus");
				prof.employmentInstitution = resultSet.getString("employmentInstitution");
				prof.professionalExperience = resultSet.getString("professionalExperience");
				prof.educationBackground = resultSet.getString("educationBackground");
				prof.skills = resultSet.getString("skills");
				prof.profExpVisibility = resultSet.getBoolean("professionalExperienceVisibility");
				prof.edBackgroundVisibility = resultSet.getBoolean("educationBackgroundVisibility");
				prof.skillsVisibility = resultSet.getBoolean("skillsVisibility");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return prof;
	}
	
}

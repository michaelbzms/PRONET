package model;

// import JDBC packets
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DataBaseBridge {
	
	/* These fields are better hardcoded only here than all over the playce on the caller's side */
	final private String database_url = "jdbc:mysql://localhost:3306/TED?serverTimezone=UTC";
	final private String user = "myuser";
	final private String password = "MYUSERSQL";
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
	
	/* DataBaseBridge services: */
	public boolean registerNewProfessional(String email, String password, String firstName, String lastName, String phone, String profilePicFilePath) {
		if (!connected) return false;
		String registerInsert = "INSERT INTO Professionals (idProfessional, email, password, firstName, lastName, phoneNumber, profilePictureFilePath, employmentStatus, employmentInstitution,professionalExperience, educationBackground, skills, professionalExperienceVisibility, educationBackgroundVisibility, skillsVisibility) "
				              + " VALUES (default, '" + email +"' , '" + password + "', '" + firstName + "', '" + lastName + "', '" + phone + "', '" + profilePicFilePath + "' , NULL, NULL, NULL, NULL, NULL, default, default, default); ";
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(registerInsert);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public professional recoverProfessionalRecord(String email) {
		if (!connected) return null;
		professional record = null;
		String Query = "SELECT idProfessional, email, password, firstName, lastName, phoneNumber, profilePictureFilePath"
				     + "FROM Professionals WHERE email = '" + email + "';";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(Query);
			if (resultSet == null) {        // empty set
				return null;
			} else if (resultSet.next()) {  // more than one record with the same email --> SHOULD NOT HAPPEN
				return null;
			} else {
				record = new professional();
				record.ID = resultSet.getString("idProfessional");
				record.email = resultSet.getString("email");      // should be equal to argument 'email'
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
	
}

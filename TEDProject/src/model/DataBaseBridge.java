package model;

// import JDBC packets
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DataBaseBridge {
	
	/* These fields are better hardcoded only here than all over the place on the caller's side */
	final private String database_url = "jdbc:mysql://localhost:3306/TED?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";   // not using SSL yet
	final private String DBUser = "myuser";
	final private String DBPassword = "MYUSERSQL";
	private Connection connection;
	private boolean connected;
	private Calendar cal;
	
	
	public DataBaseBridge() {
		connected = true;
		cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(database_url, DBUser, DBPassword);
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
	
	public boolean checkIfConnected() {
		return connected;
	}
	
	/* DataBaseBridge services: (using prepared statements in order to be safe from SQL Injection) */
	public boolean registerNewProfessional(String email, String password, String firstName, String lastName, String phone, String profilePicFilePath) {
		if (!connected) return false;
		String registerInsert = "INSERT INTO Professionals (idProfessional, email, password, firstName, lastName, phoneNumber, profilePictureURI, employmentStatus, employmentInstitution,professionalExperience, educationBackground, skills, professionalExperienceVisibility, educationBackgroundVisibility, skillsVisibility, registrationDate) "
				              + " VALUES (default, ?, ?, ?, ?, ?, ?, NULL, NULL, NULL, NULL, NULL, default, default, default, ?); ";
		try {
			PreparedStatement statement = connection.prepareStatement(registerInsert);
			statement.setString(1, email);
			statement.setString(2, password);
			statement.setString(3, firstName);
			statement.setString(4, lastName);
			statement.setString(5, phone);
			statement.setString(6, profilePicFilePath);
			statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
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
		String Query = "SELECT idProfessional, email, password, firstName, lastName, phoneNumber, profilePictureURI FROM Professionals WHERE email = ?;";
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
				record.setID(resultSet.getInt("idProfessional"));
				record.setEmail(resultSet.getString("email"));         // should be equal to argument 'email'
				record.setPassword(resultSet.getString("password"));
				record.setFirstName(resultSet.getString("firstName"));
				record.setLastName(resultSet.getString("lastName"));
				record.setPhone(resultSet.getString("phoneNumber"));
				record.setProfilePicURI(resultSet.getString("profilePictureURI"));
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
				record.setEmail(resultSet.getString("email"));         // should be equal to argument 'email'
				record.setPassword(resultSet.getString("password"));
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
				while (i < count && resultSet.next()) {                  // load all professionals onto memory
					P[i] = new Professional();
					P[i].setID(resultSet.getInt("idProfessional"));
					P[i].setEmail(resultSet.getString("email"));
					P[i].setPassword(resultSet.getString("password"));
					P[i].setFirstName(resultSet.getString("firstName"));
					P[i].setLastName(resultSet.getString("lastName"));
					P[i].setPhone(resultSet.getString("phoneNumber"));
					P[i].setProfilePicURI(resultSet.getString("profilePictureURI"));
					P[i].setEmploymentStatus(resultSet.getString("employmentStatus"));
					P[i].setEmploymentInstitution(resultSet.getString("employmentInstitution"));
					P[i].setDescription(resultSet.getString("description"));
					P[i].setProfessionalExperience(resultSet.getString("professionalExperience"));
					P[i].setEducationBackground(resultSet.getString("educationBackground"));
					P[i].setSkills(resultSet.getString("skills"));
					P[i].setProfExpVisibility(resultSet.getBoolean("professionalExperienceVisibility"));
					P[i].setEdBackgroundVisibility(resultSet.getBoolean("educationBackgroundVisibility"));
					P[i].setSkillsVisibility(resultSet.getBoolean("skillsVisibility"));
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
				prof.setID(resultSet.getInt("idProfessional"));
				prof.setEmail(resultSet.getString("email"));
				prof.setPassword(resultSet.getString("password"));
				prof.setFirstName(resultSet.getString("firstName"));
				prof.setLastName(resultSet.getString("lastName"));
				prof.setPhone(resultSet.getString("phoneNumber"));
				prof.setProfilePicURI(resultSet.getString("profilePictureURI"));
				prof.setEmploymentStatus(resultSet.getString("employmentStatus"));
				prof.setEmploymentInstitution(resultSet.getString("employmentInstitution"));
				prof.setDescription(resultSet.getString("description"));
				prof.setProfessionalExperience(resultSet.getString("professionalExperience"));
				prof.setEducationBackground(resultSet.getString("educationBackground"));
				prof.setSkills(resultSet.getString("skills"));
				prof.setProfExpVisibility(resultSet.getBoolean("professionalExperienceVisibility"));
				prof.setEdBackgroundVisibility(resultSet.getBoolean("educationBackgroundVisibility"));
				prof.setSkillsVisibility(resultSet.getBoolean("skillsVisibility"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return prof;
	}

	public List<Professional> getConnectedProfessionalsFor(int ID) {
		if (!connected) return null;
		 List<Professional> P = null;
		// Could also use DISTINCT just in case ConnectedProfessional has the same connection twice but in order to see that mistake if it exists I chose not to
		String Query = "SELECT p.idProfessional, p.firstName, p.lastName, p.profilePictureURI, p.employmentStatus, p.employmentInstitution "
				     + "FROM Professionals p, ConnectedProfessionals cp "
				     + "WHERE ( p.idProfessional = cp.idProfessional1 and cp.idProfessional2 = ?) "
				     +    "or ( p.idProfessional = cp.idProfessional2 and cp.idProfessional1 = ?);";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, ID);
			statement.setInt(2, ID);
			ResultSet resultSet = statement.executeQuery();
			P = new ArrayList<Professional>();
			while (resultSet.next()) {
				Professional prof = new Professional();
				prof.setID(resultSet.getInt("idProfessional"));
				prof.setFirstName(resultSet.getString("firstName"));
				prof.setLastName(resultSet.getString("lastName"));
				prof.setProfilePicURI(resultSet.getString("profilePictureURI"));
				prof.setEmploymentStatus(resultSet.getString("employmentStatus"));
				prof.setEmploymentInstitution(resultSet.getString("employmentInstitution"));
				P.add(prof);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return P;
	}
	
	public List<Professional> getConnectionRequestsFor(int ID){
		if (!connected) return null;
		List<Professional> P = null;
		// Could also use DISTINCT just in case ConnectedProfessional has the same connection twice but in order to see that mistake if it exists I chose not to
		String Query = "SELECT p.idProfessional, p.firstName, p.lastName, p.profilePictureURI, p.employmentStatus, p.employmentInstitution "
				     + "FROM Professionals p, ConnectionRequests cr "
				     + "WHERE p.idProfessional = cr.idAsker and cr.idReceiver = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			P = new ArrayList<Professional>();
			while (resultSet.next()) {
				Professional prof = new Professional();
				prof.setID(resultSet.getInt("idProfessional"));
				prof.setFirstName(resultSet.getString("firstName"));
				prof.setLastName(resultSet.getString("lastName"));
				prof.setProfilePicURI(resultSet.getString("profilePictureURI"));
				prof.setEmploymentStatus(resultSet.getString("employmentStatus"));
				prof.setEmploymentInstitution(resultSet.getString("employmentInstitution"));
				P.add(prof);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return P;
	}

	public String getProfessionalPassword(int ID) {
		if (!connected) return null;
		String profPasssword = null;
		String Query = "SELECT password FROM Professionals WHERE idProfessional = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {            // move cursor to first record, if false is returned then we got an empty set
				return null;
			} else {
				profPasssword = resultSet.getString("password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return profPasssword;
	}
	
	public boolean updateProfessionalEmail(int profID, String newEmail) {
		if (!connected) return false;
		String updateString = "UPDATE Professionals SET email = ? WHERE idProfessional = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(updateString);
			statement.setString(1, newEmail);
			statement.setInt(2, profID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateProfessionalPassword(int profID, String newPassword) {
		if (!connected) return false;
		String updateString = "UPDATE Professionals SET password = ? WHERE idProfessional = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(updateString);
			statement.setString(1, newPassword);
			statement.setInt(2, profID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<Professional> getSearchResultsFor(String searchString) {
		if (!connected) return null;
		List<Professional> P = new ArrayList<Professional>();
		String Query = "SELECT idProfessional, firstName, lastName, profilePictureURI, employmentStatus, employmentInstitution "
				     + "FROM Professionals "
				     + "WHERE (firstName LIKE ?) or (lastName LIKE ?);";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			for (String word : searchString.split(" ")) {
				statement.setString(1, "%" + word + "%");
				statement.setString(2, "%" + word + "%");
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Professional prof = new Professional();
					prof.setID(resultSet.getInt("idProfessional"));
					prof.setFirstName(resultSet.getString("firstName"));
					prof.setLastName(resultSet.getString("lastName"));
					prof.setProfilePicURI(resultSet.getString("profilePictureURI"));
					prof.setEmploymentStatus(resultSet.getString("employmentStatus"));
					prof.setEmploymentInstitution(resultSet.getString("employmentInstitution"));
					if ( !P.contains(prof) ) {
						P.add(prof);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return P;
	}	

	public boolean removeConnectionRequest(int AskerID, int ReceiverID) {
		if (!connected) return false;
		String deleteString = "DELETE FROM ConnectionRequests WHERE idAsker = ? and idReceiver = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(deleteString);
			statement.setInt(1, AskerID);
			statement.setInt(2, ReceiverID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addConnectedProfessionals(int prof1ID, int prof2ID) {
		if (!connected) return false;
		String insertString = "INSERT INTO ConnectedProfessionals (idProfessional1, idProfessional2, connectionDate) VALUES (?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(insertString);
			statement.setInt(1, prof1ID);
			statement.setInt(2, prof2ID);
			statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean areProfessionalsConnected(int profID1, int profID2) {
		if (profID1 == profID2) return true;		// one should be considered connected to themselves, right?
		if (profID1 < 0 || profID2 < 0) return false;
		if (!connected) return false;
		String Query = "SELECT * FROM ConnectedProfessionals WHERE (idProfessional1 = ? AND idProfessional2 = ?) "
				+ "OR (idProfessional1 = ? AND idProfessional2 = ?);";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, profID1);
			statement.setInt(2, profID2);
			statement.setInt(3, profID2);
			statement.setInt(4, profID1);
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();            // false if empty set, true otherwise
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateProfessionalProfile(int profID, Professional tempProf) {
		if (!connected) return false;
		String updateString = "UPDATE Professionals SET phoneNumber = ?, employmentStatus = ?, employmentInstitution = ?, description = ?, "
				+ "professionalExperience = ?, educationBackground = ?, skills = ?, professionalExperienceVisibility = ?, "
				+ "educationBackgroundVisibility = ?, skillsVisibility = ?, profilePictureURI = ? WHERE idProfessional = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(updateString);
			statement.setString(1, tempProf.getPhone());
			statement.setString(2, tempProf.getEmploymentStatus());
			statement.setString(3, tempProf.getEmploymentInstitution());
			statement.setString(4, tempProf.getDescription());
			statement.setString(5, tempProf.getProfessionalExperience());
			statement.setString(6, tempProf.getEducationBackground());
			statement.setString(7, tempProf.getSkills());
			statement.setBoolean(8, tempProf.getProfExpVisibility());
			statement.setBoolean(9, tempProf.getEdBackgroundVisibility());
			statement.setBoolean(10, tempProf.getSkillsVisibility());
			statement.setString(11, tempProf.getProfilePicURI());
			statement.setInt(12, profID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean createConnectionRequest(int askerID, int receiverID) {
		if (!connected) return false;
		String insertString = "INSERT INTO ConnectionRequests (idAsker, idReceiver, requestDate) VALUES (?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(insertString);
			statement.setInt(1, askerID);
			statement.setInt(2, receiverID);
			statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteConnection(int profID1, int profID2) {
		if (!connected) return false;
		String deleteString = "DELETE FROM ConnectedProfessionals WHERE (idProfessional1 = ? AND idProfessional2 = ?) " + 
				"OR (idProfessional1 = ? AND idProfessional2 = ?);";
		try {
			PreparedStatement statement = connection.prepareStatement(deleteString);
			statement.setInt(1, profID1);
			statement.setInt(2, profID2);
			statement.setInt(3, profID2);
			statement.setInt(4, profID1);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean pendingConnectionRequest(int askerID, int receiverID) {
		if (!connected) return false;
		String Query = "SELECT * FROM ConnectionRequests WHERE idAsker = ? and idReceiver = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, askerID);
			statement.setInt(2, receiverID);
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();            // false if empty set, true otherwise
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public LocalDateTime getConnectionRequestDate(int askerID, int receiverID) {
		if (!connected) return null;
		String Query = "SELECT requestDate FROM ConnectionRequests WHERE idAsker = ? and idReceiver = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, askerID);
			statement.setInt(2, receiverID);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {            // move cursor to first record, if false is returned then we got an empty set
				return null;
			} else {
				return resultSet.getTimestamp("requestDate", cal).toLocalDateTime();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Professional> getProfsMessagingWith(int profID) {
		if (!connected) return null;
		List<Professional> P = new ArrayList<Professional>();
		String Query = "SELECT p.idProfessional, p.firstName, p.lastName "
				     + "FROM Professionals p, Conversations c "
				     + "WHERE (p.idProfessional = c.idProfessional1 AND c.idProfessional2 = ?) OR "
				     +       "(p.idProfessional = c.idProfessional2 AND c.idProfessional1 = ?) "
				     + "ORDER BY c.lastMessageSent DESC;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, profID);
			statement.setInt(2, profID);
			ResultSet resultSet = statement.executeQuery();
			P = new ArrayList<Professional>();
			while (resultSet.next()) {
				Professional prof = new Professional();
				prof.setID(resultSet.getInt("idProfessional"));
				prof.setFirstName(resultSet.getString("firstName"));
				prof.setLastName(resultSet.getString("lastName"));
				if ( !P.contains(prof) ) {
					P.add(prof);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return P;
	}
	
	public String getProfessionalFullName(int ID) {
		if (!connected) return null;
		String Query = "SELECT firstName, lastName FROM Professionals WHERE idProfessional = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {            // move cursor to first record, if false is returned then we got an empty set
				return null;
			} else {
				return resultSet.getString("firstName") + " " + resultSet.getString("lastName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<WorkAd> getWorkAds(int profID, int mode) {		// mode: 0 for own ads, 1 for ads from connected profs, 2 from others
		if (!connected || mode < 0 || mode > 2) return null;
		List<WorkAd> ads = null;
		String Query;
		if (mode == 0) {
			Query = "SELECT idAd, idPublishedBy, title, postedDate FROM Ads WHERE idPublishedBy = ?;";
		} else if (mode == 1) {
			Query = "SELECT a.idAd, a.idPublishedBy, a.title, a.postedDate FROM Ads a, ConnectedProfessionals cp "
				  + "WHERE a.idPublishedBy != ? AND "
				  + "((cp.idProfessional1 = a.idPublishedBy AND cp.idProfessional2 = ?) OR "
				  + "(cp.idProfessional1 = ? AND cp.idProfessional2 = a.idPublishedBy));";
		} else {
			Query = "SELECT idAd, idPublishedBy, title, postedDate FROM Ads WHERE idPublishedBy != ? AND idAd NOT IN "
				  + "(SELECT a.idAd FROM Ads a, ConnectedProfessionals cp WHERE "
				  + "((cp.idProfessional1 = a.idPublishedBy AND cp.idProfessional2 = ?) OR "
				  + "(cp.idProfessional1 = ? AND cp.idProfessional2 = a.idPublishedBy)));";
		}
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, profID);
			if (mode == 1 || mode == 2) {
				statement.setInt(2, profID);
				statement.setInt(3, profID);
			}
			ResultSet resultSet = statement.executeQuery();
			ads = new ArrayList<WorkAd>();
			while (resultSet.next()) {
				WorkAd ad = new WorkAd();
				ad.setID(resultSet.getInt("idAd"));
				ad.setPublishedByID(resultSet.getInt("idPublishedBy"));
				ad.setTitle(resultSet.getString("title"));
				ad.setPostedDate(resultSet.getTimestamp("postedDate", cal).toLocalDateTime());
				ads.add(ad);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return ads;
	}
	
	public WorkAd getWorkAd(int adID) {
		if (!connected) return null;
		WorkAd ad = null;
		String Query = "SELECT * FROM Ads WHERE idAd = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, adID);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {            // move cursor to first record, if false is returned then we got an empty set
				return null;
			} else {
				ad = new WorkAd();
				ad.setID(resultSet.getInt("idAd"));
				ad.setPublishedByID(resultSet.getInt("idPublishedBy"));
				ad.setTitle(resultSet.getString("title"));
				ad.setPostedDate(resultSet.getTimestamp("postedDate", cal).toLocalDateTime());
				ad.setDescription(resultSet.getString("description"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return ad;
	}
	
	public int getWorkAdPublishedByID(int adID) {
		if (!connected) return -1;
		int publishedByID = -1;
		String Query = "SELECT idPublishedBy FROM Ads WHERE idAd = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, adID);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {            // move cursor to first record, if false is returned then we got an empty set
				return -1;
			} else {
				publishedByID = resultSet.getInt("idPublishedBy");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return publishedByID;
	}
	
	public boolean createWorkAd(int profID, String title, String description) {
		if (!connected) return false;
		String insertString = "INSERT INTO Ads (idAd, idPublishedBy, title, postedDate, description, containsFiles) VALUES (default, ?, ?, ?, ?, 0)";
		try {
			PreparedStatement statement = connection.prepareStatement(insertString);
			statement.setInt(1, profID);
			statement.setString(2, title);
			statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
			statement.setString(4, description);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateWorkAd(int adID, String description) {		// TODO: also update datetime?
		if (!connected) return false;
		String updateString = "UPDATE Ads SET description = ? WHERE idAd = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(updateString);
			statement.setString(1, description);
			statement.setInt(2, adID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteWorkAd(int adID) {
		if (!connected) return false;
		// First delete all applications to that that ad:
		String deleteString = "DELETE FROM Applications WHERE idAd = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(deleteString);
			statement.setInt(1, adID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		// Then delete the ad itself:
		deleteString = "DELETE FROM Ads WHERE idAd = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(deleteString);
			statement.setInt(1, adID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean createApplication(int adID, int profID, String applyNote) {
		if (!connected) return false;
		String insertString = "INSERT INTO Applications (idApplication, idAd, idApplicant, applyDate, note) VALUES (default, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(insertString);
			statement.setInt(1, adID);
			statement.setInt(2, profID);
			statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
			statement.setString(4, applyNote);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<Message> getMessagesForConvo(int profID1, int profID2){
		if (!connected) return null;
		List<Message> M = null;
		String Query = "SELECT text, timeSent, containsFiles, idSentByProf "
				     + "FROM Messages "
				     + "WHERE (idProfessional1 = ? AND idProfessional2 = ?) OR "
				     + 		 "(idProfessional1 = ? AND idProfessional2 = ?) "
				     + "ORDER BY timeSent;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, profID1);
			statement.setInt(2, profID2);
			statement.setInt(3, profID2);
			statement.setInt(4, profID1);
			ResultSet resultSet = statement.executeQuery();
			M = new ArrayList<Message>();
			while (resultSet.next()) {
				Message newmsg = new Message();
				newmsg.setText(resultSet.getString("text"));
				newmsg.setSentByProfID(resultSet.getInt("idSentByProf"));
				newmsg.setTimeSent(resultSet.getTimestamp("timeSent", cal).toLocalDateTime());
				newmsg.setContainsFiles(resultSet.getBoolean("containsFiles"));
				M.add(newmsg);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return M;
	}
	
	public List<Message> getNewAwayMessagesAfter(LocalDateTime latestGot, int homeprof, int awayprof){
		if (!connected) return null;
		List<Message> M = null;
		String Query = "SELECT text, timeSent, containsFiles "
				     + "FROM Messages "
				     + "WHERE ((idProfessional1 = ? AND idProfessional2 = ?) OR (idProfessional1 = ? AND idProfessional2 = ?)) "
				     +        "AND idSentByProf = ? " +  ( (latestGot != null) ? "AND timeSent > ? " : "")
				     + "ORDER BY timeSent;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, homeprof);
			statement.setInt(2, awayprof);
			statement.setInt(3, awayprof);
			statement.setInt(4, homeprof);
			statement.setInt(5, awayprof);      // sent by awayprof
			if (latestGot != null) {
				statement.setTimestamp(6, Timestamp.valueOf(latestGot));  // after latestGot datetime
			}
			ResultSet resultSet = statement.executeQuery();
			M = new ArrayList<Message>();
			while (resultSet.next()) {
				Message newmsg = new Message();
				newmsg.setText(resultSet.getString("text"));
				newmsg.setTimeSent(resultSet.getTimestamp("timeSent", cal).toLocalDateTime());
				newmsg.setContainsFiles(resultSet.getBoolean("containsFiles"));
				M.add(newmsg);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return M;
	}
	
	public int existsConversationBetween(int sentById, int sentToId) {
		if (!connected) return -1;
		String Query = "SELECT 'exists' FROM Conversations WHERE idProfessional1 = ? AND idProfessional2 = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, sentById);
			statement.setInt(2, sentToId);
			ResultSet resultSet = statement.executeQuery();
			if ( resultSet.next() ) {   // if there exists such a record with order (sentById, sentToId) then return code 1
				return 1;
			} else {                    // reverse order and try again
				statement.setInt(1, sentToId);
				statement.setInt(2, sentById);
				resultSet = statement.executeQuery();
				if ( resultSet.next() ) {
					return 2;            // if there exists such a record with order (sentToId, sentById) then return code 2
				} else {
					return 0;            // no conversation exists between the two professionals
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		}
	}

	public boolean createConversationInOrder(int sentById, int sentToId) {
		if (!connected) return false;
		String Insert = "INSERT INTO Conversations (`idProfessional1`, `idProfessional2`, `lastMessageSent`) "
					  + "VALUES (?, ?, ?);";
		try {
			PreparedStatement statement = connection.prepareStatement(Insert);
			statement.setInt(1, sentById);
			statement.setInt(2, sentToId);
			statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addMessageToConversationInOrder(int firstId, int secondID, int sentById, String text, boolean containsFiles) {
		if (!connected) return false;
		String Insert = "INSERT INTO Messages (`idMessage`, `idProfessional1`, `idProfessional2`, `idSentByProf`, `text`, `timeSent`, `containsFiles`) "
					  + "VALUES (default, ?, ?, ?, ?, ?, ?);";
		try {
			PreparedStatement statement = connection.prepareStatement(Insert);
			statement.setInt(1, firstId);
			statement.setInt(2, secondID);
			statement.setInt(3, sentById);
			statement.setString(4, text);
			statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
			statement.setBoolean(6, containsFiles);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateLastSentToConversation(int sentById, int sentToId) {
		if (!connected) return false;
		String Update = "UPDATE Conversations SET lastMessageSent = ? "
					  + "WHERE (idProfessional1 = ? and idProfessional2 = ?) OR (idProfessional1 = ? and idProfessional2 = ?);";
		try {
			PreparedStatement statement = connection.prepareStatement(Update);
			statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
			statement.setInt(2, sentById);
			statement.setInt(3, sentToId);
			statement.setInt(4, sentToId);
			statement.setInt(5, sentById);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean pendingWorkAdApplication(int profID, int adID) {
		if (!connected) return false;
		String Query = "SELECT * FROM Applications WHERE idAd = ? and idApplicant = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, adID);
			statement.setInt(2, profID);
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();            // false if empty set, true otherwise
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteApplication(int profID, int adID) {
		if (!connected) return false;
		String deleteString = "DELETE FROM Applications WHERE idAd = ? and idApplicant = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(deleteString);
			statement.setInt(1, adID);
			statement.setInt(2, profID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<Application> getApplications(int ID, boolean mode) {		// mode = true for ad's applications, false for prof's applications 
		if (!connected) return null;
		List<Application> applications = null;
		String Query;
		if (mode) {
			Query = "SELECT * FROM Applications WHERE idAd = ? ORDER BY applyDate;";
		} else {
			Query = "SELECT * FROM Applications WHERE idApplicant = ? ORDER BY applyDate;";
		}
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			applications = new ArrayList<Application>();
			while (resultSet.next()) {
				Application apl = new Application();
				apl.setID(resultSet.getInt("idApplication"));
				apl.setAdID(resultSet.getInt("idAd"));
				apl.setProfID(resultSet.getInt("idApplicant"));
				apl.setApplyDate(resultSet.getTimestamp("applyDate", cal).toLocalDateTime());
				apl.setNote(resultSet.getString("note"));
				applications.add(apl);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return applications;
	}
	
	public String getWorkAdTitle(int adID) {
		if (!connected) return null;
		String Query = "SELECT title FROM Ads WHERE idAd = ?;";
		try {
			PreparedStatement statement = connection.prepareStatement(Query);
			statement.setInt(1, adID);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next()) {            // move cursor to first record, if false is returned then we got an empty set
				return null;
			} else {
				return resultSet.getString("title");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}



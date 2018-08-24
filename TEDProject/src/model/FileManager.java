package model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.Part;

public class FileManager {
	
	public static boolean editProfilePicture(Professional tempProf, Part filePart, String saveLocation, String userUploadFilePath, int profID) {     // (!) modifies tempProf accordingly
		DataBaseBridge db = new DataBaseBridge();
		Professional prof = db.getProfessional(profID);
		db.close();
		if ( prof.getProfile_pic_file_path() == null || !(new File(saveLocation + "/profile/" + prof.getProfile_pic_name())).exists()  || prof.getProfile_pic_file_path().equals("http://localhost:8080/TEDProject/images/defaultProfilePic.png") ) {
			// if previous picture was the default picture (or simply does not exist) then must choose a new unique name and save the picture under it
			String unique_name = "", extension = "";
			int i = userUploadFilePath.lastIndexOf('.');
			if (i > 0) { extension = userUploadFilePath.substring(i+1); }
			int min = 0, max = 999999; 
			File f;
			do {	
				unique_name = "img" + Integer.toString(ThreadLocalRandom.current().nextInt(min, max + 1)) ;
				f = new File(saveLocation + "/profile/" + unique_name + "." + extension);
			} while (f.exists() && !f.isDirectory());           // assure it's unique
			// update tempProf to reflect changes
			tempProf.setProfile_pic_file_path("http://localhost:8080/TEDProject/FileServlet?file=" + unique_name + "." + extension +"&type=profile");
			// save to disk
			File UploadedProfiles = new File(saveLocation + "/profile");
			File newfile = new File(UploadedProfiles, unique_name + "." + extension);
			try (InputStream input = filePart.getInputStream()) {
			    Files.copy(input, newfile.toPath());
			    System.out.println("File " + unique_name + "." + extension + " saved to disk!");
			} catch ( IOException e ) {
				System.err.println("Could not save file " + unique_name + "." + extension + " to disk!");
			}
		} else {
			//  if a picture already existed first delete the old profile picture file
			Path filepath = FileSystems.getDefault().getPath(saveLocation + "/profile", prof.getProfile_pic_name());
			try {
			    Files.delete(filepath);
			    System.out.println("File " + prof.getProfile_pic_name() + " deleted from disk!");
			} catch (NoSuchFileException x) {
			    System.err.format("Tried to delete %s but no such file or directory%n", filepath);
			} catch (IOException x) {    // File permission problems are caught here.
			    System.err.println("Do not have permission to delete previous profile picture!");
			}
			// and then save the new one under the SAME name, so tempProf.profile_picture_file_path shall remain the same as prof's
			tempProf.setProfile_pic_file_path(prof.getProfile_pic_file_path());
			// save to disk
			File UploadedProfiles = new File(saveLocation + "/profile");
			File newfile = new File(UploadedProfiles, prof.getProfile_pic_name());
			try (InputStream input = filePart.getInputStream()) {
			    Files.copy(input, newfile.toPath());
			    System.out.println("File " +prof.getProfile_pic_name() + " saved to disk!");
			} catch ( IOException e ) {
				System.err.println("Could not save file " + prof.getProfile_pic_name() + " to disk!");
			}
		}	
		return true;
	}
	
	
}

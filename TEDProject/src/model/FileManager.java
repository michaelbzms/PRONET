package model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.Part;

public class FileManager {
	
	public static boolean editProfilePicture(Professional tempProf, Part filePart, String saveLocation, String userUploadFilePath, int profID) {     // (!) modifies tempProf accordingly
		DataBaseBridge db = new DataBaseBridge();
		Professional prof = db.getProfessional(profID);
		db.close();
		if ( prof.getProfilePicURI() == null || !(new File(saveLocation + "/profile/" + prof.getProfile_pic_name())).exists()  || prof.getProfilePicURI().equals("http://localhost:8080/TEDProject/images/defaultProfilePic.png") ) {
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
			tempProf.setProfilePicURI("http://localhost:8080/TEDProject/FileServlet?file=" + unique_name + "." + extension +"&type=profile");
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
			tempProf.setProfilePicURI(prof.getProfilePicURI());
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
	
	public static boolean saveArticleFiles(Collection<Part> fileParts, String saveLocation, DataBaseBridge db, int articleID) {
		for (Part filePart : fileParts) {
			String userUploadFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();    // MSIE fix
			String contentType = filePart.getContentType();
			
			// Debug:
			System.out.println("Saving uploaded file: " + userUploadFileName + " to " + saveLocation + " under a new unique name...");
			
			// figure out an appropriate unique name for the file
			String type, unique_name = "", extension = "";
			int j = contentType.indexOf('/');
			if ( contentType.substring(0, j).equals("image") ) {
				type = "img";
			} else if ( contentType.substring(0, j).equals("video") ) {
				type = "vid";
			} else if ( contentType.substring(0, j).equals("video") ) {
				type = "aud";
			} else {      // if file is not an image or a video or an audio, then
				// do NOT save it
				System.out.println("(!) Did not save file" + userUploadFileName + "because it was not supported!");
				continue;
			}
			int i = userUploadFileName.lastIndexOf('.');
			if (i > 0) { extension = userUploadFileName.substring(i+1); }
			int min = 0, max = 999999; 
			File f;
			do {	
				unique_name = type + Integer.toString(ThreadLocalRandom.current().nextInt(min, max + 1)) + "." + extension ;
				f = new File(saveLocation + "/" + unique_name);
			} while (f.exists() && !f.isDirectory()); 
			
			// save to disk
			File UploadedArticleFiles = new File(saveLocation);
			File newfile = new File(UploadedArticleFiles, unique_name);
			try (InputStream input = filePart.getInputStream()) {
			    Files.copy(input, newfile.toPath());
			    System.out.println("File " + unique_name + " saved to disk!");
			} catch ( IOException e ) {
				System.err.println("Could not save file " + unique_name + " to disk!");
				continue;
			}
			
			// save to database its URI
			String fileURI = "http://localhost:8080/TEDProject/FileServlet?file=" + unique_name + "&type=article";
			db.addArticleFile(articleID, fileURI);
		}
		return true;
	}
	
}

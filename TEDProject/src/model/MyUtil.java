package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

public final class MyUtil {
	
	private static int timezoneOffset = Integer.parseInt(PropertiesManager.getProperty("timezoneOffset"));
	
	private MyUtil() { 
		System.out.println("This class is not meant to be instantiated");
	}

	public static String printDate(LocalDateTime date, boolean withTime) {
		if (date == null) {		// print current time
			date = LocalDateTime.now(ZoneOffset.UTC);
		}
		LocalDateTime offsetDateTime = date.plusHours(-timezoneOffset/60);			// timezoneOffset is in minutes
		DateTimeFormatter formatter;
		String dateFormat = PropertiesManager.getProperty("dateFormat");
		if (withTime) {
			formatter = DateTimeFormatter.ofPattern(dateFormat + " HH:mm:ss");
		} else {
			formatter = DateTimeFormatter.ofPattern(dateFormat);
		}
		return offsetDateTime.format(formatter);
	}
	
	public static LocalDateTime getLocalDateTimeFromString(String date_str, boolean withTime) {
		DateTimeFormatter formatter;
		String dateFormat = PropertiesManager.getProperty("dateFormat");
		if (withTime) {
			formatter = DateTimeFormatter.ofPattern(dateFormat + " HH:mm:ss");
		} else {
			formatter = DateTimeFormatter.ofPattern(dateFormat);
		}
		LocalDateTime utcDateTime = LocalDateTime.parse(date_str, formatter);
		return utcDateTime.plusHours(timezoneOffset/60);
	}
	
	public static String getTimeAgo(LocalDateTime date) {
		if (date == null) { 
			return "null ago";
		}
		long secAgo = ChronoUnit.SECONDS.between(date, LocalDateTime.now(ZoneOffset.UTC));
		if (secAgo < 0) {
			System.out.println("Either someone invented time travel or we messed up with timezones.");
			return Long.toString(-secAgo) + " seconds in the future?";
		} else if (secAgo < 60) {
			return "a few seconds ago";
		} else if (secAgo < 60*60) {
			return Long.toString(secAgo/60) + " minute" + (secAgo < 60*2 ? "" : "s") + " ago";
		} else if (secAgo < 24*60*60) {
			return Long.toString(secAgo/(60*60)) + " hour" + (secAgo < 60*60*2 ? "" : "s") + " ago";
		} else if (secAgo < 30*24*60*60) {
			return Long.toString(secAgo/(24*60*60)) + " day" + (secAgo < 24*60*60*2 ? "" : "s") + " ago";
		} else if (secAgo < 12*30*24*60*60) {
			return Long.toString(secAgo/(30*24*60*60)) + " month" + (secAgo < 30*24*60*60*2 ? "" : "s") + " ago";
		} else {
			return Long.toString(secAgo/(12*30*24*60*60)) + " year" + (secAgo < 12*30*24*60*60*2 ? "" : "s") + " ago";
		}
	}
	
	public static boolean forceDownloadFile(HttpServletResponse response, ServletContext context, String filepath) {
		File profFile = new File(filepath);  
        FileInputStream inStream;
		try {
			inStream = new FileInputStream(profFile);	
	        String mimeType = context.getMimeType(filepath);
	        if (mimeType == null) {        
	            // set to binary type if MIME mapping not found
	            mimeType = "application/octet-stream";
	        }
	        response.setContentType(mimeType);
	        response.setContentLength((int) profFile.length());     
	        // forces download
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + profFile.getName() + '"');	         
	        OutputStream outStream = response.getOutputStream();	         
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;         
	        while ((bytesRead = inStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }	         
	        inStream.close();
	        outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}  
        return true;
	}
	
	// file URI Parsing
	public static int getFileType(final String fileURI) {       // parse URI String to find out its type	
		int i = fileURI.lastIndexOf('?') , start = -1, end = -1;
		if (i > 0) {
			i++;
			while( i + 4 < fileURI.length() ) {           // repeatedly read the first five characters
				char c1 = fileURI.charAt(i), 
					 c2 = fileURI.charAt(i+1), 
					 c3 = fileURI.charAt(i+2),
					 c4 = fileURI.charAt(i+3),
					 c5 = fileURI.charAt(i+4);
				if ( c1 == 'f' && c2 == 'i' && c3 == 'l' && c4 == 'e' && c5 == '=' ) {   // until reached "file=" or end of String
					i += 5;
					start = i;
					for ( ; i < fileURI.length() && fileURI.charAt(i) != '&' && !(fileURI.charAt(i) >= '0' && fileURI.charAt(i) <= '9') ; i++);   // while not met the end or a number
					end = i;
					break;
				}
				i++;
			}
		}
		if ( start == -1 || end == -1 ) return -1;   // should not happen
		String type = fileURI.substring(start, end);
		// DEBUG: System.out.println("Parsing found type: " + type);
		if ( type.equals("img") ){
			return 1;
		} else if ( type.equals("vid") ){
			return 2;
		} else if ( type.equals("aud") ){
			return 3;
		} else {
			return 0;        // unknown type
		}
	}
	
	public static String getFileName(String fileURI){
		int i = fileURI.lastIndexOf('?') , start = -1, end = -1;
		if (i > 0) {
			i++;
			while( i + 4 < fileURI.length() ) {           // repeatedly read the first five characters
				char c1 = fileURI.charAt(i), 
					 c2 = fileURI.charAt(i+1), 
					 c3 = fileURI.charAt(i+2),
					 c4 = fileURI.charAt(i+3),
					 c5 = fileURI.charAt(i+4);
				if ( c1 == 'f' && c2 == 'i' && c3 == 'l' && c4 == 'e' && c5 == '=' ) {   // until reached "file=" or end of String
					i += 5;
					start = i;
					for ( ; i < fileURI.length() && fileURI.charAt(i) != '&' ; i++);
					end = i;
					break;
				}
				i++;
			}
		}
		if ( start == -1 || end == -1 ) return null;   // should not happen
		return fileURI.substring(start, end);
	}
	
}

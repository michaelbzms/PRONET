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
	
	private MyUtil() { 
		System.out.println("This class is not meant to be instantiated");
	}

	public static String printDate(LocalDateTime date, boolean withTime) {
		DateTimeFormatter formatter;
		String dateFormat = PropertiesManager.getProperty("dateFormat");
		if (withTime) {
			formatter = DateTimeFormatter.ofPattern(dateFormat + " HH:mm:ss");
		} else {
			formatter = DateTimeFormatter.ofPattern(dateFormat);
		}
		return date.format(formatter);
	}
	
	public static LocalDateTime getLocalDateTimeFromString(String date_str, boolean withTime) {
		DateTimeFormatter formatter;
		String dateFormat = PropertiesManager.getProperty("dateFormat");
		if (withTime) {
			formatter = DateTimeFormatter.ofPattern(dateFormat + " HH:mm:ss");
		} else {
			formatter = DateTimeFormatter.ofPattern(dateFormat);
		}
		return LocalDateTime.parse(date_str, formatter);
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
	
}

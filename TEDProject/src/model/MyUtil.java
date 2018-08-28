package model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class MyUtil {
	
	private MyUtil() { 
		System.out.println("This class is not meant to be instantiated");
	}

	public static String printDate(LocalDateTime date, boolean withTime) {
		DateTimeFormatter formatter;
		if (withTime) {
			formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		} else {
			formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		}
		return date.format(formatter);
	}
	
	public static String getTimeAgo(LocalDateTime date) {		// TODO: remove (s)
		long secAgo = ChronoUnit.SECONDS.between(date, LocalDateTime.now(ZoneOffset.UTC));
		if (secAgo < 0) {
			System.out.println("Either someone invented time travel or we messed up with timezones.");
			return Long.toString(-secAgo) + " seconds in the future?";
		} else if (secAgo < 60) {
			return "a few seconds ago";
		} else if (secAgo < 60*60) {
			return Long.toString(secAgo/60) + " minute(s) ago";
		} else if (secAgo < 24*60*60) {
			return Long.toString(secAgo/(60*60)) + " hour(s) ago";
		} else if (secAgo < 30*24*60*60) {
			return Long.toString(secAgo/(24*60*60)) + " day(s) ago";
		} else if (secAgo < 12*30*24*60*60) {
			return Long.toString(secAgo/(30*24*60*60)) + " month(s) ago";
		} else {
			return "more than a year ago";
		}
	}
	
	
}

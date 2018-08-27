package model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class WorkAd {
	
	/* Characteristics */
	private int ID = -1;          // its database ID
	private int publishedByID = -1;
	private String title = null, description = null;
	private LocalDateTime postedDate;
	
	/* Setters & Getters*/
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	public int getPublishedByID() {
		return publishedByID;
	}
	public void setPublishedByID(int publishedByID) {
		this.publishedByID = publishedByID;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDateTime getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(LocalDateTime postedDate) {
		this.postedDate = postedDate;
	}
	
	public String printDate(boolean withTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		if (withTime) {
			formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		} else {
			formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		}
		return this.postedDate.format(formatter);
	}
	
	public String getTimeAgo() {		// TODO: remove (s)
		long secAgo = ChronoUnit.SECONDS.between(this.postedDate, LocalDateTime.now(ZoneOffset.UTC));
		if (secAgo < 0) {
			return Long.toString(-secAgo) + " seconds in the future?";
		} if (secAgo < 60) {
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

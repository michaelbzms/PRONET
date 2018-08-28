package model;

import java.time.LocalDateTime;

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
	
}

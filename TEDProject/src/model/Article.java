package model;

import java.time.LocalDateTime;

public class Article {
	
	/* Characteristics */
	private int ID = -1;
	private int authorID = -1;
	private LocalDateTime postedDate = null;
	private String content = null;
	private boolean containsFiles = false;

	/* Setters & Getters */
	public int getID() { return ID; }
	public void setID(int iD) {	ID = iD; }
	
	public int getAuthorID() { return authorID; }
	public void setAuthorID(int authorID) {	this.authorID = authorID; }
	
	public LocalDateTime getPostedDate() { return postedDate; }
	public void setPostedDate(LocalDateTime postedDate) { this.postedDate = postedDate;	}
	
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	
	public boolean getContainsFiles() { return containsFiles; }
	public void setContainsFiles(boolean containsFiles) { this.containsFiles = containsFiles; }
	
}

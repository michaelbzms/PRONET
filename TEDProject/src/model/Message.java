package model;

import java.time.LocalDateTime;


public class Message {
	
	/* Characteristics */
	private String text = null;
	private int professionalID1 = -1, professionalID2 = -1;
	private int sentByProfID = -1;
	private boolean containsFiles = false;
	private LocalDateTime timeSent = null;

	/* Setters & Getters */
	public void setText(String text) { this.text = text; }
	public String getText() { return this.text; }

	public int getProfessionalID1() { return professionalID1; }
	public void setProfessionalID1(int professionalID1) { this.professionalID1 = professionalID1; }
	
	public int getProfessionalID2() { return professionalID2; }
	public void setProfessionalID2(int professionalID2) { this.professionalID2 = professionalID2; }
	
	public int getSentByProfID() { return sentByProfID; }
	public void setSentByProfID(int sentByProfID) { this.sentByProfID = sentByProfID; }
	
	public boolean getContainsFiles() { return containsFiles; }
	public void setContainsFiles(boolean containsFiles) { this.containsFiles = containsFiles; }
	
	public LocalDateTime getTimeSent() { return timeSent; }
	public void setTimeSent(LocalDateTime timeSent) { this.timeSent = timeSent; }
	
	/* Methods */
	public Message() {
		
	}
	
}

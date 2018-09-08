package model;

import java.time.LocalDateTime;


public class Notification {

	/* Characteristics */
	private String type = null;
	private String text = null;
	private int notifiedByProfID = -1;
	private int postID = -1;
	private long notificationID = -1;
	private LocalDateTime timeHappened = null;


	/* Setters & Getters */
	public String getType() { return type; }
	// No setter because it should not be changed

	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	public int getPostID() { return postID; }
	public void setPostID(int postID) { this.postID = postID; }
	
	public long getNotificationID() { return notificationID; }
	public void setNotificationID(long notificationID) { this.notificationID = notificationID; }
	
	public int getNotifiedByProfID() { return notifiedByProfID; }
	public void setNotifiedByProfID(int notifiedByProfID) { this.notifiedByProfID = notifiedByProfID; }
	
	public LocalDateTime getTimeHappened() { return timeHappened; }
	public void setTimeHappened(LocalDateTime time_happened) { this.timeHappened = time_happened; }
	
	/* Methods */
	public Notification(String type) {
		this.type = type;
	}

}

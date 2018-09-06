package model;

import java.time.LocalDateTime;


public class Notification {

	/* Characteristics */
	private boolean isComment;
	private String comment = null;
	private int notifiedByProfID = -1;
	private int articleID = -1;
	private long commentID = -1;
	private LocalDateTime timeHappened = null;


	/* Setters & Getters */
	public boolean getIsComment() { return isComment; }
	// no setter because it should not be changed
	
	public String getComment() { return comment; }
	public void setComment(String comment) { this.comment = comment; }
	
	public int getNotifiedByProfID() { return notifiedByProfID; }
	public void setNotifiedByProfID(int notifiedByProfID) { this.notifiedByProfID = notifiedByProfID; }

	public int getArticleID() { return articleID; }
	public void setArticleID(int articleID) { this.articleID = articleID; }
	
	public long getCommentID() { return commentID; }
	public void setCommentID(long commentID) { this.commentID = commentID; }
	
	public LocalDateTime getTimeHappened() { return timeHappened; }
	public void setTimeHappened(LocalDateTime time_happened) { this.timeHappened = time_happened; }
	
	/* Methods */
	public Notification(boolean is_comment) {
		this.isComment = is_comment;
	}

}

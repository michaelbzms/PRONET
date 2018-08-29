package model;

import java.time.LocalDateTime;

public class Application {
	
	/* Characteristics */
	private int ID = -1;
	private int adID = -1, profID = -1;
	private LocalDateTime applyDate = null;
	private String note = null;
	
	/* Setters & Getters*/
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		this.ID = iD;
	}

	public int getAdID() {
		return adID;
	}
	public void setAdID(int adID) {
		this.adID = adID;
	}
	
	public int getProfID() {
		return profID;
	}
	public void setProfID(int profID) {
		this.profID = profID;
	}
	
	public LocalDateTime getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(LocalDateTime applyDate) {
		this.applyDate = applyDate;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}

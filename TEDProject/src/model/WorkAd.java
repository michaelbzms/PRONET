package model;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "workAd")
@XmlType(propOrder = {"title", "description", "postedDate"})
public class WorkAd {
	
	/* Characteristics */
	private int ID = -1;
	private int publishedByID = -1;
	private String title = null, description = null;
	private LocalDateTime postedDate = null;
	
	/* Setters & Getters*/
	@XmlAttribute
	public int getID() { return ID; }
	public void setID(int iD) {	this.ID = iD; }
	
	@XmlTransient
	public int getPublishedByID() { return publishedByID; }
	public void setPublishedByID(int publishedByID) { this.publishedByID = publishedByID; }
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	@XmlJavaTypeAdapter(value = XMLLocalDateTimeAdapter.class)
	public LocalDateTime getPostedDate() { return postedDate; }
	public void setPostedDate(LocalDateTime postedDate) { this.postedDate = postedDate; }
	
}

package model;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "comment")
@XmlType(propOrder = {"articleID", "text", "dateWritten"})
public class Comment {

	/* Characteristics */
	private int ID;
	private int articleID, authorID;
	private String text;
	private LocalDateTime dateWritten;
	
	/* Setters & Getters */
	@XmlAttribute
	public int getID() { return ID; }
	public void setID(int iD) { ID = iD; }
	
	public int getArticleID() { return articleID; }
	public void setArticleID(int articleID) { this.articleID = articleID; }
	
	@XmlTransient
	public int getAuthorID() { return authorID; }
	public void setAuthorID(int authorID) {	this.authorID = authorID; }
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }

	@XmlJavaTypeAdapter(value = XMLLocalDateTimeAdapter.class)
	public LocalDateTime getDateWritten() {	return dateWritten; }
	public void setDateWritten(LocalDateTime dateWritten) { this.dateWritten = dateWritten; }
	
}

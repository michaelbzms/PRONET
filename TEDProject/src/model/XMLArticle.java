package model;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "article")
@XmlType(propOrder = {"content", "postedDate", "filePaths"})
public class XMLArticle {
	
	/* Characteristics */
	private int ID = -1;
	private int authorID = -1;
	private LocalDateTime postedDate = null;
	private String content = null;
	private List<String> filePaths = null;

	/* Setters & Getters */
	@XmlAttribute
	public int getID() { return ID; }
	public void setID(int iD) {	ID = iD; }
	
	@XmlTransient
	public int getAuthorID() { return authorID; }
	public void setAuthorID(int authorID) {	this.authorID = authorID; }
	
	@XmlJavaTypeAdapter(value = XMLLocalDateTimeAdapter.class)
	public LocalDateTime getPostedDate() { return postedDate; }
	public void setPostedDate(LocalDateTime postedDate) { this.postedDate = postedDate;	}
	
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	
	@XmlElementWrapper(name = "containedFiles") 
	@XmlElement(name = "filePath")
	public List<String> getFilePaths() { return filePaths; }
	public void setFilePaths(List<String> filePaths) { this.filePaths = filePaths; }
	
}

package model;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "professional")
public class XMLProfessional {

	private int ID;
	private Professional prof = null;
	private List<Article> profArticles = null;
	private List<WorkAd> profWorkAds = null;
	private List<Integer> profInterests = null;
	private List<Comment> profComments = null;
	private List<Integer> profNetwork = null;
	
	@XmlAttribute
	public int getID() { return ID;	}
	public void setID(int iD) { ID = iD; }
	
	@XmlElement(name = "bio")
	public Professional getProf() { return prof; }
	public void setProf(Professional prof) { this.prof = prof; }

	@XmlElementWrapper(name = "articles") 
	@XmlElement(name = "article")
	public List<Article> getProfArticles() { return profArticles; }
	public void setProfArticles(List<Article> profArticles) { this.profArticles = profArticles;	}

	@XmlElementWrapper(name = "workAds") 
	@XmlElement(name = "workAd")
	public List<WorkAd> getProfWorkAds() { return profWorkAds; }
	public void setProfWorkAds(List<WorkAd> profWorkAds) { this.profWorkAds = profWorkAds; }

	@XmlElementWrapper(name = "interests") 
	@XmlElement(name = "articleID")
	public List<Integer> getProfInterests() { return profInterests;	}
	public void setProfInterests(List<Integer> profInterests) { this.profInterests = profInterests; }
	
	@XmlElementWrapper(name = "comments") 
	@XmlElement(name = "comment")
	public List<Comment> getProfComments() { return profComments; }
	public void setProfComments(List<Comment> profComments) { this.profComments = profComments; }
	
	@XmlElementWrapper(name = "network") 
	@XmlElement(name = "profID")
	public List<Integer> getProfNetwork() {	return profNetwork;	}
	public void setProfNetwork(List<Integer> profNetwork) { this.profNetwork = profNetwork;	}
	
	public static void jaxbProfToXML(XMLProfessional xmlProf, String filename) {
		try {
            JAXBContext jcontext = JAXBContext.newInstance(XMLProfessional.class);
            Marshaller m = jcontext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            if (filename == null) {			// print to console
            	m.marshal(xmlProf, System.out);
            } else {
            	m.marshal(xmlProf, new File(filename));
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
	}
	
}

package model;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "professional")
public class XMLProfessional {

	private Professional prof = null;
	private List<Article> profArticles = null;
	private List<WorkAd> profWorkAds = null;
	//private List<Likes> profLikes = null;
	//private List<Comments> profComments = null;
	//private List<Integer> profNetwork = null;

	// XmlAttribute profID ?

	@XmlElement(name = "profBio")
	public Professional getProf() { return prof; }
	public void setProf(Professional prof) { this.prof = prof; }

	@XmlElementWrapper(name = "profArticles") 
	@XmlElement(name = "article")
	public List<Article> getProfArticles() { return profArticles; }
	public void setProfArticles(List<Article> profArticles) { this.profArticles = profArticles;	}

	@XmlElementWrapper(name = "profWorkAds") 
	@XmlElement(name = "workAd")
	public List<WorkAd> getProfWorkAds() { return profWorkAds; }
	public void setProfWorkAds(List<WorkAd> profWorkAds) { this.profWorkAds = profWorkAds; }
	
	
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

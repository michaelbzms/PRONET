package model;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "professionals")
public class XMLProfessionalList {

	private List<XMLProfessional> profList = null;
	
	@XmlElement(name = "professional")
	public List<XMLProfessional> getProfList() { return profList; }
	public void setProfList(List<XMLProfessional> profList) { this.profList = profList; }


	public static void jaxbProfListToXML(XMLProfessionalList xmlProfList, String filename) {
		try {
            JAXBContext jcontext = JAXBContext.newInstance(XMLProfessionalList.class);
            Marshaller m = jcontext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            if (filename == null) {			// print to console
            	m.marshal(xmlProfList, System.out);
            } else {
            	m.marshal(xmlProfList, new File(filename));
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
	}
	
	
}

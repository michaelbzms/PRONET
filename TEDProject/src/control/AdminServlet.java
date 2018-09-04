package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SiteFunctionality;
import model.XMLProfessional;
import model.XMLProfessionalList;


@WebServlet("/admin/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public AdminServlet() {
        super();
    }
    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String exportXML = request.getParameter("exportXML");
		RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/AdminPage.jsp");
		if (exportXML != null) {
			if (exportXML.equals("submitted")) {
				String profIDstrs[] = request.getParameterValues("profID");
				if (profIDstrs != null) {
					Integer profIDs[] = new Integer[profIDstrs.length];
					int i = 0;
					for (String profIDstr : profIDstrs) {
						profIDs[i++] = Integer.parseInt(profIDstr);
					}
				    XMLProfessionalList.jaxbProfListToXML(SiteFunctionality.createXMLprofList(profIDs), null);
				}
				// redirect to download button?
			}
			request.setAttribute("exportXML", exportXML);		// both for "form" and "submitted"
		} 
		RequetsDispatcherObj.forward(request, response);
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

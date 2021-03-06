package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MyUtil;
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
		if (exportXML != null) {
			if (exportXML.equals("form")) {
				RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/AdminPage.jsp");
				request.setAttribute("exportXML", exportXML);
				RequetsDispatcherObj.forward(request, response);
			} else if (exportXML.equals("submitted")) {
				String profIDstrs[] = request.getParameterValues("profID");
				if (profIDstrs != null) {
					Integer profIDs[] = new Integer[profIDstrs.length];
					int i = 0;
					for (String profIDstr : profIDstrs) {
						profIDs[i++] = Integer.parseInt(profIDstr);
					}
			        ServletContext context = getServletContext();
					String profListFilePath = context.getRealPath("/professionalsList.xml");
				    XMLProfessionalList.jaxbProfListToXML(SiteFunctionality.createXMLprofList(profIDs), profListFilePath);
				    response.addCookie(new Cookie("fileDownloading", "true"));
				    if (! MyUtil.forceDownloadFile(response, context, profListFilePath)) {
						request.setAttribute("errorType", "downloadFailed");
						RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
						RequetsDispatcherObj.forward(request, response);
				    }
				}
			} else if (exportXML.equals("downloadProf")) {
				String profIDstr = request.getParameter("ProfID");
				if (profIDstr != null) {
					int profID = Integer.parseInt(profIDstr);
			        ServletContext context = getServletContext();
					String profFilePath = context.getRealPath("/prof" + profID + ".xml");
				    XMLProfessional.jaxbProfToXML(SiteFunctionality.createXMLprof(profID), profFilePath);		    
				    if (! MyUtil.forceDownloadFile(response, context, profFilePath)) {
						request.setAttribute("errorType", "downloadFailed");
						RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
						RequetsDispatcherObj.forward(request, response);
				    }
				}
			}
		} else {
			RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/AdminPage.jsp");
			RequetsDispatcherObj.forward(request, response);
		}
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

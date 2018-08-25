package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/FileServlet")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public static final String SaveDirectory = "D:/TEDProjectStorage";
	
    
    public FileServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestedFile =  request.getParameter("file");
		String type = request.getParameter("type");
		if ( requestedFile == null || type == null ) {
			request.setAttribute("errorType", "404Request");
			RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
			RequetsDispatcherObj.forward(request, response);
		} else {
				switch (type) {
					case "profile":
						boolean success = serveFile("profile", requestedFile, response);
						if (!success) {
							if ( !serveErrorFile(response, "/images/errorImage.png") ) {
								request.setAttribute("errorType", "404Request");
								RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
								RequetsDispatcherObj.forward(request, response);
							}	
						}
						break;
					// case "image"  //TODO
					// case "video"  //TODO
					default:
						request.setAttribute("errorType", "404Request");
						RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
						RequetsDispatcherObj.forward(request, response);
						break;
				}
				
		}	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	private boolean serveErrorFile(HttpServletResponse response, String errorFilePath) {
		// Get the absolute path of the image
		ServletContext sc = getServletContext();
		String filepath = sc.getRealPath(errorFilePath);
		
		// check if file exists
		File f = new File(filepath);
		if(!f.exists()) { 
			System.out.println("Requested file that doesn't exist: " + filepath);
		    return false;
		}
		
		// Get the MIME type of the image
		String mimeType = sc.getMimeType(filepath);
		if (mimeType == null) {
			sc.log("Could not get MIME type of "+ filepath);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return false;
		}
	
		// Set content type
		response.setContentType(mimeType);
	
		// Set content size
		File file = new File(filepath);
		response.setContentLength((int)file.length());
	
		// write image to output
		try {
			FileInputStream in = new FileInputStream(file);
			OutputStream out;
			out = response.getOutputStream();
			// Copy the contents of the file to the output stream
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			System.err.println("FileServlet could NOT fetch requested image!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean serveFile(String fileFolder, String fileName,  HttpServletResponse response) {
		// Get the absolute path of the image
		ServletContext sc = getServletContext();
		String filepath = SaveDirectory + "/" + fileFolder + "/" + fileName;
		
		// check if file exists
		File f = new File(filepath);
		if(!f.exists()) { 
			System.out.println("Requested file that doesn't exist: " + filepath);
		    return false;
		}
		
		// Get the MIME type of the image
		String mimeType = sc.getMimeType(filepath);
		if (mimeType == null) {
			sc.log("Could not get MIME type of "+ filepath);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return false;
		}
	
		// Set content type
		response.setContentType(mimeType);
	
		// Set content size
		File file = new File(filepath);
		response.setContentLength((int)file.length());
	
		// write image to output
		try {
			FileInputStream in = new FileInputStream(file);
			OutputStream out;
			out = response.getOutputStream();
			// Copy the contents of the file to the output stream
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			System.err.println("FileServlet could NOT fetch requested file!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}

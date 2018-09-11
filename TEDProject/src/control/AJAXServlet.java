package control;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.DataBaseBridge;
import model.FileManager;
import model.SiteFunctionality;


@WebServlet("/AJAXServlet")
@MultipartConfig(location = "D:/eclipse-workspace/TEDProject/WebContent/files", fileSizeThreshold = 1024*1024, maxFileSize = 25*1024*1024)      // this location is only a temporary save location in case we ran out of memory
public class AJAXServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ArticleSaveDirectory = FileServlet.SaveDirectory + "/article";
	private File Uploads = new File(ArticleSaveDirectory);
    private boolean warned = false;

    public AJAXServlet() {
        super();
        if (!Uploads.exists()){
        	Uploads.mkdirs();
        }
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Got an AJAX GET request!!!");
	    response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
	    response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
	    PrintWriter out = response.getWriter();	    
	    out.write("AJAX SAYS HI!");       // Write response body.
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// System.out.println("Got an AJAX POST request!!!");
	    response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
	    response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
	    PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		if (action == null) {
			out.write("Error: null action!");
		} else {
			RequestDispatcher RequetsDispatcherObj;
			String articleIDstr, authorIDstr;
			switch(action) {
				case "searchProfessional":
					RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/SearchResults.jsp");
					RequetsDispatcherObj.forward(request, response);		
					break;
				case "connectionRequest":
					String decision = request.getParameter("decision");
					String askerIDstr = request.getParameter("AskerID");
					String receiverIDstr = request.getParameter("ReceiverID");
					if ( decision == null || askerIDstr == null || receiverIDstr == null ) {
						out.write("AJAX connection request answer reached server with invalid parameters");
					} else {
						int AskerID = Integer.parseInt(askerIDstr);
						int ReceiverID = Integer.parseInt(receiverIDstr);
						SiteFunctionality.updateConnectionRequest(AskerID, ReceiverID, (decision.equals("accept")) ? true : false);
					}
					break;
				case "loadConvo":
					String homeprofIDstr = request.getParameter("homeprof");
					String awayprofIDstr = request.getParameter("awayprof");
					if ( homeprofIDstr == null || awayprofIDstr == null ) {
						out.write("AJAX load conversation request reached server with invalid parameters");
					} else {
						RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/Conversation.jsp");
						RequetsDispatcherObj.forward(request, response);
					}
					break;
				case "addMessage":
					String text = request.getParameter("text");
					String sentByProfStr = request.getParameter("sentBy");
					String sentToProfStr = request.getParameter("sentTo");
					if ( text == null || sentByProfStr == null || sentToProfStr == null ) {
						out.write("AJAX add message request reached server with invalid parameters"); 
					} 
					else if ( !SiteFunctionality.checkInputText(text, false, 0) ) {      // TODO: size restriction policy for messages?
						out.write("illegal text message input characters");
					}
					else {
						text = text.replace("\n", "\n<br>\n");
						int sentById, sentToId;
						try {
							sentById = Integer.parseInt(sentByProfStr);
							sentToId = Integer.parseInt(sentToProfStr);
						} catch ( NumberFormatException e ) {
							out.write("AJAX add message request reached server with invalid FORMAT parameters");
							return;
						}
						SiteFunctionality.addMessage(text, sentById, sentToId);
						out.write("success");
					}
					break;
				case "checkForNewMessages":             // this happens really often (ex: every 2 secs)
					String latestGot = request.getParameter("latestGot");
					String homeprof = request.getParameter("homeprof");
					String awayprof = request.getParameter("awayprof");
					if ( latestGot == null || homeprof == null || awayprof == null ) {
						if (!warned) { System.out.println("Invalid arguements at checkForNewMessages action in AJAXServlet.java"); warned = true; }
					} else {
						int homeprofID, awayprofID;
						try {
							homeprofID = Integer.parseInt(homeprof);
							awayprofID = Integer.parseInt(awayprof);
						} catch ( NumberFormatException e ) {
							if (!warned) { System.out.println("Could not cast ProfIDs to int at checkForNewMessages action in AJAXServlet.java"); warned = true; }
							return;
						}
						RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/NewMessages.jsp");
						RequetsDispatcherObj.forward(request, response);
					}
					break;
				case "addArticle":
					String postText = request.getParameter("text");
					Collection<Part> fileParts = request.getParts();
					if ( postText == null || fileParts == null ) {
						out.write("AJAX add article request reached server with invalid parameters");
						System.out.println("AJAX add article request reached server with invalid parameters");
					} else if ( !SiteFunctionality.checkInputText(postText, false, 0) ) {    // TODO: size restriction policy for article posts?
						out.write("illegal post text input characters");
					} else {
						postText = postText.replace("\n", "\n<br>\n");
						
						// remove all non-file Parts
						fileParts.removeIf((Part filePart) -> !filePart.getName().equals("file_input"));
						
						boolean containsFiles = false;
						for (Part filePart : fileParts) {
							String userUploadFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();  // MSIE fix
							if ( !userUploadFileName.isEmpty() ) {
								// debug:
								//System.out.println("- filePart: " + filePart.getName() + " , submitted file name: " + filePart.getSubmittedFileName() + ", type: " + filePart.getContentType());
								containsFiles = true;
							}
						}
						
						DataBaseBridge db = new DataBaseBridge();
						int articleID = SiteFunctionality.addArticle(request, db, postText, containsFiles);
						if ( articleID < 0 ) {
							out.write("failed to add article (wont attempt to save any files)");
							return;
						}
						if (containsFiles) {
							FileManager.saveArticleFiles(fileParts, ArticleSaveDirectory, db, articleID);
						}
						db.close();
						out.write(Integer.toString(articleID));
					}
					break;
				case "loadArticle":
					articleIDstr = request.getParameter("ArticleID");
					if ( articleIDstr == null ) {
						out.write("error: empty request for article");
					} else {
						int articleID = -1;
						try {
							articleID = Integer.parseInt(articleIDstr);
						} catch ( NumberFormatException e ) {
							out.write("error: article ID not an integer number");
							return;
						}   // "ArticleID" is already a request parameter as needed for Article.jsp
						RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/Article.jsp");
						RequetsDispatcherObj.forward(request, response);
					}
					break;
				case "deleteArticle":
					articleIDstr = request.getParameter("ArticleID");
					authorIDstr = request.getParameter("AuthorID");
					if (articleIDstr == null || authorIDstr == null) {
						out.write("AJAX delete comment request reached server with invalid parameters");
						System.out.println("AJAX delete comment request reached server with invalid parameters");
					} else {
						int articleID = -1;
						int authorID = -1;
						try {
							articleID = Integer.parseInt(articleIDstr);
							authorID = Integer.parseInt(authorIDstr);
						} catch ( NumberFormatException e ) {
							e.printStackTrace();	
							return;
						}
						if ( SiteFunctionality.deleteArticle(articleID, authorID) < 0 ) {
							out.write("failed to delete article");
							break;
						}
						out.write("success");
					}
					break;
				case "addComment":
					String commentText = request.getParameter("commentText");
					articleIDstr = request.getParameter("ArticleID");
					authorIDstr = request.getParameter("AuthorID");
					if (commentText == null || articleIDstr == null || authorIDstr == null) {
						out.write("AJAX add comment request reached server with invalid parameters");
						System.out.println("AJAX add comment request reached server with invalid parameters");
					} else if ( !SiteFunctionality.checkInputText(commentText, false, 0) ) {    // TODO: size restriction policy for article posts?
						out.write("illegal comment text input characters");
					} else {
						int articleID = -1;
						int authorID = -1;
						try {
							articleID = Integer.parseInt(articleIDstr);
							authorID = Integer.parseInt(authorIDstr);
						} catch ( NumberFormatException e ) {
							e.printStackTrace();	
							return;
						}
						commentText = commentText.replace("\n", "\n<br>\n");
						if ( SiteFunctionality.addComment(articleID, authorID, commentText) < 0 ) {
							out.write("failed to add comment");
							break;
						}
						out.write("success");
					}
					break;
				case "deleteComment":
					String commentIDstr = request.getParameter("CommentID");
					String sessionProfIDstr = request.getParameter("AuthorID");
					if (commentIDstr == null || sessionProfIDstr == null) {
						out.write("AJAX delete comment request reached server with invalid parameters");
						System.out.println("AJAX delete comment request reached server with invalid parameters");
					} else {
						int commentID = -1;
						int sessionProfID = -1;
						try {
							commentID = Integer.parseInt(commentIDstr);
							sessionProfID = Integer.parseInt(sessionProfIDstr);
						} catch ( NumberFormatException e ) {
							e.printStackTrace();	
							return;
						}
						if ( SiteFunctionality.deleteComment(commentID, sessionProfID) < 0 ) {
							out.write("failed to delete comment");
							break;
						}
						out.write("success");
					}
					break;
				case "toggleInterest":
					articleIDstr = request.getParameter("ArticleID");
					String profIDstr = request.getParameter("ProfID");
					if (articleIDstr == null || profIDstr == null) {
						out.write("AJAX add comment request reached server with invalid parameters");
						System.out.println("AJAX add comment request reached server with invalid parameters");
					} else {
						int articleID = -1;
						int profID = -1;
						try {
							articleID = Integer.parseInt(articleIDstr);
							profID = Integer.parseInt(profIDstr);
						} catch ( NumberFormatException e ) {
							e.printStackTrace();	
							return;
						}
						if ( SiteFunctionality.toggleInterest(articleID, profID) < 0 ) {
							out.write("failed to toggle interest");
							break;
						}
						out.write("success");
					}
					break;
				case "markAsSeen":
					String type = request.getParameter("type");
					String commentORapplicationIDstr = request.getParameter("commentORapplicationID");
					String interestBystr = request.getParameter("interestBy");
					String articleIdstr = request.getParameter("articleID");
					if ( type == null || commentORapplicationIDstr == null || interestBystr == null || articleIdstr == null ) {
						out.write("error: ajax request missing parameter(s)");
					} else {
						int articleId = -1, interestById = -1;
						long commentID = -1;
						int applicationID = -1;
						try {
							articleId = Integer.parseInt(articleIdstr);
							interestById = Integer.parseInt(interestBystr);
							if ( type.equals("comment") ) {
								commentID = Long.parseLong(commentORapplicationIDstr);
							} else if (type.equals("application")) {
								applicationID = Integer.parseInt(commentORapplicationIDstr);
							}
						} catch ( NumberFormatException e ) {
							out.write("error: parameter should be integer number but isn't");
							return;
						}
						if ( type.equals("interest") ) {
							DataBaseBridge db = new DataBaseBridge();
							db.markInterestAsSeen(articleId, interestById);
							db.close();
							out.write("success");
						} else if (type.equals("comment")) {
							DataBaseBridge db = new DataBaseBridge();
							db.markCommentAsSeen(commentID);
							db.close();
							out.write("success");
						} else if (type.equals("application")) {
							DataBaseBridge db = new DataBaseBridge();
							db.markApplicationAsSeen(applicationID);
							db.close();
							out.write("success");
						} else {
							out.write("error: ambiguous type parameter");
						}
					}
					break;
				default:
					out.write("Error: Invalid AJAX action!");
					break;
			}
		}
	}

}

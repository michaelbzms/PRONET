<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="model.DataBaseBridge, model.SiteFunctionality, model.Professional" %>

<%! private String addIDIfActive(String activePage, String page){
		if ( activePage.equals(page) ){
			return "id=\"active_page\"";
		} else {
			return "";
		}
	} %>

<%! private String addCLASSIfActive(String activePage, String page){
		if ( activePage.equals(page) ){
			return "active";
		} else {
			return "";
		}
	} %>

<% String activePage = request.getParameter("activePage"); %>

<nav class="navbar navbar-expand-xl bg-light justify-content-center">
	<div class="container-fluid">
	    <div class="navbar-header">
	      <a class="navbar-brand" href="/TEDProject/prof/NavigationServlet?page=HomePage">PRONET</a>
	    </div>
		<ul class="navbar-nav"  role="navigation">
			<li class="nav-item <%= addCLASSIfActive(activePage, "HomePage") %>">
				<a <%= addIDIfActive(activePage, "HomePage") %> class="nav-link" href="/TEDProject/prof/NavigationServlet?page=HomePage">Home Page</a>
			</li>
			<li class="nav-item <%= addCLASSIfActive(activePage, "Network") %>">
				<a <%= addIDIfActive(activePage, "Network") %> class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Network">Network</a>
			</li>
			<li class="nav-item <%= addCLASSIfActive(activePage, "WorkAds") %>">
				<a <%= addIDIfActive(activePage, "WorkAds") %> class="nav-link" href="/TEDProject/prof/NavigationServlet?page=WorkAds">Work Ads</a>
			</li>
			<li class="nav-item <%= addCLASSIfActive(activePage, "Messages") %>">
				<a <%= addIDIfActive(activePage, "Messages") %> class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Messages">Messages</a>
			</li>
			<li class="nav-item <%= addCLASSIfActive(activePage, "Notifications")%>">
				<a <%= addIDIfActive(activePage, "Notifications") %> class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Notifications">Notifications
				<% 	{ DataBaseBridge db = new DataBaseBridge();
					  Professional prof = SiteFunctionality.acquireProfFromSession(db, request);
					  int numberOfNotifications = db.getNumberOfNotifications(prof.getID());
					  if ( numberOfNotifications > 0 ){ %>
						<span id="numberOfNotifications" class="badge badge-danger"><%= numberOfNotifications %></span>
				<% 	  } 
				   	  db.close(); 
				   	} %>
				</a>
			</li>
			<li class="nav-item <%= addCLASSIfActive(activePage, "PersonalInformation") %>">
				<a <%= addIDIfActive(activePage, "PersonalInformation") %> class="nav-link" href="/TEDProject/ProfileLink">Personal Information</a>
			</li>
			<li class="nav-item <%= addCLASSIfActive(activePage, "Settings") %>">
				<a <%= addIDIfActive(activePage, "Settings") %> class="nav-link" href="/TEDProject/prof/NavigationServlet?page=Settings">Settings</a>
			</li>
			<li class="nav-item ml-xl-1">
				<form class="form-inline" action="/TEDProject/LogoutServlet" method="post">
					<input class="btn btn-primary" type="submit" value="Logout" >
				</form>
			</li>
		</ul>
	</div>
</nav>
    
package control;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter("/ProfAuthenticationFilter")
public class ProfAuthenticationFilter implements Filter {
	
    public ProfAuthenticationFilter() {}

	public void destroy() {}

	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("AuthenticationFilter initialized");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        if ( session == null || session.getAttribute("ProfID") == null || session.getAttribute("isAdmin") == null ) {   
        	System.out.println("Unauthorized access request - no active session.");
    		request.setAttribute("errorType", "nullSession");
    		RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
    		RequetsDispatcherObj.forward(request, response);
        } else if ( (boolean) session.getAttribute("isAdmin") ) {
        	System.out.println("Unauthorized access request - admin attemted to access prof page.");
    		request.setAttribute("errorType", "unauthorizedAccess");
    		RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
    		RequetsDispatcherObj.forward(request, response);
        } else {
            // pass the filter
            chain.doFilter(request, response);
        }
	}

}

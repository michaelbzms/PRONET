package control;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class ActiveSessionFilter
 */
@WebFilter("/ProfAuthenticationFilter")
public class ProfAuthenticationFilter implements Filter {

	private ServletContext context;
	
    public ProfAuthenticationFilter() {
    	// TODO Auto-generated method stub
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
        this.context.log("AuthenticationFilter initialized");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        //String uri = req.getRequestURI();
        if ( session == null ) {   
            this.context.log("Unauthorized access request - no active session.");
    		request.setAttribute("errorType", "nullSession");
    		RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
    		RequetsDispatcherObj.forward(request, response);
        } else if ( (boolean) session.getAttribute("isAdmin") ) {
            this.context.log("Unauthorized access request - admin attemted to access prof page.");
    		request.setAttribute("errorType", "unauthorizedAccess");
    		RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/WEB-INF/JSPs/ErrorPage.jsp");
    		RequetsDispatcherObj.forward(request, response);
        } else {
            // pass the filter
            chain.doFilter(request, response);
        }
	}

}

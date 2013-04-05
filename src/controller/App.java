package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class App
 */
@WebFilter("/App")
public class App implements Filter {

    /**
     * Default constructor. 
     */
    public App() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getRequestURI().replaceFirst(req.getContextPath(), "");				
		
		String topfolder = path.substring(1);
		if (topfolder.contains("/"))
		{
			// either css or javascript
			chain.doFilter(request, response);
		}
		else
		{
			// render normal page
			String page = topfolder;
			String parameter = "";
			if (topfolder.contains("?"))
			{
				page = topfolder.substring(topfolder.indexOf('?'));
				parameter = topfolder.substring(topfolder.indexOf('?')+1, topfolder.length() - topfolder.indexOf('?') - 1);
			}
			request.getRequestDispatcher("/MainApp?page="+page+parameter).forward(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

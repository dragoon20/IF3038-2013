package com.template;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class BasicFilter
 */
public class BasicFilter implements Filter {

    /**
     * Default constructor. 
     */
    public BasicFilter() 
    {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() 
	{
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
			// either css or javascript or page
			chain.doFilter(request, response);
		}
		else
		{
			// render normal page
			//chain.doFilter(request, response);
			String page = topfolder;
			String parameter = "";
			if (topfolder.contains("?"))
			{
				page = topfolder.substring(topfolder.indexOf('?'));
				parameter = topfolder.substring(topfolder.indexOf('?')+1, topfolder.length() - topfolder.indexOf('?') - 1);
			}
			request.setAttribute("page", page);
			request.getRequestDispatcher("/authorize?"+parameter).forward(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException 
	{
	}

}

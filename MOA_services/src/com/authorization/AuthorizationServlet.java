package com.authorization;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.DBConnection;
import com.models.DBSimpleRecord;
import com.models.User;

/**
 * Servlet implementation class AuthorizationServlet
 */
public class AuthorizationServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public AuthorizationServlet() 
    {
        super();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException 
    {
    	processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException 
    {
    	processRequest(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    {
    	String page = ("".equals((String)request.getAttribute("page")))? "index" : (String)request.getAttribute("page");
    	
    	Class<?>[] param_handler = new Class[2];
		param_handler[0] = HttpServletRequest.class;
		param_handler[1] = HttpServletResponse.class;
		
		try {
			Method method;
			method = this.getClass().getMethod(page, param_handler);				
			method.invoke(this, request, response);			
		} catch (NoSuchMethodException e) {
			// TODO Redirect to error page
			System.out.println("-----------------");
			System.out.println(page);
			System.out.println("-----------------");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void register_app_check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	if (("POST".equals(request.getMethod())) && (request.getParameter("app_name")!=null) && (request.getParameter("redirect_url")!=null))
    	{
    		try 
    		{
	    		Connection conn = DBConnection.getConnection();
	    		String app_id = DBSimpleRecord.MD5(request.getParameter("app_name") + UUID.randomUUID());
				PreparedStatement prep = conn.prepareStatement("INSERT INTO `applications` (app_name, app_id, redirect_url) VALUES (?, ?, ?)");
				prep.setString(1, request.getParameter("app_name"));
				prep.setString(2, app_id);
				prep.setString(3, request.getParameter("redirect_url"));
				if (prep.executeUpdate()==1)	
				{
					request.setAttribute("app_id", app_id);
					request.getRequestDispatcher("pages/register_app_success.jsp").forward(request, response);
				}
				else
				{
					throw new Exception();
				}
			} catch (Exception e) 
			{
				e.printStackTrace();
				request.getRequestDispatcher("pages/error.jsp").forward(request, response);
			}
    	}
    	else
    	{
    		request.getRequestDispatcher("pages/error.jsp").forward(request, response);
    	}
    }
    
    public void login_check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	if (("POST".equals(request.getMethod())) &&
    		(request.getParameter("username")!=null) && (request.getParameter("password")!=null)) 
		{
    		try 
			{
	    		Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement("SELECT * FROM `applications` WHERE app_id = ?");
				prep.setString(1, request.getParameter("app_id"));
				ResultSet rs = prep.executeQuery();
				if ((rs.last()) && (rs.getRow()==1))
				{
					String redirect_url = rs.getString(4);
					int id_app = rs.getInt(1);
					String query = "username = ? AND password = ?";
		    		User user = (User)User.getModel().find(query, 
		    				new Object[]{request.getParameter("username"), DBSimpleRecord.MD5(request.getParameter("password"))}, 
		    				new String[]{"string", "string"}, null);
		    		
		    		if (!user.isEmpty())
		    		{
		    			String token = DBSimpleRecord.MD5(user.getUsername() + UUID.randomUUID());
		    			
		    			conn = DBConnection.getConnection();
		    			prep = conn.prepareStatement("SELECT * FROM `tokens` WHERE id_user = ? AND id_app = ?");
		    			prep.setInt(1, user.getId_user());
		    			prep.setInt(2, id_app);
		    			rs = prep.executeQuery();
		    			
		    			if ((rs.last()) && (rs.getRow()==1))
		    			{
		    				conn = DBConnection.getConnection();
			    			prep = conn.prepareStatement("UPDATE `tokens` set timestamp = NOW(), token = ? WHERE id_user = ? AND id_app = ?");
			    			prep.setString(1, token);
			    			prep.setInt(2, user.getId_user());
			    			prep.setInt(3, id_app);
			    			
			    			int affected = prep.executeUpdate();
			    			if (affected == 1)
			    			{
			    				response.sendRedirect(redirect_url+"?token="+token);
			    			}
		    			}
		    			else if (rs.getRow()==0)
		    			{
			    			conn = DBConnection.getConnection();
			    			prep = conn.prepareStatement("INSERT INTO `tokens` (timestamp, token, id_user, id_app) " +
			    								"VALUES (NOW(), ?, ?, ?)");
			    			prep.setString(1, token);
			    			prep.setInt(2, user.getId_user());
			    			prep.setInt(3, id_app);
			    			
			    			int affected = prep.executeUpdate();
			    			if (affected == 1)
			    			{
			    				response.sendRedirect(redirect_url+"?token="+token);
			    			}

		    			}
		    		}
				}
				else
				{
					throw new Exception();
				}
			} catch (Exception e) 
			{
				e.printStackTrace();
				request.getRequestDispatcher("pages/error.jsp").forward(request, response);
			}
		}
    }

    public void register_app(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	request.getRequestDispatcher("pages/register_app.jsp").forward(request, response);
    }
    
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	if (request.getParameter("app_id")!=null)
    	{
    		try 
			{
	    		Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement("SELECT redirect_url FROM `applications` WHERE app_id = ?");
				prep.setString(1, request.getParameter("app_id"));
				ResultSet rs = prep.executeQuery();
				if ((rs.last()) && (rs.getRow()==1))
				{
					request.getRequestDispatcher("pages/login.jsp").forward(request, response);
				}
				else
				{
					throw new Exception();
				}
			} catch (Exception e) 
			{
				e.printStackTrace();
				request.getRequestDispatcher("pages/error.jsp").forward(request, response);
			}
    	}
    	else
    	{
    		request.getRequestDispatcher("pages/error.jsp").forward(request, response);
    	}
    }
}

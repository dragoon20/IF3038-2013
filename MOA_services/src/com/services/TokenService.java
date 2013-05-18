package com.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.helper.GeneralHelper;
import com.models.DBConnection;
import com.models.DBSimpleRecord;
import com.models.User;
import com.template.BasicServlet;

public class TokenService extends BasicServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		boolean status = false;
		String token_ret = "";
		if (("POST".equals(request.getMethod())) && (request.getParameter("app_id")!=null) && 
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
			    				status = true;
			    				token_ret = token;
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
			    				status = true;
			    				token_ret = token;
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
				//request.getRequestDispatcher("pages/error.jsp").forward(request, response);
			}
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("token", token_ret);
		
		JSONObject ret = new JSONObject(map);
		PrintWriter pw = response.getWriter();
		pw.println(ret.toJSONString());
		pw.close();
	}
	
	public void check_token(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		boolean status = false;
		if ((request.getParameter("token")!=null) && (request.getParameter("app_id")!=null))
		{
			status = (GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")) != -1);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		
		JSONObject ret = new JSONObject(map);
		PrintWriter pw = response.getWriter();
		pw.println(ret.toJSONString());
		pw.close();
	}
	
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		boolean status = false;
		if ((request.getParameter("token")!=null) && (request.getParameter("app_id")!=null))
		{
			int id_user = -1;
			status = ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id"))) != -1);
			if (status)
			{
				try
				{
					Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement("DELETE FROM `tokens` WHERE id_user = ? AND id_app IN (SELECT id FROM `applications` WHERE app_id = ?)");
					prep.setInt(1, id_user);
					prep.setString(2, request.getParameter("app_id"));
					status = (prep.executeUpdate()==1);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		
		JSONObject ret = new JSONObject(map);
		PrintWriter pw = response.getWriter();
		pw.println(ret.toJSONString());
		pw.close();
	}
}

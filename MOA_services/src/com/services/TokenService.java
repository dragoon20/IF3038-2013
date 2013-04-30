package com.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.helper.GeneralHelper;
import com.models.DBConnection;
import com.template.BasicServlet;

public class TokenService extends BasicServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

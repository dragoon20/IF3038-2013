package com.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.helper.GeneralHelper;
import com.models.DBConnection;
import com.template.BasicServlet;

/**
 * Servlet implementation class TagService
 */
public class TagService extends BasicServlet 
{
	private static final long serialVersionUID = 1L;
	
	public void delete_tag(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_tag")!=null)
				{
					Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement("DELETE FROM `tag` WHERE id_tag = ?");
					prep.setInt(1, Integer.parseInt(request.getParameter("id_tag")));
					
					boolean success = (prep.executeUpdate()==1);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", success);
					JSONObject ret = new JSONObject(map);
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
				}
				else
				{
					throw new Exception();
				}
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(ret));
		}
	}
}

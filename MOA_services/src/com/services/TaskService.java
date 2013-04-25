package com.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.helper.GeneralHelper;
import com.models.Category;
import com.models.DBSimpleRecord;
import com.models.Task;
import com.template.BasicServlet;

public class TaskService extends BasicServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void add_new_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin(session, request.getParameter("token")))!=-1))
			{
				if (("POST".equals(request.getMethod())) && 
					(request.getParameter("nama_task")!=null) && (request.getParameter("deadline")!=null) && 
					(request.getParameter("id_kategori")!=null) && 
					(((Category)Category.getModel().find("id_kategori = ?", new Object[]{Integer.parseInt(request.getParameter("id_kategori"))}, new String[]{"integer"}, null)).getEditable(id_user)))
				{
					Task task = new Task();
					task.setNama_task(request.getParameter("nama_task"));
					task.setDeadline(new Date(DBSimpleRecord.sdf.parse(request.getParameter("deadline")).getTime()));
					task.setId_kategori(Integer.parseInt(request.getParameter("id_kategori")));
					task.setId_user(id_user);
					
					if ((!task.checkValidity()) && (task.save()))
					{
						// return SOAP response
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
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
			request.getRequestDispatcher("pages/error.jsp").forward(request, response);
		}
	}
	
	public void update_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin(session, request.getParameter("token")))!=-1))
			{
				Task task;
				if (("POST".equals(request.getMethod())) && 
					(request.getParameter("id_task")!=null) && (request.getParameter("nama_task")!=null) && 
					(request.getParameter("deadline")!=null) && (request.getParameter("id_kategori")!=null) && 
					((task = (Task)Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).getEditable(id_user)))
				{
					task.setNama_task(request.getParameter("nama_task"));
					task.setDeadline(new Date(DBSimpleRecord.sdf.parse(request.getParameter("deadline")).getTime()));
					task.setId_kategori(Integer.parseInt(request.getParameter("id_kategori")));
					task.setId_user(id_user);
					
					if ((!task.checkValidity()) && (task.save()))
					{
						Map<String, Boolean> map = new HashMap<String, Boolean>();
						map.put("success", true);
						JSONObject ret = new JSONObject(map);
						
						PrintWriter pw = response.getWriter();
						pw.print(ret.toJSONString());
						pw.close();
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
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(ret.toJSONString());
			pw.close();
		}
	}
	
	public void delete_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin(session, request.getParameter("token")))!=-1))
			{
				if (("POST".equals(request.getMethod())) && 
					(request.getParameter("id_task")!=null) && (request.getParameter("nama_task")!=null) && 
					(request.getParameter("deadline")!=null) && (request.getParameter("id_kategori")!=null) && 
					(((Task)Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).getDeletable(id_user)))
				{
					if (Task.getModel().delete("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"})==1)
					{
						Map<String, Boolean> map = new HashMap<String, Boolean>();
						map.put("success", true);
						JSONObject ret = new JSONObject(map);
						
						PrintWriter pw = response.getWriter();
						pw.print(ret.toJSONString());
						pw.close();
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
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(ret.toJSONString());
			pw.close();
		}
	}
}


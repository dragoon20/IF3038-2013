package com.services;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.helper.GeneralHelper;
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
					(request.getParameter("nama_task")!=null) &&
					(request.getParameter("deadline")!=null) && (request.getParameter("id_kategori")!=null))
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
}

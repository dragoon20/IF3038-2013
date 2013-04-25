/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;
import java.io.PrintWriter;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.helper.GeneralHelper;
import com.models.DBSimpleRecord;
import com.models.Assign;
import com.template.BasicServlet;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author TOSHIBA
 */
public class AssignService extends BasicServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void add_assign(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin(session, request.getParameter("token")))!=-1))
			{
				if (("POST".equals(request.getMethod())) && 
					(request.getParameter("id_user")!=null))
				{
					Assign assign = new Assign();
                                        assign.setId_user(Integer.parseInt(request.getParameter("id_user")));
                                        
					if ((!assign.checkValidity()) && (assign.save()))
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
        
        /*** ----- START OF TASK MODULE -----***/
	
	/**
	 * Delete a task
	 * @return string contains whether success or fail
	 */
	public void delete_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
            if ("POST".equals(request.getMethod().toUpperCase()))
		{
			PrintWriter pw = response.getWriter();
			JSONObject ret = new JSONObject();
			
                    try{    
			int id_task;
                        int id_user;
			boolean success = false;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin(session, request.getParameter("token")))!=-1))
			{
				if (("POST".equals(request.getMethod())) && 
					(request.getParameter("user_id")!=null))
				{
					/*if(Assign.getModel().delete("id_user = ? AND id_task = ? ", new Object[]{Integer.parseInt(request.getParameter("user_id")),Integer.parseInt(request.getParameter("id_task"))}, params_type)){
                                        
                                        }*/
                                        
					/*if ((!assign.checkValidity()) && (assign.save()))
					{
						// return SOAP response
					}
					else
					{
						throw new Exception();
					}*/
				}
				else
				{
					throw new Exception();
				}
			}else
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
	
}


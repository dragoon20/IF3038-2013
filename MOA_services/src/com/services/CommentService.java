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
import com.models.Comment;
import com.template.BasicServlet;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author TOSHIBA
 */
public class CommentService extends BasicServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void add_comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin(session, request.getParameter("token")))!=-1))
			{
				if (("POST".equals(request.getMethod())) && (request.getParameter("id_task")!=null) && (request.getParameter("komentar")!=null))
				{
					Comment komentar = new Comment();
                                        komentar.setId_user(id_user);
                                        komentar.setId_task(Integer.parseInt(request.getParameter("id_task")));
                                        komentar.setKomentar(request.getParameter("komentar"));
					if ((!komentar.checkValidity()) && (komentar.save()))
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


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
import com.models.User;
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

	public void add_new_comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
        
	public void delete_comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin(session, request.getParameter("token")))!=-1))
			{
                            PrintWriter pw = response.getWriter();
                            JSONObject ret = new JSONObject();
				if (("POST".equals(request.getMethod())) && (request.getParameter("id")!=null))
				{
					if (Comment.getModel().delete("id_komentar = ? AND id_user = ? ", new Object[]{Integer.parseInt(request.getParameter("id")),id_user}, new String[]{"integer","integer"})==1){
                                            ret.put("status", "success");
                                        }else{
                                            ret.put("status", "fail");
                                        }
                                        pw.println(ret.toJSONString());
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
        
        public User getUser(){
            
        }
}


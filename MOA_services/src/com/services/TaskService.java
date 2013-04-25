package com.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONValue;

import com.helper.GeneralHelper;
import com.template.BasicServlet;

public class TaskService extends BasicServlet
{
	public void add_new_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int id_user;
		if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin(session, request.getParameter("token")))!=-1))
		{
			
		}
	}
}

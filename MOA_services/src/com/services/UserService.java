package com.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.json.simple.JSONValue;

import com.models.User;
import com.template.BasicServlet;

/**
 * Servlet implementation class User
 */
public class UserService extends BasicServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserService() 
    {
        super();
    }
    
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	boolean success = false;
    	if (("POST".equals(request.getMethod())) &&
    		(request.getParameter("username")!=null) && (request.getParameter("email")!=null) && 
    		(request.getParameter("fullname")!=null) && (request.getParameter("avatar")!=null) &&
    		(request.getParameter("avatar")!=null) && (request.getParameter("password")!=null))
		{
    		User new_user = new User();
    		new_user.addData(request.getParameterMap());
    		
    		if (!new_user.checkValidity())
    		{
    			success = new_user.save();
    		}
		}
		try {
			MessageFactory msgfactory = MessageFactory.newInstance();
			SOAPMessage out_message = msgfactory.createMessage();
		} catch (SOAPException e) {
			e.printStackTrace();
			request.getRequestDispatcher("pages/error.jsp").forward(request, response);
		}

    	// return SOAP response
    }
    
    public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
		pw.println(JSONValue.toJSONString("tes"));
	}
}

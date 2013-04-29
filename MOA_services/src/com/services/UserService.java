package com.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.helper.GeneralHelper;
import com.models.Category;
import com.models.Task;
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
    		(request.getParameter("password")!=null) && (request.getParameter("birthdate")!=null))
		{
                    User new_user = new User();
                    new_user.addData(request.getParameterMap());
                    
                    // DO THIS !!!!
                    //new_user.setPassword(DBSimpleRecord.MD5(null));
                    
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
                PrintWriter pw = response.getWriter();
		pw.println(JSONValue.toJSONString("No Problem"));
    	// return SOAP response
    }
    
    public void update_user(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
       try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin( request.getParameter("token"), GeneralHelper.app_id))!=-1))
			{
				if (("POST".equals(request.getMethod())) &&
    		(request.getParameter("username")!=null) && (request.getParameter("email")!=null) && 
    		(request.getParameter("fullname")!=null) && (request.getParameter("avatar")!=null) &&
    		(request.getParameter("password")!=null) && (request.getParameter("birthdate")!=null))
				{
					 User new_user = new User();
                                         new_user.addData(request.getParameterMap());
                                        
					if ((!new_user.checkValidity()) && (new_user.save()))
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
    
    public void get_created_tasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
		{
        	int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin( request.getParameter("token"), GeneralHelper.app_id))!=-1))
			{
                            User user = new User();
                            user.setId_user(id_user);

                            Task [] arrayOfTask = user.getCreatedTasks();
                            List<Map<String, String>> listOfTask = new ArrayList<Map<String, String>>();
                            for (Task task : arrayOfTask)
                                {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("id_task",""+task.getId_task());
                                        map.put("nama_task",task.getNama_task());
                                        map.put("status",""+task.isStatus());
                                        map.put("deadline",""+task.getDeadline());
                                        map.put("id_kategori",""+task.getId_kategori());
                                        map.put("id_user",""+task.getId_user());
                                        listOfTask.add(map);
                                }
                            PrintWriter pw = response.getWriter();
                            pw.println(listOfTask);
                            pw.close();
			}
			else
			{
				throw new Exception("Token tidak ada");
			}
		} catch(Exception e)
		{
                    e.printStackTrace();
                    Map<String, Boolean> map = new HashMap<String, Boolean>();
                    map.put("Succes", false);
                    JSONObject ret = new JSONObject(map);

                    PrintWriter pw = response.getWriter();
                    pw.print(ret.toJSONString());
                }
    }
    
    public void get_assigned_tasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin( request.getParameter("token"), GeneralHelper.app_id))!=-1))
			{
                            User user = new User();
                            user.setId_user(id_user);

                            Task [] arrayOfTask = user.getAssignedTasks();
                            List<Map<String, String>> listOfTask = new ArrayList<Map<String, String>>();
                            for (Task task : arrayOfTask)
                                {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("id_task",""+task.getId_task());
                                        map.put("nama_task",task.getNama_task());
                                        map.put("status",""+task.isStatus());
                                        map.put("deadline",""+task.getDeadline());
                                        map.put("id_kategori",""+task.getId_kategori());
                                        map.put("id_user",""+task.getId_user());
                                        listOfTask.add(map);
                                }

                            PrintWriter pw = response.getWriter();
                            pw.println(listOfTask);
                            pw.close();
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
                }
    }
    
    public void get_categories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin( request.getParameter("token"), GeneralHelper.app_id))!=-1))
			{
                            User user = new User();
                            user.setId_user(id_user);

                            Category [] arrayOfCategorys = user.getCategories();
                            List<Map<String, String>> listOfCategorys = new ArrayList<Map<String, String>>();
                            for (Category cat : arrayOfCategorys)
                                {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("id_kategori",""+cat.getId_kategori());
                                        map.put("nama_kategori",cat.getNama_kategori());
                                        map.put("id_user",""+cat.getId_user());
                                        listOfCategorys.add(map);
                                }

                            PrintWriter pw = response.getWriter();
                            pw.println(listOfCategorys);
                            pw.close();
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
                }
    }
    
    public void get_tasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin( request.getParameter("token"), GeneralHelper.app_id))!=-1))
			{
                            User user = new User();
                            user.setId_user(id_user);
                            Task [] arrayOfTask = null;
                            
                            if((request.getParameter("status")!=null) && (request.getParameter("id_kategori")!=null)){
                                arrayOfTask = user.getTasks(Integer.parseInt(request.getParameter("status")), Integer.parseInt(request.getParameter("id_kategori")));    
                            }else{
                                arrayOfTask = user.getTasks();    
                            }
                            List<Map<String, String>> listOfTask = new ArrayList<Map<String, String>>();
                                for (Task task : arrayOfTask)
                                    {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("id_task",""+task.getId_task());
                                            map.put("nama_task",task.getNama_task());
                                            map.put("status",""+task.isStatus());
                                            map.put("deadline",""+task.getDeadline());
                                            map.put("id_kategori",""+task.getId_kategori());
                                            map.put("id_user",""+task.getId_user());
                                            listOfTask.add(map);
                                    }

                                PrintWriter pw = response.getWriter();
                                pw.println(listOfTask);
                                pw.close();
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
                }
    }
    
    public void get_tasks_like(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin( request.getParameter("token"), GeneralHelper.app_id))!=-1))
			{
                            if((request.getParameter("key")!=null)){
                                User user = new User();
                                user.setId_user(id_user);

                                Task [] arrayOfTask = user.getTasksLike(request.getParameter("key"));
                                List<Map<String, String>> listOfTask = new ArrayList<Map<String, String>>();
                                for (Task task : arrayOfTask)
                                    {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("id_task",""+task.getId_task());
                                            map.put("nama_task",task.getNama_task());
                                            map.put("status",""+task.isStatus());
                                            map.put("deadline",""+task.getDeadline());
                                            map.put("id_kategori",""+task.getId_kategori());
                                            map.put("id_user",""+task.getId_user());
                                            listOfTask.add(map);
                                    }

                                PrintWriter pw = response.getWriter();
                                pw.println(listOfTask);
                                pw.close();
                            }else{
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
                }
    }
    
    public void get_categories_like(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin( request.getParameter("token"), GeneralHelper.app_id))!=-1))
			{
                            if((request.getParameter("key")!=null)){
                                User user = new User();
                                user.setId_user(id_user);

                                Category [] arrayOfCategorys = user.getCategoriesLike(request.getParameter("key"));
                                List<Map<String, String>> listOfCategorys = new ArrayList<Map<String, String>>();
                                for (Category cat : arrayOfCategorys)
                                    {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("id_kategori",""+cat.getId_kategori());
                                            map.put("nama_kategori",cat.getNama_kategori());
                                            map.put("id_user",""+cat.getId_user());
                                            listOfCategorys.add(map);
                                    }

                                PrintWriter pw = response.getWriter();
                                pw.println(listOfCategorys);
                                pw.close();
                            }else{
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
                }
    }
    
    public void find_all_like(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin( request.getParameter("token"), GeneralHelper.app_id))!=-1))
			{
                            if((request.getParameter("key")!=null)){
                                User user = new User();
                                user.setId_user(id_user);

                                User [] arrayOfUser = user.findAllLike(request.getParameter("key"));
                                List<Map<String, String>> listOfUser = new ArrayList<Map<String, String>>();
                                for (User usr : arrayOfUser)
                                    {
                                            Map<String, String> map = new HashMap<String, String>();
                                            
                                            map.put("id_user",""+usr.getId_user());
                                            map.put("username",""+usr.getUsername());
                                            map.put("email",""+usr.getEmail());
                                            map.put("fullname",""+usr.getFullname());
                                            map.put("avatar",""+usr.getAvatar());
                                            map.put("birthdate",""+usr.getBirthdate());
                                            
                                            listOfUser.add(map);
                                    }

                                PrintWriter pw = response.getWriter();
                                pw.println(listOfUser);
                                pw.close();
                            }else{
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
                }
    }
    
    public void find_by_username(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && ((id_user = GeneralHelper.isLogin( request.getParameter("token"), GeneralHelper.app_id))!=-1))
			{
                            if((request.getParameter("username")!=null)){
                                User user = new User();
                                user.setId_user(id_user);

                                User usr = user.findByUsername(request.getParameter("username"));
                                Map<String, String> map = new HashMap<String, String>();
                                            
                                map.put("id_user",""+usr.getId_user());
                                map.put("username",""+usr.getUsername());
                                map.put("email",""+usr.getEmail());
                                map.put("fullname",""+usr.getFullname());
                                map.put("avatar",""+usr.getAvatar());
                                map.put("birthdate",""+usr.getBirthdate());

                                PrintWriter pw = response.getWriter();
                                pw.println(map);
                                pw.close();
                            }else{
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
                }
    }
    
    public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
		pw.println(JSONValue.toJSONString("Hello World"));
	}
}

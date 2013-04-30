package com.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.helper.GeneralHelper;
import com.models.Category;
import com.models.DBSimpleRecord;
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
    
    public void update_user(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
       try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (("POST".equals(request.getMethod())) &&
	    		(request.getParameter("username")!=null) && (request.getParameter("email")!=null) && 
	    		(request.getParameter("fullname")!=null) && (request.getParameter("avatar")!=null) &&
	    		(request.getParameter("password")!=null) && (request.getParameter("birthdate")!=null))
				{
					User new_user = (User)User.getModel().find("id_user = ?", new Object[]{id_user}, new String[]{"integer"}, null);
                    new_user.addData(request.getParameterMap());
                    new_user.putData("confirm_password", new_user.getPassword());
                    
					if (!new_user.checkValidity())
					{
						new_user.hashPassword();
						if (new_user.save())
						{
							Map<String, Boolean> map = new HashMap<String, Boolean>();
							map.put("success", true);
							JSONObject ret = new JSONObject(map);
							
							PrintWriter pw = response.getWriter();
							pw.print(ret.toJSONString());
							pw.close();
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
    
    public void update_password(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
       try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (("POST".equals(request.getMethod())) &&
	    		(request.getParameter("password")!=null) && (request.getParameter("new_password")!=null) && (request.getParameter("confirm_password")!=null))
				{
					User new_user = (User)User.getModel().find("id_user = ?", new Object[]{id_user}, new String[]{"integer"}, null);
					
					if (new_user.getPassword()==DBSimpleRecord.MD5(request.getParameter("password")))
					{
	                    new_user.addData(request.getParameterMap());
	                    new_user.setPassword(request.getParameter("new_password"));
	                    
						if (!new_user.checkValidity())
						{
							new_user.hashPassword();
							if (new_user.save())
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
                pw.println(JSONValue.toJSONString(listOfTask));
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
                            pw.println(JSONValue.toJSONString(listOfTask));
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
                            pw.println(JSONValue.toJSONString(listOfCategorys));
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
                                pw.println(JSONValue.toJSONString(listOfTask));
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
                                pw.println(JSONValue.toJSONString(listOfTask));
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
                                pw.println(JSONValue.toJSONString(listOfCategorys));
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
                                            map.put("password",""+usr.getPassword());
                                            
                                            listOfUser.add(map);
                                    }

                                PrintWriter pw = response.getWriter();
                                pw.println(JSONValue.toJSONString(listOfUser));
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
                                pw.println(JSONValue.toJSONString(map));
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
    
    public void get_username(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
                if((request.getParameter("username")!=null))
                {
        			String[] users = request.getParameter("username").split(",");
        			StringBuilder not_query = new StringBuilder();
        			
        			List<Object> param = new ArrayList<Object>();
        			List<String> paramTypes = new ArrayList<String>();
        			param.add(users[users.length-1]+"%");
        			paramTypes.add("string");
        			for (int i=0;i<users.length-1;++i)
        			{
        				not_query.append(" AND ");
        				not_query.append(" username <> ? ");
        				param.add(users[i]);
        				paramTypes.add("string");
        			}
        			
        			List<DBSimpleRecord> list = Arrays.asList(User.getModel().findAll(" username LIKE ? "+not_query+" LIMIT 10", param.toArray(), paramTypes.toArray(new String[paramTypes.size()]), null));
        			User[] ret = list.toArray(new User[list.size()]);
        			List<String> result = new ArrayList<String>();
        			for (int i=0;i<ret.length;++i)
        			{
        				result.add(ret[i].getUsername());
        			}
        			
                	PrintWriter pw = response.getWriter();
        			pw.println(JSONValue.toJSONString(result));
        			pw.close();
                } else {
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
    
    public void register_check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
    	HashMap<String, Object> ret = new HashMap<String, Object>();
		if (("POST".equals(request.getMethod().toUpperCase())) && (request.getParameter("username")!=null) &&
				(request.getParameter("email")!=null) && (request.getParameter("password")!=null) && 
				(request.getParameter("confirm_password")!=null) && (request.getParameter("fullname")!=null) && 
				(request.getParameter("birthdate")!=null) && (request.getParameter("avatar")!=null))
		{
			String status = "success";
			List<String> errors = new ArrayList<String>();
			
			User user = new User();
			user.addData(request.getParameterMap());
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
			try {
				user.setBirthdate(new Date(sdf.parse(request.getParameter("birthdate")).getTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean temperror = user.checkValidity();
			
			if (temperror)
			{
				status = "fail";
				errors.add("Data-data yang dimasukkan tidak valid.");
			}
			
			if (!User.getModel().find("username = ? OR email = ? ", new Object[]{request.getParameter("username"), request.getParameter("email")}, new String[]{"string", "string"}, null).isEmpty())
			{
				status = "fail";
				errors.add("Username/email sudah digunakan.");
			}
			ret.put("status", status);
			ret.put("error", errors);
		}
		else
		{
			ret.put("status", "fail");
		}

		PrintWriter pw = response.getWriter();
		pw.println(new JSONObject(ret).toJSONString());
		pw.close();
	}
    
    public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
		pw.println(JSONValue.toJSONString("Hello World"));
	}
}

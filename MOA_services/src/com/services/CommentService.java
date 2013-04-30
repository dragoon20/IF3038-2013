package com.services;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.helper.GeneralHelper;
import com.models.Comment;
import com.models.Task;
import com.models.User;
import com.template.BasicServlet;

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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
		}
	}
        
	public void delete_comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(ret.toJSONString());
			pw.close();
		}
    }
        
    public void get_user(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
                if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
                {
                        if ((request.getParameter("id_komentar")!=null))
                        {
                               Comment komentar = (Comment)Comment.getModel().find("id_komentar = ?", new Object[]{Integer.parseInt(request.getParameter("id_komentar"))}, new String[]{"integer"}, null);
                               User user = komentar.getUser();
                               Map<String, String> map = new HashMap<String,String>();
                               
                               map.put("id_user", Integer.toString(user.getId_user()));
                               map.put("user_name", user.getUsername());
                               map.put("fullname",user.getFullname());
                               map.put("avatar",user.getAvatar());
                               JSONObject ret = new JSONObject(map);
				
                               PrintWriter pw = response.getWriter();
                               pw.print(ret.toJSONString());
                              
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("id_user", "");
                map.put("user_name", "");
                map.put("fullname","");
                map.put("avatar","");
                JSONObject ret = new JSONObject(map);
		
                PrintWriter pw = response.getWriter();
                pw.print(ret.toJSONString());
        }
    }
   
    public void get_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
                if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
                {
                        if ((request.getParameter("id_komentar")!=null))
                        {
                               Comment komentar = (Comment)Comment.getModel().find("id_komentar = ?", new Object[]{Integer.parseInt(request.getParameter("id_komentar"))}, new String[]{"integer"}, null);
                               Task task = komentar.getTask();
                               Map<String, String> map = new HashMap<String,String>();
                               
                               map.put("id_task", Integer.toString(task.getId_task()));
                               map.put("nama_task", task.getNama_task());
                               if (task.isStatus()){
                                   map.put("task_status", "selesai");
                               }else{
                                   map.put("task_status", "belum selesai");
                               }
                               map.put("deadline",task.getDeadline().toString());
                               map.put("id_kategori", Integer.toString(task.getId_kategori()));
                               map.put("id_task", Integer.toString(task.getId_task()));
                               JSONObject ret = new JSONObject(map);
				
                               PrintWriter pw = response.getWriter();
                               pw.print(ret.toJSONString());
                               
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("id_task", "");
                map.put("nama_task","");
                map.put("task_status", "");
                map.put("deadline","");
                map.put("id_kategori", "");
                map.put("id_task", "");
                JSONObject ret = new JSONObject(map);
		
                PrintWriter pw = response.getWriter();
                pw.print(ret.toJSONString());
        }
    }
    
    public void get_latest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
                if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
                {
                        if ((request.getParameter("id_task")!=null)&&(request.getParameter("timestamp")!=null))
                        {
                               Comment[] komentarlatest = Comment.getModel().getLatest(Integer.parseInt(request.getParameter("id_task")), request.getParameter("timestamp").toString());
                               
                               List<Map<String, String>> komentar_val = new ArrayList<Map<String, String>>();
                               for(Comment comment : komentarlatest){
                                   Map<String, String> map = new HashMap<String,String>();
                                   
                                   map.put("id_komentar", comment.getKomentar());
                                   map.put("timestamp", comment.getTimestamp().toString());
                                   map.put("komentar", comment.getKomentar());
                                   map.put("id_user",Integer.toString(comment.getId_user()));
                                   User user = comment.getUser();
                                   map.put("username",user.getUsername());
                                   map.put("fullname",user.getFullname());
                                   map.put("avatar", user.getAvatar());
                                   komentar_val.add(map);
                               }
				
                               PrintWriter pw = response.getWriter();
                               pw.print(JSONValue.toJSONString(komentar_val));
                               
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
                List<Map<String, String>> komentar_val = new ArrayList<Map<String, String>>();
                
                
                PrintWriter pw = response.getWriter();
                pw.print(JSONValue.toJSONString(komentar_val));
        }
    }
    
    public void get_older(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
                if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
                {
                        if ((request.getParameter("id_task")!=null)&&(request.getParameter("timestamp")!=null))
                        {
                               Comment[] komentarlatest = Comment.getModel().getOlder(Integer.parseInt(request.getParameter("id_task")), request.getParameter("timestamp").toString());
                               
                               List<Map<String, String>> komentar_val = new ArrayList<Map<String, String>>();
                               for(Comment comment : komentarlatest){
                                   Map<String, String> map = new HashMap<String,String>();
                                   
                                   map.put("id_komentar", comment.getKomentar());
                                   map.put("timestamp", comment.getTimestamp().toString());
                                   map.put("komentar", comment.getKomentar());
                                   map.put("id_user",Integer.toString(comment.getId_user()));
                                   User user = comment.getUser();
                                   map.put("username",user.getUsername());
                                   map.put("username",user.getFullname());
                                   map.put("avatar", user.getAvatar());
                                   komentar_val.add(map);
                               }
				
                               PrintWriter pw = response.getWriter();
                               pw.print(JSONValue.toJSONString(komentar_val));
                               
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
                List<Map<String, String>> komentar_val = new ArrayList<Map<String, String>>();
                
                
                PrintWriter pw = response.getWriter();
                pw.print(JSONValue.toJSONString(komentar_val));
        }
    }
}

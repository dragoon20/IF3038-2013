package com.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
import com.models.Attachment;
import com.models.Category;
import com.models.Comment;
import com.models.DBConnection;
import com.models.DBSimpleRecord;
import com.models.Tag;
import com.models.Task;
import com.models.User;
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
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
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (("POST".equals(request.getMethod())) && 
					(request.getParameter("id_task")!=null) &&  
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
	
	public void get_tags(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_task")!=null)
				{
					Task task = (Task)Task.getModel().find("id_task = ?", 
							new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null);
					Tag[] tags = task.getTags();
					List<Map<String, String>> tags_val = new ArrayList<Map<String, String>>();
					for (Tag tag : tags)
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("tag_name", tag.getTag_name());
						tags_val.add(map);
					}
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(tags_val));
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
			List<Map<String, String>> tags_val = new ArrayList<Map<String, String>>();
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(tags_val));
		}
	}
	
	public void get_category(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_task")!=null)
				{
					Task task = (Task)Task.getModel().find("id_task = ?", 
							new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null);
					
					Category category = task.getCategory();
					Map<String, String> map = new HashMap<String, String>();
					map.put("category", category.getNama_kategori());
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
			e.printStackTrace();
			Map<String, String> map = new HashMap<String, String>();
			map.put("category", "");
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(ret.toJSONString());
		}
	}
	
	public void get_attachments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_task")!=null)
				{
					Task task = (Task)Task.getModel().find("id_task = ?", 
							new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null);
					Attachment[] attachments = task.getAttachment();
					List<Map<String, String>> attachments_val = new ArrayList<Map<String, String>>();
					for (Attachment attachment : attachments)
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("attachment_link", attachment.getAttachment());
						attachments_val.add(map);
					}
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(attachments_val));
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
			List<Map<String, String>> attachments_val = new ArrayList<Map<String, String>>();
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(attachments_val));
		}
	}
	
	public void get_assignees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_task")!=null)
				{
					Task task = (Task)Task.getModel().find("id_task = ?", 
							new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null);
					User[] assignees = task.getAssignee();
					List<Map<String, String>> assignees_val = new ArrayList<Map<String, String>>();
					for (User assignee : assignees)
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("assignee_name", assignee.getUsername());
						assignees_val.add(map);
					}
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(assignees_val));
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
			List<Map<String, String>> assignees_val = new ArrayList<Map<String, String>>();
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(assignees_val));
		}
	}
	
	public void get_comments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_task")!=null)
				{
					Task task = (Task)Task.getModel().find("id_task = ?", 
							new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null);
					Comment[] comments = task.getComment();
					List<Map<String, Object>> comments_val = new ArrayList<Map<String, Object>>();
					for (Comment comment : comments)
					{
						Map<String, Object> map = new HashMap<String, Object>();
						
						User u = comment.getUser();
						Map<String, String> map_user = new HashMap<String, String>();
						map_user.put("id_user", ""+u.getId_user());
						map_user.put("username", u.getUsername());
						map_user.put("fullname", u.getFullname());
						map_user.put("avatar", u.getAvatar());
						
						map.put("user", map_user);
						map.put("comment", comment.getKomentar());
						comments_val.add(map);
					}
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(comments_val));
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
			List<Map<String, String>> comments_val = new ArrayList<Map<String, String>>();
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(comments_val));
		}
	}
	
	public void get_total_comments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_task")!=null)
				{
					Task task = (Task)Task.getModel().find("id_task = ?", 
							new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null);
					Integer total_comment = task.getTotalComment();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("total_comments", total_comment);
					JSONObject ret = new JSONObject(map);
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total_comments", 0);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(ret));
		}
	}
	
	public void get_editable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_task")!=null)
				{
					boolean success = ((Task)Task.getModel().find("id_task = ?", 
							new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).getEditable(id_user);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", success);
					JSONObject ret = new JSONObject(map);
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(ret));
		}
	}
	
	public void get_deleteable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_task")!=null)
				{
					boolean success = ((Task)Task.getModel().find("id_task = ?", 
							new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).getDeletable(id_user);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", success);
					JSONObject ret = new JSONObject(map);
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(ret));
		}
	}
	
	public void add_assignee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if ((request.getParameter("id_task")!=null) && (request.getParameter("id_user")!=null) && 
					(((Task)Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).getEditable(id_user)) && 
					(!((User)User.getModel().find("id_user = ?", new Object[]{Integer.parseInt(request.getParameter("id_user"))}, new String[]{"integer"}, null)).isEmpty()))
				{
					Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement("INSERT INTO `assign` (id_user, id_task) VALUES(?, ?)");
					prep.setInt(1, Integer.parseInt(request.getParameter("id_user")));
					prep.setInt(2, Integer.parseInt(request.getParameter("id_task")));
					
					boolean success = (prep.executeUpdate()==1);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", success);
					JSONObject ret = new JSONObject(map);
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(ret));
		}
	}
	
	public void add_tag(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if ((request.getParameter("id_task")!=null) && (request.getParameter("id_tag")!=null) && 
					(((Task)Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).getEditable(id_user)) && 
					(!((Tag)Tag.getModel().find("id_tag = ?", new Object[]{Integer.parseInt(request.getParameter("id_tag"))}, new String[]{"integer"}, null)).isEmpty()))
				{
					Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement("INSERT INTO `have_tags` (id_task, id_tag) VALUES(?, ?)");
					prep.setInt(1, Integer.parseInt(request.getParameter("id_task")));
					prep.setInt(2, Integer.parseInt(request.getParameter("id_tag")));
					
					boolean success = (prep.executeUpdate()==1);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", success);
					JSONObject ret = new JSONObject(map);
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(ret));
		}
	}
	
	public void delete_assignees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if ((request.getParameter("id_task")!=null) && 
					(((Task)Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).getEditable(id_user)))
				{
					Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement("DELETE FROM `assign` WHERE id_task = ?");
					prep.setInt(1, Integer.parseInt(request.getParameter("id_task")));
					
					boolean success = (prep.executeUpdate()==1);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", success);
					JSONObject ret = new JSONObject(map);
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(ret));
		}
	}
	
	public void delete_tags(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if ((request.getParameter("id_task")!=null) && 
					(((Task)Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).getEditable(id_user)))
				{
					Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement("DELETE FROM `have_tags` WHERE id_task = ?");
					prep.setInt(1, Integer.parseInt(request.getParameter("id_task")));
					
					boolean success = (prep.executeUpdate()>0);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", success);
					JSONObject ret = new JSONObject(map);
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(ret));
		}
	}
	
	public void delete_attachments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if ((request.getParameter("id_task")!=null) && 
					(((Task)Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).getEditable(id_user)))
				{
					Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement("DELETE FROM `task_attachment` WHERE id_task = ?");
					prep.setInt(1, Integer.parseInt(request.getParameter("id_task")));
					
					boolean success = (prep.executeUpdate()>0);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", success);
					JSONObject ret = new JSONObject(map);
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(ret));
		}
	}
}



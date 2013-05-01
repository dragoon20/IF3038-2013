package com.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
					(request.getParameter("deadline")!=null) && 
					((task = (Task)Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).getEditable(id_user)))
				{
					task.setNama_task(request.getParameter("nama_task"));
					task.setDeadline(new Date(DBSimpleRecord.sdf.parse(request.getParameter("deadline")).getTime()));
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
				if (("POST".equals(request.getMethod()) ) && 
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
	
	public void check_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_task")!=null)
				{
					Task task = (Task)Task.getModel().find("id_task = ?", 
							new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null);
					boolean success = !task.isEmpty();
					
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("success", success);

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
		} catch(Exception e)
		{
			e.printStackTrace();
			
			HashMap<String, Object> map = new HashMap<String, Object>();
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
					throw new Exception("ID task tidak ada");
				}
			}
			else
			{
				throw new Exception("token / app id tidak ada");
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
                                                map.put("assignee_id", ""+assignee.getId_user());
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
                                                map.put("id_comment", comment.getId_komentar());
						map.put("comment", comment.getKomentar());
                                                map.put("timestamp", comment.getTimestamp().toString());
                                                
                                                
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
	
	public void fetch_latest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if ((request.getParameter("id_task")!=null) && 
					(!((Task)Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null)).isEmpty()))
				{
					Task[] ret = null;
					
					if (request.getParameter("id_category")!=null)
					{
						ret = (Task[])Task.getModel().findAll("task_id > ? AND EXISTS (SELECT * FROM "+Category.getTableName()+
								"WHERE category_id = ? AND task_id = task.id)", 
								new Object[]{request.getParameter("id_task"), request.getParameter("id_category")}, 
								new String[]{"integer", "integer"}, null);
					}
					else
					{
						ret = (Task[])Task.getModel().findAll("task_id > ?", new Object[]{request.getParameter("id_task")}, new String[]{"integer"}, null);
					}
					
					PrintWriter pw = response.getWriter();
					pw.println(JSONValue.toJSONString(Arrays.asList(ret)));
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
		} catch(Exception e)
		{
			e.printStackTrace();

			PrintWriter pw = response.getWriter();
			pw.print("[]");
			pw.close();
		}
	}
	
	public void retrieve_tasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				PrintWriter pw = response.getWriter();
				HashMap<String, Object> ret = new HashMap<String, Object>();
				if (request.getParameter("category_id")!=null)
				{
					Category cat = (Category)Category.getModel().find("id_kategori = ?", new Object[]{Integer.parseInt(request.getParameter("category_id"))}, new String[]{"integer"}, null);
		
					if (!cat.isEmpty()) 
					{
						int categoryId = cat.getId_kategori();
						String categoryName = cat.getNama_kategori();
						boolean canDeleteCategory = (cat.getId_user()==id_user);
						boolean canEditCategory = ((canDeleteCategory) || (cat.getEditable(id_user)));
						
						ret.put("success", true);
						ret.put("categoryID", categoryId);
						ret.put("categoryName", categoryName);
						ret.put("canDeleteCategory", canDeleteCategory);
						ret.put("canEditCategory", canEditCategory);
						
						List<DBSimpleRecord> list = Arrays.asList(Task.getModel().findAll("id_kategori = ?", new Object[]{request.getParameter("category_id")}, new String[]{"string"}, null));
						Task[] tasks = list.toArray(new Task[list.size()]);
						
						SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY");
						List<Map<String, Object>> maps = new LinkedList<Map<String, Object>>();
						for (int i=0;i<tasks.length;++i)
						{
							Map<String, Object> map = new LinkedHashMap<String, Object>();
							map.put("name", tasks[i].getNama_task());
							map.put("id", tasks[i].getId_task());
							map.put("done", tasks[i].isStatus());
							map.put("deadline", sdf.format(tasks[i].getDeadline()));
							map.put("deletable", tasks[i].getDeletable(id_user));
							
							Tag[] tags = tasks[i].getTags();
							List<String> str_tags = new ArrayList<String>();
							for (Tag t : tags)
							{
								str_tags.add(t.getTag_name());
							}
							map.put("tags", str_tags);
							
							maps.add(map);
						}
						ret.put("tasks", maps);
						
						pw.println(new JSONObject(ret).toJSONString());
					}
					else 
					{
						// not found
						ret.put("success", false);
						pw.println(new JSONObject(ret).toJSONString());
					}
				}
				else 
				{
					ret.put("success", true);
					int id = id_user;
					List<DBSimpleRecord> list = Arrays.asList(Task.getModel().findAll("id_kategori IN (SELECT id_kategori FROM "+Category.getTableName()+" WHERE id_user = ? "+
							"OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user = ?) "+
							"OR id_kategori IN (SELECT id_kategori FROM "+Task.getTableName()+" AS t LEFT OUTER JOIN assign AS a "+
							"ON t.id_task=a.id_task WHERE t.id_user = ? OR a.id_user = ?))", 
							new Object[]{id, id, id, id}, new String[]{"integer", "integer", "integer", "integer"}, null));
					Task[] tasks = list.toArray(new Task[list.size()]);
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
					List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
					for (int i=0;i<tasks.length;++i)
					{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("name", tasks[i].getNama_task());
						map.put("id", tasks[i].getId_task());
						map.put("done", tasks[i].isStatus());
						map.put("deadline", sdf.format(tasks[i].getDeadline()));
						map.put("deletable", tasks[i].getDeletable(id_user));
						
						Tag[] tags = tasks[i].getTags();
						List<String> str_tags = new ArrayList<String>();
						for (Tag t : tags)
						{
							str_tags.add(t.getTag_name());
						}
						map.put("tags", str_tags);
						
						maps.add(map);
					}
					ret.put("tasks", maps);
					pw.println(new JSONObject(ret).toJSONString());
				}
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();

			PrintWriter pw = response.getWriter();
			pw.print("{}");
			pw.close();
		}
	}
	
	public void mark_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if ((request.getParameter("taskID")!=null) && (request.getParameter("completed")!=null))
				{
					PrintWriter pw = response.getWriter();
					HashMap<String, Object> ret = new HashMap<String, Object>();
					
					int id_task = Integer.parseInt(request.getParameter("taskID"));
					int completed = ("true".equals(request.getParameter("completed"))) ? 1 : 0;
					
					String update = "UPDATE "+Task.getTableName()+" SET status = ? WHERE id_task = ?";
					Connection conn = DBConnection.getConnection();
					PreparedStatement statement;
					
					statement = conn.prepareStatement(update);
					statement.setInt(1, completed);
					statement.setInt(2, id_task);
					int affectedrows = statement.executeUpdate();
					
					if (affectedrows >= 1)
					{
						ret.put("success", true);
						ret.put("taskId", id_task);
						ret.put("done", completed);
					}
					else
					{
						ret.put("success", false);
					}
					pw.println(new JSONObject(ret).toJSONString());
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

			PrintWriter pw = response.getWriter();
			pw.print("{success : false}");
			pw.close();
		}
	}
	
	public void search_suggestions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if ((request.getParameter("type")!=null) && (request.getParameter("q")!=null))
				{
					String type = request.getParameter("type");
					String q = request.getParameter("q");
					
					if (!"".equals(q))
					{
						PrintWriter pw = response.getWriter();
						
						boolean all = ("all".equals(type));
						
						List<String> suggestion = new ArrayList<String>();
						if (("task".equals(type)) || (all))
						{
							Task[] tasks  = ((User)User.getModel().find("id_user = ?", new Object[]{id_user}, new String[]{"integer"}, null)).getTasksLike(q);
							for (Task task : tasks)
							{
								if (!suggestion.contains(task.getNama_task()))
								{
									suggestion.add(task.getNama_task());
								}
							}
						}
						
						if (("category".equals(type)) || (all))
						{
							Category[] categories  = ((User)User.getModel().find("id_user = ?", new Object[]{id_user}, new String[]{"integer"}, null)).getCategoriesLike(q);
							for (Category category : categories)
							{
								if (!suggestion.contains(category.getNama_kategori()))
								{
									suggestion.add(category.getNama_kategori());
								}
							}
						}
						
						if (("user".equals(type)) || (all))
						{
							User[] users  = User.getModel().findAllLike(q);
							for (User user : users)
							{
								if (!suggestion.contains(user.getUsername()))
								{
									suggestion.add(user.getUsername());
								}
							}
						}
						
						pw.println(JSONValue.toJSONString(suggestion));
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

			PrintWriter pw = response.getWriter();
			pw.print("{success : false}");
			pw.close();
		}
	}
	
	public void search_tasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if ((request.getParameter("terms")!=null) && (request.getParameter("start")!=null))
				{
					List<DBSimpleRecord> list = Arrays.asList(Task.getModel().findAllLimit("id_kategori IN ( SELECT id_kategori FROM "+Category.getTableName()+" WHERE id_user=? "+
							 "OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user=?) "+
							 "OR id_kategori IN (SELECT id_kategori FROM "+ Task.getTableName() +" AS t LEFT OUTER JOIN assign AS a "+
							 "ON t.id_task=a.id_task WHERE t.id_user = ? OR a.id_user = ? )) AND nama_task LIKE ?"
							 , new Object[]{id_user, id_user, id_user, id_user, request.getParameter("terms")}, new String[]{"integer", "integer", "integer", "integer", "string"}, null, Integer.parseInt(request.getParameter("start")), 10));

					List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
					Task[] tasks = list.toArray(new Task[list.size()]);
					for (Task task : tasks)
					{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id_task", task.getId_task());
						map.put("nama_task", task.getNama_task());
						map.put("deadline", DBSimpleRecord.sdf.format(task.getDeadline()));
						map.put("id_kategori", task.getId_kategori());
						map.put("status", task.isStatus());
						
						ret.add(map);
					}
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
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
		} catch(Exception e)
		{
			e.printStackTrace();
			
			PrintWriter pw = response.getWriter();
			pw.print("[]");
			pw.close();
		}
	}
        
    public void get_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
        try
        {
            if ((request.getParameter("token")!=null) && (request.getParameter("app_id")!=null) && (GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1)
            {
                Task task = (Task)Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(request.getParameter("id_task"))}, new String[]{"integer"}, null);
                
                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put("id_task", ""+task.getId_task());
                hashMap.put("nama_task", task.getNama_task());
                hashMap.put("deadline", DBSimpleRecord.sdf.format(task.getDeadline()));
                hashMap.put("nama_kategori", task.getCategory().getNama_kategori());
                
                JSONObject ret = new JSONObject(hashMap);
                PrintWriter pw = response.getWriter();
                pw.println(ret.toJSONString());
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
}



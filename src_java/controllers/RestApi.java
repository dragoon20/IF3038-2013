package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import models.Category;
import models.Comment;
import models.DBConnection;
import models.DBSimpleRecord;
import models.Tag;
import models.Task;
import models.User;

/**
 * Servlet implementation class RestApi
 */
@WebServlet("/RestApi")
public class RestApi extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	private HttpSession session;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestApi() 
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		proccessRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		proccessRequest(request, response);
	}
	
	private void proccessRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		session = request.getSession();
		String api = request.getRequestURL().substring(request.getRequestURL().lastIndexOf("/")+1, request.getRequestURL().length());
		
		Class<?>[] param_handler = new Class[2];
		param_handler[0] = HttpServletRequest.class;
		param_handler[1] = HttpServletResponse.class;
		
		try {
			Method method;
			method = this.getClass().getMethod(api, param_handler);				
			method.invoke(this, request, response);
		} catch (NoSuchMethodException e) {
			// TODO Redirect to error page
			System.out.println("-----------------");
			System.out.println(api);
			System.out.println("-----------------");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fetch_latest_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (MainApp.LoggedIn(session))
		{
			if (request.getParameter("task_id")!=null)
			{
				PrintWriter pw = response.getWriter();
				JSONObject res = new JSONObject();
				
				if (request.getParameter("category_id")!=null)
				{
					Task[] ret = (Task[])Task.getModel().findAll("task_id > ? AND EXISTS (SELECT * FROM "+Category.getTableName()+
										"WHERE category_id = ? AND task_id = task.id)", 
										new Object[]{request.getParameter("task_id"), request.getParameter("task_id")}, 
										new String[]{"integer", "integer"}, null);
					res.put("", Arrays.asList(ret));
				}
				else
				{
					// retrieve all
					Task[] ret = (Task[])Task.getModel().findAll("task_id > ?", new Object[]{request.getParameter("task_id")}, new String[]{"integer"}, null);
					res.put("", Arrays.asList(ret));
				}
				pw.println(res.toJSONString());
			}
		}
	}
	
	public void retrieve_tags(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (MainApp.LoggedIn(session))
		{
			if (request.getParameter("tags")!=null)
			{
				// retrieve based on existing tags
				StringBuilder condition = new StringBuilder();
				/*$condition = "";
				foreach($params['tags'] as $tag)
				{
					$condition .= "tag_name != ".$tag." ";
				}
				$ret = Tag::model()->findAll($condition);*/
			}
			else
			{
				// retrieve all
				//$ret = Tag::model()->findAll();
			}
			// print result
			//return $ret;
		}
	}
	
	/**
	 * Get list of users search by username
	 * @return array of users with likability
	 */
	public void get_tag(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((request.getParameter("tag")!=null) && (MainApp.LoggedIn(session)))
		{	
			PrintWriter pw = response.getWriter();
			
			String[] tags = request.getParameter("tag").split(",");
			StringBuilder not_query = new StringBuilder();
			List<Object> param = new ArrayList<Object>();
			List<String> paramTypes = new ArrayList<String>();
			param.add(tags[tags.length-1]);
			paramTypes.add("string");
			for (int i=0;i<tags.length-1;++i)
			{
				not_query.append(" tag_name <> ? ");
				if (i!=tags.length-2)
				{
					not_query.append(" AND ");
				}
				param.add(tags[i]);
				paramTypes.add("string");
			}
			
			Tag[] ret = (Tag[])Tag.getModel().findAll(" tag_name LIKE '?%' "+not_query+" LIMIT 10", param.toArray(), (String[])paramTypes.toArray(), null);
			
			JSONObject res = new JSONObject();
			for (int i=0;i<ret.length;++i)
			{
				res.put(i, ret[i].getTag_name());
			}
			pw.println(res.toJSONString());
		}
	}
	
	
	public void retrieve_users(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//$ret = array();
		if (request.getParameter("users")!=null)
		{
			// retrieve based on existing users
			StringBuilder condition = new StringBuilder();
			/*foreach($params['users'] as $user)
			{
				$condition .= "username != ".$user." ";
			}
			$ret = User::model()->findAll($condition, array("username"));*/
		}
		else
		{
			// retrieve all
			//$ret = User::model()->findAll("", array("username"));
		}
		
		// print result
		//return $ret;
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
			
			String id_task = request.getParameter("task_id");
			boolean success = false;
			if (((Task)(Task.getModel().find("id_task = ?", new Object[]{id_task}, new String[]{"string"}, null))).getDeletable(MainApp.currentUserId(session)) && (MainApp.LoggedIn(session)))
			{
				if (Task.getModel().delete("id_task = ?", new Object[]{id_task}, new String[]{"string"})==1)
				{
					ret.put("success", true);
				}
				else
				{
					ret.put("success", false);
				}
			}
			ret.put("taskID", id_task);
			pw.println(ret.toJSONString());
		}
	}
	
	/**
	 * Retrieve list of tasks for dashboard
	 * @return array of tasks
	 */
	public void retrieve_tasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (MainApp.LoggedIn(session))
		{
			PrintWriter pw = response.getWriter();
			JSONObject ret = new JSONObject();
			if (request.getParameter("category_id")!=null)
			{
				// TODO check if user session is in some category
				// retrieve based on category
				Category cat = (Category)Category.getModel().find("id_kategori = ?", new Object[]{request.getParameter("category_id")}, new String[]{"integer"}, null);
	
				if (!cat.isEmpty()) 
				{
					int categoryId = cat.getId_kategori();
					String categoryName = cat.getNama_kategori();
					boolean canDeleteCategory = (cat.getId_user()==MainApp.currentUserId(session));
					boolean canEditCategory = (cat.getEditable(MainApp.currentUserId(session)));
					
					ret.put("success", true);
					ret.put("categoryId", categoryId);
					ret.put("categoryName", categoryName);
					ret.put("canDeleteCategory", canDeleteCategory);
					ret.put("canEditCategory", canEditCategory);
					
					Task[] tasks = (Task[])Task.getModel().findAll("id_kategori = ?", new Object[]{request.getParameter("category_id")}, new String[]{"string"}, null);
					
					SimpleDateFormat sdf = new SimpleDateFormat("j F Y");
					Map<String, Object>[] maps = new HashMap[tasks.length];
					for (int i=0;i<tasks.length;++i)
					{
						maps[i] = new HashMap<String, Object>();
						maps[i].put("name", tasks[i].getNama_task());
						maps[i].put("id", tasks[i].getId_task());
						maps[i].put("done", tasks[i].isStatus());
						maps[i].put("deadline", sdf.format(tasks[i].getDeadline()));
						maps[i].put("deletable", tasks[i].getDeletable(MainApp.currentUserId(session)));
						
						Tag[] tags = tasks[i].getTags();
						List<String> str_tags = new ArrayList<String>();
						for (Tag t : tags)
						{
							str_tags.add(t.getTag_name());
						}
						maps[i].put("tags", str_tags);
					}
					ret.put("tasks", maps);
					
					pw.println(ret.toJSONString());
				}
				else 
				{
					// not found
					ret.put("success", false);
					pw.println(ret.toJSONString());
				}
			}
			else 
			{
				ret.put("success", true);
				int id = MainApp.currentUserId(session);
				Task[] tasks = (Task[])Task.getModel().findAll("id_kategori IN (SELECT id_kategori FROM "+Category.getTableName()+" WHERE id_user = ? "+
																"OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user = ?) "+
																"OR id_kategori IN (SELECT id_kategori FROM "+Task.getTableName()+" AS t LEFT OUTER JOIN assign AS a "+
																"OR t.id_task=a.id_task WHERE t.id_user = ? OR a.id_user = ?))", 
																new Object[]{id, id, id, id}, new String[]{"integer", "integer", "integer", "integer"}, null);
				
				SimpleDateFormat sdf = new SimpleDateFormat("j F Y");
				Map<String, Object>[] maps = new HashMap[tasks.length];
				for (int i=0;i<tasks.length;++i)
				{
					maps[i] = new HashMap<String, Object>();
					maps[i].put("name", tasks[i].getNama_task());
					maps[i].put("id", tasks[i].getId_task());
					maps[i].put("done", tasks[i].isStatus());
					maps[i].put("deadline", sdf.format(tasks[i].getDeadline()));
					maps[i].put("deletable", tasks[i].getDeletable(MainApp.currentUserId(session)));
					
					Tag[] tags = tasks[i].getTags();
					List<String> str_tags = new ArrayList<String>();
					for (Tag t : tags)
					{
						str_tags.add(t.getTag_name());
					}
					maps[i].put("tags", str_tags);
				}
				ret.put("tasks", maps);
				pw.println(ret.toJSONString());
			}
		}
	}

	public void get_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((MainApp.LoggedIn(session)) && (request.getParameter("id_task")!=null))
		{
			// todo use prepared statement
			String id_task = request.getParameter("id_task");
			//$task = array();
			Task task = (Task)Task.getModel().find("id_task = ? ", new Object[]{id_task}, new String[]{"integer"}, new String[]{"id_task", "nama_task", "status", "deadline"});
	
			User[] users = task.getAssignee();
			List<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
			for (User user : users)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("username", user.getUsername());
				map.put("id_user", user.getId_user());
				temp.add(map);
			}
			task.putData("asignee", temp);
			
			Tag[] tags = task.getTags();
			
			
				/*$task = Task::model()->find("id_task=".$_GET['id_task'], array("id_task","nama_task","status","deadline"));
	
				$users = $task->getAssignee();
				$temp = array();
				$i = 0;
				foreach ($users as $user)
				{
					$temp[]['username'] = $user->username;
					$temp[$i]['id_user'] = $user->id_user;
					$i++;
				}
				$task->asignee = $temp;
				
				$tags = $task->getTags();
				$temp = array();
				foreach ($tags as $tag)
				{
					$temp[] = $tag->tag_name;
				}
				$task->tag = $temp;*/
			// print result
			//return $task->data;
		}
	}

	public void mark_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((MainApp.LoggedIn(session)) && (request.getParameter("taskID")!=null) && (request.getParameter("completed")!=null))
		{
			try 
			{
				PrintWriter pw = response.getWriter();
				JSONObject ret = new JSONObject();
				
				long start = System.nanoTime();
				int id_task = Integer.parseInt(request.getParameter("taskID"));
				int completed = ("true".equals(request.getParameter("completed"))) ? 1 : 0;
				
				String update = "UPDATE "+Task.getTableName()+" SET status = ? WHERE id_task = ?";
				Connection conn = DBConnection.getConnection();
				PreparedStatement statement;
				
				statement = conn.prepareStatement(update);
				statement.setInt(1, completed);
				statement.setInt(2, id_task);
				int affectedrows = statement.executeUpdate();
				
				if (affectedrows > 1)
				{
					ret.put("success", true);
					ret.put("taskId", id_task);
					ret.put("done", completed);
				}
				else
				{
					ret.put("success", false);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	/*** ----- END OF TASK MODULE -----***/
	
	/*** ----- START OF CATEGORY MODULE -----***/

	/**
	 * Retrieve list of category
	 * @return array of categories
	 */
	public void retrieve_categories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (MainApp.LoggedIn(session))
		{
			PrintWriter pw = response.getWriter();
			JSONObject res = new JSONObject();
			
			Category[] raw = MainApp.currentUser(session).getCategories();
			
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			for (Category cat : raw)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("nama_kategori", cat.getNama_kategori());
				map.put("id", cat.getId_kategori());
				map.put("canDeleteCategory", cat.getDeletable(MainApp.currentUserId(session)));
				map.put("canEditCategory", cat.getEditable(MainApp.currentUserId(session)));
				
				result.add(map);
			}
			res.put("", result);
		}
	}
	
	/**
	 * Add a new category
	 * @return array of new category data
	 */
	public void add_category(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (MainApp.LoggedIn(session))
		{
			if (("POST".equals(request.getMethod().toUpperCase())) && (request.getParameter("nama_kategori")!=null) && (request.getParameter("usernames_list")!=null))
			{
				try 
				{
					PrintWriter pw = response.getWriter();
					JSONObject res = new JSONObject();
					
					String nama_kategori = request.getParameter("nama_kategori");
					int id_user = MainApp.currentUserId(session);
					
					Category category = new Category();
					category.setNama_kategori(nama_kategori);
					category.setId_user(id_user);
					category.save();
					
					String[] usernames = request.getParameter("usernames_list").split(";");
					String[] paramType = new String[usernames.length];
					for (int i=0;i<usernames.length;++i)
					{
						usernames[i] = usernames[i].trim();
					}
					if (usernames.length > 1)
					{
						StringBuilder query = new StringBuilder("username IN (");
						for (int i=0;i<usernames.length;++i)
						{
							query.append("?");
							if (i!=usernames.length-1)
							{
								query.append(",");
							}
							paramType[i] = "string";
						}
						query.append(")");
						
						User[] users = (User[])User.getModel().findAll(query.toString(), usernames, paramType, null);
						Connection conn = DBConnection.getConnection();
						for (User user : users)
						{
							PreparedStatement prep = conn.prepareStatement("INSERT INTO edit_kategori (id_user, id_katego) VALUES (?, ?)");
							prep.setInt(1, user.getId_user());
							prep.setInt(2, category.getId_kategori());
						}
					}
				
					res.put("categoryID", category.getId_kategori());
					res.put("categoryName", category.getNama_kategori());
					
					
					Category[] raw = MainApp.currentUser(session).getCategories();
					
					List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
					for (Category cat : raw)
					{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("nama_kategori", cat.getNama_kategori());
						map.put("id", cat.getId_kategori());
						map.put("canDeleteCategory", cat.getDeletable(MainApp.currentUserId(session)));
						map.put("canEditCategory", cat.getEditable(MainApp.currentUserId(session)));
						
						result.add(map);
					}
					res.put("categories", result);
					
					pw.println(res.toJSONString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Delete a category
	 * @return string contains whether success or fail
	 */
	public void delete_category(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (MainApp.LoggedIn(session))
		{
			if (("POST".equals(request.getMethod().toUpperCase())) && (request.getParameter("category_id")!=null))
			{
				PrintWriter pw = response.getWriter();
				JSONObject res = new JSONObject();
				
				int id_kategori = Integer.parseInt(request.getParameter("category_id"));
				boolean success = false;
				
				if (((Category)Category.getModel().find("id_kategori = ?", new Object[]{id_kategori}, new String[]{"integer"}, null)).getDeletable(MainApp.currentUserId(session)))
				{
					if (Category.getModel().delete("id_kategori = ?", new Object[]{id_kategori}, new String[]{"integer"})==1)
					{
						success = true;
					}
				}
				
				res.put("success", success);
				pw.println(res.toJSONString());
			}
		}
	}

	/*** ----- END OF CATEGORY MODULE -----***/
	
	/*** ----- START OF COMMENT MODULE -----***/
	
	/**
	 * Get the previous comment from before timestamp
	 * @return array of comments before timestamp
	 */
	public void get_previous_comments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((request.getParameter("id_task")!=null) && (request.getParameter("timestamp")!=null) && (MainApp.LoggedIn(session)))
		{
			//$return = Comment::getOlder($params['id_task'], $params['timestamp']);
		}
		// print result
		//return $return;
	}
	
	/**
	 * Retrieve comment after timestamp
	 * @return array of comments after timestamp
	 */
	public void retrieve_comments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((request.getParameter("id_task")!=null) && (request.getParameter("timestamp")!=null) && (MainApp.LoggedIn(session)))
		{
			PrintWriter pw = response.getWriter();
			JSONObject ret = new JSONObject();
			
			Comment[] comments = Comment.getModel().getLatest(Integer.parseInt(request.getParameter("id_task")), request.getParameter("timestamp"));
			
			int i = 0;
			for (Comment c : comments)
			{
				ret.put(i, c.getData());
				i++;
			}
			pw.println(ret.toJSONString());
		}
	}
	
	/**
	 * Post a new comment
	 * @return string contains whether success or fail
	 */
	public void comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
		JSONObject ret = new JSONObject();
		if (("POST".equals(request.getMethod())) && (request.getParameter("komentar")!=null) && (MainApp.LoggedIn(session)))
		{
			Comment comment = new Comment();
			comment.addData(request.getParameterMap());
			comment.setId_user(MainApp.currentUserId(session));
			if (comment.save())
			{
				ret.put("status", "success");
			}
			else
			{
				ret.put("status", "fail");
			}
		}
		pw.println(ret.toJSONString());
	}
	
	/**
	 * Remove a comment
	 * @return string contains whether success or fail
	 */
	public void remove_comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
		JSONObject ret = new JSONObject();
		if (("POST".equals(request.getMethod())) && (request.getParameter("id")!=null) && (MainApp.LoggedIn(session)))
		{
			if (Comment.getModel().delete("id_komentar = ? AND id_user = ? ", new Object[]{request.getParameter("id"), MainApp.currentUserId(session)}, new String[]{"integer", "integer"})==1)
			{
				ret.put("status", "success");
			}
			else
			{
				ret.put("status", "fail");
			}
		}
		pw.println(ret.toJSONString());
	}
	
	/*** ----- END OF COMMENT MODULE -----***/
	
	/*** ----- START OF USER MODULE -----***/
	
	/**
	 * Get list of users search by username
	 * @return array of users with likability
	 */
	public void get_username(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((request.getParameter("username")!=null) && (MainApp.LoggedIn(session))) 
		{
			PrintWriter pw = response.getWriter();
			
			String[] users = request.getParameter("username").split(",");
			StringBuilder not_query = new StringBuilder();
			
			List<Object> param = new ArrayList<Object>();
			List<String> paramTypes = new ArrayList<String>();
			param.add(users[users.length-1]);
			paramTypes.add("string");
			for (int i=0;i<users.length-1;++i)
			{
				not_query.append(" username <> ? ");
				if (i!=users.length-2)
				{
					not_query.append(" AND ");
				}
				
				param.add(users[i]);
				paramTypes.add("string");
			}
			User[] ret = (User[])User.getModel().findAll(" username LIKE '?%' "+not_query+" LIMIT 10", param.toArray(), (String[])paramTypes.toArray(), null);
			JSONObject res = new JSONObject();
			for (int i=0;i<ret.length;++i)
			{
				res.put(i, ret[i].getUsername());
			}
			pw.println(res.toJSONString());
		}
	}
	
	/**
	 * Login using username and password through Rest API
	 * @return string contains whether success or fail
	 */
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		JSONObject ret = new JSONObject();
		if (("POST".equals(request.getMethod().toUpperCase())) && 
				(request.getParameter("username")!=null) && (request.getParameter("password")!=null))
		{
			
			User user = (User)User.getModel().find("username = ? AND password = ?", 
						new Object[]{request.getParameter("username"), DBSimpleRecord.MD5(request.getParameter("password"))}, 
						new String[]{"string", "string"},
						null);
			if ((user!=null) && (!user.isEmpty()))
			{
				session.setAttribute("user_id", user.getId_user());
				
				User u = new User();
				u.setId_user(user.getId_user());
				u.setFullname(user.getFullname());
				u.setUsername(user.getUsername());
				u.setEmail(user.getEmail());
				session.setAttribute("current_user", u);
				
				ret.put("status", "success");
			}
			else
			{
				ret.put("status", "fail");
			}
		}
		else 
		{
			ret.put("status", "fail");
		}
		
		PrintWriter pw = response.getWriter();
		pw.println(ret.toJSONString());
	}
	
	/**
	 * Check register parameter through Rest API
	 * @return array of status and possible errors
	 */
	public void register_check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		JSONObject ret = new JSONObject();
		if (("POST".equals(request.getMethod().toUpperCase())) && (request.getParameter("username")!=null) &&
				(request.getParameter("email")!=null) && (request.getParameter("password")!=null) && 
				(request.getParameter("confirm_password")!=null) && (request.getParameter("fullname")!=null) && 
				(request.getParameter("birthdate")!=null) && (request.getParameter("avatar")!=null))
		{
			String status = "success";
			List<String> errors = new ArrayList<String>();
			
			User user = new User();
			user.addData(request.getParameterMap());
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
			ret.put("errors", errors);
		}
		else
		{
			ret.put("status", "fail");
		}

		PrintWriter pw = response.getWriter();
		pw.println(ret.toJSONString());
	}

	/*** ----- END OF USER MODULE -----***/	

	/*** ----- START OF SEARCH MODULE -----***/
	public void search_suggestions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((MainApp.LoggedIn(session)) && (request.getParameter("type")!=null) && (request.getParameter("q")!=null))
		{
			String type = request.getParameter("type");
			String q = request.getParameter("q");
			
			if (!"".equals(q))
			{
				PrintWriter pw = response.getWriter();
				JSONObject ret = new JSONObject();
				
				boolean all = ("all".equals(type));
				
				List<String> suggestion = new ArrayList<String>();
				if (("task".equals(type)) || (all))
				{
					Task[] tasks  = MainApp.currentUser(session).getTasksLike(q);
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
					Category[] categories  = MainApp.currentUser(session).getCategoriesLike(q);
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
				
				ret.put("", suggestion);
			}
		}
	}
}

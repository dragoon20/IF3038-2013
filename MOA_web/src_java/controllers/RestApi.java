package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Comment;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

/**
 * Servlet implementation class RestApi
 */
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
				try {
					HashMap<String, String> parameter = new HashMap<String,String>();
					parameter.put("token", MainApp.token(session));
					parameter.put("app_id", MainApp.appId);
					parameter.put("id_task", request.getParameter("task_id"));
					parameter.put("nama_task", request.getParameter("category_id"));
					
					String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"task/fetch_latest", parameter, "", 0);
					
					PrintWriter pw = response.getWriter();
					pw.println(responseString);
					pw.close();
				} catch(Exception exc) {
					exc.printStackTrace();
				}
			}
		}
	}
	
	public void get_tag(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((request.getParameter("tag")!=null) && (MainApp.LoggedIn(session)))
		{	
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("tag", request.getParameter("tag"));
				
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"tag/get_tag", parameter, "", 0);
				
				PrintWriter pw = response.getWriter();
				pw.println(responseString);
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
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
<<<<<<< HEAD
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("id_task", request.getParameter("task_id"));
				
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"task/delete_task", parameter, "", 0);
				
				PrintWriter pw = response.getWriter();
				pw.println(responseString);
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
=======
			PrintWriter pw = response.getWriter();
			JSONObject ret = new JSONObject();
			
			String id_task = request.getParameter("task_id");
			boolean success = false;
			if (((Task)(Task.getModel().find("id_task = ?", new Object[]{Integer.parseInt(id_task)}, new String[]{"integer"}, null))).getDeletable("TOKEN",0))
			{
				if (Task.getModel().delete("id_task = ?", new Object[]{Integer.parseInt(id_task)}, new String[]{"integer"})==1)
				{
					ret.put("success", true);
				}
				else
				{
					ret.put("success", false);
				}
>>>>>>> ee3cc0b4d97340f4a8f86cada8e1d1a20a399122
			}
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
<<<<<<< HEAD
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("category_id", request.getParameter("category_id"));
				
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"task/retrieve_tasks", parameter, "", 0);
=======
			PrintWriter pw = response.getWriter();
			JSONObject ret = new JSONObject();
			if (request.getParameter("category_id")!=null)
			{
				// TODO check if user session is in some category
				// retrieve based on category
				Category cat = (Category)Category.getModel().find("id_kategori = ?", new Object[]{Integer.parseInt(request.getParameter("category_id"))}, new String[]{"integer"}, null);
	
				if (!cat.isEmpty()) 
				{
					int categoryId = cat.getId_kategori();
					String categoryName = cat.getNama_kategori();
					boolean canDeleteCategory = (cat.getId_user()==MainApp.currentUserId(session));
					//boolean canEditCategory = ((canDeleteCategory) || (cat.getEditable(MainApp.currentUserId(session))));
					
					ret.put("success", true);
					ret.put("categoryID", categoryId);
					ret.put("categoryName", categoryName);
					ret.put("canDeleteCategory", canDeleteCategory);
					//ret.put("canEditCategory", canEditCategory);
					
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
						map.put("deletable", tasks[i].getDeletable("TOKEN", 0));
						
						Tag[] tags = tasks[i].getTags("TOKEN",1);
						List<String> str_tags = new ArrayList<String>();
						for (Tag t : tags)
						{
							str_tags.add(t.getTag_name());
						}
						map.put("tags", str_tags);
						
						maps.add(map);
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
				List<DBSimpleRecord> list = Arrays.asList(Task.getModel().findAll("id_kategori IN (SELECT id_kategori FROM "+Category.getTableName()+" WHERE id_user = ? "+
						"OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user = ?) "+
						"OR id_kategori IN (SELECT id_kategori FROM "+Task.getTableName()+" AS t LEFT OUTER JOIN assign AS a "+
						"ON t.id_task=a.id_task WHERE t.id_user = ? OR a.id_user = ?))", 
						new Object[]{id, id, id, id}, new String[]{"integer", "integer", "integer", "integer"}, null));
				Task[] tasks = list.toArray(new Task[list.size()]);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY");
				List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
				for (int i=0;i<tasks.length;++i)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", tasks[i].getNama_task());
					map.put("id", tasks[i].getId_task());
					map.put("done", tasks[i].isStatus());
					map.put("deadline", sdf.format(tasks[i].getDeadline()));
					map.put("deletable", tasks[i].getDeletable("TOKEN", 0));
					
					Tag[] tags = tasks[i].getTags("TOKEN",0);
					List<String> str_tags = new ArrayList<String>();
					for (Tag t : tags)
					{
						str_tags.add(t.getTag_name());
					}
					map.put("tags", str_tags);
					
					maps.add(map);
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
			int id_task = Integer.parseInt(request.getParameter("id_task"));
			//$task = array();
			Task task = (Task)Task.getModel().find("id_task = ? ", new Object[]{id_task}, new String[]{"integer"}, new String[]{"id_task", "nama_task", "status", "deadline"});
			User[] users = task.getAssignee("TOKEN", 0);
			List<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
			for (User user : users)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("username", user.getUsername());
				map.put("id_user", user.getId_user());
				temp.add(map);
			}
			task.putData("asignee", temp);
			
			Tag[] tags = task.getTags("TOKEN",0);
			
			
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
>>>>>>> ee3cc0b4d97340f4a8f86cada8e1d1a20a399122
				
				PrintWriter pw = response.getWriter();
				pw.println(responseString);
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
	}

	public void mark_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((MainApp.LoggedIn(session)) && (request.getParameter("taskID")!=null) && (request.getParameter("completed")!=null))
		{
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("taskID", request.getParameter("taskID"));
				parameter.put("completed", request.getParameter("completed"));
				
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"task/mark_task", parameter, "", 0);
				
				PrintWriter pw = response.getWriter();
				pw.println(responseString);
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
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
<<<<<<< HEAD
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
=======
			PrintWriter pw = response.getWriter();
			JSONObject res = new JSONObject();
			
			Category[] raw = MainApp.currentUser(session).getCategories("TOKEN");
			
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			for (Category cat : raw)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("nama_kategori", cat.getNama_kategori());
				map.put("id", cat.getId_kategori());
				//map.put("canDeleteCategory", cat.getDeletable(MainApp.currentUserId(session)));
				//map.put("canEditCategory", cat.getEditable(MainApp.currentUserId(session)));
>>>>>>> ee3cc0b4d97340f4a8f86cada8e1d1a20a399122
				
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"category/retrieve_categories", parameter, "", 0);
				
				PrintWriter pw = response.getWriter();
				pw.println(responseString);
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
			}
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
			if ((request.getParameter("nama_kategori")!=null) && (request.getParameter("usernames_list")!=null))
			{
<<<<<<< HEAD
				JaxWsProxyFactoryBean fb = new JaxWsProxyFactoryBean();
	
				fb.setServiceClass(com.soap.AddService.class);
		        fb.setAddress(MainApp.serviceURL+"services/AddService?wsdl");
		        fb.getInInterceptors().add(new LoggingInInterceptor());
		        fb.getOutInterceptors().add(new LoggingOutInterceptor());
		        com.soap.AddService client = (com.soap.AddService) fb.create();
		        int id_category = client.add_new_category(MainApp.token(session), MainApp.appId, request.getParameter("nama_kategori"), request.getParameter("usernames_list"));
	
		        try {
					HashMap<String, String> parameter = new HashMap<String,String>();
					parameter.put("token", MainApp.token(session));
					parameter.put("app_id", MainApp.appId);
					
					String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"category/retrieve_categories", parameter, "", 0);
=======
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
					
					
					Category[] raw = MainApp.currentUser(session).getCategories("TOKEN");
					
					List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
					for (Category cat : raw)
					{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("nama_kategori", cat.getNama_kategori());
						map.put("id", cat.getId_kategori());
						//map.put("canDeleteCategory", cat.getDeletable(MainApp.currentUserId(session)));
						//map.put("canEditCategory", cat.getEditable(MainApp.currentUserId(session)));
						
						result.add(map);
					}
					res.put("categories", result);
>>>>>>> ee3cc0b4d97340f4a8f86cada8e1d1a20a399122
					
					PrintWriter pw = response.getWriter();
					pw.println(responseString);
					pw.close();
				} catch(Exception exc) {
					exc.printStackTrace();
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
<<<<<<< HEAD
				try {
					HashMap<String, String> parameter = new HashMap<String,String>();
					parameter.put("token", MainApp.token(session));
					parameter.put("app_id", MainApp.appId);
					parameter.put("id_category", request.getParameter("category_id"));
					
					String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"category/delete_category", parameter, "", 0);
					
					PrintWriter pw = response.getWriter();
					pw.println(responseString);
					pw.close();
				} catch(Exception exc) {
					exc.printStackTrace();
				}
=======
				PrintWriter pw = response.getWriter();
				JSONObject res = new JSONObject();
				
				int id_kategori = Integer.parseInt(request.getParameter("category_id"));
				boolean success = false;
				
				/*if (((Category)Category.getModel().find("id_kategori = ?", new Object[]{id_kategori}, new String[]{"integer"}, null)).getDeletable(MainApp.currentUserId(session)))
				{
					if (Category.getModel().delete("id_kategori = ?", new Object[]{id_kategori}, new String[]{"integer"})==1)
					{
						success = true;
					}
				}*/
				
				res.put("success", success);
				pw.println(res.toJSONString());
>>>>>>> ee3cc0b4d97340f4a8f86cada8e1d1a20a399122
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
			PrintWriter pw = response.getWriter();
			
			//Comment[] comments = Comment.getModel().getOlder(Integer.parseInt(request.getParameter("id_task")), request.getParameter("timestamp"));
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			/*for (Comment c : comments)
			{
				Map<String, Object> map = c.getData();
				map.put("timestamp", ((Timestamp)map.get("timestamp")).toString());
				list.add(map);
			}*/			
			pw.println(JSONValue.toJSONString(list));
			pw.close();
		}
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
			
			//Comment[] comments = Comment.getModel().getLatest(Integer.parseInt(request.getParameter("id_task")), request.getParameter("timestamp"));
			
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			/*for (Comment c : comments)
			{
				Map<String, Object> map = c.getData();
				map.put("timestamp", ((Timestamp)map.get("timestamp")).toString());
				list.add(map);
			}*/
			pw.println(JSONValue.toJSONString(list));
			pw.close();
		}
	}
	
	/**
	 * Post a new comment
	 * @return string contains whether success or fail
	 */
	public void comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (("POST".equals(request.getMethod())) && (request.getParameter("komentar")!=null) && (request.getParameter("id_task")!=null) && (MainApp.LoggedIn(session)))
		{
			JaxWsProxyFactoryBean fb = new JaxWsProxyFactoryBean();
			
			fb.setServiceClass(com.soap.AddService.class);
	        fb.setAddress(MainApp.serviceURL+"services/AddService?wsdl");
	        fb.getInInterceptors().add(new LoggingInInterceptor());
	        fb.getOutInterceptors().add(new LoggingOutInterceptor());
	        com.soap.AddService client = (com.soap.AddService) fb.create();
	        boolean success = (client.add_new_comment(MainApp.token(session), MainApp.appId, Integer.parseInt(request.getParameter("id_task")), request.getParameter("komentar"))!=-1);
			
	        PrintWriter pw = response.getWriter();
			HashMap<String, Object> ret = new HashMap<String, Object>();
			if (success)
			{
				ret.put("status", "success");
			}
			else
			{
				ret.put("status", "fail");
			}
			pw.println(new JSONObject(ret).toJSONString());
			pw.close();
		}
	}
	
	/**
	 * Remove a comment
	 * @return string contains whether success or fail
	 */
	public void remove_comment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (("POST".equals(request.getMethod())) && (request.getParameter("id")!=null) && (MainApp.LoggedIn(session)))
		{
			try {
				HashMap<String, Object> res = new HashMap<String, Object>();
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("id", request.getParameter("id"));
				
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"comment/delete_comment", parameter, "", 0);
				
				JSONObject ret = (JSONObject)new JSONParser().parse(responseString);
				
				if ((Boolean)ret.get("status"))
				{
					res.put("status", "success");
				}
				else
				{
					res.put("status", "fail");
				}
				PrintWriter pw = response.getWriter();
				pw.println(new JSONObject(res).toJSONString());
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
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
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("username", request.getParameter("username"));
				
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"user/get_username", parameter, "", 0);
				
				PrintWriter pw = response.getWriter();
				pw.println(responseString);
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	/**
	 * Login using username and password through Rest API
	 * @return string contains whether success or fail
	 */
	/*public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
				u.setAvatar(user.getAvatar());
				
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
	}*/
	
	/**
	 * Check register parameter through Rest API
	 * @return array of status and possible errors
	 */
	public void register_check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (("POST".equals(request.getMethod().toUpperCase())) && (request.getParameter("username")!=null) &&
				(request.getParameter("email")!=null) && (request.getParameter("password")!=null) && 
				(request.getParameter("confirm_password")!=null) && (request.getParameter("fullname")!=null) && 
				(request.getParameter("birthdate")!=null) && (request.getParameter("avatar")!=null))
		{
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("username", request.getParameter("username"));
				parameter.put("email", request.getParameter("email"));
				parameter.put("password", request.getParameter("password"));
				parameter.put("confirm_password", request.getParameter("confirm_password"));
				parameter.put("fullname", request.getParameter("fullname"));
				parameter.put("birthdate", request.getParameter("birthdate"));
				parameter.put("avatar", request.getParameter("avatar"));
				
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"user/register_check", parameter, "", 0);
				
				PrintWriter pw = response.getWriter();
				pw.println(responseString);
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
		else
		{
			PrintWriter pw = response.getWriter();
			pw.println("{status : \"fail\"}");
			pw.close();
		}
	}

	/*** ----- END OF USER MODULE -----***/	

	/*** ----- START OF SEARCH MODULE -----***/
	public void search_suggestions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((MainApp.LoggedIn(session)) && (request.getParameter("type")!=null) && (request.getParameter("q")!=null))
		{
<<<<<<< HEAD
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("type", request.getParameter("type"));
				parameter.put("q", request.getParameter("q"));
				
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"task/search_suggestions", parameter, "", 0);
=======
			String type = request.getParameter("type");
			String q = request.getParameter("q");
			
			if (!"".equals(q))
			{
				PrintWriter pw = response.getWriter();
				
				boolean all = ("all".equals(type));
				
				List<String> suggestion = new ArrayList<String>();
				if (("task".equals(type)) || (all))
				{
					Task[] tasks  = MainApp.currentUser(session).getTasksLike(q,"TOKEN");
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
					Category[] categories  = MainApp.currentUser(session).getCategoriesLike(q,"TOKEN");
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
					User[] users  = User.getModel().findAllLike(q,"TOKEN");
					for (User user : users)
					{
						if (!suggestion.contains(user.getUsername()))
						{
							suggestion.add(user.getUsername());
						}
					}
				}
>>>>>>> ee3cc0b4d97340f4a8f86cada8e1d1a20a399122
				
				PrintWriter pw = response.getWriter();
				pw.println(responseString);
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
	}
}

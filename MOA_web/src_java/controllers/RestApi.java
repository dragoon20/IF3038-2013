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

	public void get_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if ((MainApp.LoggedIn(session)) && (request.getParameter("id_task")!=null))
		{
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("id_task", request.getParameter("id_task"));

				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"task/get_task", parameter, "", 0);

				PrintWriter pw = response.getWriter();
				pw.println(responseString);
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	/**
	 * Delete a task
	 * @return string contains whether success or fail
	 */
	public void delete_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (MainApp.LoggedIn(session))
		{
			if ("POST".equals(request.getMethod().toUpperCase()))
			{
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
				}
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
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				if (request.getParameter("category_id")!=null)
				{
					parameter.put("category_id", request.getParameter("category_id"));
				}

				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"task/retrieve_tasks", parameter, "", 0);

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
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);

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
				try {
					HashMap<String, String> parameter = new HashMap<String,String>();
					parameter.put("token", MainApp.token(session));
					parameter.put("app_id", MainApp.appId);
					parameter.put("id_kategori", request.getParameter("category_id"));

					String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"category/delete_category", parameter, "", 0);

					PrintWriter pw = response.getWriter();
					pw.println(responseString);
					pw.close();
				} catch(Exception exc) {
					exc.printStackTrace();
				}
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

			Comment[] comments = Comment.getModel().getOlder(Integer.parseInt(request.getParameter("id_task")), request.getParameter("timestamp"), MainApp.token(session));
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for (Comment c : comments)
			{
				Map<String, Object> map = c.getData();
				map.put("timestamp", ((Timestamp)map.get("timestamp")).toString());
				list.add(map);
			}			
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

			Comment[] comments = Comment.getModel().getLatest(Integer.parseInt(request.getParameter("id_task")), request.getParameter("timestamp"), MainApp.token(session));

			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for (Comment c : comments)
			{
				Map<String, Object> map = c.getData();
				map.put("timestamp", ((Timestamp)map.get("timestamp")).toString());
				list.add(map);
			}
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
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("type", request.getParameter("type"));
				parameter.put("q", request.getParameter("q"));

				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"task/search_suggestions", parameter, "", 0);

				PrintWriter pw = response.getWriter();
				pw.println(responseString);
				pw.close();
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
}
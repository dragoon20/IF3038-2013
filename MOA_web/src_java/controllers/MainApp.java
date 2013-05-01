package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.DBSimpleRecord;
import models.User;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Servlet implementation class MainApp
 */
public class MainApp extends HttpServlet 
{
    private static final long serialVersionUID = 1L;

    public static final String appId = "0d2d2a7531376b3b05ff4203aeaa6b41";
    public static final String appName = "MOA";
    public static final String appTagline = "Multiuser Online Agenda";
    //public static final String serviceURL = "http://localhost:8080/MOA_services/";
    public static final String serviceURL = "http://moa-service.ap01.aws.af.cm/";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainApp() 
    {
        super();
    }

    public static String token(HttpSession session)
    {
    	return (String)session.getAttribute("token");
    }
    
    public static boolean LoggedIn(HttpSession session)
	{
    	boolean status = false;
    	if (session.getAttribute("token")!=null)
    	{
    		try
    		{
		    	HashMap<String, String> map = new HashMap<String, String>();
		    	map.put("token", (String)session.getAttribute("token"));
		    	map.put("app_id", appId);

		    	String response = callRestfulWebService(serviceURL+"token/check_token", map, "", 0);
		    	JSONObject resp_obj = (JSONObject)JSONValue.parse(response);
		    	status = (boolean)resp_obj.get("status");
		    	
		    	if ((status) && (currentUser(session)==null))
		    	{
		    		try {
						HashMap<String, String> parameter = new HashMap<String,String>();
						parameter.put("token", token(session));
						parameter.put("app_id", appId);
						String responseString = callRestfulWebService(serviceURL+"user/get_user_data", parameter, "", 0);
						JSONObject ret = (JSONObject)JSONValue.parse(responseString);

						User user = new User();
						user.setUsername((String)ret.get("username"));
						user.setAvatar((String)ret.get("avatar"));
						user.setBirthdate(new Date(DBSimpleRecord.sdf.parse((String)ret.get("birthdate")).getTime()));
						user.setEmail((String)ret.get("email"));
						user.setFullname((String)ret.get("fullname"));
						session.setAttribute("current_user", user);
						
					} catch(Exception exc) {
						exc.printStackTrace();
					}
		    	}
    		} catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
		return status;
	}
    
    public static User currentUser(HttpSession session)
    {
    	return ((User)session.getAttribute("current_user"));
    }
    
    public static String fullPath(HttpSession session)
    {
    	return ((String)session.getAttribute("full_path"));
    }
    
    public static String appUrl(HttpSession session)
    {
    	return ((String)session.getAttribute("app_url"));
    }
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		proccessRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		proccessRequest(request, response);
	}

	private void proccessRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String page = ("".equals((String)request.getAttribute("page")))? "index" : (String)request.getAttribute("page");

		Class<?>[] param_handler = new Class[2];
		param_handler[0] = HttpServletRequest.class;
		param_handler[1] = HttpServletResponse.class;

		try {
			Method method;
			method = this.getClass().getMethod(page, param_handler);				
			method.invoke(this, request, response);
		} catch (NoSuchMethodException e) {
			// TODO Redirect to error page
			System.out.println("-----------------");
			System.out.println(page);
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

	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher("pages/index.jsp").forward(request, response);
	}

	public void dashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher("pages/dashboard.jsp").forward(request, response);
	}

	public void profile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher("pages/profile.jsp").forward(request, response);
	}

	public void edit_profile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher("pages/edit_profile.jsp").forward(request, response);
	}

	public void change_password(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher("pages/change_password.jsp").forward(request, response);
	}

	public void new_work(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher("pages/new_work.jsp").forward(request, response);
	}

	public void tugas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher("pages/tugas.jsp").forward(request, response);
	}   

	public void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher("pages/search.jsp").forward(request, response);
	}

	public void search_partial(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getRequestDispatcher("pages/search_partial.jsp").forward(request, response);
	}

	public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (("POST".equals(request.getMethod().toUpperCase())) && (ServletFileUpload.isMultipartContent(request)))
		{
			HashMap<String, String> params = new HashMap<String, String>();

			FileItem avatar = null;
			boolean check = false;
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = null;
			try
			{
				items = upload.parseRequest(request);
			}
			catch (FileUploadException e)
			{
				e.printStackTrace();
			}
			for (FileItem fi : items)
			{
				if (fi.isFormField())
				{
					params.put(fi.getFieldName(), fi.getString());
				}
				else
				{
					if ("avatar".equals(fi.getFieldName()))
					{
						check = true;
						avatar = fi;
						String extension = avatar.getName().split("\\.")[avatar.getName().split("\\.").length-1];
						String new_filename = DBSimpleRecord.MD5(UUID.randomUUID().toString()).toUpperCase()+"."+extension;
						params.put("avatar", MainApp.appUrl(request.getSession())+"upload/user_profile_pict/"+new_filename);
						params.put("avatar_filename", new_filename);
					}
				}
			}

			JaxWsProxyFactoryBean fb = new JaxWsProxyFactoryBean();

			fb.setServiceClass(com.soap.AddService.class);
	        fb.setAddress(serviceURL+"services/AddService?wsdl");
	        fb.getInInterceptors().add(new LoggingInInterceptor());
	        fb.getOutInterceptors().add(new LoggingOutInterceptor());
	        com.soap.AddService client = (com.soap.AddService) fb.create();
	        boolean result = client.add_new_user(params.get("username"), params.get("email"), params.get("password"), 
	        									params.get("fullname"), params.get("avatar"), params.get("birthdate"));

			if ((check) && (result))
			{
				InputStream in = avatar.getInputStream();
				FileOutputStream out = new FileOutputStream(new File(MainApp.fullPath(request.getSession())+"upload/user_profile_pict/"+params.get("avatar_filename")));

				int read = 0;
				byte[] bytes = new byte[1024];
				while ((read = in.read(bytes)) != -1)
				{
					out.write(bytes, 0, read);
				}
				out.close();

				response.sendRedirect("dashboard_fake");
			}
			else
			{
				// TODO print error screen
				PrintWriter pw = response.getWriter();
				pw.println("error2");
				pw.close();
			}
		}
		else
		{
			// TODO print error screen
			PrintWriter pw = response.getWriter();
			pw.println("error3");
			pw.close();
		}
	}

	public void login_check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getParameter("token")!=null)
		{
			HttpSession session = request.getSession();
			session.setAttribute("token", request.getParameter("token"));
			
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", token(request.getSession()));
				parameter.put("app_id", appId);
				String responseString = callRestfulWebService(serviceURL+"user/get_user_data", parameter, "", 0);
				JSONObject ret = (JSONObject)JSONValue.parse(responseString);

				User user = new User();
				user.setUsername((String)ret.get("username"));
				user.setAvatar((String)ret.get("avatar"));
				user.setBirthdate(new Date(DBSimpleRecord.sdf.parse((String)ret.get("birthdate")).getTime()));
				user.setEmail((String)ret.get("email"));
				user.setFullname((String)ret.get("fullname"));
				request.getSession().setAttribute("current_user", user);
				
			} catch(Exception exc) {
				exc.printStackTrace();
			}
			
			response.sendRedirect("dashboard");
		}
		else
		{
			response.sendRedirect("error");
		}
	}

	public void new_task(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (!MainApp.LoggedIn(request.getSession()))
		{
			response.sendRedirect("index");
		}

		if (("POST".equals(request.getMethod().toUpperCase())) && (ServletFileUpload.isMultipartContent(request)))
		{
			HashMap<String, Object> params = new HashMap<String, Object>();
			List<Map<String, Object>> attachments = new ArrayList<Map<String,Object>>();
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = null;
			try
			{
				items = upload.parseRequest(request);
			}
			catch (FileUploadException e)
			{
				e.printStackTrace();
			}
			for (FileItem fi : items)
			{
				if (fi.isFormField())
				{
					if ("id_kategori".equals(fi.getFieldName()))
					{
						params.put(fi.getFieldName(), Integer.parseInt(fi.getString()));
					}
					else
					{
						params.put(fi.getFieldName(), fi.getString());
					}
				}
				else
				{
					Map<String, Object> tempmap = new HashMap<String, Object>();
					String extension = fi.getName().split("\\.")[fi.getName().split("\\.").length-1]; 
					String name = DBSimpleRecord.MD5(UUID.randomUUID().toString()).toUpperCase()+"."+extension;
					tempmap.put("attachment", MainApp.appUrl(request.getSession())+"upload/attachments/"+name);
					tempmap.put("temp", fi.getInputStream());
					tempmap.put("location", MainApp.fullPath(request.getSession())+"upload/attachments/"+name);
					attachments.add(tempmap);
				}
			}

			JaxWsProxyFactoryBean fb = new JaxWsProxyFactoryBean();

			fb.setServiceClass(com.soap.AddService.class);
	        fb.setAddress(serviceURL+"services/AddService?wsdl");
	        fb.getInInterceptors().add(new LoggingInInterceptor());
	        fb.getOutInterceptors().add(new LoggingOutInterceptor());
	        com.soap.AddService client = (com.soap.AddService) fb.create();
	        int result = client.add_new_task(token(request.getSession()), appId, (String)params.get("nama_task"), 
	        									(String)params.get("deadline"), (Integer)params.get("id_kategori"));

			if (result!=-1)
			{
				String[] assignees = ((String)params.get("assignee")).split(",");
				for (String assignee : assignees)
				{
					client.add_assignee(token(request.getSession()), appId, result, assignee);
				}
				String[] tags = ((String)params.get("tag")).split(",");
				for (String tag : tags)
				{
					client.add_tag(token(request.getSession()), appId, result, tag);
				}

				for (Map<String, Object> attachment : attachments)
				{
					if (client.add_new_attachment(token(request.getSession()), appId, result, (String)attachment.get("attachment")))
					{
						FileOutputStream out = new FileOutputStream(new File((String)attachment.get("location")));

						InputStream in = (InputStream)attachment.get("temp");
						int read = 0;
						byte[] bytes = new byte[1024];
						while ((read = in.read(bytes)) != -1)
						{
							out.write(bytes, 0, read);
						}
						out.close();
					}
				}
				response.sendRedirect("tugas?id="+result);
			}
			else
			{
				// TODO got to error page
				System.out.println("error2");
			}
		}
		else
		{
			// TODO go to error page
			System.out.println("error3");
		}
	}

	public void edit_tugas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (!MainApp.LoggedIn(request.getSession()))
		{
			response.sendRedirect("index");
		}

		if (("POST".equals(request.getMethod().toUpperCase())) && (ServletFileUpload.isMultipartContent(request)))
		{
			HashMap<String, Object> params = new HashMap<String, Object>();
			List<Map<String, Object>> attachments = new ArrayList<Map<String,Object>>();
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = null;
			try
			{
				items = upload.parseRequest(request);
			}
			catch (FileUploadException e)
			{
				e.printStackTrace();
			}
			for (FileItem fi : items)
			{
				if (fi.isFormField())
				{
					if ("id_task".equals(fi.getFieldName()))
					{
						params.put(fi.getFieldName(), Integer.parseInt(fi.getString()));
					}
					else
					{
						params.put(fi.getFieldName(), fi.getString());
					}
				}
				else
				{
					Map<String, Object> tempmap = new HashMap<String, Object>();
					String extension = fi.getName().split("\\.")[fi.getName().split("\\.").length-1]; 
					String name = DBSimpleRecord.MD5(UUID.randomUUID().toString()).toUpperCase()+"."+extension;
					tempmap.put("attachment", MainApp.appUrl(request.getSession())+"upload/attachments/"+name);
					tempmap.put("temp", fi.getInputStream());
					tempmap.put("location", MainApp.fullPath(request.getSession())+"upload/attachments/"+name);
					attachments.add(tempmap);
				}
			}


			boolean check_task = false;
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", token(request.getSession()));
				parameter.put("app_id", appId);
				parameter.put("id_task", ""+params.get("id_task"));
				String responseString = callRestfulWebService(serviceURL+"task/check_task", parameter, "", 0);
				JSONObject ret = (JSONObject)JSONValue.parse(responseString);
				check_task = (Boolean)ret.get("success");
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			boolean editable = false;
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", token(request.getSession()));
				parameter.put("app_id", appId);
				parameter.put("id_task", ""+params.get("id_task"));
				String responseString = callRestfulWebService(serviceURL+"task/get_editable", parameter, "", 0);
				JSONObject ret = (JSONObject)JSONValue.parse(responseString);
				editable = (Boolean)ret.get("success");
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			if ((check_task) && (editable))
			{
				boolean success = false;
				try {
					HashMap<String, String> parameter = new HashMap<String,String>();
					parameter.put("token", token(request.getSession()));
					parameter.put("app_id", appId);
					parameter.put("id_task", ""+params.get("id_task"));
					parameter.put("nama_task", (String)params.get("nama_task"));
					parameter.put("deadline", (String)params.get("deadline"));

					String responseString = callRestfulWebService(serviceURL+"task/update_task", parameter, "", 0);
					JSONObject ret = (JSONObject)JSONValue.parse(responseString);
					success = (Boolean)ret.get("success");
				} catch(Exception exc) {
					exc.printStackTrace();
				}

				if (success)
				{
					JaxWsProxyFactoryBean fb = new JaxWsProxyFactoryBean();

					fb.setServiceClass(com.soap.AddService.class);
                                        fb.setAddress(serviceURL+"services/AddService?wsdl");
                                        fb.getInInterceptors().add(new LoggingInInterceptor());
                                        fb.getOutInterceptors().add(new LoggingOutInterceptor());
                                        com.soap.AddService client = (com.soap.AddService) fb.create();

			        try {
						HashMap<String, String> parameter = new HashMap<String,String>();
						parameter.put("token", token(request.getSession()));
						parameter.put("app_id", appId);
						parameter.put("id_task", (String)params.get("id_task"));

						String responseString = callRestfulWebService(serviceURL+"task/delete_assignees", parameter, "", 0);
						JSONObject ret = (JSONObject)JSONValue.parse(responseString);
						success = (Boolean)ret.get("success");
					} catch(Exception exc) {
						exc.printStackTrace();
					}
					String[] assignees = ((String)params.get("assignee")).split(",");
					for (String assignee : assignees)
					{
						client.add_assignee(token(request.getSession()), appId, (Integer)params.get("id_task"), assignee);
					}

					try {
						HashMap<String, String> parameter = new HashMap<String,String>();
						parameter.put("token", token(request.getSession()));
						parameter.put("app_id", appId);
						parameter.put("id_task", (String)params.get("id_task"));

						String responseString = callRestfulWebService(serviceURL+"task/delete_tags", parameter, "", 0);
						JSONObject ret = (JSONObject)JSONValue.parse(responseString);
						success = (Boolean)ret.get("success");
					} catch(Exception exc) {
						exc.printStackTrace();
					}
					String[] tags = ((String)params.get("tag")).split(",");
					for (String tag : tags)
					{
						client.add_tag(token(request.getSession()), appId, (Integer)params.get("id_task"), tag);
					}

					for (Map<String, Object> attachment : attachments)
					{
						if (client.add_new_attachment(token(request.getSession()), appId, (Integer)params.get("id_task"), (String)attachment.get("attachment")))
						{
							FileOutputStream out = new FileOutputStream(new File((String)attachment.get("location")));

							InputStream in = (InputStream)attachment.get("temp");
							int read = 0;
							byte[] bytes = new byte[1024];
							while ((read = in.read(bytes)) != -1)
							{
								out.write(bytes, 0, read);
							}
							out.close();
						}
					}
					response.sendRedirect("tugas?id="+(Integer)params.get("id_task"));
				}
				else
				{
					// TODO got to error page
					System.out.println("error2");
				}
			}
			else
			{
				// TODO go to error page
				System.out.println("error3");
			}
		}
		else
		{
			// TODO go to error page
			System.out.println("error4");
		}
	}

	public void change_profile_data(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (!MainApp.LoggedIn(request.getSession()))
		{
			response.sendRedirect("index");
		}

		if (("POST".equals(request.getMethod().toUpperCase())) && (ServletFileUpload.isMultipartContent(request)))
		{
			HashMap<String, String> params = new HashMap<String, String>();
			FileItem avatar = null;
			boolean check = false;
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = null;
			try
			{
				items = upload.parseRequest(request);
			}
			catch (FileUploadException e)
			{
				e.printStackTrace();
			}
			for (FileItem fi : items)
			{
				if (fi.isFormField())
				{
					params.put(fi.getFieldName(), fi.getString());
				}
				else
				{
					if (("avatar".equals(fi.getFieldName())) && (fi.getSize() > 0))
					{
						check = true;
						avatar = fi;
						String extension = avatar.getName().split("\\.")[avatar.getName().split("\\.").length-1];
						String new_filename = DBSimpleRecord.MD5(UUID.randomUUID().toString()).toUpperCase()+"."+extension;
						params.put("avatar", MainApp.fullPath(request.getSession())+"upload/user_profile_pict/"+new_filename);
						params.put("avatar_filename", new_filename);
					}
				}
			}

			boolean success = false;
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", token(request.getSession()));
				parameter.put("app_id", appId);
				parameter.put("id_task", (String)params.get("id_task"));
				parameter.put("username", (String)params.get("username"));
				parameter.put("email", (String)params.get("email"));
				parameter.put("fullname", (String)params.get("fullname"));
				parameter.put("avatar", (String)params.get("avatar"));
				parameter.put("password", (String)params.get("password"));
				parameter.put("birthdate", (String)params.get("birthdate"));

				String responseString = callRestfulWebService(serviceURL+"task/update_user", parameter, "", 0);
				JSONObject ret = (JSONObject)JSONValue.parse(responseString);
				success = (Boolean)ret.get("success");
				
				if (success)
				{
					try {
						HashMap<String, String> param = new HashMap<String,String>();
						param.put("token", token(request.getSession()));
						param.put("app_id", appId);
						String responseStr = callRestfulWebService(serviceURL+"user/get_user_data", parameter, "", 0);
						JSONObject retjs = (JSONObject)JSONValue.parse(responseStr);

						User user = new User();
						user.setUsername((String)retjs.get("username"));
						user.setAvatar((String)retjs.get("avatar"));
						user.setBirthdate(new Date(DBSimpleRecord.sdf.parse((String)retjs.get("birthdate")).getTime()));
						user.setEmail((String)retjs.get("email"));
						user.setFullname((String)retjs.get("fullname"));
						request.getSession().setAttribute("current_user", user);
						
					} catch(Exception exc) {
						exc.printStackTrace();
					}
				}
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			if (success)
			{
				if (check)
				{
					InputStream in = avatar.getInputStream();
					FileOutputStream out = new FileOutputStream(new File(MainApp.fullPath(request.getSession())+"upload/user_profile_pict/"+params.get("avatar_filename")));

					int read = 0;
					byte[] bytes = new byte[1024];
					while ((read = in.read(bytes)) != -1)
					{
						out.write(bytes, 0, read);
					}
					out.close();
				}
				response.sendRedirect("index");
			}
			else
			{
				// TODO go to error page
				System.out.println("error 2");
			}
		}
		else
		{
			// TODO go to error page
			System.out.println("error 3");
		}
	}

	public void change_user_password(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (!MainApp.LoggedIn(request.getSession()))
		{
			response.sendRedirect("index");
		}

		if ("POST".equals(request.getMethod().toUpperCase()))
		{
			boolean success = false;
			try {
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", token(request.getSession()));
				parameter.put("app_id", appId);
				parameter.put("password", request.getParameter("current_password"));
				parameter.put("new_password", request.getParameter("new_password"));
				parameter.put("confirm_password", request.getParameter("confirm_password"));

				String responseString = callRestfulWebService(serviceURL+"task/update_user", parameter, "", 0);
				JSONObject ret = (JSONObject)JSONValue.parse(responseString);
				success = (Boolean)ret.get("success");
			} catch(Exception exc) {
				exc.printStackTrace();
			}

			if (success)
			{
				response.sendRedirect("index");
			}
			else
			{
				// TODO print error page
			}
		}
		else
		{
			// TODO print error page
		}
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		boolean status = false;
		HttpSession session = request.getSession();
    	if (session.getAttribute("token")!=null)
    	{
    		try
    		{
		    	HashMap<String, String> map = new HashMap<String, String>();
		    	map.put("token", (String)session.getAttribute("token"));
		    	map.put("app_id", appId);

		    	String resp = callRestfulWebService(serviceURL+"token/logout", map, "", 0);
		    	JSONObject resp_obj = (JSONObject)JSONValue.parse(resp);
		    	status = (Boolean)resp_obj.get("status");
    		} catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    	}

    	if (status)
    	{
			session.removeAttribute("token");
			response.sendRedirect("index");
    	}
    	else
    	{
    		response.sendRedirect("error");
    	}
	}

	private static String buildWebQuery(Map<String, String> parameters) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = URLEncoder.encode(entry.getKey(), "UTF-8");
            String value = URLEncoder.encode(entry.getValue(), "UTF-8");
            sb.append(key).append("=").append(value).append("&");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
    
    // contoh pemanggilan : callRestfulWebService("http://localhost:8080/MOA_Servicesnew/",params,"",0)
    public static String callRestfulWebService(String address, Map<String, String> parameters, String proxy, int port) throws Exception 
    {
        Proxy proxyObject = null;
        if (!proxy.equals("") && port > 0) 
        {
            InetSocketAddress proxyAddress = new InetSocketAddress(proxy, port);
            proxyObject = new Proxy(Proxy.Type.HTTP, proxyAddress);
        }

        String response = "";
        String query = buildWebQuery(parameters);

        URL url = new URL(address);
        
        URLConnection urlc = null;
        if (proxyObject == null) {
            urlc = url.openConnection();
        } else {
            urlc = url.openConnection(proxyObject);
        }
        urlc.setDoOutput(true);
        urlc.setAllowUserInteraction(false);

        // Send message
        PrintStream ps = new PrintStream(urlc.getOutputStream());
        ps.print(query);
        ps.close();

        // retrieve result
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        response = sb.toString();

        return response;
    }
    
    public void dashboard_fake(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
		pw.println(request.getSession().getAttribute("token"));
		pw.close();
	}

	public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try {
			String token = request.getParameter("token");
			TreeMap<String, String> parameter = new TreeMap<String,String>();
			parameter.put("token", token);
			parameter.put("app_id", MainApp.appId);
			String responseString = callRestfulWebService("http://localhost:8080/MOA_services/user/get_created_tasks", parameter, "", 0);
			PrintWriter pw = response.getWriter();
			pw.println(responseString);
			pw.close();
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}

	public void test2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
		pw.println("http://"+request.getServerName()+":"+request.getServerPort()+request.getServletContext().getContextPath()+"/");
		pw.close();
	}
        
}
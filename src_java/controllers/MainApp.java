package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import models.DBSimpleRecord;
import models.Task;
import models.User;

/**
 * Servlet implementation class MainApp
 */
@WebServlet("/MainApp")
public class MainApp extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	public static final String appName = "MOA";
    public static final String appTagline = "Multiuser Online Agenda";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainApp() 
    {
        super();
    }

    public static boolean LoggedIn(HttpSession session)
	{
		return (((session.getAttribute("user_id")!=null)) && 
				(((User)session.getAttribute("current_user"))!=null) && 
				(((String)session.getAttribute("full_path"))!=null));
	}
    
    public static int currentUserId(HttpSession session)
    {
    	return ((int)session.getAttribute("user_id"));
    }
    
    public static User currentUser(HttpSession session)
    {
    	return ((User)session.getAttribute("current_user"));
    }
    
    public static String fullPath(HttpSession session)
    {
    	return ((String)session.getAttribute("full_path"));
    }
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
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
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
			User user = new User();
			
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
					if ("birthdate".equals(fi.getFieldName()))
					{
						try {
							user.putData(fi.getFieldName(), new Date(DBSimpleRecord.sdf.parse(fi.getString()).getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						user.putData(fi.getFieldName(), fi.getString());
					}
				}
				else
				{
					if ("avatar".equals(fi.getFieldName()))
					{
						check = true;
						user.setAvatar(fi.getName());
						avatar = fi;
					}
				}
			}
			
			boolean temperror = user.checkValidity();
			
			if ((temperror) || (!check))
			{
				// TODO print error screen
				System.out.println("error1");
			}
			else
			{
				String extension = avatar.getName().split("\\.")[avatar.getName().split("\\.").length-1];
				String new_filename = DBSimpleRecord.MD5(UUID.randomUUID().toString()).toUpperCase()+"."+extension;
				user.setAvatar(new_filename);
				user.hashPassword();
				if (user.save())
				{
					InputStream in = avatar.getInputStream();
					FileOutputStream out = new FileOutputStream(new File(MainApp.fullPath(request.getSession())+"upload/user_profile_pict/"+new_filename));
					
					int read = 0;
					byte[] bytes = new byte[1024];
					while ((read = in.read(bytes)) != -1)
					{
						out.write(bytes, 0, read);
					}
					out.close();
					
					HttpSession session = request.getSession();
					session.setAttribute("user_id", user.getId_user());
					
					User u = new User();
					u.setId_user(user.getId_user());
					u.setFullname(user.getFullname());
					u.setUsername(user.getUsername());
					u.setEmail(user.getEmail());
					u.setAvatar(user.getAvatar());
					session.setAttribute("current_user", u);
					
					response.sendRedirect("dashboard");
				}
				else
				{
					// TODO print error screen
					System.out.println("error2");
				}
			}
		}
		else
		{
			// TODO print error screen
			System.out.println("error3");
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
			Task task = new Task();
			task.setId_user(MainApp.currentUserId(request.getSession()));
			
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
					if ("deadline".equals(fi.getFieldName()))
					{
						try {
							task.putData(fi.getFieldName(), new Date(DBSimpleRecord.sdf.parse(fi.getString()).getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if ("id_kategori".equals(fi.getFieldName()))
					{
						task.putData(fi.getFieldName(), Integer.parseInt(fi.getString()));
					}
					else
					{
						task.putData(fi.getFieldName(), fi.getString());
					}
				}
				else
				{
					Map<String, Object> tempmap = new HashMap<String, Object>();
					String extension = fi.getName().split("\\.")[fi.getName().split("\\.").length-1]; 
					String name = DBSimpleRecord.MD5(UUID.randomUUID().toString()).toUpperCase()+"."+extension;
					tempmap.put("attachment", name);
					tempmap.put("temp", fi.getInputStream());
					tempmap.put("location", MainApp.fullPath(request.getSession())+"upload/attachments/"+name);
					attachments.add(tempmap);
				}
			}
			task.putData("attachments", attachments);
			task.setId_user(MainApp.currentUserId(request.getSession()));
			
			boolean temperror = task.checkValidity();
			if (temperror)
			{
				// TODO go to error page
				System.out.println("error1");
			}
			else
			{
				if (task.save())
				{
					response.sendRedirect("tugas?id="+task.getId_task());
				}
				else
				{
					// TODO got to error page
					System.out.println("error2");
				}
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
			Task task = new Task();
			
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
					if ("deadline".equals(fi.getFieldName()))
					{
						try {
							task.putData(fi.getFieldName(), new Date(DBSimpleRecord.sdf.parse(fi.getString()).getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if ("id_task".equals(fi.getFieldName()))
					{
						task.putData(fi.getFieldName(), Integer.parseInt(fi.getString()));
					}
					else if ("id_kategori".equals(fi.getFieldName()))
					{
						task.putData(fi.getFieldName(), Integer.parseInt(fi.getString()));
					}
					else
					{
						task.putData(fi.getFieldName(), fi.getString());
					}
				}
				else
				{
					Map<String, Object> tempmap = new HashMap<String, Object>();
					String extension = fi.getName().split("\\.")[fi.getName().split("\\.").length-1]; 
					String name = DBSimpleRecord.MD5(UUID.randomUUID().toString()).toUpperCase()+"."+extension;
					tempmap.put("attachment", name);
					tempmap.put("temp", fi.getInputStream());
					tempmap.put("location", MainApp.fullPath(request.getSession())+"upload/attachments/"+name);
					attachments.add(tempmap);
				}
			}
			
			Task real_task = (Task)Task.getModel().find("id_task = ?", new Object[]{task.getId_task()}, new String[]{"integer"}, null);
			
			if ((!real_task.isEmpty()) && (real_task.getEditable(MainApp.currentUserId(request.getSession()))))
			{				
				real_task.replaceData(task);
				real_task.putData("attachments", attachments);
				
				boolean temperror = real_task.checkValidity();
				if (temperror)
				{
					// TODO go to error page
					System.out.println("error1");
				}
				else
				{
					if (real_task.save())
					{
						response.sendRedirect("tugas?id="+real_task.getId_task());
					}
					else
					{
						// TODO got to error page
						System.out.println("error2");
					}
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
			User user = (User)User.getModel().find("id_user = ?", new Object[]{MainApp.currentUserId(request.getSession())}, new String[]{"integer"}, null);
			user.putData("confirm_password", user.getPassword());
			
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
					if ("birthdate".equals(fi.getFieldName()))
					{
						try {
							user.putData(fi.getFieldName(), new Date(DBSimpleRecord.sdf.parse(fi.getString()).getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						user.putData(fi.getFieldName(), fi.getString());
					}
				}
				else
				{
					if (("avatar".equals(fi.getFieldName())) && (fi.getSize() > 0))
					{
						check = true;
						avatar = fi;
					}
				}
			}
			
			boolean temperror = user.checkValidity();
			
			if (temperror)
			{
				// TODO go to error page
				System.out.println("error 1");
			}
			else
			{
				if (user.save())
				{
					if (check)
					{
						InputStream in = avatar.getInputStream();
						FileOutputStream out = new FileOutputStream(new File(MainApp.fullPath(request.getSession())+"upload/user_profile_pict/"+user.getAvatar()));
						
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
			User user = (User)User.getModel().find("id_user = ?", new Object[]{MainApp.currentUserId(request.getSession())}, new String[]{"integer"}, null);
			
			if (user.getPassword().equals(DBSimpleRecord.MD5((request.getParameter("current_password")!=null ? request.getParameter("current_password"): ""))))
			{
				user.setPassword(request.getParameter("new_password"));
				user.putData("confirm_password", request.getParameter("confirm_password"));
				
				boolean temperror = user.checkValidity();
				if (temperror)
				{
					// TODO print error screen
				}
				else
				{
					user.hashPassword();
					if (user.save())
					{
						response.sendRedirect("index");
					}
					else
					{
						// TODO print error page
					}
				}
			}
		}
		else
		{
			// TODO print error page
		}
	}
	
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		session.removeAttribute("user_id");
		response.sendRedirect("index");
	}
	
	public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
		pw.println(request.getServletContext().getRealPath("/"));
		pw.close();
	}
}

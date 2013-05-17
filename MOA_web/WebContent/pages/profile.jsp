<%@page import="java.sql.Date"%>
<%@page import="models.DBSimpleRecord"%>
<%@page import="org.json.simple.JSONValue"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="models.User"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="models.Task"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if (!MainApp.LoggedIn(session))
	{
		response.sendRedirect("index");
	}
	else
	{
		String username = MainApp.currentUser(session).getUsername();
		if (request.getParameter("username")!=null)
		{
			try
			{
				username = request.getParameter("username");
			}
			catch (NumberFormatException e)
			{
			}
		}
		
		User user = null;
		try {
			HashMap<String, String> parameter = new HashMap<String,String>();
			parameter.put("token", MainApp.token(request.getSession()));
			parameter.put("app_id", MainApp.appId);
			parameter.put("username", username);
			String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"user/get_user_data_from_username", parameter, "", 0);
			JSONObject ret = (JSONObject)JSONValue.parse(responseString);

			user = new User();
			user.setUsername((String)ret.get("username"));
			user.setAvatar((String)ret.get("avatar"));
			user.setBirthdate(new Date(DBSimpleRecord.sdf.parse((String)ret.get("birthdate")).getTime()));
			user.setEmail((String)ret.get("email"));
			user.setFullname((String)ret.get("fullname"));
			
		} catch(Exception exc) {
			exc.printStackTrace();
			user = null;
		}
		
		if (user!=null)
		{
		
			Task[] tasks = user.getAssignedTasks(MainApp.token(session));
			
			SimpleDateFormat date_format = new SimpleDateFormat("dd MMMM yyyy");
%>
			<!DOCTYPE html>
			<html xmlns="http://www.w3.org/1999/xhtml">
				<head>
					<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
					<meta name="Description" content="" />
					<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
					<title>MOA - Profil</title>
					<link rel="stylesheet" href="css/style.css" />
					<link rel="stylesheet" href="css/profil.css" />
				</head>
				<body>
					<%
						Map<String, Map<String, String>> menu_main = new HashMap<String, Map<String, String>>();
			
						Map<String, String> dashboard = new  HashMap<String, String>();
						dashboard.put("href", "dashboard");
						menu_main.put("Dashboard", dashboard);
						
						request.setAttribute("menu", menu_main);
					%>
					<%@ include file="../template/header.jsp" %>
					<section>
						<div id="content_wrap" class="wrap">
							<div id="profil_left">
								<div id="profil_form_wrap">
									<div id="profil_head">
										<h1><%= user.getUsername() %></h1>
									</div>
									<div class="row">
										<span class="label">Username</span>
										<span id="profil_username"><%= user.getUsername() %></span>
									</div>
									<div class="row">
										<span class="label">Nama Lengkap</span>
										<span id="profil_name"><%= user.getFullname() %></span>
									</div>
									<div class="row">
										<span class="label">Tanggal Lahir</span>
										<span id="profil_birth_date"><%= date_format.format(user.getBirthdate()) %></span>
									</div>
									<div class="row">
										<span class="label">Email</span>
										<span id="profil_email"><%= user.getEmail() %></span>
									</div>
									<div class="row">
										<span class="label">Avatar</span>
										<img id="avatar_image" src="<%= user.getAvatar() %>" alt="avatar profil" />
									</div>
									<%
										if (username.equals(MainApp.currentUser(session).getUsername()))
										{
									%>
											<a href="change_password" class="button_link"> Ganti Sandi</a>
											<a href="edit_profile" class="button_link"> Edit Profil </a>
									<%
										}
									%>
								</div>
							</div>
							<div id="profil_right">
								<div id="task_head">
									<h1>Tugas</h1>
								</div>
								<div id="task_left">
									<div class="profil_sub_head">
										<h4>Belum Selesai</h4>
									</div>
									<ul>
										<%
											for (Task task : tasks)
											{
												if (!task.isStatus())
												{
										%>
										<li>
											<div class="row">
												<div>
													<a href="task?taskId=<%= task.getId_task() %>">
														<%= task.getNama_task() %>
													</a>
													<br />
													<div class="task_date"><%= date_format.format(task.getDeadline()) %></div>
												</div>
											</div>
										</li>
										<%
												}
											}
										%>
									</ul>
								</div>
								<div id="task_right">
									<div class="profil_sub_head">
										<h4>Selesai</h4>
									</div>
									<ul>
										<%
											for (Task task : tasks)
											{
												if (task.isStatus())
												{
										%>
										<li>
											<div class="row">
												<div>
													<a href="task?taskId=<%= task.getId_task() %>">
														<%= task.getNama_task() %>
													</a>
													<br />
													<div class="task_date"><%= date_format.format(task.getDeadline()) %></div>
												</div>
											</div>
										</li>
										<%
												}
											}
										%>
									</ul>
								</div>
							</div>
						</div>
					</section>
					<%
						Map<String, Map<String, String>> breadcrumbs_main = new HashMap<String, Map<String, String>>();
			
						Map<String, String> profile = new  HashMap<String, String>();
						profile.put("href", "profile");
						profile.put("class", "active");
					
						breadcrumbs_main.put("Profil", profile);
						request.setAttribute("breadcrumbs", breadcrumbs_main);
					%>
					<%@ include file="../template/footer.jsp" %>
					
					<script type="text/javascript" src="js/search.js"></script>
				</body>
			</html>
<%
		}
		else
		{
			response.sendRedirect("404");
		}
	}
%>

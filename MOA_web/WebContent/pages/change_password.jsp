<%@page import="java.text.SimpleDateFormat"%>
<%@page import="models.Task"%>
<%@page import="models.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if (!MainApp.LoggedIn(session))
	{
		response.sendRedirect("index");
	}
	else
	{
		User user = MainApp.currentUser(session);
		
		Task[] tasks = user.getAssignedTasks(MainApp.token(session));
		
		SimpleDateFormat date_format = new SimpleDateFormat("YYYY-MM-dd");
%>
		<!DOCTYPE html>
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<meta name="Description" content="" />
				<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
				<title>MOA - Ganti Sandi</title>
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
									<h1>Ganti Sandi</h1>
								</div>
								<form id="profil_form" method="post" action="change_user_password">
									<div class="row">
										<label for="profil_current_password">Sandi sekarang</label>
										<input id="profil_current_password" name="current_password" pattern="^.{8,}$" type="password" title="Sandi minimal harus terdiri dari 8 karakter dan tidak sama dengan username dan email." required />							
									</div>
									<div class="row">
										<label for="profil_password">Sandi baru</label>
										<input id="profil_password" name="new_password" pattern="^.{8,}$" type="password" title="Sandi minimal harus terdiri dari 8 karakter dan tidak sama dengan username dan email." required />							
									</div>
									<div class="row">
										<label for="profil_confirm_password">Konfirmasi Sandi baru</label>
										<input id="profil_confirm_password" name="confirm_password" pattern="^.{8,}$" type="password" title="Konfirmasi sandi harus sama dengan sandi." required />
									</div>
									<input id="profil_submit" type="submit" value="Ganti Sandi" disabled="disabled" title="Semua elemen form harus diisi dengan benar dahulu."/>
								</form>
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
												<a href="task?id=<%= task.getId_task() %>">
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
												<a href="task?id=<%= task.getId_task() %>">
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
					
					Map<String, String> profile_edit = new  HashMap<String, String>();
					profile_edit.put("href", "change_password");
					profile_edit.put("class", "active");
				
					breadcrumbs_main.put("Profil", profile);
					breadcrumbs_main.put("Ganti Sandi", profile_edit);
					request.setAttribute("breadcrumbs", breadcrumbs_main);
				%>
				<%@ include file="../template/footer.jsp" %>
		
				<script type="text/javascript" src="js/search.js"></script>
				<% //<script type="text/javascript" src="js/logout.js"></script> %>
				<script type="text/javascript">
					
					/*----- Bagian pass ----*/
					var pass_form = document.getElementById("pass_form");
					
					var profil_password = document.getElementById("profil_password");
					var profil_confirm_password = document.getElementById("profil_confirm_password");
					
					profil_form.onsubmit = function()
					{
						if ((profil_password.value==profil_confirm_password.value))
						{
							return true;
						}
						else if (profil_password.value!=profil_confirm_password.value)
						{
							alert("Konfirmasi sandi harus sama dengan sandi");
						}
					};
					
					profil_password.onkeyup = function()
					{
						if (this.checkValidity())
						{
							this.style.backgroundImage = "url('images/valid.png')";
							document.getElementById("profil_confirm_password").pattern = this.value;
						}
						else
							this.style.backgroundImage = "url('images/warning.png')";
						check_submit();
					};
					
					profil_confirm_password.onkeyup = function()
					{
						if (this.checkValidity() && profil_password.checkValidity())
							this.style.backgroundImage = "url('images/valid.png')";
						else
						
							this.style.backgroundImage = "url('images/warning.png')";
						check_submit();
					};
					
					function check_submit()
					{
						if ((profil_password.checkValidity()) && 
							(profil_confirm_password.checkValidity()))
						{
							document.getElementById("profil_submit").disabled="";
						}
						else
						{
							document.getElementById("profil_submit").disabled="disabled";
						}
					}
					
				</script>
				
			</body>
		</html>
<%
	}
%>

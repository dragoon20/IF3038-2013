<%@page import="models.Task"%>
<%@page import="java.text.SimpleDateFormat"%>
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
				<title>MOA - Edit Profil</title>
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
								<form enctype="multipart/form-data" id="profil_form" action="change_profile_data" method="post">
									<div id="profil_head">
										<h1>Edit Profil</h1>
									</div>
									<div class="row">
										<label for="profil_name">Nama Lengkap</label>
										<input id="profil_name" name="fullname" pattern="^.+ .+$" value="<%= user.getFullname() %>" type="text" title="Nama lengkap harus terdiri dari 2 kata dipisah oleh sebuah spasi." required/>
									</div>
									<div class="row">
										<label for="birth_date">Tanggal Lahir</label>
										<input id="birth_date" name="birthdate" value="<%= date_format.format(user.getBirthdate()) %>" pattern="^[1-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$" type="text" onclick="datePicker.showCalendar(event);" title="Tahun harus minimal dari tahun 1955." required/>
									</div>
									<div class="row">
										<label for="profil_email">Email</label>
										<input id="profil_email" name="email" value="<%= user.getEmail() %>" pattern="^.+@.+\...+$" type="text" title="Email yang dimasukkan harus benar dan tidak sama dengan sandi." required/>
									</div>
									<div class="row">
										<label for="profil_avatar">Avatar</label>
										<input id="profil_avatar" name="avatar" type="file" title="Avatar yang diupload harus berekstensi jpg atau jpeg."/>
									</div>
									<div class="row">
										<img id="avatar_image" src="<%= user.getAvatar() %>" alt="avatar profil" />
									</div>
									<input id="profil_submit" type="submit" value="Edit" title="Semua elemen form harus diisi dengan benar dahulu."/>
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
					profile_edit.put("href", "edit_profile");
					profile_edit.put("class", "active");
				
					breadcrumbs_main.put("Profil", profile);
					breadcrumbs_main.put("Edit Profil", profile_edit);
					request.setAttribute("breadcrumbs", breadcrumbs_main);
				%>
				<%@ include file="../template/footer.jsp" %>
				
				<%@ include file="../template/calendar.jsp" %>	
				
				<script type="text/javascript" src="js/search.js"></script>
				<script type="text/javascript" src="js/datepicker.js"></script>
				<% //<script type="text/javascript" src="js/logout.js"></script> %>
				<script type="text/javascript">
					/*----- Bagian Profil ----*/
					var profil_name = document.getElementById("profil_name");
					var birth_date = document.getElementById("birth_date");
					var profil_email = document.getElementById("profil_email");
					var profil_avatar = document.getElementById("profil_avatar");
					
					profil_name.onkeyup = function()
					{
						if (this.checkValidity())
							this.style.backgroundImage = "url('images/valid.png')";
						else
							this.style.backgroundImage = "url('images/warning.png')";
						check_submit();
					}
					
					birth_date.onkeyup = function()
					{
						if ((this.checkValidity()) && (check_date(this.value)))
							this.style.backgroundImage = "url('images/valid.png')";
						else
							this.style.backgroundImage = "url('images/warning.png')";
						check_submit();
					}
					
					profil_email.onkeyup = function()
					{
						if (this.checkValidity())
							this.style.backgroundImage = "url('images/valid.png')";
						else
							this.style.backgroundImage = "url('images/warning.png')";
						check_submit();
					}
					
					profil_avatar.onchange = function()
					{
						if (check_file_jpeg(this))
							this.style.backgroundImage = "url('images/valid.png')";
						else
							this.style.backgroundImage = "url('images/warning.png')";
						check_submit();
					}
					
					function check_date(date)
					{
						var temp = date.split("-");
						var d = new Date(parseInt(temp[0]), parseInt(temp[1]) - 1, parseInt(temp[2]));
						if ((d) && ((d.getMonth() + 1) == parseInt(temp[1])) && (d.getDate() == Number(parseInt(temp[2]))) && (parseInt(temp[0]) >= 1955))
						{
							datePicker.populateTable(d.getMonth(),d.getFullYear());
							return true;
						}
						else
							return false;
					}
					
					function check_file_jpeg(image)
					{
						return (image.value.match("^.+\.(jpe?g|JPE?G)$"));
					}
					
					function check_submit()
					{
						if ((profil_name.checkValidity()) && (birth_date.checkValidity()) && 
							(profil_email.checkValidity()) && ((profil_avatar.value=="")||(check_file_jpeg(profil_avatar))) &&
							(check_date(birth_date.value)))
						{
							document.getElementById("profil_submit").disabled="";
						}
						else
						{
							document.getElementById("profil_submit").disabled="disabled";
						}
					}
					window.onload = function() 
					{
						datePicker.init(document.getElementById("calendar"), document.getElementById("profil_form"));
					}
				</script>
			</body>
		</html>
<%
	}
%>
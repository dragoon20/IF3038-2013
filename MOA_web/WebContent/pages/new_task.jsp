<%@page import="org.json.simple.JSONValue"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="models.Category"%>
<%@page import="controllers.MainApp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if (!MainApp.LoggedIn(session))
	{
		response.sendRedirect("index");
	}
	else
	{
		int id = 0;
		if (request.getParameter("cat")!=null)
		{
			try
			{
				id = Integer.parseInt(request.getParameter("cat"));
			}
			catch (NumberFormatException e)
			{
			}
		}
		
		Category cat = null;
		
		try {
			HashMap<String, String> parameter = new HashMap<String,String>();
			parameter.put("token", MainApp.token(session));
			parameter.put("app_id", MainApp.appId);
			parameter.put("id_kategori", ""+id);
			String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"category/get_category", parameter, "", 0);
			JSONObject ret = (JSONObject)JSONValue.parse(responseString);
			cat = new Category();
			cat.setId_kategori(id);
			cat.setNama_kategori((String)ret.get("nama_kategori"));
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		if ((cat==null) || (cat.getEditable(MainApp.token(session), ""+id)))
		{
			// redirect to error page
		}
%>
		<!DOCTYPE html>
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<meta name="Description" content="" />
				<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
				<title>MOA - Tambah Tugas</title>
				<link rel="stylesheet" href="css/style.css" />
				<link rel="stylesheet" href="css/work.css" />
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
						<div id="work_area">
							<div id="new_task_form_wrap">
								<div id="work_head">
									<h1>Tambah Tugas Baru</h1>
								</div>
								<form id="new_task_form" action="new_task_impl" method="post" enctype="multipart/form-data">
									<input name="id_kategori" type="hidden" value="<%= id %>">
									<div class="row">
										<span class="label">Nama Kategori</span>
										<span id="category_name"><%= cat.getNama_kategori() %></span>
									</div>
									<div class="row">
										<label for="new_task_name">Nama Tugas</label>
										<input id="new_task_name" name="nama_task" pattern="^[a-zA-Z0-9 ]{1,25}$" type="text" title="Nama tugas maksimal 25 karakter terdiri dari karakter alfanumerik dan spasi." required />
									</div>
									<div class="row">
										<label for="new_task_attachment">Attachment</label>
										<input id="new_task_attachment" name="attachment[]" type="file" title="Attachment dari tugas." required />
									</div>
									<div class="row">
										<label for="birth_date">Deadline</label>
										<input id="birth_date" name="deadline" type="text" pattern="^[1-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$" onclick="datePicker.showCalendar(event);" title="Deadlinenya harus setelah hari ini." required/>
									</div>
									<div class="row">
										<label for="new_task_username">Asignee</label>
										<input id="new_task_username" name="assignee" list="asignee_option" pattern="^[^;]{5,}(;[^;]{5,})*$" type="text" title="Asignee yang dimasukkan dan terdaftar dan dipisahkan tanda titik-koma. Jika tidak dimasukkan akan jadi task pribadi."/>
										<div id="auto_comp_assignee">
											<ul id="auto_comp_inflate_assignee"></ul>
										</div>
									</div>
									<div class="row">
										<label for="new_task_tag">Tag</label>
										<input id="new_task_tag" name="tag" pattern="^[^,]+(,[^,]+)*$" type="text" title="Tag harus dimasukkan dan dipisahkan tanda koma jika banyak." required />
										<div id="auto_comp_tag">
											<ul id="auto_comp_inflate_tag"></ul>
										</div>
									</div>
									<input id="new_task_submit" type="submit" value="Tambah" disabled="disabled" title="Semua elemen form harus diisi dengan benar dahulu."/>
								</form>
							</div>
						</div>
					</div>
				</section>
				
				<%
					Map<String, Map<String, String>> breadcrumbs_main = new HashMap<String, Map<String, String>>();

					breadcrumbs_main.put("Dashboard", dashboard);
												
					Map<String, String> new_task = new  HashMap<String, String>();
					new_task.put("href", "new_task?cat="+id);
					new_task.put("class", "active");
					
					breadcrumbs_main.put("Tambah Tugas Baru", new_task);
					
					request.setAttribute("breadcrumbs", breadcrumbs_main);
				%>
				<%@ include file="../template/footer.jsp" %>
				
				<%@ include file="../template/calendar.jsp" %>
				
				<script type="text/javascript" src="js/search.js"></script>
				<script type="text/javascript" src="js/tugas_baru.js"></script>
				<% //<script type="text/javascript" src="js/logout.js"></script> %>
			</body>
		</html>
<%
	}
%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="models.DBSimpleRecord"%>
<%@page import="org.json.simple.JSONValue"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="models.Attachment"%>
<%@page import="models.Tag"%>
<%@page import="models.User"%>
<%@page import="models.Task"%>
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
		Task task = null;
		User[] users = null;
		Tag[] tags = null;
		Attachment[] attachments = null;
	    String namaKategori = "";
	        
		if (request.getParameter("id")!=null)
		{
            try{
	           	HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("id_task", ""+request.getParameter("id"));
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"task/get_task", parameter, "", 0);
				JSONObject ret = (JSONObject)JSONValue.parse(responseString);

				task = new Task();
                task.setId_task(Integer.valueOf(ret.get("id_task").toString()));
                task.setNama_task(String.valueOf(ret.get("nama_task")));
                task.setDeadline(new java.sql.Date(DBSimpleRecord.sdf.parse(ret.get("deadline").toString()).getTime()));
                task.setStatus((Boolean)ret.get("status"));
                namaKategori = String.valueOf(ret.get("nama_kategori"));
                
			} catch(Exception exc) {
				exc.printStackTrace();
			}
			if ((task!=null) && (task.getEditable(MainApp.token(session), task.getId_task())))
			{
				users = task.getAssignee(MainApp.token(session), task.getId_task());
				tags = task.getTags(MainApp.token(session), task.getId_task());
				attachments = task.getAttachment(MainApp.token(session), task.getId_task());
				SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
%>
		<!DOCTYPE html>
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<meta name="Description" content="" />
				<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
				<title>MOA - Edit Tugas</title>
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
							<div id="edit_task_form_wrap">
								<div id="work_head">
									<h1>Edit Tugas</h1>
								</div>
								<form id="edit_task_form" action="edit_task_impl" method="post" enctype="multipart/form-data">
									<input type="hidden" name="id_task" value="<%= task.getId_task() %>" >
									<div class="row">
										<label for="category_name">Nama Kategori</label>
										<span id="category_name"><%= namaKategori %></span>
									</div>
									<div class="row">
										<label for="edit_task_name">Nama Tugas</label>
										<input id="edit_task_name" name="nama_task" value="<%= task.getNama_task() %>" pattern="^[a-zA-Z0-9 ]{1,25}$" type="text" title="Nama tugas maksimal 25 karakter terdiri dari karakter alfanumerik dan spasi." required />
									</div>
									<div class="row">
										<label for="edit_task_attachment">Attachment</label>
										<input id="edit_task_attachment" name="attachment[]" type="file" title="Attachment dari tugas." multiple="" />
										<div id="task_attachment">
											<%
												int i = 1;
												for (Attachment attachment : attachments)
												{
													String file = "<a href=\""+attachment.getAttachment()+"\">Attachment "+i+"</a><br />";
													out.println(file);
													i++;
												}
											%>
										</div>
									</div>
									<div class="row">
										<label for="birth_date">Deadline</label>
										<input id="birth_date" name="deadline" type="text" value="<%= date_format.format(task.getDeadline()) %>" pattern="^[1-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$" onclick="datePicker.showCalendar(event);" title="Deadlinenya harus setelah hari ini." required/>
									</div>
									<div class="row">
										<label for="edit_task_username">Asignee</label>
										<%
											StringBuilder sb_asignee = new StringBuilder();
											for (User u : users)
											{
												sb_asignee.append(u.getUsername());
												sb_asignee.append(",");
											}
											if (users.length > 0)
											{
												sb_asignee.deleteCharAt(sb_asignee.length()-1);
											}
										%>
										<input id="edit_task_username" name="assignee" value="<%= sb_asignee.toString() %>" list="asignee_option" pattern="^[^;]{5,}(;[^;]{5,})*$" type="text" title="Asignee yang dimasukkan dan terdaftar dan dipisahkan tanda titik-koma. Jika tidak dimasukkan akan jadi task pribadi."/>
										<div id="auto_comp_assignee">
											<ul id="auto_comp_inflate_assignee"></ul>
										</div>
									</div>
									<div class="row">
										<label for="edit_task_tag">Tag</label>
										<%
											StringBuilder sb_tag = new StringBuilder();
											for (Tag t : tags)
											{
												sb_tag.append(t.getTag_name());
												sb_tag.append(",");
											}
											if (tags.length > 0)
											{
												sb_tag.deleteCharAt(sb_tag.length()-1);
											}
										%>
										<input id="edit_task_tag" name="tag" value="<%= sb_tag.toString() %>" pattern="^[^,]+(,[^,]+)*$" type="text" title="Tag harus dimasukkan dan dipisahkan tanda koma jika banyak." required />
										<div id="auto_comp_tag">
											<ul id="auto_comp_inflate_tag"></ul>
										</div>
									</div>
									<div class="row">
										<span class="label">Status</span>
										<span id="task_status" class="<%= (task.isStatus()) ? "status_done" : "status_not_done" %>"><%= (task.isStatus())? "selesai" : "belum selesai" %></span>
									</div>
									<div class="clear"></div>
									<input id="edit_task_submit" type="submit" value="Edit" disabled="disabled" title="Semua elemen form harus diisi dengan benar dahulu."/>
									<a href="#" id="editTaskStatusLink" class="button_link">Ubah Status</a>
								</form>
							</div>
						</div>
					</div>
				</section>
				<%
					Map<String, Map<String, String>> breadcrumbs_main = new HashMap<String, Map<String, String>>();

					breadcrumbs_main.put("Dashboard", dashboard);
												
					Map<String, String> cur_task = new  HashMap<String, String>();
					cur_task.put("href", "task?id="+request.getParameter("id"));
					
					breadcrumbs_main.put("Tugas", cur_task);
					
					Map<String, String> edit_task = new  HashMap<String, String>();
					edit_task.put("href", "edit_task?id="+request.getParameter("id"));
					edit_task.put("class", "active");
					
					breadcrumbs_main.put("Edit Tugas", edit_task);
					
					request.setAttribute("breadcrumbs", breadcrumbs_main);
				%>
				<%@ include file="../template/footer.jsp" %>
				
				<%@ include file="../template/calendar.jsp" %>
				
				<script type="text/javascript" src="js/search.js"></script>
				<script type="text/javascript" >
					var id_task = <%= task.getId_task() %>
					var checked = <%= !task.isStatus() %>
				</script>
				<script type="text/javascript" src="js/tugas_edit.js"></script>
				<% //<script type="text/javascript" src="js/logout.js"></script> %>
			</body>
		</html>
<%
			}
			else
			{
				// redirect to error page
			}
		}
		else
		{
		}
	}
%>
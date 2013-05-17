<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="models.DBSimpleRecord"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONValue"%>
<%@page import="java.util.HashMap"%>
<%@page import="models.Comment"%>
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
		Comment[] comments = null;
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
			if (task!=null)
			{
				users = task.getAssignee(MainApp.token(session), task.getId_task());
				tags = task.getTags(MainApp.token(session), task.getId_task());
				attachments = task.getAttachment(MainApp.token(session), task.getId_task());
				comments = task.getComment(MainApp.token(session), task.getId_task());
				
				SimpleDateFormat date_format = new SimpleDateFormat("dd MMMM yyyy");
				SimpleDateFormat date_format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat date_format4 = new SimpleDateFormat("HH:mm - EEE/MMMM");
%>
		<!DOCTYPE html>
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<meta name="Description" content="" />
				<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
				<title>MOA - Detail Tugas</title>
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
						<div id="task_left">
							<div id="task_form_wrap">
								<div id="work_head">
									<h1>Tugas</h1>
								</div>
								<div class="row">
									<span class="label">Nama Kategori</span>
									<span id="category_name"><%= namaKategori %></span>
								</div>
								<div class="row">
									<span class="label">Nama Tugas</span>
									<span id="task_name"><%= task.getNama_task() %></span>
								</div>
								<div class="row">
									<span class="label">Attachment</span>
									<div id="task_attachment">
									<%
										int i = 1;
										for (Attachment attachment : attachments)
										{
											String[] atch = attachment.getAttachment().split("\\.");
											if ("MP4".equals(atch[atch.length-1].toUpperCase()))
											{
									%>
												<video class="atchmt" controls>
													<source src="<%= attachment.getAttachment() %>" type="video/mp4">
													</source>
												</video>
									<%		
											}
											else if ((atch[atch.length-1].toUpperCase().matches("JPG")) || ("PNG".equals(atch[atch.length-1].toUpperCase())) || ("GIF".equals(atch[atch.length-1].toUpperCase())))
											{
									%>
												<img class="atchmt" src="<%= attachment.getAttachment() %>" alt="Task Attachment">
									<%
											}
											else
											{
												String file = "<a href=\""+attachment.getAttachment()+"\">Download attachment "+i+"</a><br>";
												out.println(file);
											}
											i++;
										}
									%>
									</div>
								</div>
								<div class="row">
									<span class="label">Deadline</span>
									<span id="birth_date"><%= date_format.format(task.getDeadline()) %></span>
								</div>
								<div class="row">
									<span class="label">Asignee</span>
									<span id="task_username">
									<%
										StringBuilder sb_asignee = new StringBuilder();
										for (User u : users)
										{
											sb_asignee.append("<a href=\"profile?username="+u.getUsername()+"\">");
											sb_asignee.append(u.getUsername());
											sb_asignee.append("</a>");
											sb_asignee.append(",");
										}
										if (users.length > 0)
										{
											sb_asignee.deleteCharAt(sb_asignee.length()-1);
										}
										out.println(sb_asignee.toString());
									%>
									</span>
								</div>
								<div class="row">
									<span class="label">Tag</span>
									<span id="task_tag">
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
										out.println(sb_tag.toString());
									%>
									</span>
								</div>
								<div class="row">
									<span class="label">Status</span>
									<span id="task_status" class="<%= (task.isStatus()) ? "status_done" : "status_not_done" %>"><%= (task.isStatus())? "selesai" : "belum selesai" %></span>
								</div>
								<div class="clear"></div>
								<%
									if (task.getEditable(MainApp.token(session), task.getId_task()))
									{
										out.println("<a href=\"edit_task?id="+task.getId_task()+"\" id=\"editTaskLink\" class=\"button_link\">Edit Tugas</a>");
										out.println("<a href=\"#\" id=\"editTaskStatusLink\" class=\"button_link\">Ubah Status</a>");
									}
								%>
								<%
									if (task.getDeletable(MainApp.token(session), task.getId_task()))
									{
										out.println("<a href=\"#\" id=\"removeTaskLink\" class=\"button_link\">Hapus</a>");
									}
								%>
							</div>
						</div>
						<div id="task_right">
							<div id="work_head">
								<h1><% int total_comment = task.getTotalComment(MainApp.token(session), task.getId_task()); %>Komentar</h1>
							</div>
							<div id="comments">
								<%
									if (total_comment > 10)
									{
								%>
									<a id="more_link" href="javascript:more_comment()">Komentar Sebelumnya</a>
								<%
									}
								%>
								<span id="show_status" class="right">Menunjukkan <%= Math.min(10, total_comment) %> dari <%= total_comment %></span>
								<div class="clear"></div>
								<div id="commentsList">
									<%
										String firsttimestamp = "";
										if (total_comment > 0)
										{
											firsttimestamp = comments[0].getTimestamp().toString();
										}
										else
										{
											firsttimestamp = date_format3.format(new Date());
										}
										String lasttimestamp = date_format3.format(new Date());
										
										for (Comment comment : comments)
										{
											User user = comment.getUser(MainApp.token(session), ""+comment.getId_komentar());
											out.println("<article id=\"comment_"+comment.getId_komentar()+"\" class=\"comment\">");
			                                                                out.println("<a href=\"profile?id="+user.getId_user()+"\">");
													out.println("<img src=\""+user.getAvatar()+"\" alt=\""+user.getFullname()+"\" class=\"icon_pict\" >");
												out.println("</a>");
												out.println("<div class=\"right\">");
													out.println(date_format4.format(comment.getTimestamp()));
			                                                                                
			                                                                                if(comment.getDeletable(user.getId_user(),MainApp.token(session)))
													{
														out.println("<a href=\"javascript:delete_comment("+comment.getId_komentar()+")\">DELETE</a>");
													}
												out.println("</div>");
												out.println("<header>");
													out.println("<a href=\"profile?id="+user.getId_user()+"\">");
														out.println(user.getUsername());
													out.println("</a>");
												out.println("</header>");
												out.println("<p>");
													out.println(comment.getKomentar());
												out.println("</p>");
											out.println("</article>");
											lasttimestamp = comment.getTimestamp().toString();
										}
									%>
								</div>
							</div>
							<div id="comment_form_wrap">
								<form id="comment_form" method="post">
									<div class="row">
										<%
											out.println("<a href=\"profile\">");
												out.println("<img src=\""+MainApp.currentUser(session).getAvatar()+"\" alt=\""+MainApp.currentUser(session).getFullname()+"\" class=\"icon_pict\" >");
											out.println("</a>");
										%>
										<textarea id="comment_input"></textarea>
									</div>
									<input id="comment_submit" type="submit" value="Komen"/>
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
					cur_task.put("class", "active");
					
					breadcrumbs_main.put("Tugas", cur_task);
					
					request.setAttribute("breadcrumbs", breadcrumbs_main);
				%>
				<%@ include file="../template/footer.jsp" %>
				
				<script type="text/javascript" src="js/search.js"></script>
				<script type="text/javascript" >
					var id_task = <%= task.getId_task() %>
					var checked = <%= !task.isStatus() %>
					var first_timestamp = "<%= firsttimestamp %>";
					var timestamp = "<%= lasttimestamp %>";
					var username = "<%= MainApp.currentUser(session).getUsername() %>";
					var total_comment = <%= total_comment %>;
					var current_total_comment = <%= Math.min(10, total_comment) %>;
				</script>
				<script type="text/javascript" src="js/tugas.js"></script>
				<script type="text/javascript" src="js/comment.js"></script>
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
			// redirect to error page
		}
	}
%>

<%@page import="models.Tag"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="models.Task"%>
<%@page import="models.User"%>
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
		int cat = 0;
		if (request.getParameter("cat")!=null)
		{
			try
			{
				cat = Integer.parseInt(request.getParameter("cat"));
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}

		String todoQ = "status=0";
		String doneQ = "status=1";
		String narrowQ = "";
		boolean canDelete = false;

		Category currentCat = null;
		if (cat != 0) 
		{
			try 
			{
				HashMap<String, String> parameter = new HashMap<String,String>();
				parameter.put("token", MainApp.token(session));
				parameter.put("app_id", MainApp.appId);
				parameter.put("id_category", ""+cat);
				String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"category/get_category", parameter, "", 0);
				JSONObject ret = (JSONObject)JSONValue.parse(responseString);
				currentCat = new Category();
				currentCat.setId_kategori(cat);
				currentCat.setNama_kategori((String)ret.get("nama_kategori"));
			}
			catch(Exception exc) 
			{
				exc.printStackTrace();
			}
			
			if (currentCat!=null)
			{
				canDelete = currentCat.getDeletable(MainApp.token(session), ""+currentCat.getId_kategori());
			}
		}
	        
	    User user = new User();
	    String tokenString = MainApp.token(session);
		Task[] tasks = user.getTasks(tokenString);
		Task[] todo = user.getTasks(tokenString,0, cat);
		Task[] done = user.getTasks(tokenString,1, cat);

		Category[] categories = user.getCategories(tokenString);

		// Presentation Logic Here
		
		String pageTitle = "";
		if (cat!=0) 
		{
			pageTitle = currentCat.getNama_kategori();
		}
		else 
		{
			pageTitle = "All Tasks";
		}
		
		SimpleDateFormat temp_task_deadline = new SimpleDateFormat("dd MMMM yyyy");
		ArrayList<String> javascripts = new ArrayList<String>();
		javascripts.add("checker");
		javascripts.add("dashboard");
%>
		<!DOCTYPE html>
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<meta name="Description" content="" />
				<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
				<title>MOA - Dashboard</title>
				<link rel="stylesheet" href="css/style.css" />
				<link rel="stylesheet" href="css/dashboard.css" />
			</head>
			<body>
				<%
					Map<String, Map<String, String>> menu_main = new HashMap<String, Map<String, String>>();

					Map<String, String> dashboard = new  HashMap<String, String>();
					dashboard.put("href", "dashboard");
					dashboard.put("class", "active");
					menu_main.put("Dashboard", dashboard);
										
					request.setAttribute("menu", menu_main);
				%>
				<%@ include file="../template/header.jsp" %>
				<section>
					<div id="content_wrap" class="wrap">
		                <div id="dashboard_category">
		                	<nav>
		                        <ul id="categories">
		                            <li class="dashboard_category_element<%= (cat==0) ? "_aktif" : "" %>"> <a href="javascript:void(0);" onclick="checkCat(0, this);">Semua Kategori</a></li>
		                            <% 
										for (Category cate : categories)
										{
									%> 
											<li class="dashboard_category_element<%= (cat==cate.getId_kategori()) ? "_aktif" : "" %>"> 
												<a href="javascript:void(0);" onclick="checkCat(<%= cate.getId_kategori() %>, this);">
													<%= cate.getNama_kategori() %>
												</a>
												<%
													if(cate.getDeletable(tokenString, ""+cate.getId_kategori()))
													{
												%>
													<a href="javascript:void(0);" onclick="deleteCat(<%= cate.getId_kategori() %>, this);" class="bold right">X</a>
												<%
													}
												%>
											</li>
									<%
										}
									%>
		                        </ul>
		                    </nav>
							<div id="new_category" class="dashboard_category_element">
								<a href="javascript:new_category();">Tambah Kategori Baru</a>
							</div>
		                </div>
		                <div id="dashboard_tugas">
		                	<div id="dashboard_tugas_inner">
			                	<div id="dashboard_tugas_left">
			                		<div id="slide_right">
									</div>
				                	<h1>Belum Selesai</h1>
				                    <hr/>
				                    <nav>
				                        <ul id="not_done_tasks">
				                        	<%
												for (Task task : todo)
												{
											%>
													<li>
														<div class="dashboard_tugas_element">
															<div class="dashboard_tugas_judul">
																<a class="task_link" href="task?id=<%= task.getId_task() %>">
																	<%= task.getNama_task() %>
																</a>
															</div>
															<div class="dashboard_tugas_tanda"><a href="javascript:void(0);" onclick="checkTask(<%= task.getId_task() %>, true);">&gt;&gt;&gt;&gt;</a></div>
															<div class="dashboard_tugas_tanggal">Deadline: <%= temp_task_deadline.format(task.getDeadline()) %></div>
															<div class="dashboard_tugas_tag_area">
																<ul>
																	<%
																		for (Tag tag : task.getTags(tokenString, task.getId_task()))
																		{
																	%>
																			<li>
																				<%= tag.getTag_name() %>
																			</li>
																	<%
																		}
																	%>
																</ul>
															</div>
														</div>
													</li>
											<%
												}
											%>
				                        </ul>       
				                    </nav>
									<a id="new_task_link" href="new_task">
										<div class="dashboard_tugas_element">
											<div class="dashboard_tugas_judul">Tugas Baru</div>
										</div>
									</a>
								</div>
								<div id="dashboard_tugas_right">
									<div id="slide_left">
									</div>
				                	<h1>Selesai</h1>
				                    <hr/>
				                    <nav>
				                        <ul id="done_tasks">
				                        	<%
												for (Task task : done)
												{
											%>
													<li>
														<div class="dashboard_tugas_element">
															<div class="dashboard_tugas_judul">
																<a class="task_link" href="task?id=<%= task.getId_task() %>">
																	<%= task.getNama_task() %>
																</a>
															</div>
															<div class="dashboard_tugas_tanda"><a href="javascript:void(0);" onclick="checkTask(<%= task.getId_task() %>, false">&lt;&lt;&lt;&lt;</a></div>
															<div class="dashboard_tugas_tanggal">Deadline: <%= temp_task_deadline.format(task.getDeadline()) %></div>
															<div class="dashboard_tugas_tag_area">
																<ul>
																	<%
																		for (Tag tag : task.getTags(tokenString, task.getId_task()))
																		{
																	%>
																			<li>
																				<%= tag.getTag_name() %>
																			</li>
																	<%
																		}
																	%>
																</ul>
															</div>
														</div>
													</li>
											<%
												}
											%>
				                        </ul>       
				                    </nav>
								</div>
							</div>
		                </div>
		                <div class="clear"></div>
					</div>
				</section>
				
				<%
					Map<String, Map<String, String>> breadcrumbs_main = new HashMap<String, Map<String, String>>();

					breadcrumbs_main.put("Dashboard", dashboard);
					request.setAttribute("breadcrumbs", breadcrumbs_main);
				%>
				<%@ include file="../template/footer.jsp" %>
				
				<div id="black_trans">
					<div id="frame_new_category">
						<div id="close_button">
							<a href="javascript:close();">X</a>
						</div>
						<div id="new_category_area">
							<div id="new_category_form_wrap">
								<div id="new_category_head">
									<h2>Tambah Kategori Baru</h2>
								</div>
								<form id="new_category_form">
									<div class="row">
										<label for="new_category_name">Nama Kategori</label>
										<input id="new_category_name" name="nama_kategori" type="text" title="Nama kategori harus diisi." required />
									</div>
									<div class="row">
										<label for="new_category_username">Username</label>
										<input id="new_category_username" name="usernames_list" pattern="^[^;]{5,}(;[^;]{5,})*$" type="text" title="Username harus terdaftar dan dipisahkan tanda titik-koma. Kosongkan jika private." />
										<div id="auto_comp_assignee">
											<ul id="auto_comp_inflate_assignee"></ul>
										</div>
									</div>
									<input id="new_category_submit" type="submit" value="Tambah" disabled="disabled" title="Semua elemen form harus diisi dengan benar dahulu."/>
								</form>
							</div>
						</div>
					</div>
				</div>
		
				<script type="text/javascript" src="js/search.js"></script>
				<script type="text/javascript" src="js/dashboard.js"></script>
				<script>
					var currentCat = <%= cat %>;
					<%
						if ((currentCat==null) || (!currentCat.getEditable(tokenString, ""+currentCat.getId_kategori())))
						{
							out.println("document.getElementById(\"new_task_link\").style.display = \"none\";");
						}
					%>
					var not_done = true;
					var dashboard_tugas = document.getElementById("dashboard_tugas");
					dashboard_tugas.onmouseover = function()
					{
						document.getElementById("slide_right").style.opacity = "1";
						document.getElementById("slide_left").style.opacity = "1";
					}
					
					dashboard_tugas.onmouseout = function()
					{
						document.getElementById("slide_left").style.opacity = "0";
						document.getElementById("slide_right").style.opacity = "0";
					}
					
					function next_tasks()
					{
						if (not_done)
						{
							not_done = false;
							document.getElementById("dashboard_tugas_inner").style.left = "-"+document.defaultView.getComputedStyle(document.getElementById("dashboard_tugas"),"").getPropertyValue("width");
						}
					}
					function prev_tasks()
					{
						if (!not_done)
						{
							not_done = true;
							document.getElementById("dashboard_tugas_inner").style.left = 0+"px";
						}
					}
					
					document.getElementById("slide_left").onclick = function() {prev_tasks();};
					document.getElementById("slide_right").onclick = function() {next_tasks();};
					
					document.onkeydown = function(event)
					{
						if (event.keyCode=="37")
							prev_tasks();
						else if (event.keyCode=="39")
							next_tasks();
					};
				</script>
			</body>
		</html>
<%
	}
%>
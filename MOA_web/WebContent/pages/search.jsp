<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Date"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONValue"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Arrays"%>
<%@page import="models.DBSimpleRecord"%>
<%@page import="java.util.List"%>
<%@page import="models.Task"%>
<%@page import="models.Category"%>
<%@page import="controllers.MainApp"%>
<%
	if (!MainApp.LoggedIn(session))
	{
		response.sendRedirect("index");
	}
	else
	{
		String query = "";
		if (request.getParameter("search_query")!=null)
		{
			query = request.getParameter("search_query");
		}
		
		String terms = "%"+query.replace(" ", "%")+"%";
		
		String catf = "nama_kategori";
		String taskf = "nama_task";
		String userf = "nama_user";
		
		String query_type = "all";
		if (request.getParameter("type")!=null)
		{
			query_type = request.getParameter("type");
		}
		boolean all = ("all".equals(query_type.toLowerCase()));
		
		Task[] tasks = null;
		User[] users = null;
		Category[] categories = null;
%>
			<!DOCTYPE html>
			<html xmlns="http://www.w3.org/1999/xhtml">
				<head>
					<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
					<meta name="Description" content="" />
					<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
					<title>MOA - Pencarian</title>
					<link rel="stylesheet" href="css/style.css" />
					<link rel="stylesheet" href="css/search.css" />
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
							<div id="search_area">
								<div id="search_head">
									<h1>Pencarian</h1>
								</div>
								<div class="search-results<%= (all) ? "all" : "" %>">
								<%
									if (("task".equals(query_type.toLowerCase())) || (all))
									{
										List<Task> list = new ArrayList<Task>();
										try {
											HashMap<String, String> parameter = new HashMap<String,String>();
											parameter.put("token", MainApp.token(session));
											parameter.put("app_id", MainApp.appId);
											parameter.put("terms", terms);
											parameter.put("start", ""+0);
						
											String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"task/search_tasks", parameter, "", 0);
											JSONArray ret = (JSONArray)JSONValue.parse(responseString);
											for (Object obj : ret)
											{
												JSONObject js_obj = (JSONObject) obj;
												Task task = new Task();
												task.setId_task(Integer.parseInt(js_obj.get("id_task").toString()));
												task.setNama_task(js_obj.get("nama_task").toString());
												task.setDeadline(new Date(DBSimpleRecord.sdf.parse(js_obj.get("deadline").toString()).getTime()));
												task.setId_kategori(Integer.parseInt(js_obj.get("id_kategori").toString()));
												task.setStatus(Boolean.valueOf(js_obj.get("id_task").toString()));
												
												list.add(task);
											}
										} catch(Exception exc) {
											exc.printStackTrace();
										}
										tasks = list.toArray(new Task[list.size()]);
								%>
										<div id="tasks">
											<div id="tasks_head">
												<h3>Tasks</h3>
											</div>
											<!-- Tasks -->
											<div id="taskList">
												<%
													if (tasks!=null)
													{
														for (Task task : tasks)
														{
															request.setAttribute("task", task);
												%>
															<%@ include file="../template/task.jsp" %>
												<%
														}
													}
													else
													{
														out.println("<p>No tasks were found.</p>");
													}
												%>
											</div>
										</div>
								<%
									}
									if (("user".equals(query_type.toLowerCase())) || (all))
									{
										List<User> list = new ArrayList<User>();
										try {
											HashMap<String, String> parameter = new HashMap<String,String>();
											parameter.put("token", MainApp.token(session));
											parameter.put("app_id", MainApp.appId);
											parameter.put("terms", terms);
											parameter.put("start", ""+0);
						
											String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"user/search_users", parameter, "", 0);
											JSONArray ret = (JSONArray)JSONValue.parse(responseString);
											for (Object obj : ret)
											{
												JSONObject js_obj = (JSONObject) obj;
												User user = new User();
												user.setUsername(js_obj.get("username").toString());
												user.setAvatar(js_obj.get("avatar").toString());
												user.setFullname(js_obj.get("fullname").toString());
												
												list.add(user);
											}
										} catch(Exception exc) {
											exc.printStackTrace();
										}
										users = list.toArray(new User[list.size()]);
								%>
										<div id="users">
											<div id="users_head">
												<h3>Users</h3>
											</div>
											<div id="userList">
												<%
													if (users!=null)
													{
														for (User user : users)
														{
															request.setAttribute("user", user);
												%>
															<%@ include file="../template/user.jsp" %>
												<%
														}
													}
													else
													{
														out.println("<p>No users were found.</p>");
													}
												%>
											</div>
										</div>
								<%
									}
									if (("category".equals(query_type.toLowerCase())) || (all))
									{
										List<Category> list = new ArrayList<Category>();
										try {
											HashMap<String, String> parameter = new HashMap<String,String>();
											parameter.put("token", MainApp.token(session));
											parameter.put("app_id", MainApp.appId);
											parameter.put("terms", terms);
											parameter.put("start", ""+0);
						
											String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"category/search_categories", parameter, "", 0);
											JSONArray ret = (JSONArray)JSONValue.parse(responseString);
											for (Object obj : ret)
											{
												JSONObject js_obj = (JSONObject) obj;
												Category cate = new Category();
												cate.setId_kategori(Integer.parseInt(js_obj.get("id_category").toString()));
												cate.setNama_kategori(js_obj.get("category_name").toString());
												
												list.add(cate);
											}
										} catch(Exception exc) {
											exc.printStackTrace();
										}
										categories = list.toArray(new Category[list.size()]);
								%>
										<div id="categories">
											<div id="categories_head">
												<h3>Categories</h3>
											</div>
											<ul id="categoryList">
											<%
												if (categories!=null)
												{
													for (Category cat : categories)
													{
											%>
														<li id="categoryLi<%= cat.getId_kategori() %>">
															<a href="dashboard.php?cat=<%= cat.getId_kategori() %>" data-category-id="<%= cat.getId_kategori() %>">
																<%= cat.getNama_kategori() %>
															</a>
														</li>
											<%
													}
											%>
													</ul>
											<%
												}
												else
												{
													out.println("<p>No categories were found.</p>");
												}
											%>
										</div>
								<%
									}
								%>
								<div class="clear"></div>
							</div>
						</div>
					</div>
				</section>
				
				<%
					Map<String, Map<String, String>> breadcrumbs_main = new HashMap<String, Map<String, String>>();

					breadcrumbs_main.put("Dashboard", dashboard);
												
					Map<String, String> search = new  HashMap<String, String>();
					search.put("href", "search?search_query=" + query);
					search.put("class", "active");
					
					breadcrumbs_main.put("Pencarian", search);
					
					request.setAttribute("breadcrumbs", breadcrumbs_main);
				%>
				<%@ include file="../template/footer.jsp" %>
				<script type="text/javascript" src="js/search.js"></script>
				<script>
					var isAll = <%= (all) ? "true" : "false" %>;
					var q = "<%= query %>";
					var currentType = "<%= query_type %>";
				</script>
			</body>
		</html>
<%
	}
%>
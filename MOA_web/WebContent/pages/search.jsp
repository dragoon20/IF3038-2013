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
	
	String query = "";
	if (request.getParameter("q")!=null)
	{
		query = request.getParameter("q");
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
	
	request.setAttribute("title", "MOA - Search");
	request.setAttribute("currentPage", (request.getParameter("id")!=null) ? "" : "search");
	
	ArrayList<String> javascripts = new ArrayList<String>();
	javascripts.add("search");
	request.setAttribute("javascripts", javascripts);
	
	Task[] tasks = null;
	User[] users = null;
	Category[] categories = null;
%>
<%@ include file="../template/header.jsp" %>
	<div class="content">
		<div>
		<header>
			<h1>Search</h1>
		</header>
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
				<div class="result-set">
					<section class="tasks">
						<header>
							<h3>Tasks</h3>
						</header>
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
					</section>
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
				<div class="result-set">
					<section class="users">
						<header>
							<h3>Users</h3>
						</header>
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
					</section>
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
		<div class="result-set">
			<section class="categories">
				<header>
					<h3>Categories</h3>
				</header>
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
			</section>
		</div>
		<%
			}
		%>
	</div>
	</div>
<script>
	var isAll = <%= (all) ? "true" : "false" %>;
	var q = "<%= query %>";
	var currentType = "<%= query_type %>";
</script>
<%@ include file="../template/footer.jsp" %>
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
	
	int id = MainApp.currentUserId(session);
	String baseTaskQ = "id_kategori IN ( SELECT id_kategori FROM "+Category.getTableName()+" WHERE id_user='?' "+
			 "OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user='?') "+
			 "OR id_kategori IN (SELECT id_kategori FROM "+ Task.getTableName() +" AS t LEFT OUTER JOIN assign AS a "+
			 "ON t.id_task=a.id_task WHERE t.id_user = '?' OR a.id_user = '?' ))";
	
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
		<div class="search-results<?php if ($all) echo ' all' ?>">
		<%
			if (("task".equals(query_type.toLowerCase())) || (all))
			{
				tasks = (Task[])Task.getModel().findAllLimit(baseTaskQ+" AND nama_task LIKE '?'", new Object[]{id, id, id, id, terms}, new String[]{"integer", "integer", "integer", "integer", "string"}, null, 0, 10);
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
				users = (User[])User.getModel().findAllLimit("username LIKE '?' OR fullname LIKE '?' OR email LIKE '?' OR birthdate LIKE '?'", new Object[]{terms, terms, terms, terms}, new String[]{"string", "string", "string", "string"}, new String[]{"id_user", "username", "avatar", "fullname"}, 0, 10);
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
				categories = (Category[])Category.getModel().findAllLimit("nama_kategori LIKE '?'", new Object[]{terms}, new String[]{"string"}, null, 0, 10);
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
<%@page import="models.Task"%>
<%@page import="models.Category"%>
<%@page import="java.util.ArrayList"%>
<%@page import="controllers.MainApp"%>
<%
	if (!MainApp.LoggedIn(session))
	{
		response.sendRedirect("index");
	}
	
	// Business Logic Here

	int cat = 0;
	if (request.getParameter("cat")!=null)
	{
		try
		{
			cat = Integer.parseInt(request.getParameter("cat"));
		}
		catch (NumberFormatException e)
		{
			
		}
	}

	String todoQ = "status=0";
	String doneQ = "status=1";
	String narrowQ = "";
	boolean canDelete = false;

	Category currentCat = null;
	if (cat != 0) 
	{
		currentCat = (Category)Category.getModel().find("id_kategori = ?", new Object[]{cat}, new String[]{"integer"}, null);
		if (currentCat!=null)
		{
			canDelete = (currentCat.getId_user()==MainApp.currentUserId(session));
		}
		/*
		else {
			unset($currentCat);
			unset($cat);
		}*/
	}
        
        User user = new User();
        String tokenString = request.getSession().getAttribute("token").toString();
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
	
	ArrayList<String> javascripts = new ArrayList<String>();
	javascripts.add("checker");
	javascripts.add("dashboard");
	request.setAttribute("javascripts", javascripts);
	request.setAttribute("title", "MOA - Dashboard");
	request.setAttribute("currentPage", "dashboard");
%>
<%@ include file="../template/header.jsp" %>
		<div class="content">
			<div class="dashboard">	
				<header>
					<h1>Dashboard</h1>
					<ul id="categoryTasks">
						<li class="add-task-link" id="deleteCategoryLi"><a href="newwork" id="deleteCategoryLink">Delete Category</a></li>
						<li class="add-task-link" id="addTaskLi"><a id="addTaskCat" href="new_work">New Task</a></li>
					</ul>
				</header>
				<div class="primary" id="dashboardPrimary">
					<form action="" method="POST">
					<section class="tasks current">
						<header>
							<h3 id="pageTitle"><%= pageTitle %></h3>
						</header>

						<div id="tasksList">
						<%
							for (Task task : todo)
							{
								request.setAttribute("task", task);
						%>
								<%@ include file="../template/task.jsp" %>
						<%
							}
						%>
						</div>
					</section>

					<section class="tasks completed">
						<header>
							<h3>Completed Tasks</h3>
						</header>

						<div id="completedTasksList">
						<%
							for (Task task : done)
							{
								request.setAttribute("task", task);
						%>
								<%@ include file="../template/task.jsp" %>
						<%
							}
						%>
						</div>
					</section>
					</form>
				</div>
			
				<div class="secondary">
					<section class="categories">
						<header>
							<h3>Categories</h3>
						</header>
						<ul id="categoryList">
							<li id="categoryLi0" <% if (currentCat==null) out.print("class=\"active\""); %>>
								<a href="dashboard" data-category-id="0">
									All Tasks
								</a>
							</li>
							<% 
								for (Category cate : categories)
								{
							%> 
                                                                        <li data-deletable="<%= cate.getDeletable(tokenString, ""+cat) ? "true" : "false" %>" id="categoryLi<%= cate.getId_kategori() %>"<%= ((currentCat!=null) && (currentCat.getId_kategori() == cate.getId_kategori()))? "class=\"active\"" : "" %>>
										<a href="dashboard?cat=<%= cate.getId_kategori() %>" data-category-id="<%= cate.getId_kategori() %>">
											<%= cate.getNama_kategori() %>
										</a>
									</li>
							<%
								}
							%>
						</ul>
						<button type="button" id="addCategoryButton">Tambah Kategori</button>
					</section>
				</div>

			</div>

		</div>
		<% 
			if (currentCat!=null)
			{
		%>
		<script>
			var currentCat = <%= currentCat.getId_kategori() %>;
			var canDelete = <%= (canDelete) ? "true" : "false" %>;
		</script>
		<%
			}
		%>
		<div class="modal-overlay" id="modalOverlay">
			<div class="modal-dialog">
				<a class="close">&times;</a>
				<header><h3>Tambah Kategori</h3></header>
				<form id="newCategoryForm">
					<div class="field">
						<label for="nama_kategori">Nama Kategori</label>
						<input id="nama_kategori" name="nama_kategori" type="text" title="Nama kategori harus diisi." required />
					</div>
					<div class="field">
						<label for="usernames_list">Username</label>
						<input id="usernames_list" name="usernames_list" pattern="^[^;]{5,}(;[^;]{5,})*$" type="text" title="Username harus terdaftar dan dipisahkan tanda titik-koma. Kosongkan jika private." />
						<div id="auto_comp_assignee">
							<ul id="auto_comp_inflate_assignee"></ul>
						</div>
					</div>
					<div class="buttons">
						<button type="submit" title="Semua elemen form harus diisi dengan benar dahulu.">Simpan</button>
					</div>
				</form>
			</div>
		</div>
<%@ include file="../template/footer.jsp" %>

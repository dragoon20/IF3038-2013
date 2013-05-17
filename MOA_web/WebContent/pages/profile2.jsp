<%@page import="java.sql.Date"%>
<%@page import="models.DBSimpleRecord"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONValue"%>
<%@page import="java.util.HashMap"%>
<%@page import="models.Tag"%>
<%@page import="models.Task"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="models.User"%>
<%@page import="controllers.MainApp"%>
<%
	if (!MainApp.LoggedIn(session))
	{
		response.sendRedirect("index");
	}

	String username = MainApp.currentUser(session).getUsername();
	if (request.getParameter("username")!=null)
	{
		try
		{
			username = request.getParameter("username");
		}
		catch (NumberFormatException e)
		{
		}
	}
	
	User user = null;
	try {
		HashMap<String, String> parameter = new HashMap<String,String>();
		parameter.put("token", MainApp.token(request.getSession()));
		parameter.put("app_id", MainApp.appId);
		parameter.put("username", username);
		String responseString = MainApp.callRestfulWebService(MainApp.serviceURL+"user/get_user_data_from_username", parameter, "", 0);
		JSONObject ret = (JSONObject)JSONValue.parse(responseString);

		user = new User();
		user.setUsername((String)ret.get("username"));
		user.setAvatar((String)ret.get("avatar"));
		user.setBirthdate(new Date(DBSimpleRecord.sdf.parse((String)ret.get("birthdate")).getTime()));
		user.setEmail((String)ret.get("email"));
		user.setFullname((String)ret.get("fullname"));
		request.getSession().setAttribute("current_user", user);
		
	} catch(Exception exc) {
		exc.printStackTrace();
		user = null;
	}
	
	if (user==null)
	{
		response.sendRedirect("404");
	}
	
	Task[] tasks = user.getAssignedTasks(MainApp.token(session));

	request.setAttribute("title", "MOA - Profile");
	request.setAttribute("currentPage", (request.getParameter("id")!=null) ? "" : "profile");
	
	SimpleDateFormat date_format = new SimpleDateFormat("dd MMMM yyyy");
%>	
<%@ include file="../template/header.jsp" %>
	<div class="content">
		<div class="profile">
			<header>
				<h1><%= user.getFullname() %></h1>
				<%
					if (username.equals(MainApp.currentUser(session).getUsername()))
					{
				%>
						<ul>
							<li class="edit-profile-link"><a href="edit_profile" id="editProfileButton">Edit Profile</a></li>
							<li class="edit-profile-link"><a href="change_password">Change Password</a></li>
						</ul>
				<%
					}
				%>
			</header>

			<section class="profile-details">
				<figure class="profile-image">
					<img src="<%= user.getAvatar() %>" alt="Profile Photo">
				</figure>
				<p class="username">
					<span class="detail-label">Username:</span>
					<span class="detail-value"><%= user.getUsername() %></span>
				</p>
				<p class="email">
					<span class="detail-label">Email:</span>
					<span class="detail-value"><%= user.getEmail() %></span>
				</p>
				<p class="date-of-birth">
					<span class="detail-label">Date of Birth:</span>
					<span class="detail-value"><%= date_format.format(user.getBirthdate()) %></span>
				</p>
			</section>

			<section class="tasks current">
				<header>
					<h3>Current Tasks</h3>
				</header>

				<%
					boolean check = true;
					for (Task task : tasks)
					{
						if (!task.isStatus())
						{
							check = false;
				%>
						<article class="task" data-task-id="<%= task.getId_task() %>" data-category="<%= task.getCategory(MainApp.token(session), task.getId_task()).getNama_kategori() %>">
							<header>
								<h1>
									<label>
										<a href="tugas?id=<%= task.getId_task() %>"><%= task.getNama_task() %></a>
									</label>
								</h1>
							</header>
							<div class="details">
								<p class="deadline">
									<span class="detail-label">Deadline:</span>
									<span class="detail-content">
										<%= date_format.format(task.getDeadline()) %>
									</span>
								</p>
								<p class="tags">
									<span class="detail-label">Tag:</span>
									<%
										Tag[] tags = task.getTags(MainApp.token(session), task.getId_task());
										for (Tag tag : tags)
										{
											out.println("<span class=\"tag\">" + tag.getTag_name() + "</span>");
										}
									%>
								</p>
							</div>
						</article>
				<%
						}
					}
					
					if (check)
					{
						out.println("No task currently.");
					}
				%>
			</section>

			<section class="tasks completed">
				<header>
					<h3>Completed Tasks</h3>
				</header>

				<%
					check = true;
					for (Task task : tasks)
					{
						if (task.isStatus())
						{
							check = false;
				%>
						<article class="task" data-task-id="<%= task.getId_task() %>" data-category="<%= task.getCategory(MainApp.token(session), task.getId_task()).getNama_kategori() %>">
							<header>
								<h1>
									<label>
										<a href="tugas?id=<%= task.getId_task() %>"><%= task.getNama_task() %></a>
									</label>
								</h1>
							</header>
							<div class="details">
								<p class="deadline">
									<span class="detail-label">Deadline:</span>
									<span class="detail-content">
										<%= date_format.format(task.getDeadline()) %>
									</span>
								</p>
								<p class="tags">
									<span class="detail-label">Tag:</span>
									<%
										Tag[] tags = task.getTags(MainApp.token(session), task.getId_task());
										for (Tag tag : tags)
										{
											out.println("<span class=\"tag\">" + tag.getTag_name() + "</span>");
										}
									%>
								</p>
							</div>
						</article>
				<%
						}
					}
					
					if (check)
					{
						out.println("No task completed.");
					}
				%>
			</section>
		</div>
	</div>
<%
	ArrayList<String> javascripts = new ArrayList<String>();
	request.setAttribute("javascripts", javascripts);
%>
<%@ include file="../template/footer.jsp" %>

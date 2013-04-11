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

	int id = session.getAttribute("user_id");
	if (request.getParameter("id")!=null)
	{
		id = request.getParameter("id");
	}
	User user;
	//$user = User::model()->find("id_user = ".$id, array("id_user","username","email","fullname","avatar","birthdate"));
	//$tasks = $user->getAssignedTasks();
	Task[] tasks;
	
	//$birth_date = new Datetime($user->birthdate);

	request.setAttribute("title", "MOA - Profile");
	request.setAttribute("currentPage", (request.getParameter("id")!=null) ? "" : "profile");
	
	SimpleDateFormat date_format = new SimpleDateFormat("j F Y");
%>	
<%@ include file="../template/header.jsp" %>
	<div class="content">
		<div class="profile">
			<header>
				<h1><?php echo $user->fullname; ?></h1>
				<%
					if (id == (int)session.getAttribute("user_id"))
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
					<img src="upload/user_profile_pict/<%= user.getAvatar() %>" alt="Profile Photo">
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
						<article class="task" data-task-id="<%= task.getId_task() %>" data-category="<%= task.getCategory().getNama_kategori() %>">
							<header>
								<h1>
									<label>
										<a href="tugas?id<%= task.getId_task() %>"><%= task.getNama_task() %></a>
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
										//Tag[] tags = task.getTags();
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
						<article class="task" data-task-id="<%= task.getId_task() %>" data-category="<%= task.getCategory().getNama_kategori() %>">
							<header>
								<h1>
									<label>
										<a href="tugas?id<%= task.getId_task() %>"><%= task.getNama_task() %></a>
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
										Tag[] tags = task.getTags();
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
<%@ include file="../template/footer.jsp" %>
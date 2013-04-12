<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="models.Comment"%>
<%@page import="models.Attachment"%>
<%@page import="models.Tag"%>
<%@page import="models.User"%>
<%@page import="models.Task"%>
<%@page import="controllers.MainApp"%>
<%
	if (!MainApp.LoggedIn(session))
	{
		response.sendRedirect("index");
	}

	Task task;
	User[] users;
	Tag[] tags;
	Attachment[] attachments;
	Comment[] comments;
	if (request.getParameter("id")!=null)
	{
		task = Task.getModel().find("id_task = " + (int)request.getParameter("id"));
		if (task==null)
		{
			// redirect to error page
		}
		users = task.getAssignee();
		tags = task.getTags();
		attachments = task.getAttachment();
		comments = task.getComment();
	}
	else
	{
		// redirect to error page
	}
	request.setAttribute("title", "MOA - Dashboard");
	request.setAttribute("currentPage", "dashboard");
	
	SimpleDateFormat date_format = new SimpleDateFormat("j F Y");
	SimpleDateFormat date_format2 = new SimpleDateFormat("Y-m-j");
	SimpleDateFormat date_format3 = new SimpleDateFormat("Y-m-d G:i:s");
	SimpleDateFormat date_format4 = new SimpleDateFormat("H:i – D/M");
%>
<%@ include file="../template/header.jsp" %>
		<div class="content">
			<div class="task-details not-editing">
				<header>
					<form method="POST">
						<h1>
							<label><span class="task-checkbox"><input type="checkbox" class="task-checkbox" data-task-id="<%= task.getId_task() %>"></span>
								<span id="task-title" class="task-title"><%= task.getNama_task() %></span>
							</label>
						</h1>
					</form>

					<ul>
						<%
							if (task.getDeletable(MainApp.currentUserId(session)))
							{
								out.println("<li><a href=\"#\" id=\"removeTaskLink\">Remove Task</a></li>");
							}
						%>
						<%
							if (task.getEditable(MainApp.currentUserId(session)))
							{
								out.println("<li><a href=\"#\" id=\"editTaskLink\">Edit Task</a></li>");
							}
						%>
					</ul>
				</header>
				<div id="current-task">
					<section class="details">
						<header>
							<h3>Details</h3>
						</header>
						<p class="deadline">
							<span class="detail-label">Deadline:</span>
							<span id="detail-deadline" class="detail-content"><%= date_format.format(task.getDeadline()) %></span>
						</p>
						<p class="assignee">
							<span class="detail-label">Assignee:</span>
							<span id="detail-asignee" class="detail-content">
								<%
									StringBuilder string = new StringBuilder();
									for (User user : users)
									{
										string.append("<a href='profile?id="+user.getId_user()+"'>"+user.getUsername()+"</a>,");
									}
									string.substring(0, string.length()-1);
									out.println(string.toString());
								%>
							</span>
						</p>
						<p class="category">
							<span class="detail-label">Kategori:</span>
							<span class="detail-content"><%= task.getCategory().getNama_kategori() %></span>
						</p>
						<p id="detail-tag" class="tags">
							<span class="detail-label">Tag:</span>
							<%
								for (Tag tag : tags)
								{
									out.println("<span class=\"tag\">");
									out.println(tag.getTag_name());
									out.println("</span>");
								}
							%>
						</p>
					</section>
					<section class="attachment">
						<header>
							<h3>Attachment</h3>
						</header>
						<%
							int i = 1;
							for (Attachment attachment : attachments)
							{
								String[] atch = attachment.getAttachment().split(".");
								if ("MP4".equals(atch[atch.length-1].toUpperCase()))
								{
						%>
									<video class="atchmt" controls>
									<source src="upload/attachments/<%= attachment.getAttachment() %>" type="video/mp4">
						<%		
								}
								else if ((atch[atch.length-1].toUpperCase().matches("JPG")) || ("PNG".equals(atch[atch.length-1].toUpperCase())) || ("GIF".equals(atch[atch.length-1].toUpperCase())))
								{
						%>
									<img class="atchmt" src="upload/attachments/<%= attachment.getAttachment() %>" alt="Task Attachment">
						<%
								}
								else
								{
									String file = "<a href=upload/attachments/"+attachment.getAttachment()+">Download attachment-"+i+" here</a>,";
								}
								i++;
							}
						%>
					</section>
				</div>
				<div id="edit-task">
					<form id="new_tugas" action="edit_tugas" method="post" enctype="multipart/form-data">
						<input type="hidden" name="id_task" value="<%= task.getId_task() %>" >
						<div class="field">
							<label>Task Name</label>
							<input size="25" maxlength="25" name="nama_task" id="nama" pattern="^[a-zA-Z0-9 ]{1,25}$" type="text" value="<%= task.getNama_task() %>">
						</div>
						<div class="field">
							<label>Attachment</label>
							<input size="25" name="attachment[]" id="attachment" type="file" multiple="">
						</div>
						<%
							int j = 1;
							for (Attachment attachment : attachments)
							{
								out.println("<div class=\"field\">");
								out.println("<span class=\"detail-label\">Attachment "+j+":</span>");
								out.println("<span class=\"detail-content\">");
								out.println(attachment.getAttachment());
								out.println("</span>");
								out.println("</div>");
								j++;
							}
						%>
						<div class="field">
							<label>Deadline</label>
							<input size="25" name="deadline" id="deadline" type="text" value="<%= date_format2.format(task.getDeadline()) %>">
						</div>
						<div class="field">
							<label>Assignee</label>
							<input size="25" name="assignee" id="assignee" type="text"  autocomplete="off" pattern="^[^;]{5,}(;[^;]{5,})*$" 
							value="<%
									string.delete(0, string.length());
									for (User user : users)
									{
										string.append(user.getUsername()+",");
									}
									string.substring(0, string.length()-1);
									out.println(string.toString());
								%>">
							<div id="auto_comp_assignee">
								<ul id="auto_comp_inflate_assignee"></ul>
							</div>
						</div>
						<div class="field">
							<label>Tag</label>
							<input size="25" name="tag" id="tag" type="text" autocomplete="off" value="<%
									string.delete(0, string.length());
									for (Tag tag : tags)
									{
										string.append(tag.getTag_name()+",");
									}
									string.substring(0, string.length()-1);
									out.println(string.toString());
								%>">
							<div id="auto_comp_tag">
								<ul id="auto_comp_inflate_tag"></ul>
							</div>
						</div>
						<div class="buttons">
							<button type="submit">Save Changes</button>
						</div>
					</form>
				</div>
				<section class="comments">
					<header>
						<h3 id="total_comment"><% int total_comment = task.getTotalComment(); out.print(total_comment); %> Comment<%= (total_comment > 1) ? "s" : "" %></h3>
						<%
							if (total_comment > 10)
							{
						%>
							<a id="more_link" href="javascript:more_comment()">Previous Comment<%= (total_comment-10 > 1) ? "s" : "" %></a>
						<%
							}
						%>
						<span id="show_status" class="right">Showing <%= Math.min(10, total_comment) %> of <%= total_comment %></span>
						<div class="clear"></div>
					</header>
	
					<div id="commentsList">
						<%
							String firsttimestamp = "";
							if (total_comment > 0)
							{
								firsttimestamp = comments[0].getTimestamp();
							}
							else
							{
								firsttimestamp = date_format3.format(new Date());
							}
							String lasttimestamp = date_format3.format(new Date());
							
							for (Comment comment : comments)
							{
								User user = comment.getUser();
								out.println("<article id=\"comment_"+comment.getId_komentar()+"\" class=\"comment\">");
									out.println("<a href=\"profile?id="+user.getId_user()+"\"");
										out.println("<img src=\"upload/user_profile_pict/"+user.getAvatar()+"\" alt=\""+user.getFullname()+"\" class=\"icon_pict\" >");
									out.println("</a>");
									out.println("<div class=\"right\"");
										out.println(date_format4.format(comment.getTimestamp()));
										if (user.getId_user()==MainApp.currentUserId(session))
										{
											out.println("<a href=\"javascript:delete_comment("+comment.getId_komentar()+")>DELETE</a>");
										}
									out.println("</div>");
									out.println("<header>");
										out.println("<a href=\"profile?id="+user.getId_user()+"\">");
											out.println("<h4>"+user.getUsername()+"</h4>");
										out.println("</a>");
									out.println("</header>");
									out.println("<p>");
										out.println(comment.getKomentar());
									out.println("</p>");
								out.println("</article>");
								lasttimestamp = comment.getTimestamp();
							}
						%>
					</div>

					<div class="comment-form">
						<h3>Add Comment</h3>
						<%
							out.println("<a href=\"profile\"");
								out.println("<img src=\"upload/user_profile_pict/"+MainApp.currentUser(session).getAvatar()+"\" alt=\""+MainApp.currentUser(session).getFullname()+"\" class=\"icon_pict\" >");
							out.println("</a>");
						%>
						<form id="commentForm" action="#" method="post">
							<input type="hidden" name="id_task" value="<?php echo $task->id_task; ?>">
							<textarea name="komentar" id="commentBody"></textarea>
							<button type="submit">Send</button>
						</form>
					</div>
				</section>
			</div>
		</div>
		<%@ include file="../template/calendar.jsp" %>	
		<script type="text/javascript">
			var id_task = <%= task.getId_task() %>;
			var first_timestamp = "<%= firsttimestamp %>";
			var timestamp = "<%= lasttimestamp %>";
			var total_comment = <%= total_comment %>;
			var id_user = <%= MainApp.currentUserId(session) %>;
			var current_total_comment = <%= Math.min(10, total_comment) %>;
		</script>
<% 
	ArrayList<String> javascripts = new ArrayList<String>();
	javascripts.add("datepicker");
	javascripts.add("tugas");
	javascripts.add("comment");
	request.setAttribute("javascripts", javascripts);
%>
<%@ include file="../template/footer.jsp" %>
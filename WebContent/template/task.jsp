<%@page import="controllers.MainApp"%>
<%@page import="models.Tag"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="models.Task"%>
<%
	Task temptask = (Task)request.getAttribute("task");
	SimpleDateFormat temp_task_deadline = new SimpleDateFormat("dd MMMM yyyy");
%>

						<article class="task" data-task-id="<%= temptask.getId_task() %>" id="task<%= temptask.getId_task() %>">
							<header>
								<h1>
									<label>
										<span class="task-checkbox"><input type="checkbox" class="task-checkbox" data-task-id="<%= temptask.getId_task() %>"
											<%= (temptask.isStatus()) ? "checked" : "" %>></span>
										<a href="tugas.php?id=<%= temptask.getId_task() %>"><%= temptask.getNama_task() %></a>
									</label>
								</h1>
							</header>
							<div class="details">
								<p class="deadline">
									<span class="detail-label">Deadline:</span>
									<span class="detail-content">
										<%= temp_task_deadline.format(temptask.getDeadline()) %>
									</span>
								</p>
								<p class="tags">
									<span class="detail-label">Tag:</span>
									<%
										for (Tag tag : temptask.getTags())
										{
											out.println("<span class=\"tag\">"+tag.getTag_name()+"</span>");
										}
									%>
								</p>
								<%
									if (temptask.getDeletable(MainApp.currentUserId(session)))
									{
								%>
										<p class="delete">
											<a href="delete_task.php?task_id=<%= temptask.getId_task() %>" data-task-id="<%= temptask.getId_task() %>">delete</a>
										</p>
								<%
									}
								%>
							</div>
						</article>
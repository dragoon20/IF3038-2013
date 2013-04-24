<%@page import="models.Category"%>
<%@page import="controllers.MainApp"%>
<%
	if (!MainApp.LoggedIn(session))
	{
		response.sendRedirect("index");
	}

	int id = 0;
	if (request.getParameter("cat")!=null)
	{
		try
		{
			id = Integer.parseInt(request.getParameter("cat"));
		}
		catch (NumberFormatException e)
		{
		}
	}
	
	Category cat = (Category)Category.getModel().find("id_kategori=?", new Object[]{id}, new String[]{"integer"}, null);
	if ((cat==null) || (cat.getEditable(MainApp.currentUserId(session))))
	{
		// redirect to error page
	}
	
	request.setAttribute("title", "MOA - New Task");
	request.setAttribute("currentPage", (request.getParameter("id")!=null) ? "" : "");
%>
<%@ include file="../template/header.jsp" %>
		<div class="content">
			<div class="add-task">
				<header>
					<h1>Add Task</h1>
				</header>
				<form id="new_tugas" action="new_task" method="post" enctype="multipart/form-data">
					<input name="id_kategori" type="hidden" value="<%= id %>">
					<div class="field">
						<label>Task Name</label>
						<input size="25" maxlength="25" name="nama_task" pattern="^[a-zA-Z0-9 ]{1,25}$" id="nama" type="text">
					</div>
					<div class="field">
						<label>Attachment</label>
						<input size="25" name="attachment[]" id="attachment" type="file"  multiple="">
					</div>
					<div class="field">
						<label>Deadline</label>
						<input size="25" name="deadline" id="deadline"  type="text" pattern="^[1-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$" onclick="datePicker.showCalendar(event);" title="Tahun harus minimal dari tahun 1955." required/>
					</div>
					<div class="field">
						<label>Assignee</label>
						<input size="25" name="assignee" id="assignee" type="text" pattern="^[^;]{5,}(;[^;]{5,})*$">
						<div id="auto_comp_assignee">
							<ul id="auto_comp_inflate_assignee"></ul>
						</div>
					</div>
					<div class="field">
						<label>Tag</label>
						<input size="25" name="tag" id="tag" type="text">
						<div id="auto_comp_tag">
							<ul id="auto_comp_inflate_tag"></ul>
						</div>
					</div>
					<div class="buttons">
						<button type="submit">Save</button>
					</div>
				</form>
			</div>
		</div>
		<%@ include file="../template/calendar.jsp" %>	
		<script type="text/javascript">
			var id_user = <%= MainApp.currentUserId(session) %>;
		</script>
<% 
	ArrayList<String> javascripts = new ArrayList<String>();
	javascripts.add("datepicker");
	javascripts.add("tugas");
	request.setAttribute("javascripts", javascripts);
%>
<%@ include file="../template/footer.jsp" %>
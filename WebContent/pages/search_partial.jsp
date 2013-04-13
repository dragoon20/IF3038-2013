<%@page import="models.DBSimpleRecord"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="models.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="models.Task"%>
<%@page import="models.Category"%>
<%@page import="controllers.MainApp"%>
<%
	if (!MainApp.LoggedIn(session))
	{
		response.sendRedirect("index");
	}

	String query = (request.getParameter("q")!=null) ? ((String)request.getParameter("q")) : "";
	
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
	String baseTaskQ = "id_kategori IN ( SELECT id_kategori FROM "+Category.getTableName()+" WHERE id_user=? "+
			 "OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user=?) "+
			 "OR id_kategori IN (SELECT id_kategori FROM "+ Task.getTableName() +" AS t LEFT OUTER JOIN assign AS a "+
			 "ON t.id_task=a.id_task WHERE t.id_user = ? OR a.id_user = ? ))";
	
	int start = (Integer.parseInt(request.getParameter("page"))-1) * 10;
		
	Task[] tasks = null;
	User[] users = null;
	Category[] categories = null;
	
	if (("task".equals(query_type.toLowerCase())) || (all))
	{
		List<DBSimpleRecord> list = Arrays.asList(Task.getModel().findAllLimit(baseTaskQ+" AND nama_task LIKE ?", new Object[]{id, id, id, id, terms}, new String[]{"integer", "integer", "integer", "integer", "string"}, null, start, 10));
		tasks = list.toArray(new Task[list.size()]);
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
	}
	if (("user".equals(query_type.toLowerCase())) || (all))
	{
		List<DBSimpleRecord> list = Arrays.asList(User.getModel().findAllLimit("username LIKE ? OR fullname LIKE ? OR email LIKE ? OR birthdate LIKE ?", new Object[]{terms, terms, terms, terms}, new String[]{"string", "string", "string", "string"}, new String[]{"id_user", "username", "avatar", "fullname"}, start, 10));
		users = list.toArray(new User[list.size()]);
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
	%>
<%
	}
	if (("category".equals(query_type.toLowerCase())) || (all))
	{
		List<DBSimpleRecord> list = Arrays.asList(Category.getModel().findAllLimit("nama_kategori LIKE ?", new Object[]{terms}, new String[]{"string"}, null, start, 10));
		categories = list.toArray(new Category[list.size()]);
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
		}
	}
%>
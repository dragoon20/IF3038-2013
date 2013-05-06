<!DOCTYPE html>

<%@page import="java.util.ArrayList"%>
<%@page import="models.User"%>
<%@page import="controllers.MainApp"%>
<html>
	<head>
		<meta charset="utf-8">
		<title><%= request.getAttribute("title") %></title>
		<base href="<%= session.getAttribute("base_url") %>">
		<link rel="stylesheet" href="css/style.css">
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
	</head>

	<body>
		<div class="site-container">
			<header class="site-header">
				<h1><a href="dashboard"><%= MainApp.appName %></a></h1>
				<p><%= MainApp.appTagline %></p>

				<% 
					if (MainApp.LoggedIn(session))
					{
				%>
					<nav>
						<ul class="main-links">
							<% 
								ArrayList<String> keys = new ArrayList<String>();
								ArrayList<String> values = new ArrayList<String>();
								keys.add("dashboard");
								keys.add("profile");
								keys.add("logout");
								values.add("Dashboard");
								values.add(MainApp.currentUser(session).getFullname());
								values.add("Logout");

								int x = 0;
								for (String key: keys)
								{
									String label = values.get(x);
									x++;
									boolean active = (key.equals(request.getAttribute("currentPage")));
							%>	
									<li class="<%= key %> -link<% if (active) out.print("active"); %>" id="<%= key %> Li">
									<%
										if ("profile".equals(key)) 
										{
									%>
											<img src="<%= MainApp.currentUser(session).getAvatar() %>" alt="">
									<%
										}
									%>
									<a href="<%= key %>" id="<%= key %>>Link"><%= label %></a></li>
							<%
								}
							%>
						</ul>
	
						<%
							String q = "";
							String type = "";
							if ("search".equals(session.getAttribute("currentPage")))
							{
								q = request.getParameter("q");
								type = request.getParameter("type");
							}
						%>
						<div class="search-box">
							<form action="search" method="get" id="searchForm">
								<select name="type" id="searchType">
								<%
									ArrayList<String> types = new ArrayList<String>();
									ArrayList<String> type_values = new ArrayList<String>();
									types.add("all");
									types.add("task");
									types.add("user");
									types.add("category");
									type_values.add("All");
									type_values.add("Tasks");
									type_values.add("Users");
									type_values.add("Categories");
									
									int y=0;
									for (String k: types)
									{
										String v = type_values.get(y);
										y++;
										String selected = (k.equals(type)) ? " selected" : "";
										out.println("<option value=\""+k+selected+"\">"+v+"</option>");
									}
								%>
								</select>
								<input type="search" name="q" placeholder="Search" value="<%= q %>" id="searchQuery">
								<button type="submit">Search</button>
							</form>
						</div>
					</nav>
				<% 
					}
				%>
			</header>
			<div id="content">

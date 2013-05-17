<%@page import="controllers.MainApp"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<header>
	<div id="header_wrap" class="wrap">
		<a href="index"> <img id="header_logo" src="images/logo.png" alt="Logo Produk" /> </a>
		<div id="header_title">
			<h1> <a href="index">MOA</a> </h1>
			<h4>Multiuser Online Agenda</h4>
		</div>
		<div id="border_header"></div>
		<div id="header_menu">
			<nav>
				<ul>
					<%
						Map<String, Map<String, String>> menu = (HashMap<String, Map<String, String>>) request.getAttribute("menu");
						for (Entry<String, Map<String, String>> entry : menu.entrySet())
						{
							out.println("<li>");
							out.println("<a");
							for (Entry<String, String> entry2 : entry.getValue().entrySet())
							{
								out.println(entry2.getKey()+"=\""+entry2.getValue()+"\" ");
							}
							out.println(">"+entry.getKey()+"</a>");
							out.println("</li>");
						}
					%>
				</ul>
			</nav>
			<div id="login_area">
				<%
					if (!MainApp.LoggedIn(session))
					{
				%>
					<nav id="login_button">
						<ul>
							<li> <a id="login_link" href="<%= MainApp.serviceURL %>login?app_id=<%= MainApp.appId %>">Masuk</a> </li>
						</ul>
					</nav>
					<%
						/*
						<div class="clear"></div>
						<div id="border_login"><div id="border_login_inner"></div></div>
						<div id="login_form_wrap">
							<form id="login_form" action="dashboard.html" method="post">
								<div class="row">
									<label for="login_username">Username</label> <br />
									<input id="login_username" name="username" type="text" required /> <br />
								</div>
								<div class="row">
									<label for="login_password">Sandi</label> <br />
									<input id="login_password" name="password" type="password" required /> <br />
								</div>
								<input type="submit" value="Masuk" />
							</form>
						</div>
						*/
					%>
				<%
					}
					else
					{
				%>
					<nav id="login_button">
						<ul>
							<!-- <li> <a id="login_link" href="logout">Keluar</a> </li> -->
							<li> 
								<a id="login_link" href="logout">
									<img id="profile_head_picture" src="<%= MainApp.currentUser(session).getAvatar() %>" />
									<span id="profile_head_username"><%= MainApp.currentUser(session).getUsername() %></span>
								</a>
							</li>
						</ul>
					</nav>
				<%
					}
				%>
				<div class="clear"></div>
			</div>
			<div id="border_search"></div>

			<div id="search">
				<div id="search_inner">
					<div id="search_button"></div>
					<div id="search_left"></div>
					<form id="search_form">
						<input id="search_text" name="search_query" type="text" autocomplete="off"/>
					</form>
					<div id="search_right"></div>
				</div>
			</div>
		</div>
		<div class="clear"></div>
	</div>
</header>
<script type="text/javascript" src="js/search.js"></script>
<script type="text/javascript" src="js/functions.js"></script>
<% //<script type="text/javascript" src="js/login.js"></script> %>
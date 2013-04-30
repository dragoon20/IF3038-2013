<%@page import="models.User"%>
<%
	User tempuser = (User)request.getAttribute("user");
%>
<article class="user-listing" data-user-id="<%= tempuser.getUsername() %>">
	<div class="photo"><img src="<%= tempuser.getAvatar() %>" alt="Profile Photo"></div>
	<div class="name">
		<span class="fullname"><%= tempuser.getFullname() %></span><br>
		<a class="username" href="profile?username=<%= tempuser.getUsername() %>"><%= tempuser.getUsername() %></a>
	</div>
</article>
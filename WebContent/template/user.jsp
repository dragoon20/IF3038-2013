<%@page import="models.User"%>
<%
	User tempuser = (User)request.getAttribute("user");
%>
<article class="user-listing" data-user-id="<%= tempuser.getId_user() %>">
	<div class="photo"><img src="upload/user_profile_pict/<%= tempuser.getAvatar() %>" alt="Profile Photo"></div>
	<div class="name">
		<span class="fullname"><%= tempuser.getFullname() %></span><br>
		<a class="username" href="profile.php?id=<%= tempuser.getId_user() %>"><%= tempuser.getUsername() %></a>
	</div>
</article>
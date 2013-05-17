<%@page import="models.User"%>
<%@page import="controllers.MainApp"%>
<%
	if (!MainApp.LoggedIn(session))
	{
		response.sendRedirect("index");
	}

	User user = MainApp.currentUser(session);
	
	request.setAttribute("title", "MOA - Profile");
	request.setAttribute("currentPage", "profile");
%>
<%@ include file="../template/header.jsp" %>
	<div class="content">
		<div class="profile">
			<header>
				<h1><%= user.getFullname() %></h1>
			</header>

			<section class="profile-details">
				<figure class="profile-image">
					<img src="upload/user_profile_pict/<%= user.getAvatar() %>" alt="Profile Photo">
				</figure>
				
				<form id="edit_form" action="change_user_password" method="post">
					<div class="field">
						<span class="label">Username</span>
						<span id="edit_username"><%= user.getUsername() %></span>
					</div>
					<div class="field">
						<label for="edit_current_password">Current Password</label>
						<input id="edit_current_password" name="current_password" pattern="^.{8,}$" type="password" title="Sandi minimal harus terdiri dari 8 karakter dan tidak sama dengan username dan email.">							
					</div>
					<div class="field">
						<label for="edit_new_password">New Password</label>
						<input id="edit_new_password" name="new_password" pattern="^.{8,}$" type="password" title="Sandi minimal harus terdiri dari 8 karakter dan tidak sama dengan username dan email.">	
					</div>
					<div class="field">
						<label for="edit_confirm_password">Confirm Password</label>
						<input id="edit_confirm_password" name="confirm_password" pattern="^.{8,}$" type="password" title="Konfirmasi sandi harus sama dengan sandi." required>
					</div>
					<br />
					<div class="field buttons">
						<button type="submit" id="edit_pass_submit">Save Changes</button>
					</div>
				</form>
			</section>
		</div>
	</div>
<% 
	ArrayList<String> javascripts = new ArrayList<String>();
	javascripts.add("checker_edit_pass");
	request.setAttribute("javascripts", javascripts);
%>
<%@ include file="../template/footer.jsp" %>

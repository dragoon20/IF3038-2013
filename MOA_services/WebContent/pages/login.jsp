<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>MOA - Login</title>
		<link rel="stylesheet" href="css/style.css">
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
	</head>
	<body>
		<div id="content_box">
			<h2>User Login</h2>
			<hr />
			<form action="login_check" method="POST">
				<input type="hidden" name="app_id" value="<%= request.getParameter("app_id") %>">
				<div class="row">
					<label for="app_name">Username</label>
					<input type="text" name="username">
				</div>
				<div class="row">
					<label for="redirect_url">Password</label>
					<input type="password" name="password">
				</div>
				<div class="row">
					<input type="submit" value="Login">
				</div>
			</form>
		</div>
	</body>
</html>
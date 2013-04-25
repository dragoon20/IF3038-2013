<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>MOA - Register App</title>
		<link rel="stylesheet" href="css/style.css">
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
	</head>
	<body>
		<div id="content_box">
			<h2>Register App</h2>
			<hr />
			<form action="register_app_check" method="POST">
				<div class="row">
					<label for="app_name">Application Name</label>
					<input type="text" name="app_name">
				</div>
				<div class="row">
					<label for="redirect_url">Redirect Url</label>
					<input type="text" name="redirect_url">
				</div>
				<div class="row">
					<input type="submit" value="Register">
				</div>
			</form>
		</div>
	</body>
</html>
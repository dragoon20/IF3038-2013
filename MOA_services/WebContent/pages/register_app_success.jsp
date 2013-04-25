<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String app_id = (String)request.getAttribute("app_id");
	if (app_id==null)
		response.sendRedirect("error.jsp");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>MOA - Register App Success</title>
		<link rel="stylesheet" href="css/style.css">
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
	</head>
	<body>
		<div id="content_box">
			<h2>Register App Success</h2>
			<hr />
			<p>
				Your Application ID is <b><%= app_id %></b>
			</p>
		</div>
	</body>
</html>
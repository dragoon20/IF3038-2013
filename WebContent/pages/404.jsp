<%@ page isErrorPage="true" import="java.io.*" contentType="text/plain"%>
<%
	ArrayList<String> javascripts = new ArrayList<String>();
	request.setAttribute("javascripts", javascripts);
	request.setAttribute("title", "MOA - Error");
	request.setAttribute("currentPage", "error");
%>
<%@ include file="../template/header.jsp" %>
	<div class="content">
		<div class="error">
			<header>
				<h1>Error</h1>
			</header>
			<p>Something awkward happened.</p>
		</div>
	</div>
<%@ include file="../template/footer.jsp" %>
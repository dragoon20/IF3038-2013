<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<footer>
	<div id="footer_wrap" class="wrap">
		<div id="footer_left">
			<%
				Map<String, Map<String, String>> breadcrumbs = (Map<String, Map<String, String>>) request.getAttribute("breadcrumbs");
				int total = breadcrumbs.size();
				for (Entry<String, Map<String, String>> entry : breadcrumbs.entrySet())
				{
					out.println("<a ");
					for (Entry<String, String> entry2 : entry.getValue().entrySet())
					{
						out.println(entry2.getKey()+"=\""+entry2.getValue()+"\" ");
					}
					out.println(">"+entry.getKey()+"</a>");
					total--;
					if (total > 0)
					{
						out.println(" >> ");
					}
				}
			%>
		</div>
		<div id="footer_right">
			Copyright by ImbAlAncE.
		</div>
	</div>
</footer>
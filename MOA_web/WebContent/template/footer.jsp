			<%@page import="java.util.ArrayList"%>
			</div>
			<footer class="site-footer">
			<%
				ArrayList<String> scripts = (ArrayList<String>)request.getAttribute("javascripts");
			%>
			</footer>
		</div>
		<script src="js/bajuri.js" type="text/javascript"></script>
		<script src="js/do.js" type="text/javascript"></script>
		<% 
			for (String js : scripts)
			{
		%>
		<script src="js/<%= js %>.js" type="text/javascript"></script>
		<%
			}
		%>
		<!--
		<script src="js/checker.js"></script>
		-->
	</body>
</html>

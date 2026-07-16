<%@ page contentType="text/html; charset=UTF-8" %>
<%
	String activePage = (String) request.getAttribute("activePage");
	if (activePage == null) activePage = "";
%>
<aside class="sidebar">
	<div class="sidebar-brand">
		<span class="dot"></span> SmartRoad Admin
	</div>
	<ul class="sidebar-nav">
		<li><a href="${pageContext.request.contextPath}/dashboard"
			class="<%= activePage.equals("dashboard") ? "active" : "" %>">&#128202; Dashboard</a></li>
		<li><a href="${pageContext.request.contextPath}/reports"
			class="<%= activePage.equals("reports") ? "active" : "" %>">&#9888; Hazard Reports</a></li>
	</ul>
	<div class="sidebar-foot">SmartRoad &copy; 2026</div>
</aside>

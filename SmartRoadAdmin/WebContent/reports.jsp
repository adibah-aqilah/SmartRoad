<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.smartroad.admin.model.HazardReport" %>
<%@ page import="java.util.List" %>
<% request.setAttribute("activePage", "reports"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Hazard Reports - SmartRoad Admin</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="app-shell">
		<jsp:include page="/common/sidebar.jsp" />

		<div class="main">
			<div class="topbar">
				<h1>Hazard Reports</h1>
				<div class="admin-chip">
					<span class="avatar">A</span>
					Admin
				</div>
			</div>

			<div class="content">
				<div class="panel">

					<form class="filter-bar" method="get" action="${pageContext.request.contextPath}/reports">
						<input type="text" name="keyword" placeholder="Search by user or description..."
							value="${keyword}">

						<select name="hazardType">
							<option value="">All Hazard Types</option>
							<%
								String[] types = { "Pothole", "Flooding", "Fallen Tree",
										"Traffic Accident", "Damaged Road Sign", "Broken Traffic Light" };
								String selectedType = (String) request.getAttribute("selectedHazardType");
								for (String t : types) {
									String sel = (selectedType != null && selectedType.equals(t)) ? "selected" : "";
							%>
							<option value="<%= t %>" <%= sel %>><%= t %></option>
							<% } %>
						</select>

						<select name="status">
							<option value="">All Statuses</option>
							<%
								String[] statuses = { "New", "Under Investigation", "Resolved" };
								String selectedStatus = (String) request.getAttribute("selectedStatus");
								for (String s : statuses) {
									String sel = (selectedStatus != null && selectedStatus.equals(s)) ? "selected" : "";
							%>
							<option value="<%= s %>" <%= sel %>><%= s %></option>
							<% } %>
						</select>

						<button type="submit" class="btn btn-primary">Filter</button>
						<a class="btn btn-outline" href="${pageContext.request.contextPath}/reports">Reset</a>
					</form>

					<table class="data-table">
						<thead>
							<tr>
								<th>ID</th>
								<th>User</th>
								<th>Hazard Type</th>
								<th>Date &amp; Time</th>
								<th>Status</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody>
							<%
								List<HazardReport> reports = (List<HazardReport>) request.getAttribute("reports");
								String ctx = request.getContextPath();
								if (reports != null) {
									for (HazardReport r : reports) {
										String badgeClass = "badge-new";
										if ("Under Investigation".equals(r.getStatus())) badgeClass = "badge-investigating";
										else if ("Resolved".equals(r.getStatus())) badgeClass = "badge-resolved";
							%>
							<tr>
								<td>#<%= r.getId() %></td>
								<td><%= r.getFullName() %></td>
								<td><span class="hazard-icon"><%= r.getHazardIcon() %></span><%= r.getHazardType() %></td>
								<td><%= r.getDateTime() %></td>
								<td><span class="badge <%= badgeClass %>"><%= r.getStatus() %></span></td>
								<td class="actions-cell">
									<a class="link-view" href="<%= ctx %>/report?id=<%= r.getId() %>">View</a>
									<form method="get" action="<%= ctx %>/reports"
										onsubmit="return confirm('Delete this report?');" style="display:inline;">
										<input type="hidden" name="action" value="delete">
										<input type="hidden" name="id" value="<%= r.getId() %>">
										<button type="submit" class="btn-danger-text">Delete</button>
									</form>
								</td>
							</tr>
							<%
									}
									if (reports.isEmpty()) {
							%>
							<tr><td colspan="6" class="empty-state">No reports match your search / filter.</td></tr>
							<%
									}
								}
							%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.smartroad.admin.model.HazardReport" %>
<%@ page import="java.util.List" %>
<% request.setAttribute("activePage", "dashboard"); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>SmartRoad Admin Panel</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="app-shell">
		<jsp:include page="/common/sidebar.jsp" />

		<div class="main">
			<div class="topbar">
				<h1>Dashboard</h1>
				<div class="admin-chip">
					<span class="avatar">A</span>
					Admin
				</div>
			</div>

			<div class="content">

				<div class="stat-grid">
					<div class="stat-card">
						<div class="label">Total Users</div>
						<div class="value">${totalUsers}</div>
					</div>
					<div class="stat-card">
						<div class="label">Total Reports</div>
						<div class="value">${totalReports}</div>
					</div>
					<div class="stat-card accent-new">
						<div class="label">Open Reports</div>
						<div class="value">${openReports}</div>
					</div>
					<div class="stat-card accent-resolved">
						<div class="label">Resolved Reports</div>
						<div class="value">${resolvedReports}</div>
					</div>
				</div>

				<hr class="road-divider">

				<div class="panel">
					<div class="panel-head">Recent Reports</div>
					<table class="data-table">
						<thead>
							<tr>
								<th>User</th>
								<th>Hazard</th>
								<th>Date</th>
								<th>Status</th>
							</tr>
						</thead>
						<tbody>
							<%
								List<HazardReport> recent = (List<HazardReport>) request.getAttribute("recentReports");
								if (recent != null) {
									for (HazardReport r : recent) {
										String badgeClass = "badge-new";
										if ("Under Investigation".equals(r.getStatus())) badgeClass = "badge-investigating";
										else if ("Resolved".equals(r.getStatus())) badgeClass = "badge-resolved";
							%>
							<tr>
								<td><%= r.getFullName() %></td>
								<td><span class="hazard-icon"><%= r.getHazardIcon() %></span><%= r.getHazardType() %></td>
								<td><%= r.getDateTime() %></td>
								<td><span class="badge <%= badgeClass %>"><%= r.getStatus() %></span></td>
							</tr>
							<%
									}
									if (recent.isEmpty()) {
							%>
							<tr><td colspan="4" class="empty-state">No reports yet.</td></tr>
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

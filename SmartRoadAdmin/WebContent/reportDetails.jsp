<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.smartroad.admin.model.HazardReport" %>
<% request.setAttribute("activePage", "reports"); %>
<%
	HazardReport r = (HazardReport) request.getAttribute("report");
	String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Report #<%= r.getId() %> - SmartRoad Admin</title>
	<link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
	<div class="app-shell">
		<jsp:include page="/common/sidebar.jsp" />

		<div class="main">
			<div class="topbar">
				<h1>Report #<%= r.getId() %></h1>
				<div class="admin-chip">
					<span class="avatar">A</span>
					Admin
				</div>
			</div>

			<div class="content">
				<a class="back-link" href="<%= ctx %>/reports">&#8592; Back to Hazard Reports</a>

				<div class="panel" style="padding: 24px;">
					<div class="detail-grid">

						<div>
							<div class="detail-field">
								<div class="f-label">Reported By</div>
								<div class="f-value"><%= r.getFullName() %> (@<%= r.getUsername() %>)</div>
							</div>

							<div class="detail-field">
								<div class="f-label">Hazard Type</div>
								<div class="f-value"><%= r.getHazardIcon() %> <%= r.getHazardType() %></div>
							</div>

							<div class="detail-field">
								<div class="f-label">Description</div>
								<div class="f-value"><%= r.getDescription() %></div>
							</div>

							<div class="detail-field">
								<div class="f-label">GPS Coordinates</div>
								<div class="f-value"><%= r.getLatitude() %>, <%= r.getLongitude() %></div>
							</div>

							<div class="detail-field">
								<div class="f-label">Date &amp; Time Submitted</div>
								<div class="f-value"><%= r.getDateTime() %></div>
							</div>

							<div class="detail-field">
								<div class="f-label">User Agent</div>
								<div class="f-value"><%= r.getUserAgent() %></div>
							</div>
						</div>

						<div>
							<div class="detail-field">
								<div class="f-label">Photo Evidence</div>
								<div class="photo-box">
									<%
										String photo = r.getPhotoFileName();
										if (photo != null && !photo.isEmpty()) {
									%>
										Photo file: <%= photo %><br>(hook this up to the real upload folder)
									<%
										} else {
									%>
										No photo was attached to this report.
									<%
										}
									%>
								</div>
							</div>

							<form method="post" action="<%= ctx %>/report?id=<%= r.getId() %>">
								<div class="detail-field">
									<div class="f-label">Update Status</div>
									<select name="status" class="status-select">
										<option value="New" <%= "New".equals(r.getStatus()) ? "selected" : "" %>>New</option>
										<option value="Under Investigation" <%= "Under Investigation".equals(r.getStatus()) ? "selected" : "" %>>Under Investigation</option>
										<option value="Resolved" <%= "Resolved".equals(r.getStatus()) ? "selected" : "" %>>Resolved</option>
									</select>
								</div>
								<button type="submit" class="btn btn-primary">Save Changes</button>
							</form>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>

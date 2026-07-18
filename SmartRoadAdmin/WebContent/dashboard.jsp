<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.smartroad.admin.model.HazardReport" %>
<%@ page import="com.smartroad.admin.util.HtmlUtil" %>
<%@ page import="java.util.List" %>

<%
    request.setAttribute("activePage", "dashboard");

    List<HazardReport> recentReports = (List<HazardReport>) request.getAttribute("recentReports");
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard - SmartRoad Admin</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
</head>
<body>

<div class="app-shell">

    <jsp:include page="/common/sidebar.jsp" />

    <main class="main">

        <header class="topbar">
            <h1>SmartRoad Admin Panel</h1>
            <div class="admin-chip">
                <span class="avatar">A</span>
                Admin
            </div>
        </header>

        <div class="content">

            <div class="page-actions">
                <div>
                    <h2>Dashboard</h2>
                    <p>Overview of road hazard reports.</p>
                </div>
            </div>

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

            <section class="panel">

                <div class="panel-head">Recent Reports</div>

                <div class="table-scroll">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>User</th>
                                <th>Hazard</th>
                                <th>Date &amp; Time</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            if (recentReports != null && !recentReports.isEmpty()) {
                                for (HazardReport report : recentReports) {
                        %>
                            <tr>
                                <td><%= HtmlUtil.escape(report.getUsername()) %></td>
                                <td>
                                    <span class="hazard-icon"><%= report.getHazardIcon() %></span>
                                    <%= HtmlUtil.escape(report.getHazardType()) %>
                                </td>
                                <td><%= HtmlUtil.escape(report.getDateTime()) %></td>
                                <td>
                                    <span>
                                        <%= HtmlUtil.escape(report.getStatus()) %>
                                    </span>
                                </td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="4" class="empty-state">No reports available.</td>
                            </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>

            </section>

        </div>
    </main>

</div>

</body>
</html>
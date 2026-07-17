<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.smartroad.admin.model.HazardReport" %>
<%@ page import="com.smartroad.admin.util.HtmlUtil" %>
<%@ page import="java.util.List" %>

<%
    request.setAttribute("activePage", "dashboard");

    List<HazardReport> recentReports =
            (List<HazardReport>) request.getAttribute(
                    "recentReports"
            );

    String contextPath =
            request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <title>Dashboard - SmartRoad Admin</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/css/style.css">
</head>

<body>

<div class="app-shell">

    <jsp:include page="/common/sidebar.jsp" />

    <main class="main">

        <!-- Top navigation bar -->
        <header class="topbar">

            <h1>Dashboard</h1>

            <div class="admin-chip">

                <span class="avatar">
                    A
                </span>

                Admin
            </div>

        </header>

        <div class="content">

            <!-- Page title and Add Report button -->
            <div class="page-actions">

                <div>

                    <h2>System Overview</h2>

                    <p>
                        Live hazard report data from
                        Cloud Firestore.
                    </p>

                </div>

                <a class="btn btn-primary"
                   href="<%= contextPath %>/add-report">

                    + Add Report
                </a>

            </div>

            <!-- Dashboard statistics -->
            <div class="stat-grid">

                <div class="stat-card">

                    <div class="label">
                        Total Users
                    </div>

                    <div class="value">
                        ${totalUsers}
                    </div>

                </div>

                <div class="stat-card">

                    <div class="label">
                        Total Reports
                    </div>

                    <div class="value">
                        ${totalReports}
                    </div>

                </div>

                <div class="stat-card accent-new">

                    <div class="label">
                        Open Reports
                    </div>

                    <div class="value">
                        ${openReports}
                    </div>

                </div>

                <div class="stat-card accent-resolved">

                    <div class="label">
                        Resolved Reports
                    </div>

                    <div class="value">
                        ${resolvedReports}
                    </div>

                </div>

            </div>

            <hr class="road-divider">

            <!-- Recent reports table -->
            <section class="panel">

                <div class="panel-head">
                    Recent Reports
                </div>

                <div class="table-scroll">

                    <table class="data-table">

                        <thead>

                            <tr>
                                <th>User</th>
                                <th>Hazard</th>
                                <th>Date &amp; Time</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>

                        </thead>

                        <tbody>

                        <%
                            if (recentReports != null &&
                                    !recentReports.isEmpty()) {

                                for (HazardReport report
                                        : recentReports) {

                                    String badgeClass =
                                            "badge-new";

                                    if ("Under Investigation".equals(
                                            report.getStatus())) {

                                        badgeClass =
                                                "badge-investigating";

                                    } else if ("Resolved".equals(
                                            report.getStatus())) {

                                        badgeClass =
                                                "badge-resolved";
                                    }
                        %>

                            <tr>

                                <td>
                                    @<%= HtmlUtil.escape(
                                            report.getUsername()
                                    ) %>
                                </td>

                                <td>

                                    <span class="hazard-icon">
                                        <%= report.getHazardIcon() %>
                                    </span>

                                    <%= HtmlUtil.escape(
                                            report.getHazardType()
                                    ) %>

                                </td>

                                <td>
                                    <%= HtmlUtil.escape(
                                            report.getDateTime()
                                    ) %>
                                </td>

                                <td>

                                    <span class="badge <%= badgeClass %>">

                                        <%= HtmlUtil.escape(
                                                report.getStatus()
                                        ) %>

                                    </span>

                                </td>

                                <td>

                                    <a class="link-view"
                                       href="<%= contextPath %>/report?id=<%= HtmlUtil.escape(
                                               report.getId()
                                       ) %>">

                                        View
                                    </a>

                                </td>

                            </tr>

                        <%
                                }

                            } else {
                        %>

                            <tr>

                                <td colspan="5"
                                    class="empty-state">

                                    No reports available.

                                </td>

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
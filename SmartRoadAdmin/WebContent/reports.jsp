<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.smartroad.admin.model.HazardReport" %>
<%@ page import="com.smartroad.admin.util.HtmlUtil" %>
<%@ page import="java.util.List" %>

<%
    request.setAttribute("activePage", "reports");

    List<HazardReport> reports = (List<HazardReport>) request.getAttribute("reports");

    String keyword = (String) request.getAttribute("keyword");
    String selectedHazardType = (String) request.getAttribute("selectedHazardType");
    String selectedStatus = (String) request.getAttribute("selectedStatus");
    String selectedReportDate = (String) request.getAttribute("selectedReportDate");
    String contextPath = request.getContextPath();

    String[] hazardTypes = {
            "Pothole",
            "Flooding",
            "Fallen Tree",
            "Traffic Accident",
            "Damaged Road Sign",
            "Broken Traffic Light"
    };

    String[] statuses = {
            "New",
            "Under Investigation",
            "Resolved"
    };
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hazard Reports - SmartRoad Admin</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
</head>
<body>

<div class="app-shell">

    <jsp:include page="/common/sidebar.jsp" />

    <main class="main">

        <header class="topbar">
            <h1>Hazard Reports</h1>
            <div class="admin-chip">
                <span class="avatar">A</span>
                Admin
            </div>
        </header>

        <div class="content">

            <div class="page-actions">
                <div>
                    <h2>Manage Reports</h2>
                    <p>Search and manage road hazard reports.</p>
                </div>
                <a class="btn btn-primary" href="<%= contextPath %>/add-report">
                    + Add Report
                </a>
            </div>

            <section class="panel">

                <form class="filter-bar" method="get" action="<%= contextPath %>/reports">

                    <input type="text"
                           name="keyword"
                           placeholder="Search reports..."
                           value="<%= HtmlUtil.escape(keyword) %>">

                    <select name="hazardType">
                        <option value="">All Hazard Types</option>
                        <%
                            for (String hazardType : hazardTypes) {
                                boolean selected = hazardType.equals(selectedHazardType);
                        %>
                            <option value="<%= HtmlUtil.escape(hazardType) %>" <%= selected ? "selected" : "" %>>
                                <%= HtmlUtil.escape(hazardType) %>
                            </option>
                        <%
                            }
                        %>
                    </select>

                    <input type="date"
                           name="reportDate"
                           value="<%= HtmlUtil.escape(selectedReportDate) %>">

                    <select name="status">
                        <option value="">All Status</option>
                        <%
                            for (String status : statuses) {
                                boolean selected = status.equals(selectedStatus);
                        %>
                            <option value="<%= HtmlUtil.escape(status) %>" <%= selected ? "selected" : "" %>>
                                <%= HtmlUtil.escape(status) %>
                            </option>
                        <%
                            }
                        %>
                    </select>

                    <button type="submit" class="btn btn-primary">Filter</button>
                    <a class="btn btn-outline" href="<%= contextPath %>/reports">Reset</a>

                </form>

                <div class="table-scroll">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Username</th>
                                <th>Date &amp; Time</th>
                                <th>User Agent</th>
                                <th>Hazard Type</th>
                                <th>Description</th>
                                <th>Photo</th>
                                <th>GPS Coordinates</th>
                                <th class="status-column">Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            if (reports != null && !reports.isEmpty()) {
                                for (HazardReport report : reports) {
                                    String badgeClass = "badge-new";

                                    if ("Under Investigation".equals(report.getStatus())) {
                                        badgeClass = "badge-investigating";
                                    } else if ("Resolved".equals(report.getStatus())) {
                                        badgeClass = "badge-resolved";
                                    }

                                    String imageUrl = report.getImageUrl();
                        %>
                            <tr>
                                <td title="<%= HtmlUtil.escape(report.getId()) %>">
                                    <%= HtmlUtil.escape(report.getShortId()) %>
                                </td>
                                <td><%= HtmlUtil.escape(report.getUsername()) %></td>
                                <td><%= HtmlUtil.escape(report.getDateTime()) %></td>
                                <td><%= HtmlUtil.escape(report.getUserAgent()) %></td>
                                <td>
                                    <span class="hazard-icon"><%= report.getHazardIcon() %></span>
                                    <%= HtmlUtil.escape(report.getHazardType()) %>
                                </td>
                                <td><%= HtmlUtil.escape(report.getDescription()) %></td>
                                <td>
                                <%
                                    if (imageUrl != null && !imageUrl.isBlank()) {
                                %>
                                    <a href="<%= HtmlUtil.escape(imageUrl) %>" target="_blank" rel="noopener noreferrer">
                                        <img class="photo-thumb" src="<%= HtmlUtil.escape(imageUrl) %>" alt="Hazard photo">
                                    </a>
                                <%
                                    } else {
                                %>
                                    <span class="empty-photo">No photo</span>
                                <%
                                    }
                                %>
                                </td>
                                <td><%= report.getLatitude() %>, <%= report.getLongitude() %></td>
                                <td class="status-column">
                                    <span class="badge <%= badgeClass %>">
                                        <%= HtmlUtil.escape(report.getStatus()) %>
                                    </span>
                                </td>                               
                                <td class="actions-cell">
                                    <div class="action-links">
                                        <a class="link-view" href="<%= contextPath %>/report?id=<%= HtmlUtil.escape(report.getId()) %>">
                                            View
                                        </a>
                                        
                                        <%-- edit button for report
                                        <a class="link-view" href="<%= contextPath %>/edit-report?id=<%= HtmlUtil.escape(report.getId()) %>">
                                            Edit
                                        </a>                                    
                                        --%>
                                         
                                        <form method="post"
                                              action="<%= contextPath %>/delete-report"
                                              onsubmit="return confirm('Delete this hazard report?');">
                                            <input type="hidden"
                                                   name="id"
                                                   value="<%= HtmlUtil.escape(report.getId()) %>">
                                            <button type="submit" class="btn-danger-text">
                                                Delete
                                            </button>
                                        </form>
                                    </div>
                                </td>                         
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="10" class="empty-state">
                                    No reports match the selected search or filters.
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
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.smartroad.admin.model.HazardReport" %>
<%@ page import="com.smartroad.admin.util.HtmlUtil" %>

<%
    request.setAttribute("activePage", "reports");

    HazardReport report = (HazardReport) request.getAttribute("report");
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Report Details - SmartRoad Admin</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
</head>
<body>

<div class="app-shell">

    <jsp:include page="/common/sidebar.jsp" />

    <main class="main">

        <header class="topbar">
            <h1>Report Details</h1>
            <div class="admin-chip">
                <span class="avatar">A</span>
                Admin
            </div>
        </header>

        <div class="content">

        <%
            if (report == null) {
        %>
            <div class="alert alert-error">
                The hazard report could not be found.
            </div>
            <a class="btn btn-outline" href="<%= contextPath %>/reports">
                Back to Reports
            </a>
        <%
            } else {
                String imageUrl = report.getImageUrl();
                String badgeClass = "badge-new";

                if ("Under Investigation".equals(report.getStatus())) {
                    badgeClass = "badge-investigating";
                } else if ("Resolved".equals(report.getStatus())) {
                    badgeClass = "badge-resolved";
                }
        %>
            <div class="page-actions">
                <div>
                    <h2>Report <%= HtmlUtil.escape(report.getShortId()) %></h2>
                    <p>Report ID: <%= HtmlUtil.escape(report.getId()) %></p>
                </div>
                <div class="page-action-buttons">
                    <a class="btn btn-outline" href="<%= contextPath %>/reports">
                        Back to Reports
                    </a>
                    <a class="btn btn-primary" href="<%= contextPath %>/edit-report?id=<%= HtmlUtil.escape(report.getId()) %>">
                        Edit Report
                    </a>
                </div>
            </div>

            <div class="panel form-panel">
                <div class="detail-grid">
                    <div>
                        <div class="detail-field">
                            <div class="f-label">User</div>
                            <div class="f-value">@<%= HtmlUtil.escape(report.getUsername()) %></div>
                        </div>

                        <div class="detail-field">
                            <div class="f-label">Hazard</div>
                            <div class="f-value">
                                <span class="hazard-icon"><%= report.getHazardIcon() %></span>
                                <%= HtmlUtil.escape(report.getHazardType()) %>
                            </div>
                        </div>

                        <div class="detail-field">
                            <div class="f-label">Description</div>
                            <div class="f-value"><%= HtmlUtil.escape(report.getDescription()) %></div>
                        </div>

                        <div class="detail-field">
                            <div class="f-label">Coordinates</div>
                            <div class="f-value"><%= report.getLatitude() %>, <%= report.getLongitude() %></div>
                        </div>

                        <div class="detail-field">
                            <div class="f-label">Date &amp; Time</div>
                            <div class="f-value"><%= HtmlUtil.escape(report.getDateTime()) %></div>
                        </div>

                        <div class="detail-field">
                            <div class="f-label">User Agent</div>
                            <div class="f-value"><%= HtmlUtil.escape(report.getUserAgent()) %></div>
                        </div>

                        <div class="detail-field">
                            <div class="f-label">Status</div>
                            <div class="f-value">
                                <span class="badge <%= badgeClass %>">
                                    <%= HtmlUtil.escape(report.getStatus()) %>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div>
                        <div class="detail-field">
                            <div class="f-label">Photo</div>
                            <%
                                if (imageUrl != null && !imageUrl.isBlank()) {
                            %>
                                <a href="<%= HtmlUtil.escape(imageUrl) %>" target="_blank" rel="noopener noreferrer">
                                    <img class="photo-large" src="<%= HtmlUtil.escape(imageUrl) %>" alt="Hazard photo">
                                </a>
                            <%
                                } else {
                            %>
                                <div class="photo-box">
                                    No photo is available for this report.
                                </div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>
        <%
            }
        %>
        </div>
    </main>

</div>

</body>
</html>
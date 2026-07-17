<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.smartroad.admin.model.HazardReport" %>
<%@ page import="com.smartroad.admin.util.HtmlUtil" %>

<%
    request.setAttribute(
            "activePage",
            "reports"
    );

    String contextPath =
            request.getContextPath();

    HazardReport report =
            (HazardReport) request.getAttribute(
                    "report"
            );

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

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

    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <title>Edit Report - SmartRoad Admin</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/css/style.css">

</head>

<body>

<div class="app-shell">

    <jsp:include page="/common/sidebar.jsp" />

    <div class="main">

        <div class="topbar">

            <h1>Edit Hazard Report</h1>

            <div class="admin-chip">

                <span class="avatar">
                    A
                </span>

                Admin

            </div>

        </div>

        <div class="content form-page">

        <%
            if (report == null) {
        %>

            <div class="alert alert-error">

                The hazard report could not be loaded.

            </div>

            <a class="btn btn-outline"
               href="<%= contextPath %>/reports">

                Back to Reports

            </a>

        <%
            } else {
        %>

            <a class="back-link"
               href="<%= contextPath %>/report?id=<%= HtmlUtil.escape(
                       report.getId()
               ) %>">

                &#8592; Back to Details

            </a>

            <%
                if (errorMessage != null &&
                        !errorMessage.isBlank()) {
            %>

                <div class="alert alert-error">

                    <%= HtmlUtil.escape(
                            errorMessage
                    ) %>

                </div>

            <%
                }
            %>

            <div class="panel form-panel">

                <form method="post"
                      action="<%= contextPath %>/edit-report"
                      class="report-form">

                    <input type="hidden"
                           name="id"
                           value="<%= HtmlUtil.escape(
                                   report.getId()
                           ) %>">

                    <div class="form-grid">

                        <div class="form-group">

                            <label for="username">
                                Username
                            </label>

                            <input id="username"
                                   type="text"
                                   name="username"
                                   required
                                   value="<%= HtmlUtil.escape(
                                           report.getUsername()
                                   ) %>">

                        </div>

                        <div class="form-group">

                            <label for="hazardType">
                                Hazard Type
                            </label>

                            <select id="hazardType"
                                    name="hazardType"
                                    required>

                                <%
                                    for (String hazardType
                                            : hazardTypes) {
                                %>

                                    <option value="<%= HtmlUtil.escape(
                                            hazardType
                                    ) %>"
                                        <%= hazardType.equals(
                                                report.getHazardType()
                                        ) ? "selected" : "" %>>

                                        <%= HtmlUtil.escape(
                                                hazardType
                                        ) %>

                                    </option>

                                <%
                                    }
                                %>

                            </select>

                        </div>

                        <div class="form-group">

                            <label for="status">
                                Status
                            </label>

                            <select id="status"
                                    name="status"
                                    required>

                                <%
                                    for (String status
                                            : statuses) {
                                %>

                                    <option value="<%= HtmlUtil.escape(
                                            status
                                    ) %>"
                                        <%= status.equals(
                                                report.getStatus()
                                        ) ? "selected" : "" %>>

                                        <%= HtmlUtil.escape(
                                                status
                                        ) %>

                                    </option>

                                <%
                                    }
                                %>

                            </select>

                        </div>

                        <div class="form-group">

                            <label for="dateTime">
                                Date &amp; Time
                            </label>

                            <input id="dateTime"
                                   type="datetime-local"
                                   name="dateTime"
                                   required
                                   value="<%= HtmlUtil.escape(
                                           report.getDateTimeInputValue()
                                   ) %>">

                        </div>

                        <div class="form-group">

                            <label for="latitude">
                                Latitude
                            </label>

                            <input id="latitude"
                                   type="number"
                                   name="latitude"
                                   step="any"
                                   min="-90"
                                   max="90"
                                   required
                                   value="<%= report.getLatitude() %>">

                        </div>

                        <div class="form-group">

                            <label for="longitude">
                                Longitude
                            </label>

                            <input id="longitude"
                                   type="number"
                                   name="longitude"
                                   step="any"
                                   min="-180"
                                   max="180"
                                   required
                                   value="<%= report.getLongitude() %>">

                        </div>

                        <div class="form-group full-span">

                            <label for="description">
                                Description
                            </label>

                            <textarea id="description"
                                      name="description"
                                      rows="5"
                                      required><%= HtmlUtil.escape(
                                              report.getDescription()
                                      ) %></textarea>

                        </div>

                        <div class="form-group">

                            <label for="userAgent">
                                User Agent
                            </label>

                            <input id="userAgent"
                                   type="text"
                                   name="userAgent"
                                   required
                                   value="<%= HtmlUtil.escape(
                                           report.getUserAgent()
                                   ) %>">

                        </div>

                        <div class="form-group">

                            <label for="imageUrl">
                                Photo URL
                            </label>

                            <input id="imageUrl"
                                   type="url"
                                   name="imageUrl"
                                   value="<%= HtmlUtil.escape(
                                           report.getImageUrl()
                                   ) %>">

                        </div>

                    </div>

                    <div class="form-actions">

                        <a class="btn btn-outline"
                           href="<%= contextPath %>/report?id=<%= HtmlUtil.escape(
                                   report.getId()
                           ) %>">

                            Cancel

                        </a>

                        <button type="submit"
                                class="btn btn-primary">

                            Save Changes

                        </button>

                    </div>

                </form>

            </div>

        <%
            }
        %>

        </div>

    </div>

</div>

</body>
</html>
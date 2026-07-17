<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.smartroad.admin.util.HtmlUtil" %>

<%
    request.setAttribute(
            "activePage",
            "add"
    );

    String contextPath =
            request.getContextPath();

    String errorMessage =
            (String) request.getAttribute(
                    "errorMessage"
            );

    String defaultDateTime =
            (String) request.getAttribute(
                    "defaultDateTime"
            );

    String usernameValue =
            request.getParameter("username");

    String selectedHazardType =
            request.getParameter("hazardType");

    String selectedStatus =
            request.getParameter("status");

    String dateTimeValue =
            request.getParameter("dateTime");

    String latitudeValue =
            request.getParameter("latitude");

    String longitudeValue =
            request.getParameter("longitude");

    String descriptionValue =
            request.getParameter("description");

    String userAgentValue =
            request.getParameter("userAgent");

    String imageUrlValue =
            request.getParameter("imageUrl");

    if (dateTimeValue == null) {
        dateTimeValue = defaultDateTime;
    }

    if (selectedStatus == null) {
        selectedStatus = "New";
    }

    if (userAgentValue == null) {
        userAgentValue = "Admin Web";
    }

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

    <title>Add Report - SmartRoad Admin</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/css/style.css">

</head>

<body>

<div class="app-shell">

    <jsp:include page="/common/sidebar.jsp" />

    <div class="main">

        <div class="topbar">

            <h1>Add Hazard Report</h1>

            <div class="admin-chip">

                <span class="avatar">
                    A
                </span>

                Admin

            </div>

        </div>

        <div class="content form-page">

            <a class="back-link"
               href="<%= contextPath %>/reports">

                &#8592; Back to Reports

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
                      action="<%= contextPath %>/add-report"
                      class="report-form">

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
                                           usernameValue
                                   ) %>">

                        </div>

                        <div class="form-group">

                            <label for="hazardType">
                                Hazard Type
                            </label>

                            <select id="hazardType"
                                    name="hazardType"
                                    required>

                                <option value="">
                                    Select type
                                </option>

                                <%
                                    for (String hazardType
                                            : hazardTypes) {
                                %>

                                    <option value="<%= HtmlUtil.escape(
                                            hazardType
                                    ) %>"
                                        <%= hazardType.equals(
                                                selectedHazardType
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
                                                selectedStatus
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
                                           dateTimeValue
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
                                   value="<%= HtmlUtil.escape(
                                           latitudeValue
                                   ) %>">

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
                                   value="<%= HtmlUtil.escape(
                                           longitudeValue
                                   ) %>">

                        </div>

                        <div class="form-group full-span">

                            <label for="description">
                                Description
                            </label>

                            <textarea id="description"
                                      name="description"
                                      rows="5"
                                      required><%= HtmlUtil.escape(
                                              descriptionValue
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
                                           userAgentValue
                                   ) %>">

                        </div>

                        <div class="form-group">

                            <label for="imageUrl">
                                Photo URL
                            </label>

                            <input id="imageUrl"
                                   type="url"
                                   name="imageUrl"
                                   placeholder="https://..."
                                   value="<%= HtmlUtil.escape(
                                           imageUrlValue
                                   ) %>">

                        </div>

                    </div>

                    <div class="form-actions">

                        <a class="btn btn-outline"
                           href="<%= contextPath %>/reports">

                            Cancel

                        </a>

                        <button type="submit"
                                class="btn btn-primary">

                            Create Report

                        </button>

                    </div>

                </form>

            </div>

        </div>

    </div>

</div>

</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
    String activePage =
            (String) request.getAttribute(
                    "activePage"
            );

    if (activePage == null) {
        activePage = "";
    }

    String sidebarContextPath =
            request.getContextPath();
%>

<aside class="sidebar">

    <div class="sidebar-brand">

        <span class="dot"></span>

        SmartRoad Admin

    </div>

    <ul class="sidebar-nav">

        <li>

            <a href="<%= sidebarContextPath %>/dashboard"
               class="<%= "dashboard".equals(activePage)
                       ? "active"
                       : "" %>">

                &#128202; Dashboard

            </a>

        </li>

        <li>

            <a href="<%= sidebarContextPath %>/reports"
               class="<%= "reports".equals(activePage)
                       ? "active"
                       : "" %>">

                &#9888; Hazard Reports

            </a>

        </li>

        <li>

            <a href="<%= sidebarContextPath %>/add-report"
               class="<%= "add".equals(activePage)
                       ? "active"
                       : "" %>">

                &#10133; Add Report

            </a>

        </li>

    </ul>

    <div class="sidebar-foot">

        SmartRoad &copy; 2026

    </div>

</aside>
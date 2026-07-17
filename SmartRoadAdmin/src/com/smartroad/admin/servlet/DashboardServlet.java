package com.smartroad.admin.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.smartroad.admin.dao.HazardReportDAO;
import com.smartroad.admin.model.HazardReport;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
public class DashboardServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<HazardReport> all =
                    new HazardReportDAO()
                            .getAllReports();

            Set<String> usernames =
                    new HashSet<>();

            int resolved = 0;

            for (HazardReport report : all) {

                if (report.getUsername() != null &&
                        !report.getUsername().isBlank()) {

                    usernames.add(
                            report.getUsername()
                                    .toLowerCase()
                    );
                }

                if ("Resolved".equals(
                        report.getStatus())) {

                    resolved++;
                }
            }

            int total =
                    all.size();

            int open =
                    total - resolved;

            List<HazardReport> recent =
                    new ArrayList<>(
                            all.subList(
                                    0,
                                    Math.min(
                                            5,
                                            all.size()
                                    )
                            )
                    );

            request.setAttribute(
                    "totalUsers",
                    usernames.size()
            );

            request.setAttribute(
                    "totalReports",
                    total
            );

            request.setAttribute(
                    "openReports",
                    open
            );

            request.setAttribute(
                    "resolvedReports",
                    resolved
            );

            request.setAttribute(
                    "recentReports",
                    recent
            );

            request.getRequestDispatcher(
                    "/dashboard.jsp"
            ).forward(request, response);

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();

            throw new ServletException(
                    "The Firestore request was interrupted.",
                    exception
            );

        } catch (ExecutionException exception) {

            throw new ServletException(
                    "Unable to load dashboard data.",
                    exception
            );
        }
    }
}
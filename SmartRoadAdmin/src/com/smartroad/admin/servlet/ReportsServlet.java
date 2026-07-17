package com.smartroad.admin.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.smartroad.admin.dao.HazardReportDAO;
import com.smartroad.admin.model.HazardReport;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = clean(request.getParameter("keyword"));
        String hazardType = clean(request.getParameter("hazardType"));
        String status = clean(request.getParameter("status"));
        String reportDate = clean(request.getParameter("reportDate"));

        try {
            List<HazardReport> reports = new HazardReportDAO().getAllReports();
            List<HazardReport> filtered = new ArrayList<>();

            for (HazardReport report : reports) {
                if (!matchesKeyword(report, keyword)) {
                    continue;
                }
                if (hazardType != null && !hazardType.equals(report.getHazardType())) {
                    continue;
                }
                if (status != null && !status.equals(report.getStatus())) {
                    continue;
                }
                if (!matchesDate(report, reportDate)) {
                    continue;
                }
                filtered.add(report);
            }

            request.setAttribute("reports", filtered);
            request.setAttribute("keyword", keyword == null ? "" : keyword);
            request.setAttribute("selectedHazardType", hazardType);
            request.setAttribute("selectedStatus", status);
            request.setAttribute("selectedReportDate", reportDate == null ? "" : reportDate);

            request.getRequestDispatcher("/reports.jsp").forward(request, response);

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ServletException("The Firestore request was interrupted.", exception);

        } catch (ExecutionException exception) {
            throw new ServletException("Unable to load hazard reports.", exception);
        }
    }

    private boolean matchesKeyword(HazardReport report, String keyword) {
        if (keyword == null) {
            return true;
        }

        String value = keyword.toLowerCase();
        return contains(report.getUsername(), value)
                || contains(report.getDescription(), value)
                || contains(report.getHazardType(), value);
    }

    private boolean matchesDate(HazardReport report, String dateFilter) {
        if (dateFilter == null) {
            return true;
        }

        try {
            LocalDate expected = LocalDate.parse(dateFilter);
            String[] patterns = {"dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy HH:mm"};

            for (String pattern : patterns) {
                try {
                    LocalDate actual = LocalDateTime.parse(
                            report.getDateTime(),
                            DateTimeFormatter.ofPattern(pattern)
                    ).toLocalDate();

                    return expected.equals(actual);
                } catch (DateTimeParseException ignored) {
                    // Try next format.
                }
            }
            return false;

        } catch (DateTimeParseException | NullPointerException exception) {
            return false;
        }
    }

    private boolean contains(String source, String expectedLowerCase) {
        return source != null && source.toLowerCase().contains(expectedLowerCase);
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
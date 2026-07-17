package com.smartroad.admin.servlet;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.smartroad.admin.dao.HazardReportDAO;
import com.smartroad.admin.model.HazardReport;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/edit-report")
public class EditReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id == null || id.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing report ID.");
            return;
        }

        try {
            HazardReport report = new HazardReportDAO().findById(id);

            if (report == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hazard report not found.");
                return;
            }

            request.setAttribute("report", report);
            request.getRequestDispatcher("/editReport.jsp").forward(request, response);

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ServletException("The Firestore request was interrupted.", exception);

        } catch (ExecutionException exception) {
            throw new ServletException("Unable to load the hazard report.", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HazardReport report = ReportFormUtil.fromRequest(request);
            boolean updated = new HazardReportDAO().updateReport(report);

            if (!updated) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hazard report not found.");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/report?id=" + report.getId());

        } catch (IllegalArgumentException exception) {
            request.setAttribute("errorMessage", exception.getMessage());
            request.getRequestDispatcher("/editReport.jsp").forward(request, response);

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ServletException("The Firestore request was interrupted.", exception);

        } catch (ExecutionException exception) {
            throw new ServletException("Unable to edit the hazard report.", exception);
        }
    }
}
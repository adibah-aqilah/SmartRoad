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

@WebServlet("/add-report")
public class AddReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("defaultDateTime", ReportFormUtil.currentDateTimeInput());
        request.getRequestDispatcher("/addReport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HazardReport report = ReportFormUtil.fromRequest(request);
            String id = new HazardReportDAO().addReport(report);

            response.sendRedirect(request.getContextPath() + "/report?id=" + id);

        } catch (IllegalArgumentException exception) {
            request.setAttribute("errorMessage", exception.getMessage());
            request.setAttribute("defaultDateTime", request.getParameter("dateTime"));
            request.getRequestDispatcher("/addReport.jsp").forward(request, response);

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ServletException("The Firestore request was interrupted.", exception);

        } catch (ExecutionException exception) {
            throw new ServletException("Unable to add the hazard report.", exception);
        }
    }
}
package com.smartroad.admin.servlet;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.smartroad.admin.dao.HazardReportDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/delete-report")
public class DeleteReportServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String id =
                request.getParameter("id");

        if (id == null || id.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Missing report ID."
            );

            return;
        }

        try {

            boolean deleted =
                    new HazardReportDAO()
                            .delete(id);

            if (!deleted) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Hazard report not found."
                );

                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/reports"
            );

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();

            throw new ServletException(
                    "The Firestore request was interrupted.",
                    exception
            );

        } catch (ExecutionException exception) {

            throw new ServletException(
                    "Unable to delete the hazard report.",
                    exception
            );
        }
    }
}
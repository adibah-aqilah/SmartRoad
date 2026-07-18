package com.smartroad.admin.servlet;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import com.smartroad.admin.dao.HazardReportDAO;
import com.smartroad.admin.model.HazardReport;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/report-photo")
public class ReportPhotoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            String imageBase64 = report.getImageBase64();

            /*
             * Preferred source:
             * Base64 stored by the Android app.
             */
            if (imageBase64 != null && !imageBase64.isBlank()) {
                String cleanBase64 = cleanBase64(imageBase64);
                byte[] imageBytes;

                try {
                    imageBytes = Base64.getDecoder().decode(cleanBase64);
                } catch (IllegalArgumentException exception) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The stored photo is not valid Base64.");
                    return;
                }

                response.setContentType(detectContentType(imageBytes));
                response.setContentLength(imageBytes.length);
                response.setHeader("Cache-Control", "private, max-age=3600");
                response.getOutputStream().write(imageBytes);
                return;
            }

            /*
             * Fallback:
             * normal public URL created through admin.
             */
            String imageUrl = report.getImageUrl();

            if (imageUrl != null && !imageUrl.isBlank() && (imageUrl.startsWith("https://") || imageUrl.startsWith("http://"))) {
                response.sendRedirect(imageUrl);
                return;
            }

            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No photo is available.");

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ServletException("The Firestore request was interrupted.", exception);
        } catch (ExecutionException exception) {
            throw new ServletException("Unable to load the report photo.", exception);
        }
    }

    private String cleanBase64(String value) {
        String result = value.trim();

        /*
         * Supports values such as:
         * data:image/jpeg;base64,/9j/...
         */
        int commaPosition = result.indexOf(',');

        if (result.startsWith("data:image/") && commaPosition >= 0) {
            result = result.substring(commaPosition + 1);
        }

        /*
         * Removes line breaks and spaces created
         * by Android Base64.DEFAULT.
         */
        return result.replaceAll("\\s+", "");
    }

    private String detectContentType(byte[] imageBytes) {
        /*
         * JPEG begins with FF D8 FF.
         */
        if (imageBytes.length >= 3 &&
                (imageBytes[0] & 0xFF) == 0xFF &&
                (imageBytes[1] & 0xFF) == 0xD8 &&
                (imageBytes[2] & 0xFF) == 0xFF) {
            return "image/jpeg";
        }

        /*
         * PNG begins with 89 50 4E 47.
         */
        if (imageBytes.length >= 4 &&
                (imageBytes[0] & 0xFF) == 0x89 &&
                imageBytes[1] == 0x50 &&
                imageBytes[2] == 0x4E &&
                imageBytes[3] == 0x47) {
            return "image/png";
        }

        return "application/octet-stream";
    }
}
package com.smartroad.admin.servlet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.smartroad.admin.model.HazardReport;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Converts values submitted through an admin form
 * into a HazardReport object.
 */
public final class ReportFormUtil {

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DATABASE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private ReportFormUtil() {
        // Prevent instantiation
    }

    public static HazardReport fromRequest(HttpServletRequest request) {
        HazardReport report = new HazardReport();

        report.setId(trimToNull(request.getParameter("id")));
        report.setUsername(getRequiredParameter(request, "username", "Username"));
        report.setHazardType(getRequiredParameter(request, "hazardType", "Hazard type"));
        report.setDescription(getRequiredParameter(request, "description", "Description"));

        String status = trimToNull(request.getParameter("status"));
        report.setStatus(status == null ? "New" : status);

        report.setLatitude(parseNumber(request, "latitude", "Latitude"));
        report.setLongitude(parseNumber(request, "longitude", "Longitude"));
        report.setDateTime(convertDateTime(request.getParameter("dateTime")));

        String userAgent = trimToNull(request.getParameter("userAgent"));
        report.setUserAgent(userAgent == null ? "Admin Web" : userAgent);
        report.setImageUrl(trimToNull(request.getParameter("imageUrl")));

        return report;
    }

    public static String currentDateTimeInput() {
        return LocalDateTime.now().format(INPUT_FORMAT);
    }

    private static String convertDateTime(String input) {
        String value = trimToNull(input);
        if (value == null) {
            throw new IllegalArgumentException("Date and time are required.");
        }

        try {
            LocalDateTime dateTime = LocalDateTime.parse(value, INPUT_FORMAT);
            return dateTime.format(DATABASE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Date and time format is invalid.");
        }
    }

    private static double parseNumber(HttpServletRequest request, String parameterName, String label) {
        String value = getRequiredParameter(request, parameterName, label);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(label + " must be a valid number.");
        }
    }

    private static String getRequiredParameter(HttpServletRequest request, String parameterName, String label) {
        String value = trimToNull(request.getParameter(parameterName));
        if (value == null) {
            throw new IllegalArgumentException(label + " is required.");
        }
        return value;
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
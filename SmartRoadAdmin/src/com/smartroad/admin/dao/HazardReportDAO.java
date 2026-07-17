package com.smartroad.admin.dao;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.smartroad.admin.config.FirebaseConfig;
import com.smartroad.admin.model.HazardReport;

/**
 * Provides Firestore CRUD operations for the hazards collection.
 */
public class HazardReportDAO {

    private static final String COLLECTION =
            "hazards";

    private final Firestore db;

    public HazardReportDAO()
    		// TODO Auto-generated constructor stub
            throws IOException {

        db = FirebaseConfig.getFirestore();
    }

    public List<HazardReport> getAllReports()
            throws InterruptedException,
            ExecutionException {

        QuerySnapshot snapshot =
                db.collection(COLLECTION)
                        .get()
                        .get();

        List<HazardReport> reports =
                new ArrayList<>();

        for (QueryDocumentSnapshot document
                : snapshot.getDocuments()) {

            reports.add(
                    toHazardReport(document)
            );
        }

        reports.sort((first, second) -> {

            LocalDateTime firstDate =
                    parseDateTime(first.getDateTime());

            LocalDateTime secondDate =
                    parseDateTime(second.getDateTime());

            if (firstDate == null &&
                    secondDate == null) {
                return 0;
            }

            if (firstDate == null) {
                return 1;
            }

            if (secondDate == null) {
                return -1;
            }

            return secondDate.compareTo(firstDate);
        });

        return reports;
    }

    public HazardReport findById(String id)
            throws InterruptedException,
            ExecutionException {

        if (id == null || id.isBlank()) {
            return null;
        }

        DocumentSnapshot document =
                db.collection(COLLECTION)
                        .document(id)
                        .get()
                        .get();

        if (!document.exists()) {
            return null;
        }

        return toHazardReport(document);
    }

    public String addReport(HazardReport report)
            throws InterruptedException,
            ExecutionException {

        validateReport(report);

        DocumentReference document =
                db.collection(COLLECTION)
                        .document();

        document.set(
                toMap(report)
        ).get();

        return document.getId();
    }

    public boolean updateReport(
            HazardReport report)
            throws InterruptedException,
            ExecutionException {

        if (report == null ||
                report.getId() == null ||
                report.getId().isBlank()) {

            return false;
        }

        validateReport(report);

        DocumentReference document =
                db.collection(COLLECTION)
                        .document(report.getId());

        if (!document.get().get().exists()) {
            return false;
        }

        document.set(
                toMap(report)
        ).get();

        return true;
    }

    public boolean updateStatus(
            String id,
            String status)
            throws InterruptedException,
            ExecutionException {

        if (id == null || id.isBlank()) {
            return false;
        }

        validateStatus(status);

        DocumentReference document =
                db.collection(COLLECTION)
                        .document(id);

        if (!document.get().get().exists()) {
            return false;
        }

        document.update(
                "status",
                status
        ).get();

        return true;
    }

    public boolean delete(String id)
            throws InterruptedException,
            ExecutionException {

        if (id == null || id.isBlank()) {
            return false;
        }

        DocumentReference document =
                db.collection(COLLECTION)
                        .document(id);

        if (!document.get().get().exists()) {
            return false;
        }

        document.delete().get();

        return true;
    }

    private HazardReport toHazardReport(
            DocumentSnapshot document) {

        HazardReport report =
                new HazardReport();

        report.setId(
                document.getId()
        );

        /*
         * reporter is only a fallback for old Android reports.
         */
        report.setUsername(
                firstNonBlank(
                        getString(document, "username"),
                        getString(document, "reporter"),
                        "Unknown user"
                )
        );

        report.setHazardType(
                firstNonBlank(
                        getString(document, "hazardType"),
                        "Unknown"
                )
        );

        report.setDescription(
                firstNonBlank(
                        getString(document, "description"),
                        "No description"
                )
        );

        report.setStatus(
                firstNonBlank(
                        getString(document, "status"),
                        "New"
                )
        );

        report.setLatitude(
                getDouble(document, "latitude")
        );

        report.setLongitude(
                getDouble(document, "longitude")
        );

        report.setDateTime(
                firstNonBlank(
                        getString(document, "dateTime"),
                        "Not available"
                )
        );

        report.setUserAgent(
                firstNonBlank(
                        getString(document, "userAgent"),
                        "Not available"
                )
        );

        report.setImageUrl(
                getString(document, "imageUrl")
        );

        return report;
    }

    private Map<String, Object> toMap(
            HazardReport report) {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "username",
                report.getUsername().trim()
        );

        data.put(
                "hazardType",
                report.getHazardType().trim()
        );

        data.put(
                "description",
                report.getDescription().trim()
        );

        data.put(
                "status",
                report.getStatus().trim()
        );

        data.put(
                "latitude",
                report.getLatitude()
        );

        data.put(
                "longitude",
                report.getLongitude()
        );

        data.put(
                "dateTime",
                report.getDateTime().trim()
        );

        data.put(
                "userAgent",
                report.getUserAgent().trim()
        );

        String imageUrl =
                report.getImageUrl();

        data.put(
                "imageUrl",
                imageUrl == null ||
                        imageUrl.isBlank()
                        ? null
                        : imageUrl.trim()
        );

        return data;
    }

    private void validateReport(
            HazardReport report) {

        if (report == null) {
            throw new IllegalArgumentException(
                    "Report is required."
            );
        }

        requireText(
                report.getUsername(),
                "Username"
        );

        requireText(
                report.getHazardType(),
                "Hazard type"
        );

        requireText(
                report.getDescription(),
                "Description"
        );

        requireText(
                report.getDateTime(),
                "Date and time"
        );

        requireText(
                report.getUserAgent(),
                "User agent"
        );

        validateStatus(
                report.getStatus()
        );

        if (report.getLatitude() < -90 ||
                report.getLatitude() > 90) {

            throw new IllegalArgumentException(
                    "Latitude must be between -90 and 90."
            );
        }

        if (report.getLongitude() < -180 ||
                report.getLongitude() > 180) {

            throw new IllegalArgumentException(
                    "Longitude must be between -180 and 180."
            );
        }
    }

    private void validateStatus(String status) {

        if (!"New".equals(status) &&
                !"Under Investigation".equals(status) &&
                !"Resolved".equals(status)) {

            throw new IllegalArgumentException(
                    "Invalid report status."
            );
        }
    }

    private void requireText(
            String value,
            String label) {

        if (value == null || value.isBlank()) {

            throw new IllegalArgumentException(
                    label + " is required."
            );
        }
    }

    private String getString(
            DocumentSnapshot document,
            String field) {

        Object value =
                document.get(field);

        return value == null
                ? null
                : String.valueOf(value);
    }

    private double getDouble(
            DocumentSnapshot document,
            String field) {

        Object value =
                document.get(field);

        return value instanceof Number
                ? ((Number) value).doubleValue()
                : 0.0;
    }

    private String firstNonBlank(
            String... values) {

        for (String value : values) {

            if (value != null &&
                    !value.isBlank()) {

                return value;
            }
        }

        return null;
    }

    private LocalDateTime parseDateTime(
            String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        String[] patterns = {
                "dd/MM/yyyy HH:mm:ss",
                "dd/MM/yyyy HH:mm"
        };

        for (String pattern : patterns) {

            try {

                return LocalDateTime.parse(
                        value,
                        DateTimeFormatter
                                .ofPattern(pattern)
                );

            } catch (DateTimeParseException ignored) {
                // Try the next format.
            }
        }

        return null;
    }
}
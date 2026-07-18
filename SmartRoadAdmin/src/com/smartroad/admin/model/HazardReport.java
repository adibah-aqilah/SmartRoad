package com.smartroad.admin.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HazardReport {

    private String id;
    private String username;
    private String hazardType;
    private String description;
    private String status;
    private double latitude;
    private double longitude;
    private String dateTime;
    private String userAgent;
    private String imageUrl;

    public HazardReport() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHazardType() {
        return hazardType;
    }

    public void setHazardType(String hazardType) {
        this.hazardType = hazardType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShortId() {
        if (id == null) {
            return "";
        }
        if (id.length() <= 10) {
            return id;
        }
        return id.substring(0, 10) + "...";
    }

    public String getDateTimeInputValue() {
        if (dateTime == null || dateTime.isBlank()) {
            return "";
        }

        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String[] acceptedFormats = {
                "dd/MM/yyyy HH:mm:ss",
                "dd/MM/yyyy HH:mm"
        };

        for (String format : acceptedFormats) {
            try {
                LocalDateTime parsedDate = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(format));
                return parsedDate.format(outputFormat);
            } catch (DateTimeParseException ignored) {
                // Try the next accepted format.
            }
        }
        return "";
    }

    public String getHazardIcon() {
        if (hazardType == null) {
            return "\u26A0";
        }

        switch (hazardType) {
            case "Pothole":
                return "\uD83D\uDD73";
            case "Flooding":
                return "\uD83C\uDF0A";
            case "Fallen Tree":
                return "\uD83C\uDF33";
            case "Traffic Accident":
                return "\uD83D\uDE97";
            case "Damaged Road Sign":
                return "\uD83D\uDEA7";
            case "Broken Traffic Light":
                return "\uD83D\uDEA6";
            default:
                return "\u26A0";
        }
    }
}
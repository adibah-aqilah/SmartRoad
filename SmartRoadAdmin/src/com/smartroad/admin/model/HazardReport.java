package com.smartroad.admin.model;

/**
 * Represents one hazard report row.
 *
 * This is a plain data holder only. Right now it is filled with sample
 * data by DummyDataStore so the admin UI has something to display.
 * Whoever wires up the database / servlet logic should be able to keep
 * this class as-is and just populate it from a DAO / ResultSet instead.
 */
public class HazardReport {

	private int id;
	private String username;
	private String fullName;
	private String hazardType;
	private String description;
	private String status; // "New", "Under Investigation", "Resolved"
	private double latitude;
	private double longitude;
	private String dateTime; // kept as String for simple display
	private String userAgent;
	private String photoFileName; // null/empty if no photo uploaded

	public HazardReport(int id, String username, String fullName, String hazardType,
			String description, String status, double latitude, double longitude,
			String dateTime, String userAgent, String photoFileName) {
		this.id = id;
		this.username = username;
		this.fullName = fullName;
		this.hazardType = hazardType;
		this.description = description;
		this.status = status;
		this.latitude = latitude;
		this.longitude = longitude;
		this.dateTime = dateTime;
		this.userAgent = userAgent;
		this.photoFileName = photoFileName;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getFullName() { return fullName; }
	public void setFullName(String fullName) { this.fullName = fullName; }

	public String getHazardType() { return hazardType; }
	public void setHazardType(String hazardType) { this.hazardType = hazardType; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public double getLatitude() { return latitude; }
	public void setLatitude(double latitude) { this.latitude = latitude; }

	public double getLongitude() { return longitude; }
	public void setLongitude(double longitude) { this.longitude = longitude; }

	public String getDateTime() { return dateTime; }
	public void setDateTime(String dateTime) { this.dateTime = dateTime; }

	public String getUserAgent() { return userAgent; }
	public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

	public String getPhotoFileName() { return photoFileName; }
	public void setPhotoFileName(String photoFileName) { this.photoFileName = photoFileName; }

	/** Small helper used by the icon column in the JSPs. */
	public String getHazardIcon() {
		switch (hazardType) {
			case "Pothole": return "\uD83D\uDD73"; // hole emoji
			case "Flooding": return "\uD83C\uDF0A"; // wave
			case "Fallen Tree": return "\uD83C\uDF33";
			case "Traffic Accident": return "\uD83D\uDE97";
			case "Damaged Road Sign": return "\uD83D\uDEA7";
			case "Broken Traffic Light": return "\uD83D\uDEA6";
			default: return "\u26A0";
		}
	}
}

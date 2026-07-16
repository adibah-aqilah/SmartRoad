package com.smartroad.admin.data;

import java.util.ArrayList;
import java.util.List;

import com.smartroad.admin.model.HazardReport;

/**
 * Temporary in-memory "database" so the admin UI has real data to render.
 *
 * IMPORTANT (for whoever adds the real functionality):
 * Replace the body of this class with actual JDBC / DAO calls to the
 * project's database. Keep the method names (getAllReports, findById,
 * updateStatus, delete) the same if possible so the servlets below do
 * not need to change - only this class does.
 */
public class DummyDataStore {

	private static final List<HazardReport> REPORTS = new ArrayList<>();
	private static int nextId = 9;

	// Dummy stats shown on the dashboard until real user data exists.
	public static final int TOTAL_USERS = 42;

	static {
		REPORTS.add(new HazardReport(1, "aliahmad", "Ali Ahmad", "Flooding",
				"Road flooded after heavy rain, water almost knee-deep near the junction.",
				"New", 2.1896, 102.2501, "02/06/2026 09:14", "Android 15", ""));

		REPORTS.add(new HazardReport(2, "abubakar", "Abu Bakar", "Pothole",
				"Large pothole causing traffic congestion during peak hours.",
				"Under Investigation", 2.3115, 102.3218, "02/06/2026 14:30", "Android 14", "pothole_2.jpg"));

		REPORTS.add(new HazardReport(3, "sitinur", "Siti Nurhaliza", "Fallen Tree",
				"Tree fell across both lanes after the storm last night.",
				"Resolved", 2.2010, 102.2456, "30/05/2026 07:02", "Android 13", "tree_3.jpg"));

		REPORTS.add(new HazardReport(4, "farahyusof", "Farah Yusof", "Traffic Accident",
				"Minor collision between two motorcycles, debris still on the road.",
				"New", 2.1945, 102.2601, "03/06/2026 18:47", "Android 15", "accident_4.jpg"));

		REPORTS.add(new HazardReport(5, "razakizwan", "Razak Izwan", "Damaged Road Sign",
				"Stop sign bent and facing the wrong direction after an earlier accident.",
				"Under Investigation", 2.2078, 102.2389, "28/05/2026 11:20", "Android 14", ""));

		REPORTS.add(new HazardReport(6, "wanida", "Wan Ida", "Broken Traffic Light",
				"Traffic light stuck on red in all directions, causing a jam.",
				"New", 2.1932, 102.2477, "04/06/2026 08:05", "Android 15", "light_6.jpg"));

		REPORTS.add(new HazardReport(7, "kumarsel", "Kumar Selvam", "Pothole",
				"Small pothole, not urgent but growing after each rain.",
				"Resolved", 2.2151, 102.2622, "20/05/2026 16:40", "Android 12", ""));

		REPORTS.add(new HazardReport(8, "aliahmad", "Ali Ahmad", "Flooding",
				"Same spot as before, flooding again after the afternoon downpour.",
				"Under Investigation", 2.1899, 102.2504, "05/06/2026 15:58", "Android 15", "flood_8.jpg"));
	}

	private DummyDataStore() {}

	public static List<HazardReport> getAllReports() {
		return REPORTS;
	}

	public static HazardReport findById(int id) {
		for (HazardReport r : REPORTS) {
			if (r.getId() == id) return r;
		}
		return null;
	}

	public static void updateStatus(int id, String newStatus) {
		HazardReport r = findById(id);
		if (r != null) r.setStatus(newStatus);
	}

	public static boolean delete(int id) {
		return REPORTS.removeIf(r -> r.getId() == id);
	}

	public static int getNextId() {
		return nextId++;
	}
}

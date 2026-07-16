package com.smartroad.admin.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.smartroad.admin.data.DummyDataStore;
import com.smartroad.admin.model.HazardReport;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<HazardReport> all = DummyDataStore.getAllReports();

		int total = all.size();
		int resolved = 0;
		for (HazardReport r : all) {
			if ("Resolved".equals(r.getStatus())) resolved++;
		}
		int open = total - resolved;

		// Most recent 5 reports (list is already newest-appended-last, so read backwards)
		List<HazardReport> recent = new ArrayList<>();
		for (int i = all.size() - 1; i >= 0 && recent.size() < 5; i--) {
			recent.add(all.get(i));
		}

		request.setAttribute("totalUsers", DummyDataStore.TOTAL_USERS);
		request.setAttribute("totalReports", total);
		request.setAttribute("openReports", open);
		request.setAttribute("resolvedReports", resolved);
		request.setAttribute("recentReports", recent);

		request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
	}
}

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

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		if ("delete".equals(action)) {
			String idParam = request.getParameter("id");
			try {
				DummyDataStore.delete(Integer.parseInt(idParam));
			} catch (NumberFormatException ignored) { }
			response.sendRedirect(request.getContextPath() + "/reports");
			return;
		}

		String keyword = request.getParameter("keyword");
		String hazardType = request.getParameter("hazardType");
		String status = request.getParameter("status");

		List<HazardReport> filtered = new ArrayList<>();
		for (HazardReport r : DummyDataStore.getAllReports()) {

			if (keyword != null && !keyword.trim().isEmpty()) {
				String k = keyword.trim().toLowerCase();
				boolean matches = r.getFullName().toLowerCase().contains(k)
						|| r.getUsername().toLowerCase().contains(k)
						|| r.getDescription().toLowerCase().contains(k);
				if (!matches) continue;
			}

			if (hazardType != null && !hazardType.isEmpty() && !hazardType.equals(r.getHazardType())) {
				continue;
			}

			if (status != null && !status.isEmpty() && !status.equals(r.getStatus())) {
				continue;
			}

			filtered.add(r);
		}

		request.setAttribute("reports", filtered);
		request.setAttribute("keyword", keyword);
		request.setAttribute("selectedHazardType", hazardType);
		request.setAttribute("selectedStatus", status);

		request.getRequestDispatcher("/reports.jsp").forward(request, response);
	}
}

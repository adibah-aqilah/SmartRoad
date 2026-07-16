package com.smartroad.admin.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.smartroad.admin.data.DummyDataStore;
import com.smartroad.admin.model.HazardReport;

@WebServlet("/report")
public class ReportDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = parseId(request);
		HazardReport report = DummyDataStore.findById(id);

		if (report == null) {
			response.sendRedirect(request.getContextPath() + "/reports");
			return;
		}

		request.setAttribute("report", report);
		request.getRequestDispatcher("/reportDetails.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = parseId(request);
		String newStatus = request.getParameter("status");

		if (DummyDataStore.findById(id) != null && newStatus != null) {
			DummyDataStore.updateStatus(id, newStatus);
		}

		response.sendRedirect(request.getContextPath() + "/report?id=" + id);
	}

	private int parseId(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}

package com.example.smartroad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.google.firebase.firestore.FirebaseFirestore;

public class HazardDetailsActivity extends AppCompatActivity {

    private TextView tvStatus;
    private TextView tvHazardType;
    private TextView tvHazardDescription;
    private TextView tvDetailLocation;
    private TextView tvDetailTime;
    private TextView tvReporterName;

    private ImageView ivHazardPhoto;
    private Button btnUpdateStatus;
    private String hazardId;
    private com.google.android.material.card.MaterialCardView cardStatus;
    private com.google.firebase.firestore.FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_details);

        tvStatus = findViewById(R.id.tvStatus);
        cardStatus = findViewById(R.id.cardStatus);
        tvHazardType = findViewById(R.id.tvHazardType);
        tvHazardDescription = findViewById(R.id.tvHazardDescription);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailTime = findViewById(R.id.tvDetailTime);
        tvReporterName = findViewById(R.id.tvReporterName);

        ivHazardPhoto = findViewById(R.id.ivHazardImage);
        btnUpdateStatus = findViewById(R.id.btnUpdateStatus);

        db = com.google.firebase.firestore.FirebaseFirestore.getInstance();

        Button btnBack = findViewById(R.id.btnBack);

        String documentId = getIntent().getStringExtra("DOCUMENT_ID");
        if (documentId == null) {
            documentId = getIntent().getStringExtra("ID");
        }

        if (documentId != null) {
            hazardId = documentId;
            fetchHazardDetails(documentId);
        } else {
            populateFromIntent();
        }

        checkAdminStatus();
        btnUpdateStatus.setOnClickListener(v -> showUpdateStatusDialog());
        btnBack.setOnClickListener(v -> finish());

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        // No item selected by default for details page, or we can leave it
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_report) {
                startActivity(new Intent(this, HazardFeedActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void populateFromIntent() {
        String type = getIntent().getStringExtra("TYPE");
        String status = getIntent().getStringExtra("STATUS");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String location = getIntent().getStringExtra("LOCATION");
        String reporter = getIntent().getStringExtra("USERNAME");
        if (reporter == null) reporter = getIntent().getStringExtra("REPORTER");
        String dateTime = getIntent().getStringExtra("DATE_TIME");
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        String imageBase64 = getIntent().getStringExtra("IMAGE_BASE64");

        displayData(type, status, description, location, reporter, dateTime, imageUrl, imageBase64);
    }

    private void fetchHazardDetails(String documentId) {
        db.collection("hazards").document(documentId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String type = document.getString("hazardType");
                        String status = document.getString("status");
                        String description = document.getString("description");
                        String reporter = document.getString("reporter");
                        if (reporter == null) reporter = document.getString("username");
                        String dateTime = document.getString("dateTime");
                        String imageUrl = document.getString("imageUrl");
                        String imageBase64 = document.getString("imageBase64");
                        
                        Double lat = document.getDouble("latitude");
                        Double lng = document.getDouble("longitude");
                        String location = (lat != null && lng != null) ? lat + ", " + lng : "Unknown";

                        displayData(type, status, description, location, reporter, dateTime, imageUrl, imageBase64);
                    }
                });
    }

    private void displayData(String type, String status, String description, String location, 
                             String reporter, String dateTime, String imageUrl, String imageBase64) {
        
        if (type == null) type = "Unknown Hazard";
        if (status == null) status = "New";
        if (description == null) description = "No description available";
        if (location == null) location = "Location not available";
        if (reporter == null) reporter = "Unknown Reporter";
        if (dateTime == null) dateTime = "Unknown Date";

        tvHazardType.setText(type);
        updateStatusBadge(status);
        tvHazardDescription.setText(description);
        tvDetailLocation.setText("Location: " + location);
        tvDetailTime.setText("Reported: " + dateTime);
        tvReporterName.setText("Reported By: " + reporter);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(ivHazardPhoto);
        } else if (imageBase64 != null && !imageBase64.isEmpty()) {
            byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            ivHazardPhoto.setImageBitmap(bitmap);
        }
    }

    private void checkAdminStatus() {
        com.google.firebase.auth.FirebaseUser user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && "admin@smartroad.com".equals(user.getEmail())) {
            btnUpdateStatus.setVisibility(android.view.View.VISIBLE);
        }
    }

    private void showUpdateStatusDialog() {
        String[] statuses = {"New", "In Progress", "Resolved", "Duplicate"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Update Hazard Status");
        builder.setItems(statuses, (dialog, which) -> {
            updateStatus(statuses[which]);
        });
        builder.show();
    }

    private void updateStatusBadge(String status) {
        if (status == null) status = "New";
        tvStatus.setText("STATUS: " + status.toUpperCase());
        
        int color;
        if ("Resolved".equalsIgnoreCase(status)) {
            color = androidx.core.content.ContextCompat.getColor(this, R.color.status_resolved);
        } else if ("In Progress".equalsIgnoreCase(status) || "Investigating".equalsIgnoreCase(status)) {
            color = androidx.core.content.ContextCompat.getColor(this, R.color.status_investigating);
        } else if ("Duplicate".equalsIgnoreCase(status)) {
            color = android.graphics.Color.GRAY;
        } else {
            color = androidx.core.content.ContextCompat.getColor(this, R.color.status_new);
        }
        cardStatus.setCardBackgroundColor(android.content.res.ColorStateList.valueOf(color));
    }

    private void updateStatus(String newStatus) {
        if (hazardId == null) return;
        db.collection("hazards").document(hazardId)
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    updateStatusBadge(newStatus);
                    android.widget.Toast.makeText(this, "Status updated to " + newStatus, android.widget.Toast.LENGTH_SHORT).show();
                });
    }
}

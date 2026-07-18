package com.example.smartroad;

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
import com.google.firebase.firestore.DocumentSnapshot;

public class HazardDetailsActivity extends AppCompatActivity {

    private TextView tvStatus;
    private TextView tvHazardType;
    private TextView tvHazardDescription;
    private TextView tvDetailLocation;
    private TextView tvDetailTime;
    private TextView tvReporterName;

    private ImageView ivHazardPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_details);

        tvStatus = findViewById(R.id.tvStatus);
        tvHazardType = findViewById(R.id.tvHazardType);
        tvHazardDescription = findViewById(R.id.tvHazardDescription);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailTime = findViewById(R.id.tvDetailTime);
        tvReporterName = findViewById(R.id.tvReporterName);

        ivHazardPhoto = findViewById(R.id.ivHazardImage);

        Button btnBack = findViewById(R.id.btnBack);

        String documentId = getIntent().getStringExtra("DOCUMENT_ID");
        if (documentId != null) {
            fetchHazardDetails(documentId);
        } else {
            populateFromIntent();
        }

        btnBack.setOnClickListener(v -> finish());
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
        FirebaseFirestore.getInstance().collection("hazards").document(documentId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String type = document.getString("hazardType");
                        String status = document.getString("status");
                        String description = document.getString("description");
                        String reporter = document.getString("username");
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
        tvStatus.setText("STATUS: " + status);
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
}

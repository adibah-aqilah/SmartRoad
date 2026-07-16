package com.example.smartroad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class HazardDetailsActivity extends AppCompatActivity {

    private TextView tvStatus;
    private TextView tvHazardType;
    private TextView tvHazardDescription;
    private TextView tvDetailLocation;
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
        tvReporterName = findViewById(R.id.tvReporterName);

        Button btnBack = findViewById(R.id.btnBack);

        String type =
                getIntent().getStringExtra("TYPE");

        String status =
                getIntent().getStringExtra("STATUS");

        String description =
                getIntent().getStringExtra("DESCRIPTION");

        String location =
                getIntent().getStringExtra("LOCATION");

        String reporter =
                getIntent().getStringExtra("REPORTER");

        String imageUrl =
                getIntent().getStringExtra("IMAGE_URL");

        if (type == null) {
            type = "Unknown Hazard";
        }

        if (status == null) {
            status = "New";
        }

        if (description == null) {
            description = "No description available";
        }

        if (location == null) {
            location = "Location not available";
        }

        if (reporter == null) {
            reporter = "Unknown Reporter";
        }

        tvHazardType.setText(type);

        tvStatus.setText(
                "STATUS: " + status
        );

        tvHazardDescription.setText(
                description
        );

        tvDetailLocation.setText(
                "Location: " + location
        );

        tvReporterName.setText(
                "Reported By: " + reporter
        );



        btnBack.setOnClickListener(v -> finish());
    }
}
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

        String type = getIntent().getStringExtra("TYPE");
        String status = getIntent().getStringExtra("STATUS");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String location = getIntent().getStringExtra("LOCATION");
        String reporter = getIntent().getStringExtra("USERNAME");
        String dateTime = getIntent().getStringExtra("DATE_TIME");
        String imageBase64 =
                getIntent().getStringExtra("IMAGE_BASE64");

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


        if (imageBase64 != null &&
                !imageBase64.isEmpty()) {

            byte[] decodedBytes =
                    Base64.decode(
                            imageBase64,
                            Base64.DEFAULT
                    );

            Bitmap bitmap =
                    BitmapFactory.decodeByteArray(
                            decodedBytes,
                            0,
                            decodedBytes.length
                    );

            ivHazardPhoto.setImageBitmap(bitmap);
        }

        btnBack.setOnClickListener(v -> finish());
    }
}

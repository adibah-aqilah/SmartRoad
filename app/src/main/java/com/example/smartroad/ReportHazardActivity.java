package com.example.smartroad;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportHazardActivity extends AppCompatActivity {

    private AutoCompleteTextView spinnerHazardType;
    private TextView tvAutoDateTime, tvAutoGPS;
    private Button btnUploadPhoto, btnSubmitReport;
    private ImageView ivPhotoPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_hazard);

        // Bind items
        spinnerHazardType = findViewById(R.id.spinnerHazardType);
        tvAutoDateTime = findViewById(R.id.tvAutoDateTime);
        tvAutoGPS = findViewById(R.id.tvAutoGPS);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnSubmitReport = findViewById(R.id.btnSubmitReport);
        ivPhotoPreview = findViewById(R.id.ivPhotoPreview);

        // Populating the Modern Dropdown (AutoCompleteTextView)
        String[] hazardCategories = {
                "Pothole", "Flooding", "Fallen Tree",
                "Traffic Accident", "Damaged Road Sign", "Broken Traffic Light"
        };
        
        // Use simple_list_item_1 for better compatibility with AutoCompleteTextView dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_list_item_1, hazardCategories);
        spinnerHazardType.setAdapter(adapter);

        // Simulation of Auto-Capture features for Task 1
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK).format(new Date());
        tvAutoDateTime.setText("Captured: " + currentDate);
        tvAutoGPS.setText("GPS: 3.1569° N, 101.6123° E (Mocked Location)");

        // Mock upload action
        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Task 1 Placeholder UI response
                ivPhotoPreview.setImageResource(android.R.drawable.ic_menu_camera);
                ivPhotoPreview.setVisibility(View.VISIBLE);
                Toast.makeText(ReportHazardActivity.this, "Camera/Gallery triggered.", Toast.LENGTH_SHORT).show();
            }
        });

        // Submit Action
        btnSubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedHazard = spinnerHazardType.getText().toString();
                if (selectedHazard.isEmpty()) {
                    Toast.makeText(ReportHazardActivity.this, "Please select a hazard type", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReportHazardActivity.this, "Report compiled! Submitting to backend via Task 3 protocols.", Toast.LENGTH_LONG).show();
                    finish(); // Returns user to Map View
                }
            }
        });
    }
}

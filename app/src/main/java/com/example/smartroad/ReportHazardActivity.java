package com.example.smartroad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReportHazardActivity extends AppCompatActivity {

    private AutoCompleteTextView spinnerHazardType;
    private TextView tvAutoDateTime;
    private TextView tvAutoGPS;

    private TextInputEditText etDescription;

    private Button btnUploadPhoto;
    private Button btnSubmitReport;

    private ImageView ivPhotoPreview;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri selectedImageUri;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_hazard);

        db = FirebaseFirestore.getInstance();

        spinnerHazardType = findViewById(R.id.spinnerHazardType);
        tvAutoDateTime = findViewById(R.id.tvAutoDateTime);
        tvAutoGPS = findViewById(R.id.tvAutoGPS);
        etDescription = findViewById(R.id.etDescription);

        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnSubmitReport = findViewById(R.id.btnSubmitReport);

        ivPhotoPreview = findViewById(R.id.ivPhotoPreview);

        String[] hazardCategories = {
                "Pothole",
                "Flooding",
                "Fallen Tree",
                "Traffic Accident",
                "Damaged Road Sign",
                "Broken Traffic Light"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        hazardCategories
                );

        spinnerHazardType.setAdapter(adapter);

        String currentDate =
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm:ss",
                        Locale.getDefault()
                ).format(new Date());

        tvAutoDateTime.setText(
                "Captured: " + currentDate
        );

        final double lat =
                getIntent().getDoubleExtra(
                        "LAT",
                        0
                );

        final double lng =
                getIntent().getDoubleExtra(
                        "LNG",
                        0
                );

        tvAutoGPS.setText(
                "GPS: " + lat + ", " + lng
        );

        btnUploadPhoto.setOnClickListener(v -> {

            Intent intent =
                    new Intent(Intent.ACTION_PICK);

            intent.setType("image/*");

            startActivityForResult(
                    intent,
                    PICK_IMAGE_REQUEST
            );
        });

        btnSubmitReport.setOnClickListener(v -> {

            String selectedHazard =
                    spinnerHazardType.getText()
                            .toString()
                            .trim();

            String description =
                    etDescription.getText()
                            .toString()
                            .trim();

            if (selectedHazard.isEmpty()) {

                Toast.makeText(
                        this,
                        "Please select a hazard type",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (description.isEmpty()) {

                Toast.makeText(
                        this,
                        "Please enter description",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (selectedImageUri == null) {

                Toast.makeText(
                        this,
                        "Please upload a photo",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Map<String, Object> hazard =
                    new HashMap<>();

            hazard.put(
                    "hazardType",
                    selectedHazard
            );

            hazard.put(
                    "description",
                    description
            );

            hazard.put(
                    "latitude",
                    lat
            );

            hazard.put(
                    "longitude",
                    lng
            );

            hazard.put(
                    "reporter",
                    "Ayla"
            );

            hazard.put(
                    "status",
                    "New"
            );

            hazard.put(
                    "dateTime",
                    currentDate
            );

            db.collection("hazards")
                    .add(hazard)
                    .addOnSuccessListener(documentReference -> {

                        Toast.makeText(
                                ReportHazardActivity.this,
                                "Report Submitted Successfully",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();
                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(
                                ReportHazardActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    });
        });
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            selectedImageUri = data.getData();

            ivPhotoPreview.setImageURI(
                    selectedImageUri
            );

            ivPhotoPreview.setVisibility(
                    View.VISIBLE
            );

            Toast.makeText(
                    this,
                    "Photo Added Successfully",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
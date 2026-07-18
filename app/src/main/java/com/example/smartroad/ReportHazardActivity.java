package com.example.smartroad;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    private Button btnCamera, btnGallery, btnSubmitReport;
    private ImageView ivPhotoPreview;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PERMISSION_CODE = 101;

    private Uri selectedImageUri;
    private Bitmap photoBitmap;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String reporterName = "Anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_hazard);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        fetchUserData();

        spinnerHazardType = findViewById(R.id.spinnerHazardType);
        tvAutoDateTime = findViewById(R.id.tvAutoDateTime);
        tvAutoGPS = findViewById(R.id.tvAutoGPS);
        etDescription = findViewById(R.id.etDescription);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnSubmitReport = findViewById(R.id.btnSubmitReport);
        ivPhotoPreview = findViewById(R.id.ivPhotoPreview);

        setupHazardSpinner();
        setupDateTimeAndLocation();

        btnGallery.setOnClickListener(v -> openGallery());
        btnCamera.setOnClickListener(v -> checkCameraPermission());

        btnSubmitReport.setOnClickListener(v -> submitReport());
    }

    private void fetchUserData() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (email != null) {
                reporterName = email.split("@")[0];
            }
        }
    }

    private void setupHazardSpinner() {
        String[] hazardCategories = {"Pothole", "Flooding", "Fallen Tree", "Traffic Accident", "Damaged Road Sign", "Broken Traffic Light"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hazardCategories);
        spinnerHazardType.setAdapter(adapter);
    }

    private void setupDateTimeAndLocation() {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        tvAutoDateTime.setText("Captured: " + currentDate);

        double lat = getIntent().getDoubleExtra("LAT", 0);
        double lng = getIntent().getDoubleExtra("LNG", 0);
        tvAutoGPS.setText("GPS: " + lat + ", " + lng);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST && data.getData() != null) {
                selectedImageUri = data.getData();
                photoBitmap = null; // Clear bitmap if gallery is used
                ivPhotoPreview.setImageURI(selectedImageUri);
                ivPhotoPreview.setVisibility(View.VISIBLE);
            } else if (requestCode == CAMERA_REQUEST && data.getExtras() != null) {
                photoBitmap = (Bitmap) data.getExtras().get("data");
                selectedImageUri = null; // Clear URI if camera is used
                ivPhotoPreview.setImageBitmap(photoBitmap);
                ivPhotoPreview.setVisibility(View.VISIBLE);
            }
        }
    }

    private void submitReport() {
        String selectedHazard = spinnerHazardType.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (selectedHazard.isEmpty() || description.isEmpty() || (selectedImageUri == null && photoBitmap == null)) {
            Toast.makeText(this, "Please fill all fields and add a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSubmitReport.setEnabled(false);
        btnSubmitReport.setText("Uploading...");

        String fileName = "hazard_" + System.currentTimeMillis() + ".jpg";
        StorageReference fileRef = storageRef.child("hazards/" + fileName);

        if (selectedImageUri != null) {
            uploadFromUri(selectedImageUri, fileRef, selectedHazard, description);
        } else if (photoBitmap != null) {
            uploadFromBitmap(photoBitmap, fileRef, selectedHazard, description);
        }
    }

    private void uploadFromUri(Uri uri, StorageReference fileRef, String hazardType, String description) {
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                saveToFirestore(downloadUri.toString(), hazardType, description);
            });
        }).addOnFailureListener(e -> {
            btnSubmitReport.setEnabled(true);
            btnSubmitReport.setText("Submit Report");
            Toast.makeText(this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadFromBitmap(Bitmap bitmap, StorageReference fileRef, String hazardType, String description) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] data = baos.toByteArray();

        fileRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                saveToFirestore(downloadUri.toString(), hazardType, description);
            });
        }).addOnFailureListener(e -> {
            btnSubmitReport.setEnabled(true);
            btnSubmitReport.setText("Submit Report");
            Toast.makeText(this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void saveToFirestore(String imageUrl, String hazardType, String description) {
        Map<String, Object> hazard = new HashMap<>();
        hazard.put("hazardType", hazardType);
        hazard.put("description", description);
        hazard.put("latitude", getIntent().getDoubleExtra("LAT", 0));
        hazard.put("longitude", getIntent().getDoubleExtra("LNG", 0));
        hazard.put("reporter", reporterName);
        hazard.put("status", "New");
        hazard.put("dateTime", tvAutoDateTime.getText().toString().replace("Captured: ", ""));
        hazard.put("imageUrl", imageUrl);

        db.collection("hazards").add(hazard)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Report Submitted Successfully", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnSubmitReport.setEnabled(true);
                    btnSubmitReport.setText("Submit Report");
                    Toast.makeText(this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}

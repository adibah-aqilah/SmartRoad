package com.example.smartroad;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;

    private TextView tvGreeting;
    private ImageButton btnAbout;
    private ImageButton btnProfile;
    private ExtendedFloatingActionButton fabReport;

    private double selectedLat = 0;
    private double selectedLng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        db = FirebaseFirestore.getInstance();

        tvGreeting = findViewById(R.id.tvGreeting);
        btnAbout = findViewById(R.id.btnAbout);
        btnProfile = findViewById(R.id.btnProfile);
        fabReport = findViewById(R.id.fabReport);

        FirebaseUser user =
                FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && user.getEmail() != null) {

            String name =
                    user.getEmail().split("@")[0];

            name = name.replace("_", " ");
            name = name.replace(".", " ");

            name = name.substring(0, 1).toUpperCase()
                    + name.substring(1);

            tvGreeting.setText("Hello, " + name + "!");
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager()
                                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnAbout.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AboutActivity.class
                    );

            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            ProfileActivity.class
                    );

            startActivity(intent);
        });

        fabReport.setOnClickListener(v -> {

            if (selectedLat == 0 &&
                    selectedLng == 0) {

                Toast.makeText(
                        this,
                        "Please select hazard location first",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            ReportHazardActivity.class
                    );

            intent.putExtra("LAT", selectedLat);
            intent.putExtra("LNG", selectedLng);

            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        getCurrentLocation();

        loadHazardsFromFirestore();

        mMap.setOnMapClickListener(latLng -> {

            selectedLat = latLng.latitude;
            selectedLng = latLng.longitude;

            mMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .title("Hazard Location Selected")
                            .icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_VIOLET
                                    )
                            )
            );

            Toast.makeText(
                    this,
                    "Hazard Location Selected",
                    Toast.LENGTH_SHORT
            ).show();
        });

        mMap.setOnMarkerClickListener(marker -> {

            if ("You Are Here".equals(
                    marker.getTitle())) {

                return true;
            }

            if ("Hazard Location Selected".equals(
                    marker.getTitle())) {

                return true;
            }

            String documentId =
                    (String) marker.getTag();

            if (documentId == null) {
                return true;
            }

            db.collection("hazards")
                    .document(documentId)
                    .get()
                    .addOnSuccessListener(document -> {

                        Intent intent =
                                new Intent(
                                        MainActivity.this,
                                        HazardDetailsActivity.class
                                );

                        intent.putExtra(
                                "TYPE",
                                document.getString(
                                        "hazardType"
                                )
                        );

                        intent.putExtra(
                                "STATUS",
                                document.getString(
                                        "status"
                                )
                        );

                        intent.putExtra(
                                "DESCRIPTION",
                                document.getString(
                                        "description"
                                )
                        );

                        intent.putExtra(
                                "USERNAME",
                                document.getString("username")
                        );

                        intent.putExtra(
                                "DATE_TIME",
                                document.getString(
                                        "dateTime"
                                )
                        );


                        intent.putExtra(
                                "IMAGE_BASE64",
                                document.getString("imageBase64")
                        );


                        Double lat =
                                document.getDouble(
                                        "latitude"
                                );

                        Double lng =
                                document.getDouble(
                                        "longitude"
                                );

                        intent.putExtra(
                                "LOCATION",
                                lat + ", " + lng
                        );

                        startActivity(intent);

                    });

            return true;
        });
    }

    private void loadHazardsFromFirestore() {

        db.collection("hazards")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot document :
                            queryDocumentSnapshots) {

                        String hazardType =
                                document.getString(
                                        "hazardType"
                                );

                        Double latitude =
                                document.getDouble(
                                        "latitude"
                                );

                        Double longitude =
                                document.getDouble(
                                        "longitude"
                                );

                        if (latitude != null &&
                                longitude != null) {

                            LatLng location =
                                    new LatLng(
                                            latitude,
                                            longitude
                                    );

                            Marker marker =
                                    mMap.addMarker(
                                            new MarkerOptions()
                                                    .position(location)
                                                    .title(hazardType)
                                    );

                            if (marker != null) {

                                marker.setTag(
                                        document.getId()
                                );
                            }
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMap != null) {

            mMap.clear();

            getCurrentLocation();

            loadHazardsFromFirestore();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        mMap.setMyLocationEnabled(true);

        // Fetching more accurate/current location using requestLocationUpdates would be better, 
        // but for now let's ensure getLastLocation is used robustly.
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                
                // Only move camera and add "You Are Here" marker if we haven't selected a spot yet
                if (selectedLat == 0 && selectedLng == 0) {
                    mMap.addMarker(new MarkerOptions()
                            .position(currentLocation)
                            .title("You Are Here")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f));
                }
            } else {
                // If last location is null, we can try to get the current location explicitly
                // Toast.makeText(this, "Unable to get current location. Ensure GPS is ON.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == 100 &&
                grantResults.length > 0 &&
                grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        }
    }
}
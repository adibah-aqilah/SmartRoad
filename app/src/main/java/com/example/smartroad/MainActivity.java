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
    private boolean isInitialZoomDone = false;
    private Marker selectionMarker;
    private Marker userLocationMarker;

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

        if (user != null) {
            // Default to email prefix while loading
            String email = user.getEmail();
            if (email != null) {
                String name = email.split("@")[0];
                tvGreeting.setText("Hello, " + name + "!");
            }

            // Fetch actual Full Name from Firestore
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String fullName = documentSnapshot.getString("fullName");
                            if (fullName != null && !fullName.isEmpty()) {
                                tvGreeting.setText("Hello, " + fullName + "!");
                            }
                        }
                    });
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

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        getCurrentLocation();

        loadHazardsFromFirestore();

        mMap.setOnMapClickListener(latLng -> {

            selectedLat = latLng.latitude;
            selectedLng = latLng.longitude;

            if (selectionMarker != null) {
                selectionMarker.remove();
            }

            selectionMarker = mMap.addMarker(
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

            String documentId = (String) marker.getTag();

            if (documentId != null) {
                Intent intent = new Intent(MainActivity.this, HazardDetailsActivity.class);
                intent.putExtra("DOCUMENT_ID", documentId);
                startActivity(intent);
            }

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

        // Try to get last known location first (fast)
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                updateMapToLocation(location);
            } else {
                // If last location is null, request current location explicitly
                requestFreshLocation();
            }
        });
    }

    private void requestFreshLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        com.google.android.gms.location.CurrentLocationRequest request = 
                new com.google.android.gms.location.CurrentLocationRequest.Builder()
                .setPriority(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        fusedLocationClient.getCurrentLocation(request, null).addOnSuccessListener(location -> {
            if (location != null) {
                updateMapToLocation(location);
            } else {
                Toast.makeText(this, "Still unable to get location. Is GPS on?", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateMapToLocation(android.location.Location location) {
        if (location == null) return;
        
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        // Automatically set selected location if not already set by map click
        if (selectedLat == 0 && selectedLng == 0) {
            selectedLat = location.getLatitude();
            selectedLng = location.getLongitude();
        }

        if (userLocationMarker != null) {
            userLocationMarker.remove();
        }

        userLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("You Are Here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        if (!isInitialZoomDone) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f));
            isInitialZoomDone = true;
        }
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
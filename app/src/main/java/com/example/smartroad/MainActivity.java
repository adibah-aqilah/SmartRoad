package com.example.smartroad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView tvGreeting;
    private ImageButton btnAbout, btnProfile;
    private ExtendedFloatingActionButton fabReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise UI components
        tvGreeting = findViewById(R.id.tvGreeting);
        btnAbout = findViewById(R.id.btnAbout);
        btnProfile = findViewById(R.id.btnProfile);
        fabReport = findViewById(R.id.fabReport);

        // Hardcoded greeting for Task 1 (Will pull actual name from Firebase in Task 2)
        tvGreeting.setText("Hello, John Doe!");

        // Load the Google Maps Fragment safely
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Navigate to Report Form Screen
        fabReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReportHazardActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to About Screen
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Profile Screen
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Default map coordinates centered over a region (e.g., Kuala Lumpur area)
        LatLng defaultLocation = new LatLng(3.1569, 101.6123);

        // Sample hazard marker setup for Task 1 demonstration
        mMap.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .title("Pothole (Status: New)")
                .snippet("Click for details")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Setup Marker Click Listener for Hazard Details
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                Intent intent = new Intent(MainActivity.this, HazardDetailsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        // Frame the map camera smoothly to the location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 14.0f));
    }
}
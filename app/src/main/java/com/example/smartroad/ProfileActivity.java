package com.example.smartroad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Filter;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private ImageView ivProfileGif;
    private Button btnLogout;

    private TextView tvTotalReports;
    private TextView tvResolvedReports;
    private TextView tvPoints;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        ivProfileGif = findViewById(R.id.ivProfileGif);
        btnLogout = findViewById(R.id.btnLogout);

        // Load decorative GIF
        Glide.with(this)
                .asGif()
                .load(R.drawable.thank_you)
                .into(ivProfileGif);

        tvTotalReports = findViewById(R.id.tvTotalReports);
        tvResolvedReports = findViewById(R.id.tvResolvedReports);
        tvPoints = findViewById(R.id.tvPoints);

        tvTotalReports.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HazardFeedActivity.class);
            intent.putExtra("MY_REPORTS_ONLY", true);
            startActivity(intent);
        });

        db = FirebaseFirestore.getInstance();

        FirebaseUser user =
                FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            String email = user.getEmail();

            tvProfileEmail.setText(email);

            if (email != null) {

                String username =
                        email.split("@")[0];

                String displayName =
                        username.substring(0, 1)
                                .toUpperCase()
                                + username.substring(1);

                tvProfileName.setText(
                        displayName
                );

                // LOAD STATISTICS for this specific user
                loadStatistics(username);
            }
        }

        btnLogout.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            Intent intent =
                    new Intent(
                            ProfileActivity.this,
                            LoginActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_report) {
                startActivity(new Intent(this, HazardFeedActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    private void loadStatistics(String username) {

        db.collection("hazards")
                .where(Filter.or(
                        Filter.equalTo("reporter", username),
                        Filter.equalTo("username", username)
                ))
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    int totalReports =
                            querySnapshot.size();

                    int resolved = 0;

                    for (QueryDocumentSnapshot document : querySnapshot) {

                        String status =
                                document.getString(
                                        "status"
                                );

                        if (status != null &&
                                status.equalsIgnoreCase(
                                        "Resolved"
                                )) {

                            resolved++;
                        }
                    }

                    int points =
                            totalReports * 10;

                    tvTotalReports.setText(
                            String.valueOf(
                                    totalReports
                            )
                    );

                    tvResolvedReports.setText(
                            String.valueOf(
                                    resolved
                            )
                    );

                    tvPoints.setText(
                            String.valueOf(
                                    points
                            )
                    );
                });
    }
}

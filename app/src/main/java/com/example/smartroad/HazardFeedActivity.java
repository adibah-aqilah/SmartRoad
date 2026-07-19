package com.example.smartroad;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HazardFeedActivity extends AppCompatActivity {

    private RecyclerView rvHazards;
    private HazardAdapter adapter;
    private List<Map<String, Object>> hazardList = new ArrayList<>();
    private List<Map<String, Object>> fullList = new ArrayList<>();
    private FirebaseFirestore db;
    private String currentStatusFilter = "All";
    private Button btnFilterStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_feed);

        rvHazards = findViewById(R.id.rvHazards);
        rvHazards.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HazardAdapter(hazardList);
        rvHazards.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        
        btnFilterStatus = findViewById(R.id.btnFilterStatus);
        btnFilterStatus.setOnClickListener(this::showFilterMenu);

        // Semak jika dibuka dari Profile (My Reports)
        if (getIntent().getBooleanExtra("MY_REPORTS_ONLY", false)) {
            currentStatusFilter = "My Reports";
            btnFilterStatus.setText("My Reports");
        }

        loadHazards();

        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_report);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_report) {
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void showFilterMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add("All");
        popup.getMenu().add("My Reports");
        popup.getMenu().add("New");
        popup.getMenu().add("In Progress");
        popup.getMenu().add("Resolved");
        popup.getMenu().add("Duplicate");

        popup.setOnMenuItemClickListener(item -> {
            currentStatusFilter = item.getTitle().toString();
            btnFilterStatus.setText(currentStatusFilter);
            applyFilter();
            return true;
        });
        popup.show();
    }

    private void loadHazards() {
        db.collection("hazards")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    fullList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Map<String, Object> data = doc.getData();
                        data.put("id", doc.getId());
                        fullList.add(data);
                    }
                    applyFilter();
                })
                .addOnFailureListener(e -> {
                    android.widget.Toast.makeText(this, "Error loading data: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                });
    }

    private void applyFilter() {
        hazardList.clear();
        
        // Dapatkan ID pengguna semasa (prefix email)
        String myEmailPrefix = "";
        if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null) {
            String email = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (email != null) myEmailPrefix = email.split("@")[0];
        }

        for (Map<String, Object> item : fullList) {
            String status = (String) item.get("status");
            if (status == null) status = "New";
            String reporter = (String) item.get("reporter");

            if ("All".equals(currentStatusFilter)) {
                hazardList.add(item);
            } else if ("My Reports".equals(currentStatusFilter)) {
                if (reporter != null && reporter.equals(myEmailPrefix)) {
                    hazardList.add(item);
                }
            } else if ("In Progress".equals(currentStatusFilter)) {
                if ("In Progress".equalsIgnoreCase(status) || "Investigating".equalsIgnoreCase(status)) {
                    hazardList.add(item);
                }
            } else {
                if (currentStatusFilter.equalsIgnoreCase(status)) {
                    hazardList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private class HazardAdapter extends RecyclerView.Adapter<HazardAdapter.ViewHolder> {
        private List<Map<String, Object>> list;

        public HazardAdapter(List<Map<String, Object>> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hazard, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Map<String, Object> hazard = list.get(position);
            holder.tvType.setText((String) hazard.get("hazardType"));
            
            String status = (String) hazard.get("status");
            holder.tvStatus.setText("Status: " + status);

            int statusColor;
            if ("Resolved".equalsIgnoreCase(status)) {
                statusColor = ContextCompat.getColor(HazardFeedActivity.this, R.color.status_resolved);
            } else if ("In Progress".equalsIgnoreCase(status) || "Investigating".equalsIgnoreCase(status)) {
                statusColor = ContextCompat.getColor(HazardFeedActivity.this, R.color.status_investigating);
            } else {
                statusColor = ContextCompat.getColor(HazardFeedActivity.this, R.color.status_new);
            }
            holder.tvStatus.setTextColor(statusColor);
            
            Double lat = (Double) hazard.get("latitude");
            Double lng = (Double) hazard.get("longitude");
            holder.tvLocation.setText(lat + ", " + lng);

            String imageBase64 = (String) hazard.get("imageBase64");
            if (imageBase64 != null && !imageBase64.isEmpty()) {
                byte[] imageBytes = android.util.Base64.decode(imageBase64, android.util.Base64.DEFAULT);
                Glide.with(holder.ivThumb.getContext())
                        .asBitmap()
                        .load(imageBytes)
                        .placeholder(android.R.drawable.ic_menu_report_image)
                        .into(holder.ivThumb);
            } else {
                holder.ivThumb.setImageResource(android.R.drawable.ic_menu_report_image);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(HazardFeedActivity.this, HazardDetailsActivity.class);
                intent.putExtra("DOCUMENT_ID", (String) hazard.get("id"));
                intent.putExtra("TYPE", (String) hazard.get("hazardType"));
                intent.putExtra("STATUS", (String) hazard.get("status"));
                intent.putExtra("DESCRIPTION", (String) hazard.get("description"));
                intent.putExtra("REPORTER", (String) hazard.get("reporter"));
                intent.putExtra("DATE_TIME", (String) hazard.get("dateTime"));
                intent.putExtra("IMAGE_BASE64", imageBase64);
                intent.putExtra("LOCATION", lat + ", " + lng);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvType, tvLocation, tvStatus;
            ImageView ivThumb;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvType = itemView.findViewById(R.id.tvHazardType);
                tvLocation = itemView.findViewById(R.id.tvHazardLocation);
                tvStatus = itemView.findViewById(R.id.tvHazardStatus);
                ivThumb = itemView.findViewById(R.id.ivHazardThumb);
            }
        }
    }
}
